package com.fooddash.restaurant;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RestaurantService {

  static final int MAX_PAGE_SIZE = 100;

  private final RestaurantRepository restaurants;

  RestaurantService(RestaurantRepository restaurants) {
    this.restaurants = restaurants;
  }

  public RestaurantPage list(String area, Boolean open, int limit, String cursor) {
    int size = Math.clamp(limit, 1, MAX_PAGE_SIZE);
    String areaFilter = area == null || area.isBlank() ? null : area;
    List<Restaurant> rows =
        restaurants.findPage(areaFilter, open, Cursor.decode(cursor), Limit.of(size + 1));
    boolean hasMore = rows.size() > size;
    List<Restaurant> page = hasMore ? rows.subList(0, size) : rows;
    String nextCursor = hasMore ? Cursor.encode(page.getLast().getId()) : null;
    return new RestaurantPage(page.stream().map(RestaurantResponse::from).toList(), nextCursor);
  }

  public Restaurant get(UUID publicId) {
    return restaurants
        .findByPublicId(publicId)
        .orElseThrow(() -> new RestaurantNotFoundException(publicId));
  }
}
