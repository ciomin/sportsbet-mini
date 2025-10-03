package dev.sportsbetmini.betservice.repo.spec;

import dev.sportsbetmini.betservice.domain.BetEntity;
import dev.sportsbetmini.betservice.domain.BetStatus;
import jakarta.persistence.criteria.Predicate;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class BetSpecifications {
    private BetSpecifications() {}

    public static Specification<BetEntity> filter(
            UUID eventId,
            UUID userId,
            BetStatus status,
            OffsetDateTime placedFrom,
            OffsetDateTime placedTo
    ) {
        return (root, q, cb) -> {
            var predicates = new ArrayList<Predicate>();
            if (eventId != null) predicates.add(cb.equal(root.get("eventId"), eventId));
            if (userId != null)   predicates.add(cb.equal(root.get("userId"), userId));
            if (status != null)   predicates.add(cb.equal(root.get("status"), status));
            if (placedFrom != null) predicates.add(cb.greaterThanOrEqualTo(root.get("placedAt"), placedFrom));
            if (placedTo != null)   predicates.add(cb.lessThanOrEqualTo(root.get("placedAt"), placedTo));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
