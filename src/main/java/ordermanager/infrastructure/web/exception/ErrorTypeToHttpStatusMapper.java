package ordermanager.infrastructure.web.exception;

import ordermanager.domain.exception.ErrorType;
import org.springframework.http.HttpStatus;

public final class ErrorTypeToHttpStatusMapper {

    private ErrorTypeToHttpStatusMapper() {
        throw new UnsupportedOperationException("Should not be instantiated");
    }

    public static int httpStatus(ErrorType errorType) {
        if (errorType == ErrorType.VALIDATION_ERROR) {
            return HttpStatus.BAD_REQUEST.value();
        } else if (errorType == ErrorType.UNAUTHORIZED_ERROR) {
            return HttpStatus.UNAUTHORIZED.value();
        } else if (errorType == ErrorType.NOT_FOUND_ERROR) {
            return HttpStatus.NOT_FOUND.value();
        } else if (errorType == ErrorType.SERVER_ERROR) {
            return HttpStatus.INTERNAL_SERVER_ERROR.value();
        } else if (errorType == ErrorType.CONFLICT) {
            return HttpStatus.CONFLICT.value();
        }
        return HttpStatus.BAD_REQUEST.value();
    }
}
