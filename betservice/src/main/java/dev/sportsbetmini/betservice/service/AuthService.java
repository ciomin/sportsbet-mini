package dev.sportsbetmini.betservice.service;

import dev.sportsbetmini.betservice.api.dto.RegisterRequest;
import dev.sportsbetmini.betservice.domain.UserEntity;
import dev.sportsbetmini.betservice.repo.UserRepository;
import dev.sportsbetmini.betservice.api.dto.LoginRequest;
import dev.sportsbetmini.betservice.api.dto.LoginResponse;

import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.HashMap;

@Service
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder encoder;

    private final JwtService jwt;
    public AuthService(UserRepository users, PasswordEncoder encoder, JwtService jwt) {
        this.users = users; this.encoder = encoder; this.jwt = jwt;
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

    @Transactional(Transactional.TxType.SUPPORTS)
    public LoginResponse login(LoginRequest req) {
        var user = users.findByEmail(req.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (user.getPasswordHash() == null || !encoder.matches(req.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        var claims = new HashMap<String,Object>();
        claims.put("role", user.getRole());
        claims.put("emailVerified", user.isEmailVerified());

        var token = jwt.issue(user.getEmail(), claims);
        return LoginResponse.bearer(token);
    }
}