package com.fooddash.order;

import com.fooddash.customer.CurrentCustomer;
import com.fooddash.customer.Customer;
import com.fooddash.menu.MenuItem;
import com.fooddash.menu.MenuService;
import com.fooddash.payment.PaymentDeclinedException;
import com.fooddash.payment.PaymentGateway;
import com.fooddash.payment.PaymentResult;
import com.fooddash.restaurant.Restaurant;
import com.fooddash.restaurant.RestaurantClosedException;
import com.fooddash.restaurant.RestaurantService;
import com.fooddash.rider.Rider;
import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** The order rules: placing, reading, cancelling and assigning orders. */
@Service
public class OrderService {

  private final OrderRepository orders;
  private final IdempotencyRecordRepository idempotencyKeys;
  private final RestaurantService restaurantService;
  private final MenuService menuService;
  private final DeliveryFeePolicy deliveryFeePolicy;
  private final PaymentGateway paymentGateway;
  private final CurrentCustomer currentCustomer;
  private final Clock clock;

  OrderService(
      OrderRepository orders,
      IdempotencyRecordRepository idempotencyKeys,
      RestaurantService restaurantService,
      MenuService menuService,
      DeliveryFeePolicy deliveryFeePolicy,
      PaymentGateway paymentGateway,
      CurrentCustomer currentCustomer,
      Clock clock) {
    this.orders = orders;
    this.idempotencyKeys = idempotencyKeys;
    this.restaurantService = restaurantService;
    this.menuService = menuService;
    this.deliveryFeePolicy = deliveryFeePolicy;
    this.paymentGateway = paymentGateway;
    this.currentCustomer = currentCustomer;
    this.clock = clock;
  }

  /**
   * Places and pays for an order. A retry with the same Idempotency-Key returns the order it
   * already created instead of creating and charging a second one.
   */
  @Transactional
  public Order place(PlaceOrderRequest request, String idempotencyKey) {
    if (idempotencyKey.isBlank() || idempotencyKey.length() > 64) {
      throw new IllegalArgumentException(
          "Idempotency-Key must be 1 to 64 characters, e.g. a UUID.");
    }
    String fingerprint = request.fingerprint();
    Optional<IdempotencyRecord> earlier = idempotencyKeys.findById(idempotencyKey);
    if (earlier.isPresent()) {
      if (!earlier.get().sameRequest(fingerprint)) {
        throw new IdempotencyKeyReusedException();
      }
      return loadOrder(earlier.get().getOrder().getPublicId());
    }

    Restaurant restaurant = restaurantService.get(request.restaurantId());
    if (!restaurant.isOpen()) {
      throw new RestaurantClosedException(restaurant.getName());
    }
    Map<UUID, MenuItem> menuItems =
        menuService.itemsForOrder(
            restaurant,
            request.items().stream().map(PlaceOrderRequest.OrderLine::menuItemId).toList());

    PlaceOrderRequest.Address address = request.deliveryAddress();
    Order order =
        Order.place(
            currentCustomer.get(),
            restaurant,
            new DeliveryAddress(address.line1(), address.area(), address.phone()),
            Instant.now(clock));
    request
        .items()
        .forEach(line -> order.addItem(menuItems.get(line.menuItemId()), line.quantity()));
    order.price(deliveryFeePolicy.feeFor(order.itemsTotal()));
    orders.save(order);

    PaymentResult payment =
        paymentGateway.charge(order.getPublicId(), order.getTotal(), idempotencyKey);
    if (!payment.approved()) {
      throw new PaymentDeclinedException(payment.declineReason());
    }
    order.markPaid();
    idempotencyKeys.save(
        new IdempotencyRecord(idempotencyKey, fingerprint, order, Instant.now(clock)));
    return order;
  }

  /** One of the current customer's orders; anyone else's is reported as not found. */
  @Transactional(readOnly = true)
  public Order get(UUID id) {
    return ownOrder(id);
  }

  /** Cancels an order before a rider picks it up, refunding it if it was paid. */
  @Transactional
  public Order cancel(UUID id) {
    Order order = ownOrder(id);
    switch (order.getStatus()) {
      case CANCELLED -> {
        return order;
      }
      case PICKED_UP, DELIVERED -> throw new OrderAlreadyPickedUpException(id);
      case PAID -> paymentGateway.refund(order.getPublicId(), order.getTotal());
      case PLACED -> {}
    }
    order.markCancelled();
    return order;
  }

  /** Gives the order to this rider, unless another rider already took it. */
  @Transactional
  public Order assignRider(UUID orderId, Rider rider) {
    Order order = loadOrder(orderId);
    if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.DELIVERED) {
      throw OrderNotAssignableException.finished(orderId, order.getStatus());
    }
    if (order.getRider() != null) {
      if (order.getRider().getId().equals(rider.getId())) {
        return order;
      }
      throw OrderNotAssignableException.alreadyTaken(orderId);
    }
    order.assignRider(rider);
    return order;
  }

  private Order loadOrder(UUID id) {
    return orders.findDetailedByPublicId(id).orElseThrow(() -> new OrderNotFoundException(id));
  }

  private Order ownOrder(UUID id) {
    Customer me = currentCustomer.get();
    return orders
        .findDetailedByPublicId(id)
        .filter(order -> order.getCustomer().getId().equals(me.getId()))
        .orElseThrow(() -> new OrderNotFoundException(id));
  }
}
