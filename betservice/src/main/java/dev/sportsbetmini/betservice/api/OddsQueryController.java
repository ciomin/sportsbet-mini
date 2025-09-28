package dev.sportsbetmini.betservice.api;

import dev.sportsbetmini.betservice.domain.OddsEntity;
import dev.sportsbetmini.betservice.repo.OddsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/odds")
public class OddsQueryController {
    private final OddsRepository repo;
    public OddsQueryController(OddsRepository repo) { this.repo = repo; }

    @GetMapping("/{eventId}")
    public Page<OddsEntity> byEvent(@PathVariable UUID eventId,
                                    @PageableDefault(size = 20, sort = "updatedAt") Pageable pageable) {
        return repo.findByEventId(eventId, pageable);
    }
}

