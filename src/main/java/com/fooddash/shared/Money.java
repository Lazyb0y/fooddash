package com.fooddash.shared;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Taka amounts travel as strings with two decimals ("250.00"), never as floating-point numbers. */
public final class Money {

  private Money() {}

  public static String format(BigDecimal amount) {
    return amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
  }
}
