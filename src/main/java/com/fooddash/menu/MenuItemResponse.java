package com.fooddash.menu;

import com.fooddash.shared.Money;
import java.util.UUID;

public record MenuItemResponse(UUID id, String name, String price, boolean available) {

  static MenuItemResponse from(MenuItem item) {
    return new MenuItemResponse(
        item.getPublicId(), item.getName(), Money.format(item.getPrice()), item.isAvailable());
  }
}
