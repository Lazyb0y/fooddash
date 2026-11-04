package com.fooddash.customer;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Who is calling. A demo stub until sign-in arrives in Class 10: every request acts as the demo
 * customer named in the configuration.
 */
@Component
public class CurrentCustomer {

  private final CustomerRepository customers;
  private final UUID demoCustomerId;

  CurrentCustomer(
      CustomerRepository customers, @Value("${fooddash.demo.customer-id}") UUID demoCustomerId) {
    this.customers = customers;
    this.demoCustomerId = demoCustomerId;
  }

  public Customer get() {
    return customers
        .findByPublicId(demoCustomerId)
        .orElseThrow(
            () -> new IllegalStateException("Demo customer " + demoCustomerId + " is missing"));
  }
}
