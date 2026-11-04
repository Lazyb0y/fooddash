package com.fooddash.rider;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
@Transactional
class RiderApiTest {

  static final String UNASSIGNED_ORDER = "33333333-3333-4333-8333-333333333333";
  static final String RIDER_A = "aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaaa";
  static final String RIDER_B = "bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbbbbbb";

  @Autowired MockMvc mvc;

  static String rider(String riderId) {
    return "{ \"riderId\": \"" + riderId + "\" }";
  }

  @Test
  void secondRider_getsAConflict() throws Exception {
    mvc.perform(
            post("/api/v1/orders/" + UNASSIGNED_ORDER + "/assignment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(rider(RIDER_A)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.riderId").value(RIDER_A));

    mvc.perform(
            post("/api/v1/orders/" + UNASSIGNED_ORDER + "/assignment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(rider(RIDER_B)))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.title").value("Order already has a rider"));
  }

  @Test
  void sameRiderAcceptingTwice_isFine() throws Exception {
    for (int attempt = 0; attempt < 2; attempt++) {
      mvc.perform(
              post("/api/v1/orders/" + UNASSIGNED_ORDER + "/assignment")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(rider(RIDER_A)))
          .andExpect(status().isOk());
    }
  }

  @Test
  void unknownRider_returns404() throws Exception {
    mvc.perform(
            post("/api/v1/orders/" + UNASSIGNED_ORDER + "/assignment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(rider(UUID.randomUUID().toString())))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.title").value("Rider not found"));
  }
}
