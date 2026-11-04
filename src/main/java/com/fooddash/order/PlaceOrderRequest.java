package com.fooddash.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

public record PlaceOrderRequest(
    @Schema(example = "d0000000-0000-4000-8000-000000000001") @NotNull UUID restaurantId,
    @NotEmpty @Size(min = 1, max = 20) List<@Valid @NotNull OrderLine> items,
    @NotNull @Valid Address deliveryAddress) {

  public record OrderLine(
      @Schema(example = "e0000000-0000-4000-8000-000000000001") @NotNull UUID menuItemId,
      @Schema(example = "2") @Min(1) @Max(20) int quantity) {}

  public record Address(
      @Schema(example = "House 12, Road 5") @NotBlank @Size(max = 200) String line1,
      @Schema(example = "Dhanmondi") @NotBlank @Size(max = 50) String area,
      @Schema(example = "+8801712345678")
          @NotBlank
          @Pattern(
              regexp = "\\+[1-9][0-9]{7,14}",
              message = "must be in E.164 format, e.g. +8801712345678")
          String phone) {}

  /** A fingerprint of the request, to spot an Idempotency-Key reused for a different order. */
  String fingerprint() {
    try {
      byte[] digest =
          MessageDigest.getInstance("SHA-256").digest(toString().getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(digest);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("SHA-256 is always available", e);
    }
  }
}
