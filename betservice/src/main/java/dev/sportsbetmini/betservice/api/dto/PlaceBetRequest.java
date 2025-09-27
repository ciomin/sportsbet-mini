package dev.sportsbetmini.betservice.api.dto;

import dev.sportsbetmini.betservice.domain.Market;
import dev.sportsbetmini.betservice.domain.Selection;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;
import java.math.BigDecimal;

public record PlaceBetRequest(
        @Email String userEmail,
        @NotNull UUID eventId,
        @NotNull Market market,
        @NotNull Selection selection,
        @Positive BigDecimal  stake
) {}