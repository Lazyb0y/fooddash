package com.fooddash.rider;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface RiderRepository extends JpaRepository<Rider, Long> {

  Optional<Rider> findByPublicId(UUID publicId);
}
