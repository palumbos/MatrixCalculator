package MatrixCalculator.exception;

/**
 * Thrown when matrix dimensions are invalid or incompatible for arithmetic operations.
 */
public class MatrixDimensionException extends MatrixException {
    public MatrixDimensionException(String message) {
        super(message);
    }
}
