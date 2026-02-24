package ordermanager.infrastructure.web.exception;

import lombok.Getter;
import ordermanager.domain.exception.StructuredError;

import static ordermanager.infrastructure.web.exception.ErrorTypeToHttpStatusMapper.httpStatus;

public class ErrorStructureException extends RuntimeException {

    @Getter
    private final int httpStatus;
    @Getter
    private final String message;

    public ErrorStructureException(StructuredError structuredError) {
        this.httpStatus = httpStatus(structuredError.type());
        this.message = structuredError.message();
    }
}