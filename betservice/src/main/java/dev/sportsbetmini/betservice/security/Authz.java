package dev.sportsbetmini.betservice.security;

import dev.sportsbetmini.betservice.domain.UserEntity;
import dev.sportsbetmini.betservice.repo.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class Authz {
    private final UserRepository users;
    public Authz(UserRepository users) { this.users = users; }

    public boolean emailVerified(String email) {
        return users.findByEmail(email).map(UserEntity::isEmailVerified).orElse(false);
    }
}