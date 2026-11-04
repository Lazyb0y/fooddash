package com.fooddash.order;

import com.fooddash.customer.Customer;
import com.fooddash.menu.MenuItem;
import com.fooddash.restaurant.Restaurant;
import com.fooddash.rider.Rider;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JdbcTypeCode(SqlTypes.CHAR)
  @Column(name = "public_id", nullable = false, unique = true, length = 36)
  private UUID publicId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "customer_id")
  private Customer customer;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "restaurant_id")
  private Restaurant restaurant;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "rider_id")
  private Rider rider;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private OrderStatus status;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal subtotal;

  @Column(name = "delivery_fee", nullable = false, precision = 10, scale = 2)
  private BigDecimal deliveryFee;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal total;

  @Embedded private DeliveryAddress deliveryAddress;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> items = new ArrayList<>();

  protected Order() {}

  static Order place(
      Customer customer, Restaurant restaurant, DeliveryAddress address, Instant placedAt) {
    Order order = new Order();
    order.publicId = UUID.randomUUID();
    order.customer = customer;
    order.restaurant = restaurant;
    order.deliveryAddress = address;
    order.status = OrderStatus.PLACED;
    order.createdAt = placedAt.truncatedTo(ChronoUnit.SECONDS);
    return order;
  }

  void addItem(MenuItem menuItem, int quantity) {
    items.add(new OrderItem(this, menuItem, quantity));
  }

  BigDecimal itemsTotal() {
    return items.stream().map(OrderItem::lineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  void price(BigDecimal fee) {
    this.subtotal = itemsTotal();
    this.deliveryFee = fee;
    this.total = subtotal.add(fee);
  }

  void markPaid() {
    this.status = OrderStatus.PAID;
  }

  public Long getId() {
    return id;
  }

  public UUID getPublicId() {
    return publicId;
  }

  public Customer getCustomer() {
    return customer;
  }

  public Restaurant getRestaurant() {
    return restaurant;
  }

  public Rider getRider() {
    return rider;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public BigDecimal getSubtotal() {
    return subtotal;
  }

  public BigDecimal getDeliveryFee() {
    return deliveryFee;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public List<OrderItem> getItems() {
    return List.copyOf(items);
  }
}
