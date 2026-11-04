package com.fooddash.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Orders")
class OrderController {

  private final OrderService orderService;

  OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping("/api/v1/orders")
  @Operation(summary = "Place an order")
  @ApiResponse(responseCode = "201", description = "Placed; a retry with the same key returns it")
  @ApiResponse(responseCode = "400", description = "Malformed request or no Idempotency-Key")
  @ApiResponse(responseCode = "422", description = "Closed, unavailable, invalid or declined")
  ResponseEntity<OrderResponse> place(
      @Parameter(
              description = "A new UUID for each order; send the same one when you retry it",
              example = "6f1c9a2e-0d7b-4a51-9a3e-2b8c7d1e4f60")
          @RequestHeader("Idempotency-Key")
          String idempotencyKey,
      @Valid @RequestBody PlaceOrderRequest request) {
    Order order = orderService.place(request, idempotencyKey);
    URI location = URI.create("/api/v1/orders/" + order.getPublicId());
    return ResponseEntity.created(location).body(OrderResponse.from(order));
  }

  @GetMapping("/api/v1/orders/{id}")
  @Operation(summary = "One of the customer's own orders")
  @ApiResponse(responseCode = "200", description = "The order")
  @ApiResponse(responseCode = "404", description = "No such order for this customer")
  OrderResponse get(@PathVariable UUID id) {
    return OrderResponse.from(orderService.get(id));
  }
}
