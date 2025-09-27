package dev.sportsbetmini.betservice.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class OddsId implements Serializable {
    public UUID eventId;
    public Market market;
    public Selection selection;

    public OddsId() {}
    public OddsId(UUID eventId, Market market, Selection selection) {
        this.eventId = eventId; this.market = market; this.selection = selection;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OddsId that)) return false;
        return Objects.equals(eventId, that.eventId) && market == that.market && selection == that.selection;
    }
    @Override public int hashCode() { return Objects.hash(eventId, market, selection); }
}