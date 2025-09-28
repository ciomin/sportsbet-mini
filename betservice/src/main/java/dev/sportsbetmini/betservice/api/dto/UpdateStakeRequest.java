package dev.sportsbetmini.betservice.api.dto;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record UpdateStakeRequest(
        @Positive BigDecimal stake
) {}