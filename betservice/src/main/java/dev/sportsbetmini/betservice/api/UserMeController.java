package dev.sportsbetmini.betservice.api;

import dev.sportsbetmini.betservice.service.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/me")
public class UserMeController {

    // for now, we’ll read the token from the Authorization header manually
    // later, we add a proper JwtAuthenticationFilter
    private final JwtService jwt;

    public UserMeController(JwtService jwt) { this.jwt = jwt; }

    @GetMapping
    @PreAuthorize("permitAll()") // keeping it open for now, will switch to ROLE_USER later
    public Map<String,Object> me(@RequestHeader(name="Authorization", required=false) String auth) {
        if (auth == null || !auth.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing or invalid Authorization header");
        }
        var token = auth.substring(7);
        Claims claims = jwt.parse(token).getBody();
        return Map.of(
                "sub", claims.getSubject(),
                "role", claims.get("role"),
                "emailVerified", claims.get("emailVerified")
        );
    }
}