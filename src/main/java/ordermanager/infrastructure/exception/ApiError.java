package ordermanager.infrastructure.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ApiError {

    private Instant timestamp = Instant.now();
    private int status;
    private String error;
    private String message;
    private String path;
    private List<FieldViolation> violations;

    public ApiError(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    @Getter
    @Setter
    public static class FieldViolation {
        private String field;
        private String message;

        public FieldViolation(String field, String message) {
            this.field = field;
            this.message = message;
        }

    }
}
