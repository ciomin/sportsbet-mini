package dev.sportsbetmini.betservice.service;

import dev.sportsbetmini.betservice.api.dto.RegisterRequest;
import dev.sportsbetmini.betservice.domain.UserEntity;
import dev.sportsbetmini.betservice.repo.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository users, PasswordEncoder encoder) {
        this.users = users; this.encoder = encoder;
    }

    @Transactional
    public void register(RegisterRequest req) {
        users.findByEmail(req.email()).ifPresent(u -> {
            throw new IllegalArgumentException("Email already registered");
        });

        var user = new UserEntity(UUID.randomUUID(), req.email());
        user.setPasswordHash(encoder.encode(req.password()));
        user.setRole("ROLE_USER");
        user.setEmailVerified(false);
        users.save(user);
    }
}