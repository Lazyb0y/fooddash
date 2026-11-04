package com.fooddash.menu;

import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
class MenuPageController {

  private final MenuService menuService;

  MenuPageController(MenuService menuService) {
    this.menuService = menuService;
  }

  @GetMapping("/restaurants/{id}")
  String menu(@PathVariable UUID id, Model model) {
    model.addAttribute("menu", menuService.menuFor(id));
    return "menu";
  }
}
