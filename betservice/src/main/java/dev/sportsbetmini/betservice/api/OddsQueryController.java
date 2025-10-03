package dev.sportsbetmini.betservice.api;

import dev.sportsbetmini.betservice.domain.OddsEntity;
import dev.sportsbetmini.betservice.api.dto.OddsResponse;
import dev.sportsbetmini.betservice.repo.OddsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/odds")
public class OddsQueryController {
    private final OddsRepository repo;
    public OddsQueryController(OddsRepository repo) { this.repo = repo; }

    @GetMapping("/{eventId}")
    public Page<OddsResponse> byEvent(@PathVariable UUID eventId,
                                      @PageableDefault(size = 20, sort = "updatedAt") Pageable pageable) {
        return repo.findByEventId(eventId, pageable)
                .map(e -> new OddsResponse(
                        e.getEventId(), e.getMarket(), e.getSelection(),
                        e.getPriceDecimal(), e.getUpdatedAt()
                ));
    }
}

