package com.fooddash.rider;

import com.fooddash.order.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Riders")
class RiderController {

  private final AssignmentService assignmentService;

  RiderController(AssignmentService assignmentService) {
    this.assignmentService = assignmentService;
  }

  @PostMapping("/api/v1/orders/{id}/assignment")
  @Operation(summary = "A rider accepts an order")
  @ApiResponse(responseCode = "200", description = "The order, now with this rider")
  @ApiResponse(responseCode = "404", description = "No such order or rider")
  @ApiResponse(responseCode = "409", description = "Another rider already took the order")
  OrderResponse accept(@PathVariable UUID id, @Valid @RequestBody AssignRiderRequest request) {
    return OrderResponse.from(assignmentService.accept(id, request.riderId()));
  }
}
