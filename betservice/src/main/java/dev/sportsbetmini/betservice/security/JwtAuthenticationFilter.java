package dev.sportsbetmini.betservice.security;

import dev.sportsbetmini.betservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwt;

    public JwtAuthenticationFilter(JwtService jwt) {
        this.jwt = jwt;
    }

    private boolean isProtected(HttpServletRequest req) {
        var p = req.getRequestURI();
        return p.startsWith("/bets") || p.startsWith("/users/me") || p.startsWith("/internal");
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // If there is no Bearer token, just continue (anonymous or may be permitted by HttpSecurity)
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            // Parse and extract claims
            Claims claims = jwt.parse(token).getBody();
            String email = claims.getSubject();                 // subject = user's email
            String role = (String) claims.get("role");          // e.g., "ROLE_USER" / "ROLE_ADMIN"

            // Build Authentication with a single SimpleGrantedAuthority
            var auth = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    List.of(new SimpleGrantedAuthority(role))
            );

            // Optionally attach request details (IP, session id)
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Set the security context
            SecurityContextHolder.getContext().setAuthentication(auth);

            // Proceed
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            // Invalid/expired token → clear context
            SecurityContextHolder.clearContext();

            // Only block if the path is protected; else continue as anonymous
            if (isProtected(request)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            filterChain.doFilter(request, response);
        }
    }
}
