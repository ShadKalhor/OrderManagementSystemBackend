package ordermanager.adapter.in.web.exception;
import ordermanager.exception.EntityNotFoundException;
import ordermanager.exception.InsufficientInventoryException;
import ordermanager.exception.ValidationException;
import ordermanager.shared.web.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest req) {
        var error = new ApiError(HttpStatus.NOT_FOUND.value(), "NotFound",
                ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InsufficientInventoryException.class)
    public ResponseEntity<ApiError> handleInsufficientInventoryException(InsufficientInventoryException ex, HttpServletRequest req) {
        var error = new ApiError(HttpStatus.CONFLICT.value(), "Conflict",
                ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleValidationException(ValidationException ex, HttpServletRequest req) {

        var error = new ApiError(HttpStatus.BAD_REQUEST.value(),"BadRequest",
                ex.getMessage(),req.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAny(Exception ex, HttpServletRequest req) {
        var error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "InternalServerError",
                "Unexpected error", req.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}

