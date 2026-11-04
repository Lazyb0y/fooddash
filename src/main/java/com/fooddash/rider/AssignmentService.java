package com.fooddash.rider;

import com.fooddash.order.Order;
import com.fooddash.order.OrderService;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Riders accepting orders. The order rules live in OrderService; this module owns riders. */
@Service
public class AssignmentService {

  private final RiderRepository riders;
  private final OrderService orderService;

  AssignmentService(RiderRepository riders, OrderService orderService) {
    this.riders = riders;
    this.orderService = orderService;
  }

  @Transactional
  public Order accept(UUID orderId, UUID riderId) {
    Rider rider =
        riders.findByPublicId(riderId).orElseThrow(() -> new RiderNotFoundException(riderId));
    return orderService.assignRider(orderId, rider);
  }
}
