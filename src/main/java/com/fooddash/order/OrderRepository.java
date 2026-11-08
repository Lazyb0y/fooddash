package com.fooddash.order;

import com.fooddash.restaurant.Restaurant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

interface OrderRepository extends JpaRepository<Order, Long> {

  /** The order with everything its response needs, loaded in one query. */
  @EntityGraph(attributePaths = {"customer", "restaurant", "rider", "items", "items.menuItem"})
  Optional<Order> findDetailedByPublicId(UUID publicId);

  /** A restaurant's latest orders, newest first. */
  List<Order> findByRestaurantOrderByCreatedAtDesc(Restaurant restaurant, Limit limit);
}
