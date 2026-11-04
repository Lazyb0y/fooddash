package com.fooddash.payment;

import com.fooddash.shared.BusinessRuleException;

public class PaymentDeclinedException extends BusinessRuleException {

  public PaymentDeclinedException(String reason) {
    super("payment-declined", "Payment declined", reason);
  }
}
