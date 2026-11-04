package com.fooddash.rider;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
class RiderRatingControllerTest {

  @Autowired MockMvc mvc;

  @Test
  void test1() throws Exception {
    mvc.perform(
            get("/api/v1/riders/1/rate")
                .param("stars", "5")
                .param("orderId", "11111111-1111-4111-8111-111111111111"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.average").exists());
  }
}
