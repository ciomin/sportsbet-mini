package dev.sportsbetmini.betservice.repo;

import dev.sportsbetmini.betservice.domain.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface EventRepository extends JpaRepository<EventEntity, UUID> {}