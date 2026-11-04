package com.fooddash;

import org.springframework.boot.SpringApplication;

/**
 * Runs FoodDash without MySQL, on an in-memory database: ./mvnw spring-boot:test-run
 * -Dspring-boot.run.profiles=h2
 */
class TestFoodDashApplication {

  public static void main(String[] args) {
    SpringApplication.from(FoodDashApplication::main).run(args);
  }
}
