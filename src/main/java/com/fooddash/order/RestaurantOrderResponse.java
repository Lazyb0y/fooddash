package com.fooddash.order;

import com.fooddash.shared.Money;
import java.time.Instant;
import java.util.UUID;

/** One line of a restaurant's order history. */
public record RestaurantOrderResponse(
    UUID id, OrderStatus status, String customerName, String total, Instant createdAt) {

  static RestaurantOrderResponse from(Order order) {
    return new RestaurantOrderResponse(
        order.getPublicId(),
        order.getStatus(),
        order.getCustomer().getName(),
        Money.format(order.getTotal()),
        order.getCreatedAt());
  }
}
