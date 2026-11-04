package com.fooddash.menu;

import com.fooddash.restaurant.Restaurant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

  List<MenuItem> findByRestaurantOrderByName(Restaurant restaurant);

  List<MenuItem> findByPublicIdIn(Collection<UUID> publicIds);
}
