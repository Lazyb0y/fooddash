package com.fooddash.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** The order history restaurant staff see. */
@RestController
@Tag(name = "Orders")
class RestaurantOrderController {

  private final OrderService orderService;

  RestaurantOrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping("/api/v1/restaurants/{id}/orders")
  @Operation(summary = "A restaurant's latest orders, newest first")
  @ApiResponse(responseCode = "200", description = "Up to `limit` orders")
  @ApiResponse(responseCode = "400", description = "limit is not between 1 and 100")
  @ApiResponse(responseCode = "404", description = "No such restaurant")
  List<RestaurantOrderResponse> latest(
      @PathVariable UUID id, @RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit) {
    return orderService.latestForRestaurant(id, limit);
  }
}
