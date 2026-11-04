package com.fooddash.order;

import com.fooddash.menu.MenuItem;
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

@Entity
@Table(name = "order_item")
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "order_id")
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "menu_item_id")
  private MenuItem menuItem;

  @Column(nullable = false)
  private int quantity;

  @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
  private BigDecimal unitPrice;

  protected OrderItem() {}

  OrderItem(Order order, MenuItem menuItem, int quantity) {
    this.order = order;
    this.menuItem = menuItem;
    this.quantity = quantity;
    this.unitPrice = menuItem.getPrice();
  }

  BigDecimal lineTotal() {
    return unitPrice.multiply(BigDecimal.valueOf(quantity));
  }

  public MenuItem getMenuItem() {
    return menuItem;
  }

  public int getQuantity() {
    return quantity;
  }

  public BigDecimal getUnitPrice() {
    return unitPrice;
  }
}
