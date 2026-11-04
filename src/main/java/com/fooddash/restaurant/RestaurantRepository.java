package com.fooddash.restaurant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

  Optional<Restaurant> findByPublicId(UUID publicId);

  /** One page of restaurants after a position (keyset pagination): fast and stable on big lists. */
  @Query(
      """
      select r from Restaurant r
      where (:area is null or r.area = :area)
        and (:open is null or r.open = :open)
        and r.id > :afterId
      order by r.id
      """)
  List<Restaurant> findPage(
      @Param("area") String area,
      @Param("open") Boolean open,
      @Param("afterId") long afterId,
      Limit limit);
}
