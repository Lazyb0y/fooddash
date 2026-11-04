package com.fooddash.payment;

import com.fooddash.shared.Money;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/** Local runs and tests: no network, no keys. Approves every charge unless told to decline one. */
@Component
@ConditionalOnProperty(
    name = "fooddash.payment.provider",
    havingValue = "fake",
    matchIfMissing = true)
public class FakePaymentGateway implements PaymentGateway {

  private final AtomicBoolean declineNext = new AtomicBoolean();
  private final List<String> refunds = new CopyOnWriteArrayList<>();

  @Override
  public PaymentResult charge(UUID orderId, BigDecimal amount, String idempotencyKey) {
    if (declineNext.getAndSet(false)) {
      return PaymentResult.declined("The fake gateway was told to decline this charge.");
    }
    return PaymentResult.approved("FAKE-" + idempotencyKey);
  }

  @Override
  public void refund(UUID orderId, BigDecimal amount) {
    refunds.add(orderId + " " + Money.format(amount));
  }

  public void declineNextCharge() {
    declineNext.set(true);
  }

  public List<String> refunds() {
    return List.copyOf(refunds);
  }
}
