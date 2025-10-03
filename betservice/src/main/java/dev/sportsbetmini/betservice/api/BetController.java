package dev.sportsbetmini.betservice.api;

import dev.sportsbetmini.betservice.api.dto.BetResponse;
import dev.sportsbetmini.betservice.api.dto.PlaceBetRequest;
import dev.sportsbetmini.betservice.api.dto.UpdateStakeRequest;
import dev.sportsbetmini.betservice.domain.BetStatus;
import dev.sportsbetmini.betservice.service.BetPlacementService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/bets")
public class BetController {

    private final BetPlacementService betService;

    public BetController(BetPlacementService betService) {
        this.betService = betService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<BetResponse> place(Authentication auth, @Valid @RequestBody PlaceBetRequest req) {
        return ResponseEntity.ok(betService.placeBet(auth.getName(), req)); // pass caller email
    }

    @PreAuthorize("hasRole('USER') and @authz.canReadBet(#id, authentication)")
    @GetMapping("/{id}")
    public ResponseEntity<BetResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(betService.getBet(id));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public Page<BetResponse> list(
            @RequestParam(required = false) UUID eventId,
            @RequestParam(required = false) String userEmail,
            @RequestParam(required = false) BetStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime placedFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime placedTo,
            @PageableDefault(size = 20, sort = "placedAt") Pageable pageable
    ) {
        return betService.listBets(eventId, userEmail, status, placedFrom, placedTo, pageable);
    }

    @PreAuthorize("hasRole('USER') and @authz.emailVerified(authentication.name)")
    @PutMapping("/{id}/stake")
    public ResponseEntity<BetResponse> updateStake(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateStakeRequest body
    ) {
        return ResponseEntity.ok(betService.updateStake(id, body.stake()));
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        betService.deleteBet(id);
        return ResponseEntity.noContent().build();
    }
}
