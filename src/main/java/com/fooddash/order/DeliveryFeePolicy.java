package com.fooddash.order;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;

/** How much delivery costs. A flat fee for now; Class 6 adds fee tiers and discount codes. */
@Component
public class DeliveryFeePolicy {

  static final BigDecimal BASE_DELIVERY_FEE = new BigDecimal("40.00");
  static final BigDecimal RAIN_SURCHARGE = new BigDecimal("15.00");

  /** The delivery fee for an order whose items cost this much. */
  public BigDecimal feeFor(BigDecimal subtotal) {
    return BASE_DELIVERY_FEE;
  }

  /** The fee on a rainy day: riders get a surcharge for riding in the rain. */
  public BigDecimal rainyDayFeeFor(BigDecimal subtotal) {
    return feeFor(subtotal).add(RAIN_SURCHARGE);
  }
}
