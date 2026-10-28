package MatrixCalculator.exception;

/**
 * Base unchecked exception for all matrix-related errors.
 */
public class MatrixException extends RuntimeException {
    public MatrixException(String message) {
        super(message);
    }

    public MatrixException(String message, Throwable cause) {
        super(message, cause);
    }
}
