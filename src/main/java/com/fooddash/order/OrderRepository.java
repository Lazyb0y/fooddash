package com.fooddash.order;

import com.fooddash.restaurant.Restaurant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface OrderRepository extends JpaRepository<Order, Long> {

  /** The order with everything its response needs, loaded in one query. */
  @EntityGraph(attributePaths = {"customer", "restaurant", "rider", "items", "items.menuItem"})
  Optional<Order> findDetailedByPublicId(UUID publicId);

  /** A restaurant's latest orders with their customers, newest first, in one query. */
  @Query(
      "select o from Order o join fetch o.customer where o.restaurant = :restaurant"
          + " order by o.createdAt desc")
  List<Order> findLatestWithCustomer(Restaurant restaurant, Limit limit);
}
