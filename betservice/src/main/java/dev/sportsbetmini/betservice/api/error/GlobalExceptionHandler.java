package dev.sportsbetmini.betservice.api.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    public record FieldErrorDto(String field, String message) {}
    public record ValidationErrorResponse(String code, String message, java.util.List<FieldErrorDto> errors,
                                          java.time.OffsetDateTime timestamp) {
        public static ValidationErrorResponse of(String message, java.util.List<FieldErrorDto> errors) {
            return new ValidationErrorResponse("VALIDATION_ERROR", message, errors, java.time.OffsetDateTime.now());
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new FieldErrorDto(err.getField(), err.getDefaultMessage()))
                .toList();

        return ResponseEntity.badRequest().body(
                ValidationErrorResponse.of("Validation failed", errors)
        );
    }


    // Handle illegal args (business rule violations)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArg(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(
                ErrorResponse.of("BAD_REQUEST", ex.getMessage())
        );
    }

    // Catch-all for unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ErrorResponse.of("INTERNAL_ERROR", ex.getMessage())
        );
    }

    @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> badCreds(Exception ex) {
        return ResponseEntity.status(401).body(ErrorResponse.of("UNAUTHORIZED", ex.getMessage()));
    }
}
