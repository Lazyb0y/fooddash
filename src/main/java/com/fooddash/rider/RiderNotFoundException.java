package com.fooddash.rider;

import com.fooddash.shared.NotFoundException;
import java.util.UUID;

public class RiderNotFoundException extends NotFoundException {

  public RiderNotFoundException(UUID id) {
    super("rider-not-found", "Rider not found", "No rider with id " + id + ".");
  }
}
