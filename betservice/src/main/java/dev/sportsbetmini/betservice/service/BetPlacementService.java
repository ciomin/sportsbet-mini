package dev.sportsbetmini.betservice.service;

import dev.sportsbetmini.betservice.api.dto.BetResponse;
import dev.sportsbetmini.betservice.api.dto.PlaceBetRequest;
import dev.sportsbetmini.betservice.domain.*;
import dev.sportsbetmini.betservice.repo.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static dev.sportsbetmini.betservice.repo.spec.BetSpecifications.filter;

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
    public BetResponse placeBet(String callerEmail, PlaceBetRequest req) {
        var user = users.findByEmail(callerEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found")); // no silent auto-create

        var event = events.findById(req.eventId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        var odds = oddsRepo.findByEventIdAndMarketAndSelection(event.getId(), req.market(), req.selection())
                .orElseThrow(() -> new IllegalArgumentException("No odds available for requested market/selection"));

        if (req.stake() == null || req.stake().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Stake must be > 0");
        }

        var bet = new BetEntity(
                UUID.randomUUID(),
                user.getId(),
                event.getId(),
                req.market(),
                req.selection(),
                req.stake(),
                odds.getPriceDecimal()
        );
        var saved = bets.save(bet);

        return new BetResponse(
                saved.getId(), user.getEmail(), saved.getEventId(),
                saved.getMarket(), saved.getSelection(),
                saved.getStake(), saved.getPriceAtBet(),
                saved.getStatus(), saved.getPlacedAt()
        );
    }

    @Transactional
    public Page<BetResponse> listBets(
            UUID eventId,
            String userEmail,
            BetStatus status,
            OffsetDateTime placedFrom,
            OffsetDateTime placedTo,
            Pageable pageable
    ) {
        UUID userId = null;
        String resolvedEmail = null;

        if (userEmail != null && !userEmail.isBlank()) {
            var u = users.findByEmail(userEmail).orElse(null);
            if (u == null) return Page.empty(pageable);
            userId = u.getId();
            resolvedEmail = u.getEmail();
        }

        var spec = filter(eventId, userId, status, placedFrom, placedTo);
        var page = bets.findAll(spec, pageable);

        final String emailForResolvedUser = resolvedEmail;
        return page.map(b -> {
            var email = (emailForResolvedUser != null) ? emailForResolvedUser
                    : users.findById(b.getUserId()).map(UserEntity::getEmail).orElse("unknown");
            return new BetResponse(
                    b.getId(), email, b.getEventId(),
                    b.getMarket(), b.getSelection(),
                    b.getStake(), b.getPriceAtBet(),
                    b.getStatus(), b.getPlacedAt()
            );
        });
    }

    @Transactional
    public BetResponse updateStake(UUID betId, BigDecimal newStake) {
        if (newStake == null || newStake.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Stake must be > 0");
        }

        var bet = bets.findById(betId)
                .orElseThrow(() -> new IllegalArgumentException("Bet not found"));
        var event = events.findById(bet.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        if (java.time.OffsetDateTime.now().isAfter(event.getStartTime())) {
            throw new IllegalArgumentException("Cannot update bet after event start");
        }

        bet.setStake(newStake);
        var saved = bets.save(bet);

        var email = users.findById(saved.getUserId()).map(UserEntity::getEmail).orElse("unknown");
        return new BetResponse(
                saved.getId(), email, saved.getEventId(),
                saved.getMarket(), saved.getSelection(),
                saved.getStake(), saved.getPriceAtBet(),
                saved.getStatus(), saved.getPlacedAt()
        );
    }

    @Transactional
    public void deleteBet(UUID betId) {
        var bet = bets.findById(betId)
                .orElseThrow(() -> new IllegalArgumentException("Bet not found"));
        var event = events.findById(bet.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        if (java.time.OffsetDateTime.now().isAfter(event.getStartTime())) {
            throw new IllegalArgumentException("Cannot delete bet after event start");
        }

        bets.deleteById(betId);
    }

    @Transactional
    public BetResponse getBet(UUID id) {
        var bet = bets.findById(id).orElseThrow(() -> new IllegalArgumentException("Bet not found"));
        var userEmail = users.findById(bet.getUserId()).map(UserEntity::getEmail).orElse("unknown");

        return new BetResponse(
                bet.getId(),
                userEmail,
                bet.getEventId(),
                bet.getMarket(),
                bet.getSelection(),
                bet.getStake(),
                bet.getPriceAtBet(),
                bet.getStatus(),
                bet.getPlacedAt()
        );
    }
}
