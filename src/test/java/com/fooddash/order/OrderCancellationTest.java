package com.fooddash.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fooddash.payment.FakePaymentGateway;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/** The cancellation rules in OrderService, against the seed orders. */
@SpringBootTest
@ActiveProfiles("h2")
@Transactional
class OrderCancellationTest {

  static final UUID PLACED_ORDER = UUID.fromString("11111111-1111-4111-8111-111111111111");
  static final UUID PICKED_UP_ORDER = UUID.fromString("22222222-2222-4222-8222-222222222222");
  static final UUID PAID_ORDER = UUID.fromString("33333333-3333-4333-8333-333333333333");
  static final UUID OTHER_CUSTOMERS_ORDER = UUID.fromString("44444444-4444-4444-8444-444444444444");

  @Autowired OrderService orderService;
  @Autowired FakePaymentGateway payments;

  @Test
  void placedOrder_isCancelledWithoutARefund() {
    assertEquals(OrderStatus.CANCELLED, orderService.cancel(PLACED_ORDER).getStatus());
    assertTrue(payments.refunds().stream().noneMatch(r -> r.startsWith(PLACED_ORDER.toString())));
  }

  @Test
  void paidOrder_isRefunded() {
    orderService.cancel(PAID_ORDER);
    assertTrue(payments.refunds().contains(PAID_ORDER + " 490.00"));
  }

  @Test
  void pickedUpOrder_cannotBeCancelled() {
    assertThrows(OrderAlreadyPickedUpException.class, () -> orderService.cancel(PICKED_UP_ORDER));
  }

  @Test
  void cancellingTwice_changesNothing() {
    orderService.cancel(PLACED_ORDER);
    assertEquals(OrderStatus.CANCELLED, orderService.cancel(PLACED_ORDER).getStatus());
  }

  @Test
  void anotherCustomersOrder_looksNotFound() {
    assertThrows(OrderNotFoundException.class, () -> orderService.cancel(OTHER_CUSTOMERS_ORDER));
  }
}
