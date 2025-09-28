package dev.sportsbetmini.betservice.repo;

import dev.sportsbetmini.betservice.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface OddsRepository extends JpaRepository<OddsEntity, OddsId> {
    Optional<OddsEntity> findByEventIdAndMarketAndSelection(UUID eventId, Market market, Selection selection);
    List<OddsEntity> findByEventId(UUID eventId);
    Page<OddsEntity> findByEventId(UUID eventId, Pageable pageable);
}