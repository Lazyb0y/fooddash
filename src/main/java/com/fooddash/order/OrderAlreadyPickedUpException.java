package com.fooddash.order;

import com.fooddash.shared.ConflictException;
import java.util.UUID;

public class OrderAlreadyPickedUpException extends ConflictException {

  public OrderAlreadyPickedUpException(UUID id) {
    super(
        "order-picked-up",
        "Order already picked up",
        "Order " + id + " is already with the rider, so it can't be cancelled.");
  }
}
