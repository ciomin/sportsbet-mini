package dev.sportsbetmini.betservice.api;

import dev.sportsbetmini.betservice.api.dto.*;
import dev.sportsbetmini.betservice.domain.BetEntity;
import dev.sportsbetmini.betservice.repo.*;
import dev.sportsbetmini.betservice.service.BetPlacementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/bets")
public class BetController {

    private final BetPlacementService betService;
    private final BetRepository bets;
    private final UserRepository users;

    public BetController(BetPlacementService betService, BetRepository bets, UserRepository users) {
        this.betService = betService; this.bets = bets; this.users = users;
    }

    @PostMapping
    public ResponseEntity<BetResponse> place(@Valid @RequestBody PlaceBetRequest req) {
        BetEntity saved = betService.placeBet(req);
        String email = users.findById(saved.getUserId()).map(u -> u.getEmail()).orElse("unknown");
        return ResponseEntity.ok(new BetResponse(
                saved.getId(), email, saved.getEventId(),
                saved.getMarket(), saved.getSelection(),
                saved.getStake(), saved.getPriceAtBet(),
                saved.getStatus(), saved.getPlacedAt()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BetResponse> get(@PathVariable UUID id) {
        return bets.findById(id)
                .map(b -> {
                    String email = users.findById(b.getUserId()).map(u -> u.getEmail()).orElse("unknown");
                    return ResponseEntity.ok(new BetResponse(
                            b.getId(), email, b.getEventId(),
                            b.getMarket(), b.getSelection(),
                            b.getStake(), b.getPriceAtBet(),
                            b.getStatus(), b.getPlacedAt()
                    ));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> badRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ErrorResponse.of("BAD_REQUEST", ex.getMessage()));
    }
}
