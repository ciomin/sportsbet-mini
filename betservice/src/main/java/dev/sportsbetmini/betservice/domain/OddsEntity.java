package dev.sportsbetmini.betservice.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.math.BigDecimal;

@Entity @Table(name = "odds")
@IdClass(OddsId.class)
public class OddsEntity {
    @Id @Column(name="event_id") private UUID eventId;
    @Id @Enumerated(EnumType.STRING) private Market market;
    @Id @Enumerated(EnumType.STRING) private Selection selection;

    @Column(name="price_decimal", nullable=false) private BigDecimal priceDecimal;
    @Column(name="updated_at", nullable=false) private OffsetDateTime updatedAt = OffsetDateTime.now();

    public OddsEntity() {}

    public OddsEntity(UUID eventId, Market market, Selection selection, BigDecimal priceDecimal) {
        this.eventId = eventId; this.market = market; this.selection = selection;
        this.priceDecimal = priceDecimal;
    }

    // NEW: convenience constructor that sets updatedAt explicitly
    public OddsEntity(UUID eventId, Market market, Selection selection, BigDecimal priceDecimal, OffsetDateTime updatedAt) {
        this.eventId = eventId; this.market = market; this.selection = selection;
        this.priceDecimal = priceDecimal;
        this.updatedAt = (updatedAt != null) ? updatedAt : OffsetDateTime.now();
    }

    public UUID getEventId() { return eventId; }
    public Market getMarket() { return market; }
    public Selection getSelection() { return selection; }
    public BigDecimal getPriceDecimal() { return priceDecimal; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    // NEW: minimal setters used by the consumer upsert
    public void setPriceDecimal(BigDecimal priceDecimal) { this.priceDecimal = priceDecimal; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = (updatedAt != null) ? updatedAt : OffsetDateTime.now(); }
}
