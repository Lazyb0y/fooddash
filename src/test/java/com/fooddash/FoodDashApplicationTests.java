package com.fooddash;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/** Starts the whole app, migrations included, on an in-memory database. */
@SpringBootTest
@ActiveProfiles("h2")
class FoodDashApplicationTests {

  @Test
  void contextLoads() {}
}
