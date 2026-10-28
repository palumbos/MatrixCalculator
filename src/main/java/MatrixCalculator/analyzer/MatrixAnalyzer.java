package MatrixCalculator.analyzer;

import MatrixCalculator.Matrix;

import java.util.EnumSet;
import java.util.Set;

/**
 * Stateless service responsible for analyzing a Matrix and determining
 * all its applicable mathematical properties using a robust numerical tolerance policy.
 */
public class MatrixAnalyzer {
    private static final double EPSILON = 1e-11;

    private MatrixAnalyzer() {
        // Private constructor to prevent instantiation of utility class
    }

    /**
     * Analyzes the given matrix and returns an immutable MatrixAnalysis result.
     * 
     * @param matrix the Matrix to analyze
     * @return MatrixAnalysis object containing all identified properties
     */
    public static MatrixAnalysis analyze(Matrix matrix) {
        if (matrix == null) {
            throw new IllegalArgumentException("Matrix to analyze cannot be null.");
        }

        int rows = matrix.getRows();
        int cols = matrix.getCols();
        Set<MatrixProperty> props = EnumSet.noneOf(MatrixProperty.class);

        // 1. Dimensions
        boolean isSquare = (rows == cols);
        if (isSquare) {
            props.add(MatrixProperty.SQUARE);
        } else {
            props.add(MatrixProperty.NON_SQUARE);
        }

        if (rows == 1) {
            props.add(MatrixProperty.ROW);
        }
        if (cols == 1) {
            props.add(MatrixProperty.COLUMN);
        }

        // 2. Zero check
        boolean isZero = true;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (Math.abs(matrix.get(i, j)) >= EPSILON) {
                    isZero = false;
                    break;
                }
            }
            if (!isZero) break;
        }
        if (isZero) {
            props.add(MatrixProperty.ZERO);
        }

        // 3. Triangular, Diagonal, Scalar, Identity checks (applicable to square matrices, or 1x1)
        if (isSquare) {
            boolean isUpper = true;
            boolean isLower = true;
            boolean isDiagonal = true;
            boolean isSymmetric = true;
            boolean isAntisymmetric = true;

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    double val = matrix.get(i, j);

                    // Upper triangular: below main diagonal (i > j) must be zero
                    if (i > j && Math.abs(val) >= EPSILON) {
                        isUpper = false;
                    }

                    // Lower triangular: above main diagonal (i < j) must be zero
                    if (i < j && Math.abs(val) >= EPSILON) {
                        isLower = false;
                    }

                    // Diagonal: off-diagonal (i != j) must be zero
                    if (i != j && Math.abs(val) >= EPSILON) {
                        isDiagonal = false;
                    }

                    // Symmetry: A[i][j] == A[j][i]
                    if (Math.abs(val - matrix.get(j, i)) >= EPSILON) {
                        isSymmetric = false;
                    }

                    // Antisymmetry: A[i][j] == -A[j][i], and diagonal must be zero
                    if (i == j) {
                        if (Math.abs(val) >= EPSILON) {
                            isAntisymmetric = false;
                        }
                    } else {
                        if (Math.abs(val + matrix.get(j, i)) >= EPSILON) {
                            isAntisymmetric = false;
                        }
                    }
                }
            }

            if (isUpper) {
                props.add(MatrixProperty.UPPER_TRIANGULAR);
            }
            if (isLower) {
                props.add(MatrixProperty.LOWER_TRIANGULAR);
            }
            if (isDiagonal) {
                props.add(MatrixProperty.DIAGONAL);
            }
            if (isSymmetric) {
                props.add(MatrixProperty.SYMMETRIC);
            }
            if (isAntisymmetric) {
                props.add(MatrixProperty.ANTISYMMETRIC);
            }

            // Scalar and Identity checks (require diagonal)
            if (isDiagonal && rows > 0) {
                double firstDiag = matrix.get(0, 0);
                boolean isScalar = true;
                boolean isIdentity = Math.abs(firstDiag - 1.0) < EPSILON;

                for (int i = 1; i < rows; i++) {
                    double diagVal = matrix.get(i, i);
                    if (Math.abs(diagVal - firstDiag) >= EPSILON) {
                        isScalar = false;
                    }
                    if (Math.abs(diagVal - 1.0) >= EPSILON) {
                        isIdentity = false;
                    }
                }

                if (isScalar) {
                    props.add(MatrixProperty.SCALAR);
                }
                if (isIdentity && (rows == 1 || Math.abs(firstDiag - 1.0) < EPSILON)) {
                    props.add(MatrixProperty.IDENTITY);
                }
            }
        } else {
            // For non-square matrices, check symmetry/antisymmetry (not applicable)
            // But what about 1x1 or other non-square? Non-square cannot be symmetric/triangular in standard strict sense unless trivial.
        }

        return new MatrixAnalysis(props, rows, cols);
    }
}
