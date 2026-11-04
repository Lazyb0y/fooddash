package com.fooddash.menu;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
class MenuApiTest {

  static final String HAJI_BIRYANI = "d0000000-0000-4000-8000-000000000006";

  @Autowired MockMvc mvc;

  @Test
  void menu_showsBanglaNamesAndPricesAsStrings() throws Exception {
    mvc.perform(get("/api/v1/restaurants/" + HAJI_BIRYANI + "/menu"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.restaurantName").value("হাজী বিরিয়ানি (Haji Biryani)"))
        .andExpect(jsonPath("$.items[0].price").value("300.00"));
  }

  @Test
  void menuOfAnUnknownRestaurant_returns404ProblemDetails() throws Exception {
    mvc.perform(get("/api/v1/restaurants/" + UUID.randomUUID() + "/menu"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.title").value("Restaurant not found"));
  }

  @Test
  void menuPage_showsTheItems() throws Exception {
    mvc.perform(get("/restaurants/" + HAJI_BIRYANI))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("300.00")));
  }
}
