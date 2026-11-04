package com.fooddash.shared;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AppConfig {

  /** Code asks this clock for the time instead of calling now(), so tests can control time. */
  @Bean
  Clock clock() {
    return Clock.systemUTC();
  }

  @Bean
  OpenAPI fooddashApi() {
    return new OpenAPI()
        .info(
            new Info()
                .title("FoodDash API")
                .version("v1")
                .description(
                    "Restaurants, menus, orders and riders. Errors use RFC 9457 Problem Details."));
  }
}
