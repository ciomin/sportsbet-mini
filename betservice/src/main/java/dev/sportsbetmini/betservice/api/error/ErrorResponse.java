package dev.sportsbetmini.betservice.api.error;

import java.time.OffsetDateTime;

public record ErrorResponse(String code, String message, OffsetDateTime timestamp) {
    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, message, OffsetDateTime.now());
    }
}