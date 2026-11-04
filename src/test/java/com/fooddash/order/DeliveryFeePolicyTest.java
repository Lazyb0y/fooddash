package com.fooddash.order;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class DeliveryFeePolicyTest {

  private final DeliveryFeePolicy policy = new DeliveryFeePolicy();

  @Test
  void basicOrder_paysBaseFee() {
    assertEquals(new BigDecimal("50.00"), policy.feeFor(new BigDecimal("500.00")));
  }

  @Test
  void fee_isTheSameForSmallAndLargeOrders() {
    assertEquals(policy.feeFor(new BigDecimal("100.00")), policy.feeFor(new BigDecimal("5000.00")));
  }
}
