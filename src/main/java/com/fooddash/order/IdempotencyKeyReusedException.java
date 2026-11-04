package com.fooddash.order;

import com.fooddash.shared.BusinessRuleException;

public class IdempotencyKeyReusedException extends BusinessRuleException {

  public IdempotencyKeyReusedException() {
    super(
        "idempotency-key-reused",
        "Idempotency key reused",
        "This Idempotency-Key was already used for a different order. Use a new key per order.");
  }
}
