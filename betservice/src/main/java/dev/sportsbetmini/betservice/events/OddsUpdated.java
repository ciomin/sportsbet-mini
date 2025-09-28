package dev.sportsbetmini.betservice.events;

import dev.sportsbetmini.betservice.domain.Market;
import dev.sportsbetmini.betservice.domain.Selection;

import java.time.OffsetDateTime;
import java.util.UUID;

public record OddsUpdated(
        UUID eventId,
        Market market,
        Selection selection,
        java.math.BigDecimal price,
        OffsetDateTime updatedAt
) {}