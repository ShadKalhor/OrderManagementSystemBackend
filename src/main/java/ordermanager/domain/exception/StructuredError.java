package ordermanager.domain.exception;

public record StructuredError(String message, ErrorType type) {
}
