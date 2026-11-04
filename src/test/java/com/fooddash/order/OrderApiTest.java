package com.fooddash.order;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/** Placing and reading orders against the seed data. Each test is rolled back afterwards. */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
@Transactional
class OrderApiTest {

  static final String KACCHI_HOUSE = "d0000000-0000-4000-8000-000000000001";
  static final String OLD_TOWN_BIRYANI = "d0000000-0000-4000-8000-000000000002";
  static final String KACCHI = "e0000000-0000-4000-8000-000000000001";
  static final String FIRNI_UNAVAILABLE = "e0000000-0000-4000-8000-000000000004";
  static final String OTHER_CUSTOMERS_ORDER = "44444444-4444-4444-8444-444444444444";

  @Autowired MockMvc mvc;

  static String order(String restaurantId, String menuItemId, int quantity, String phone) {
    return """
        { "restaurantId": "%s",
          "items": [ { "menuItemId": "%s", "quantity": %d } ],
          "deliveryAddress": { "line1": "House 12, Road 5", "area": "Dhanmondi", "phone": "%s" } }
        """
        .formatted(restaurantId, menuItemId, quantity, phone);
  }

  @Test
  void placeOrder_returns201WithLocation_andARetryWithTheSameKeyReturnsTheSameOrder()
      throws Exception {
    String body = order(KACCHI_HOUSE, KACCHI, 2, "+8801712345678");
    String first =
        mvc.perform(
                post("/api/v1/orders")
                    .header("Idempotency-Key", "key-1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", startsWith("/api/v1/orders/")))
            .andExpect(jsonPath("$.status").value("PAID"))
            .andExpect(jsonPath("$.subtotal").value("700.00"))
            .andExpect(jsonPath("$.deliveryFee").value("50.00"))
            .andExpect(jsonPath("$.total").value("750.00"))
            .andReturn()
            .getResponse()
            .getContentAsString();
    String orderId = JsonPath.read(first, "$.id");

    mvc.perform(
            post("/api/v1/orders")
                .header("Idempotency-Key", "key-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(orderId));
  }

  @Test
  void sameKeyForADifferentOrder_isRejected() throws Exception {
    mvc.perform(
            post("/api/v1/orders")
                .header("Idempotency-Key", "key-2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(order(KACCHI_HOUSE, KACCHI, 1, "+8801712345678")))
        .andExpect(status().isCreated());

    mvc.perform(
            post("/api/v1/orders")
                .header("Idempotency-Key", "key-2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(order(KACCHI_HOUSE, KACCHI, 3, "+8801712345678")))
        .andExpect(status().isUnprocessableContent())
        .andExpect(jsonPath("$.title").value("Idempotency key reused"));
  }

  @Test
  void orderFromAClosedRestaurant_returns422ProblemDetails() throws Exception {
    mvc.perform(
            post("/api/v1/orders")
                .header("Idempotency-Key", "key-3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(order(OLD_TOWN_BIRYANI, KACCHI, 1, "+8801712345678")))
        .andExpect(status().isUnprocessableContent())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.type").value("https://fooddash.example/problems/restaurant-closed"))
        .andExpect(jsonPath("$.status").value(422));
  }

  @Test
  void unavailableItem_returns422() throws Exception {
    mvc.perform(
            post("/api/v1/orders")
                .header("Idempotency-Key", "key-4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(order(KACCHI_HOUSE, FIRNI_UNAVAILABLE, 1, "+8801712345678")))
        .andExpect(status().isUnprocessableContent())
        .andExpect(jsonPath("$.title").value("Menu item unavailable"));
  }

  @Test
  void invalidFields_areListedInTheProblem() throws Exception {
    mvc.perform(
            post("/api/v1/orders")
                .header("Idempotency-Key", "key-5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(order(KACCHI_HOUSE, KACCHI, 0, "01712345678")))
        .andExpect(status().isUnprocessableContent())
        .andExpect(jsonPath("$.errors", hasSize(2)));
  }

  @Test
  void missingIdempotencyKey_returns400() throws Exception {
    mvc.perform(
            post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(order(KACCHI_HOUSE, KACCHI, 1, "+8801712345678")))
        .andExpect(status().isBadRequest());
  }

  @Test
  void anotherCustomersOrder_looksNotFound() throws Exception {
    mvc.perform(get("/api/v1/orders/" + OTHER_CUSTOMERS_ORDER))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.title").value("Order not found"));
  }
}
