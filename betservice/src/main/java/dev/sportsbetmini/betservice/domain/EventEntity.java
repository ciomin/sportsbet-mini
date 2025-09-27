package dev.sportsbetmini.betservice.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity @Table(name = "events")
public class EventEntity {
    @Id private UUID id;
    @Column(nullable = false) private String sport;
    @Column(name="home_team", nullable=false) private String homeTeam;
    @Column(name="away_team", nullable=false) private String awayTeam;
    @Column(name="start_time", nullable=false) private OffsetDateTime startTime;

    public EventEntity() {}
    public EventEntity(UUID id, String sport, String homeTeam, String awayTeam, OffsetDateTime startTime) {
        this.id=id; this.sport=sport; this.homeTeam=homeTeam; this.awayTeam=awayTeam; this.startTime=startTime;
    }

    public UUID getId() { return id; }
    public String getSport() { return sport; }
    public String getHomeTeam() { return homeTeam; }
    public String getAwayTeam() { return awayTeam; }
    public OffsetDateTime getStartTime() { return startTime; }
}
