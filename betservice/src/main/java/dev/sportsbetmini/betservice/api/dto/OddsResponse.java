package dev.sportsbetmini.betservice.api.dto;

import dev.sportsbetmini.betservice.domain.Market;
import dev.sportsbetmini.betservice.domain.Selection;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record OddsResponse(
        UUID eventId,
        Market market,
        Selection selection,
        BigDecimal price,
        OffsetDateTime updatedAt
) {}