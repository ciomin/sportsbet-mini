package dev.sportsbetmini.betservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Generate a request ID if none exists (could also read from header "X-Request-ID")
        String requestId = UUID.randomUUID().toString();

        String method = request.getMethod();
        String path = request.getRequestURI();

        logger.info("Incoming request: id=" + requestId + " " + method + " " + path);

        // Add requestId to response headers for traceability
        response.addHeader("X-Request-ID", requestId);

        // Continue filter chain
        filterChain.doFilter(request, response);
    }
}
