package dev.sportsbetmini.betservice.repo;

import dev.sportsbetmini.betservice.domain.BetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface BetRepository extends JpaRepository<BetEntity, UUID> {}