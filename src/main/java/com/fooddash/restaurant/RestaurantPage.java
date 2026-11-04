package com.fooddash.restaurant;

import java.util.List;

/** A page of restaurants. Send nextCursor back as cursor to get the next page; null on the last. */
public record RestaurantPage(List<RestaurantResponse> items, String nextCursor) {}
