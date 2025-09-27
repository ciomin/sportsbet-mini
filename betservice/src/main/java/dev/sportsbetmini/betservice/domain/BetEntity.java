package dev.sportsbetmini.betservice.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.math.BigDecimal;

@Entity @Table(name = "bets")
public class BetEntity {
    @Id private UUID id;

    @Column(name="user_id", nullable=false) private UUID userId;
    @Column(name="event_id", nullable=false) private UUID eventId;

    @Enumerated(EnumType.STRING) @Column(nullable=false) private Market market;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private Selection selection;

    @Column(nullable=false) private BigDecimal stake;
    @Column(name="price_at_bet", nullable=false) private BigDecimal priceAtBet;

    @Enumerated(EnumType.STRING) @Column(nullable=false) private BetStatus status = BetStatus.PENDING;
    @Column(name="placed_at", nullable=false) private OffsetDateTime placedAt = OffsetDateTime.now();

    public BetEntity() {}
    public BetEntity(UUID id, UUID userId, UUID eventId, Market market, Selection selection, BigDecimal stake, BigDecimal priceAtBet) {
        this.id = id; this.userId=userId; this.eventId=eventId; this.market=market; this.selection=selection; this.stake=stake; this.priceAtBet=priceAtBet;
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public UUID getEventId() { return eventId; }
    public Market getMarket() { return market; }
    public Selection getSelection() { return selection; }
    public BigDecimal getStake() { return stake; }
    public BigDecimal getPriceAtBet() { return priceAtBet; }
    public BetStatus getStatus() { return status; }
    public OffsetDateTime getPlacedAt() { return placedAt; }
}