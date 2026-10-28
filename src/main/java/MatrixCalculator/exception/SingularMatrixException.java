package MatrixCalculator.exception;

/**
 * Thrown when a matrix is singular (non-invertible or zero determinant).
 */
public class SingularMatrixException extends MatrixException {
    public SingularMatrixException(String message) {
        super(message);
    }
}
