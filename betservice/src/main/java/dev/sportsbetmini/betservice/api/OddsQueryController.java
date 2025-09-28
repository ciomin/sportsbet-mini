package dev.sportsbetmini.betservice.api;

import dev.sportsbetmini.betservice.domain.OddsEntity;
import dev.sportsbetmini.betservice.repo.OddsRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/odds")
public class OddsQueryController {
    private final OddsRepository repo;
    public OddsQueryController(OddsRepository repo) { this.repo = repo; }

    @GetMapping("/{eventId}")
    public List<OddsEntity> byEvent(@PathVariable UUID eventId) {
        return repo.findByEventId(eventId);
    }
}