package com.fooddash.menu;

import com.fooddash.restaurant.Restaurant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "menu_item")
public class MenuItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JdbcTypeCode(SqlTypes.CHAR)
  @Column(name = "public_id", nullable = false, unique = true, length = 36)
  private UUID publicId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "restaurant_id")
  private Restaurant restaurant;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal price;

  @Column(nullable = false)
  private boolean available;

  protected MenuItem() {}

  public Long getId() {
    return id;
  }

  public UUID getPublicId() {
    return publicId;
  }

  public Restaurant getRestaurant() {
    return restaurant;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public boolean isAvailable() {
    return available;
  }
}
