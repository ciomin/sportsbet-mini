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

    public BetController(BetPlacementService betService) {
        this.betService = betService;
    }

    @PostMapping
    public ResponseEntity<BetResponse> place(@Valid @RequestBody PlaceBetRequest req) {
        return ResponseEntity.ok(betService.placeBet(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BetResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(betService.getBet(id));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> badRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ErrorResponse.of("BAD_REQUEST", ex.getMessage()));
    }
}
