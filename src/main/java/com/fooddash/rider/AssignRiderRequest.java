package com.fooddash.rider;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AssignRiderRequest(
    @Schema(example = "aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaaa") @NotNull UUID riderId) {}
