package com.fooddash.rider;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "rider_rating")
public class RiderRating {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "rider_id")
  private Long riderId;

  @JdbcTypeCode(SqlTypes.CHAR)
  @Column(name = "order_id", length = 36)
  private UUID orderId;

  private int stars;

  protected RiderRating() {}

  public RiderRating(Long riderId, UUID orderId, int stars) {
    this.riderId = riderId;
    this.orderId = orderId;
    this.stars = stars;
  }
}
