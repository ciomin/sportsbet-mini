package dev.sportsbetmini.betservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Postman + JSON: disable CSRF for now
                .cors(cors -> {})              // keep default CORS (irrelevant for Postman)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/**",            // allow register/login
                                "/actuator/**",
                                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().permitAll()  // keep everything else open for now
                );
        return http.build();
    }
}