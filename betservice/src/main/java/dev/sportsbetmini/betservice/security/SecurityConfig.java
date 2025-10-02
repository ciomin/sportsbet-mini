package dev.sportsbetmini.betservice.security;

import dev.sportsbetmini.betservice.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtService jwt;

    public SecurityConfig(JwtService jwt) { this.jwt = jwt; }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {})
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/auth/**", "/odds/**", "/events/**", "/actuator/**").permitAll()

                        // Dev/internal tools (still require token; ADMIN only)
                        .requestMatchers("/internal/**").hasRole("ADMIN")

                        // Bets must be authenticated (ROLE_USER)
                        .requestMatchers("/bets/**", "/me").hasRole("USER")

                        // Everything else: authenticated
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwt), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

