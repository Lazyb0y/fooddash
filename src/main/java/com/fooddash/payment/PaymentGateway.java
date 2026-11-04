package com.fooddash.payment;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Taking money, in FoodDash's own words. One adapter talks to the real provider; tests and local
 * runs use {@link FakePaymentGateway}.
 */
public interface PaymentGateway {

  PaymentResult charge(UUID orderId, BigDecimal amount, String idempotencyKey);

  void refund(UUID orderId, BigDecimal amount);
}
