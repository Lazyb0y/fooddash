package com.fooddash.payment;

import com.fooddash.shared.Money;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * The only class that knows the mobile wallet provider exists. Switched on with
 * fooddash.payment.provider=wallet and a WALLET_API_KEY. The provider is invented for the course.
 */
@Component
@ConditionalOnProperty(name = "fooddash.payment.provider", havingValue = "wallet")
class MobileWalletGateway implements PaymentGateway {

  private final RestClient client;

  MobileWalletGateway(
      @Value("${fooddash.wallet.base-url}") String baseUrl,
      @Value("${fooddash.wallet.api-key}") String apiKey) {
    if (apiKey.isBlank()) {
      throw new IllegalStateException("Set WALLET_API_KEY to use the mobile wallet gateway");
    }
    SimpleClientHttpRequestFactory timeouts = new SimpleClientHttpRequestFactory();
    timeouts.setConnectTimeout(Duration.ofSeconds(2));
    timeouts.setReadTimeout(Duration.ofSeconds(5));
    this.client =
        RestClient.builder()
            .baseUrl(baseUrl)
            .requestFactory(timeouts)
            .defaultHeader("Authorization", "Bearer " + apiKey)
            .build();
  }

  @Override
  public PaymentResult charge(UUID orderId, BigDecimal amount, String idempotencyKey) {
    ChargeResponse response =
        client
            .post()
            .uri("/charges")
            .header("Idempotency-Key", idempotencyKey)
            .body(new ChargeRequest(orderId.toString(), Money.format(amount), "BDT"))
            .retrieve()
            .body(ChargeResponse.class);
    if (response == null || !"approved".equals(response.status())) {
      return PaymentResult.declined(
          response == null ? "No answer from the wallet" : response.reason());
    }
    return PaymentResult.approved(response.chargeId());
  }

  @Override
  public void refund(UUID orderId, BigDecimal amount) {
    client
        .post()
        .uri("/refunds")
        .body(new RefundRequest(orderId.toString(), Money.format(amount), "BDT"))
        .retrieve()
        .toBodilessEntity();
  }

  private record ChargeRequest(String reference, String amount, String currency) {}

  private record ChargeResponse(String chargeId, String status, String reason) {}

  private record RefundRequest(String reference, String amount, String currency) {}
}
