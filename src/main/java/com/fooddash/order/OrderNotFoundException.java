package com.fooddash.order;

import com.fooddash.shared.NotFoundException;
import java.util.UUID;

/** Also used for other customers' orders, so a response never confirms that an order exists. */
public class OrderNotFoundException extends NotFoundException {

  public OrderNotFoundException(UUID id) {
    super("order-not-found", "Order not found", "No order " + id + " for this customer.");
  }
}
