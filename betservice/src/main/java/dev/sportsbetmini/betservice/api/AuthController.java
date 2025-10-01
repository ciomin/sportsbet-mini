package dev.sportsbetmini.betservice.api;

import dev.sportsbetmini.betservice.api.dto.RegisterRequest;
import dev.sportsbetmini.betservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService auth;

    public AuthController(AuthService auth) { this.auth = auth; }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest req) {
        auth.register(req);
        return ResponseEntity.ok("registered");
    }
}