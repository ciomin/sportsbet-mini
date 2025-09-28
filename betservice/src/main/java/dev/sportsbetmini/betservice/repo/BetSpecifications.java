package dev.sportsbetmini.betservice.repo;

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
            String userEmail,
            BetStatus status,
            OffsetDateTime placedFrom,
            OffsetDateTime placedTo
    ) {
        return (root, q, cb) -> {
            var predicates = new ArrayList<Predicate>();

            if (eventId != null) {
                predicates.add(cb.equal(root.get("eventId"), eventId));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (placedFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("placedAt"), placedFrom));
            }
            if (placedTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("placedAt"), placedTo));
            }
            // userEmail joins via user_id => fetch email in service; to keep it simple we’ll filter by email in service layer
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
