package com.fooddash.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
@Transactional
class RestaurantOrdersApiTest {

  static final String KACCHI_HOUSE = "d0000000-0000-4000-8000-000000000001";

  @Autowired MockMvc mvc;

  @Test
  void latestOrders_newestFirst_withTheCustomerName() throws Exception {
    mvc.perform(get("/api/v1/restaurants/" + KACCHI_HOUSE + "/orders"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].id").value("22222222-2222-4222-8222-222222222222"))
        .andExpect(jsonPath("$[0].customerName").value("Nusrat Jahan"))
        .andExpect(jsonPath("$[1].id").value("11111111-1111-4111-8111-111111111111"));
  }

  @Test
  void limit_capsTheList() throws Exception {
    mvc.perform(get("/api/v1/restaurants/" + KACCHI_HOUSE + "/orders?limit=1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1));
  }

  @Test
  void limitOutOfRange_returns400() throws Exception {
    mvc.perform(get("/api/v1/restaurants/" + KACCHI_HOUSE + "/orders?limit=0"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void unknownRestaurant_returns404() throws Exception {
    mvc.perform(get("/api/v1/restaurants/d0000000-0000-4000-8000-000000000099/orders"))
        .andExpect(status().isNotFound());
  }
}
