package com.fooddash.rider;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "rider")
public class Rider {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JdbcTypeCode(SqlTypes.CHAR)
  @Column(name = "public_id", nullable = false, unique = true, length = 36)
  private UUID publicId;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String phone;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  protected Rider() {}

  public Long getId() {
    return id;
  }

  public UUID getPublicId() {
    return publicId;
  }

  public String getName() {
    return name;
  }
}
