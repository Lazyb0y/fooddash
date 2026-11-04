package com.fooddash.menu;

import com.fooddash.shared.BusinessRuleException;
import java.util.UUID;

public class MenuItemUnavailableException extends BusinessRuleException {

  public MenuItemUnavailableException(UUID menuItemId) {
    super(
        "item-unavailable",
        "Menu item unavailable",
        "Menu item " + menuItemId + " isn't available at this restaurant.");
  }
}
