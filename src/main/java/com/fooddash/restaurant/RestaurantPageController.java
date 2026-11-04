package com.fooddash.restaurant;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** The home page: just enough server-rendered UI for the end-to-end and usability classes. */
@Controller
class RestaurantPageController {

  private final RestaurantService restaurantService;

  RestaurantPageController(RestaurantService restaurantService) {
    this.restaurantService = restaurantService;
  }

  @GetMapping("/")
  String home(Model model) {
    model.addAttribute("restaurants", restaurantService.list(null, null, 20, null).items());
    return "index";
  }
}
