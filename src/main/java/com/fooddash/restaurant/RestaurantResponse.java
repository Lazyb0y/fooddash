package com.fooddash.restaurant;

import java.util.UUID;

public record RestaurantResponse(UUID id, String name, String area, boolean open) {

  static RestaurantResponse from(Restaurant restaurant) {
    return new RestaurantResponse(
        restaurant.getPublicId(), restaurant.getName(), restaurant.getArea(), restaurant.isOpen());
  }
}
