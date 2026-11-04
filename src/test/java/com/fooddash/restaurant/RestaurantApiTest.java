package com.fooddash.restaurant;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/** Restaurant listing against the seed data (8 restaurants), on an in-memory database. */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
class RestaurantApiTest {

  @Autowired MockMvc mvc;

  @Test
  void restaurants_comeInPagesWithACursor() throws Exception {
    String first =
        mvc.perform(get("/api/v1/restaurants").param("limit", "3"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.items", hasSize(3)))
            .andReturn()
            .getResponse()
            .getContentAsString();
    String cursor = JsonPath.read(first, "$.nextCursor");

    mvc.perform(get("/api/v1/restaurants").param("limit", "100").param("cursor", cursor))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.items", hasSize(5)))
        .andExpect(jsonPath("$.nextCursor").value(nullValue()));
  }

  @Test
  void restaurants_canBeFilteredByAreaAndOpening() throws Exception {
    mvc.perform(get("/api/v1/restaurants").param("area", "Old Dhaka").param("open", "true"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.items", hasSize(1)));
  }

  @Test
  void badCursor_returns400() throws Exception {
    mvc.perform(get("/api/v1/restaurants").param("cursor", "not-a-cursor"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void homePage_listsRestaurants() throws Exception {
    mvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Dhanmondi Kacchi House")));
  }
}
