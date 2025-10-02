package dev.sportsbetmini.betservice.api.dto;

public record LoginResponse(String accessToken, String tokenType) {
    public static LoginResponse bearer(String token) {
        return new LoginResponse(token, "Bearer");
    }
}