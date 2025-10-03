package dev.sportsbetmini.betservice.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users/me")
public class UserController {

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public Map<String, Object> me(Authentication auth) {
        return Map.of(
                "sub", auth.getName(),
                "authorities", auth.getAuthorities()
        );
    }
}