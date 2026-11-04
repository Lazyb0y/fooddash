package com.fooddash.rider;

import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class RiderRatingController {

  @Autowired private JdbcTemplate jdbc;
  @Autowired private RiderRatingRepository repo;

  @Value("${sms.api-key}")
  private String smsKey;

  // rate the rider
  @GetMapping("/api/v1/riders/{id}/rate")
  public Map<String, Object> doIt(
      @PathVariable Long id, @RequestParam int stars, @RequestParam UUID orderId) {
    if (stars > 5) {
      return Map.of("error", "bad stars");
    }
    RiderRating x = new RiderRating(id, orderId, stars);
    repo.save(x);
    double tmp2 = 0;
    try {
      tmp2 =
          jdbc.queryForObject(
              "SELECT AVG(stars) FROM rider_rating WHERE rider_id = " + id, Double.class);
    } catch (Exception e) {
    }
    // TODO text the rider their new average with smsKey
    return Map.of("average", tmp2);
  }
}
