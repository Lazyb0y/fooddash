package com.fooddash.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

/** An Idempotency-Key already used to place an order, with a fingerprint of that request. */
@Entity
@Table(name = "idempotency_key")
class IdempotencyRecord {

  @Id
  @Column(name = "idem_key", length = 64)
  private String key;

  @Column(name = "request_hash", nullable = false, length = 64)
  private String requestHash;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "order_id")
  private Order order;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  protected IdempotencyRecord() {}

  IdempotencyRecord(String key, String requestHash, Order order, Instant createdAt) {
    this.key = key;
    this.requestHash = requestHash;
    this.order = order;
    this.createdAt = createdAt;
  }

  boolean sameRequest(String hash) {
    return requestHash.equals(hash);
  }

  Order getOrder() {
    return order;
  }
}
