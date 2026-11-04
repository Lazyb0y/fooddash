package com.fooddash.order;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;

/** How much delivery costs. A flat fee for now; Class 6 adds fee tiers and discount codes. */
@Component
public class DeliveryFeePolicy {

  static final BigDecimal BASE_FEE = new BigDecimal("50.00");

  /** The delivery fee for an order whose items cost this much. */
  public BigDecimal feeFor(BigDecimal subtotal) {
    return BASE_FEE;
  }
}
