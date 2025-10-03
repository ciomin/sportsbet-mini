package dev.sportsbetmini.betservice.security;

import dev.sportsbetmini.betservice.domain.UserEntity;
import dev.sportsbetmini.betservice.repo.BetRepository;
import dev.sportsbetmini.betservice.repo.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("authz")
public class Authz {
    private final UserRepository users;
    private final BetRepository bets;

    public Authz(UserRepository users, BetRepository bets) {
        this.users = users;
        this.bets = bets;
    }

    public boolean emailVerified(String email) {
        return users.findByEmail(email).map(UserEntity::isEmailVerified).orElse(false);
    }

    public boolean canReadBet(UUID betId, Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) return true;
        var maybeUser = users.findByEmail(auth.getName());
        if (maybeUser.isEmpty()) return false;
        var userId = maybeUser.get().getId();
        return bets.findById(betId).map(b -> b.getUserId().equals(userId)).orElse(false);
    }
}