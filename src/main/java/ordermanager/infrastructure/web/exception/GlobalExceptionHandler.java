package ordermanager.infrastructure.web.exception;
import io.vavr.control.Option;
import ordermanager.infrastructure.exception.EntityNotFoundException;
import ordermanager.infrastructure.exception.InsufficientInventoryException;
import ordermanager.infrastructure.exception.ValidationException;
import org.hibernate.HibernateException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        final var httpStatus = HttpStatus.NOT_FOUND;
        final var errorResponse = new ErrorResponse("ENTITY_NOT_FOUND");
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ExceptionHandler(ValidationException.class)
    ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        final var httpStatus = HttpStatus.BAD_REQUEST;
        final var errorResponse = new ErrorResponse("INVALID_REQUEST");
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ExceptionHandler(HibernateException.class)
    ResponseEntity<ErrorResponse> handleHibernateException(HibernateException ex) {
        final var httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        final var errorResponse = new ErrorResponse("Something went wrong");
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ExceptionHandler(InsufficientInventoryException.class)
    ResponseEntity<ErrorResponse> handleInsufficientInventoryException(InsufficientInventoryException ex) {
        final var httpStatus = HttpStatus.CONFLICT;
        final var errorResponse = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(errorResponse, null, httpStatus);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        logger.error("Handle MethodArgumentTypeMismatchException", ex);
        final var errorResponse = new ErrorResponse("INVALID_REQUEST");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    ResponseEntity<ErrorResponse> handleFileTooBigException(MaxUploadSizeExceededException ex) {
        logger.error("Handling MaxUploadSizeExceededException", ex);
        final var errorResponse = new ErrorResponse("INVALID_REQUEST");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> handleException(Exception ex) {
        logger.error("handling Exception", ex);
        final var httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        final var errorResponse = new ErrorResponse("Something went wrong");
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex,
                                                         HttpHeaders headers,
                                                         HttpStatus status,
                                                         WebRequest request) {
        logger.error("Handle BindException", ex);
        return handleBindingResultErrors(ex.getBindingResult());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        logger.error("Handle HttpMessageNotReadableException", ex);
        final var errorResponse = new ErrorResponse("INVALID_REQUEST");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        logger.error("Handle MethodArgumentNotValidException", ex);
        final var errorResponse = new ErrorResponse("INVALID_REQUEST");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    private ResponseEntity<Object> handleBindingResultErrors(BindingResult bindingResult) {
        return ResponseEntity.badRequest().body(
                new ErrorResponse(
                        Option.ofOptional(
                                        bindingResult.getFieldErrors().stream()
                                                .map(FieldError::getField)
                                                .findFirst()
                                )
                                .getOrElse("Something went wrong")
                )
        );
    }

    /*

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
*/




}

