package com.fooddash.menu;

import java.util.List;
import java.util.UUID;

public record MenuResponse(
    UUID restaurantId, String restaurantName, boolean open, List<MenuItemResponse> items) {}
