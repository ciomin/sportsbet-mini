package dev.sportsbetmini.betservice.service;

import dev.sportsbetmini.betservice.api.dto.PlaceBetRequest;
import dev.sportsbetmini.betservice.domain.*;
import dev.sportsbetmini.betservice.repo.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.math.BigDecimal;

@Service
public class BetPlacementService {

    private final UserRepository users;
    private final EventRepository events;
    private final OddsRepository oddsRepo;
    private final BetRepository bets;

    public BetPlacementService(UserRepository users, EventRepository events, OddsRepository oddsRepo, BetRepository bets) {
        this.users = users; this.events = events; this.oddsRepo = oddsRepo; this.bets = bets;
    }

    @Transactional
    public BetEntity placeBet(PlaceBetRequest req) {
        var user = users.findByEmail(req.userEmail())
                .orElseGet(() -> users.save(new UserEntity(UUID.randomUUID(), req.userEmail())));

        var event = events.findById(req.eventId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        var odds = oddsRepo.findByEventIdAndMarketAndSelection(event.getId(), req.market(), req.selection())
                .orElseThrow(() -> new IllegalArgumentException("No odds available for requested market/selection"));

        if (req.stake() == null || req.stake().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Stake must be > 0");

        var bet = new BetEntity(
                UUID.randomUUID(),
                user.getId(),
                event.getId(),
                req.market(),
                req.selection(),
                req.stake(),
                odds.getPriceDecimal()
        );
        return bets.save(bet);
    }
}