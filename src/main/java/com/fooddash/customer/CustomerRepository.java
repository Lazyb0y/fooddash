package com.fooddash.customer;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface CustomerRepository extends JpaRepository<Customer, Long> {

  Optional<Customer> findByPublicId(UUID publicId);
}
