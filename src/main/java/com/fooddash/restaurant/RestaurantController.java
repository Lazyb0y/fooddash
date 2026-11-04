package com.fooddash.restaurant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Restaurants")
class RestaurantController {

  private final RestaurantService restaurantService;

  RestaurantController(RestaurantService restaurantService) {
    this.restaurantService = restaurantService;
  }

  @GetMapping("/api/v1/restaurants")
  @Operation(summary = "List restaurants, one page at a time")
  @ApiResponse(responseCode = "200", description = "A page; send nextCursor back as cursor")
  @ApiResponse(responseCode = "400", description = "The cursor is not valid")
  RestaurantPage list(
      @RequestParam(required = false) String area,
      @RequestParam(required = false) Boolean open,
      @RequestParam(defaultValue = "20") int limit,
      @RequestParam(required = false) String cursor) {
    return restaurantService.list(area, open, limit, cursor);
  }
}
