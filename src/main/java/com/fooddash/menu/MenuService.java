package com.fooddash.menu;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import com.fooddash.restaurant.Restaurant;
import com.fooddash.restaurant.RestaurantService;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {

  private final MenuItemRepository items;
  private final RestaurantService restaurantService;

  MenuService(MenuItemRepository items, RestaurantService restaurantService) {
    this.items = items;
    this.restaurantService = restaurantService;
  }

  public MenuResponse menuFor(UUID restaurantId) {
    Restaurant restaurant = restaurantService.get(restaurantId);
    return new MenuResponse(
        restaurant.getPublicId(),
        restaurant.getName(),
        restaurant.isOpen(),
        items.findByRestaurantOrderByName(restaurant).stream()
            .map(MenuItemResponse::from)
            .toList());
  }

  /**
   * The requested items, all from this restaurant and available, or a 422 naming the first bad one.
   */
  public Map<UUID, MenuItem> itemsForOrder(Restaurant restaurant, Collection<UUID> menuItemIds) {
    Map<UUID, MenuItem> found =
        items.findByPublicIdIn(menuItemIds).stream()
            .filter(item -> item.getRestaurant().getId().equals(restaurant.getId()))
            .collect(toMap(MenuItem::getPublicId, identity()));
    for (UUID id : menuItemIds) {
      MenuItem item = found.get(id);
      if (item == null || !item.isAvailable()) {
        throw new MenuItemUnavailableException(id);
      }
    }
    return found;
  }
}
