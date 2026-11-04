package com.fooddash.menu;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Restaurants")
class MenuController {

  private final MenuService menuService;

  MenuController(MenuService menuService) {
    this.menuService = menuService;
  }

  @GetMapping("/api/v1/restaurants/{id}/menu")
  @Operation(summary = "A restaurant's menu")
  @ApiResponse(responseCode = "200", description = "The menu")
  @ApiResponse(responseCode = "404", description = "No such restaurant")
  MenuResponse menu(@PathVariable UUID id) {
    return menuService.menuFor(id);
  }
}
