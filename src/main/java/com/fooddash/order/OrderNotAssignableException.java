package com.fooddash.order;

import com.fooddash.shared.ConflictException;
import java.util.UUID;

public class OrderNotAssignableException extends ConflictException {

  private OrderNotAssignableException(String type, String title, String detail) {
    super(type, title, detail);
  }

  static OrderNotAssignableException alreadyTaken(UUID id) {
    return new OrderNotAssignableException(
        "order-already-assigned",
        "Order already has a rider",
        "Another rider already accepted order " + id + ".");
  }

  static OrderNotAssignableException finished(UUID id, OrderStatus status) {
    return new OrderNotAssignableException(
        "order-not-assignable", "Order can't take a rider", "Order " + id + " is " + status + ".");
  }
}
