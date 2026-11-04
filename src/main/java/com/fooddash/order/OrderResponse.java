package com.fooddash.order;

import com.fooddash.shared.Money;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
    UUID id,
    OrderStatus status,
    UUID restaurantId,
    UUID riderId,
    List<Line> items,
    String subtotal,
    String deliveryFee,
    String total,
    Instant createdAt) {

  public record Line(UUID menuItemId, String name, int quantity, String unitPrice) {}

  public static OrderResponse from(Order order) {
    List<Line> lines =
        order.getItems().stream()
            .map(
                item ->
                    new Line(
                        item.getMenuItem().getPublicId(),
                        item.getMenuItem().getName(),
                        item.getQuantity(),
                        Money.format(item.getUnitPrice())))
            .toList();
    return new OrderResponse(
        order.getPublicId(),
        order.getStatus(),
        order.getRestaurant().getPublicId(),
        order.getRider() == null ? null : order.getRider().getPublicId(),
        lines,
        Money.format(order.getSubtotal()),
        Money.format(order.getDeliveryFee()),
        Money.format(order.getTotal()),
        order.getCreatedAt());
  }
}
