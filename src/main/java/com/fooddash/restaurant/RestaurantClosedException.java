package com.fooddash.restaurant;

import com.fooddash.shared.BusinessRuleException;

public class RestaurantClosedException extends BusinessRuleException {

  public RestaurantClosedException(String restaurantName) {
    super(
        "restaurant-closed",
        "Restaurant is closed",
        restaurantName + " isn't taking orders right now.");
  }
}
