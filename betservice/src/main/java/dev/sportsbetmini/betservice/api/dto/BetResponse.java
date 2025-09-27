package dev.sportsbetmini.betservice.api.dto;

import dev.sportsbetmini.betservice.domain.*;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.math.BigDecimal;

public record BetResponse(
        UUID id, String userEmail, UUID eventId,
        Market market, Selection selection,
        BigDecimal stake, BigDecimal priceAtBet,
        BetStatus status, OffsetDateTime placedAt
) {}