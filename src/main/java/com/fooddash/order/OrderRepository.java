package com.fooddash.order;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

interface OrderRepository extends JpaRepository<Order, Long> {

  /** The order with everything its response needs, loaded in one query. */
  @EntityGraph(attributePaths = {"customer", "restaurant", "rider", "items", "items.menuItem"})
  Optional<Order> findDetailedByPublicId(UUID publicId);
}
