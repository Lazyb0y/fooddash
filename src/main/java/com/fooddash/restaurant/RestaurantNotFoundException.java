package com.fooddash.restaurant;

import com.fooddash.shared.NotFoundException;
import java.util.UUID;

public class RestaurantNotFoundException extends NotFoundException {

  public RestaurantNotFoundException(UUID id) {
    super("restaurant-not-found", "Restaurant not found", "No restaurant with id " + id + ".");
  }
}
