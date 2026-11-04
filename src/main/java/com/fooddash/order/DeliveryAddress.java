package com.fooddash.order;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class DeliveryAddress {

  @Column(name = "delivery_line1", nullable = false)
  private String line1;

  @Column(name = "delivery_area", nullable = false)
  private String area;

  @Column(name = "delivery_phone", nullable = false)
  private String phone;

  protected DeliveryAddress() {}

  DeliveryAddress(String line1, String area, String phone) {
    this.line1 = line1;
    this.area = area;
    this.phone = phone;
  }

  public String getLine1() {
    return line1;
  }

  public String getArea() {
    return area;
  }

  public String getPhone() {
    return phone;
  }
}
