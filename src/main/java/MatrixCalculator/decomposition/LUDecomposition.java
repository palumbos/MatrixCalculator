package MatrixCalculator.decomposition;

import MatrixCalculator.Matrix;
import MatrixCalculator.exception.MatrixDimensionException;
import MatrixCalculator.exception.SingularMatrixException;

/**
 * Performs LU decomposition with partial pivoting for square matrices.
 * Decomposes matrix A such that P * A = L * U, where P is a permutation matrix,
 * L is a unit lower triangular matrix, and U is an upper triangular matrix.
 */
public class LUDecomposition {
    private static final double EPSILON = 1e-11;

    private final int n;
    private final double[][] lu;
    private final int[] piv;
    private int pivSign;

    /**
     * Constructs an LU decomposition of the given square matrix.
     * 
     * @param matrix square Matrix to decompose
     */
    public LUDecomposition(Matrix matrix) {
        if (matrix == null) {
            throw new IllegalArgumentException("Matrix cannot be null.");
        }
        if (matrix.getRows() != matrix.getCols()) {
            throw new MatrixDimensionException("LU decomposition requires a square matrix. Provided: " + matrix.getRows() + "x" + matrix.getCols());
        }

        this.n = matrix.getRows();
        double[][] srcData = matrix.getData();
        this.lu = new double[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(srcData[i], 0, this.lu[i], 0, n);
        }

        this.piv = new int[n];
        for (int i = 0; i < n; i++) {
            piv[i] = i;
        }
        this.pivSign = 1;

        double[] colVector = new double[n];

        // Outer loop for columns
        for (int j = 0; j < n; j++) {
            // Make copy of j-th column to select pivot
            for (int i = 0; i < n; i++) {
                colVector[i] = lu[i][j];
            }

            // Apply previous transformations
            for (int i = 0; i < n; i++) {
                double[] luRowi = lu[i];
                int kmax = Math.min(i, j);
                double s = 0.0;
                for (int k = 0; k < kmax; k++) {
                    s += luRowi[k] * colVector[k];
                }
                colVector[i] -= s;
                luRowi[j] = colVector[i];
            }

            // Find pivot and test for singularity
            int p = j;
            double maxVal = Math.abs(colVector[j]);
            for (int i = j + 1; i < n; i++) {
                double val = Math.abs(colVector[i]);
                if (val > maxVal) {
                    maxVal = val;
                    p = i;
                }
            }

            if (p != j) {
                // Swap rows in lu
                double[] t = lu[p];
                lu[p] = lu[j];
                lu[j] = t;

                // Swap pivot records
                int k = piv[p];
                piv[p] = piv[j];
                piv[j] = k;

                pivSign = -pivSign;
            }

            // Check if pivot is too small (singular matrix)
            if (Math.abs(lu[j][j]) < EPSILON) {
                throw new SingularMatrixException("Matrix is singular (pivot is zero or near zero at index " + j + ").");
            }

            // Compute multipliers
            if (j < n - 1) {
                double invPiv = 1.0 / lu[j][j];
                for (int i = j + 1; i < n; i++) {
                    lu[i][j] *= invPiv;
                }
            }
        }
    }

    /**
     * Calculates the determinant of the decomposed matrix.
     * 
     * @return determinant value
     */
    public double determinant() {
        double det = pivSign;
        for (int j = 0; j < n; j++) {
            det *= lu[j][j];
        }
        return det;
    }

    /**
     * Solves the linear system AX = B.
     * 
     * @param rhs the right-hand side matrix B
     * @return solution matrix X
     */
    public Matrix solve(Matrix rhs) {
        if (rhs == null) {
            throw new IllegalArgumentException("Right-hand side matrix cannot be null.");
        }
        if (rhs.getRows() != n) {
            throw new MatrixDimensionException(String.format(
                "Row dimension mismatch in solve: matrix size is %dx%d, but RHS rows is %d",
                n, n, rhs.getRows()
            ));
        }

        int nx = rhs.getCols();
        double[][] bData = rhs.getData();
        double[][] xData = new double[n][nx];

        // Apply permutation to B (P * B)
        for (int i = 0; i < n; i++) {
            System.arraycopy(bData[piv[i]], 0, xData[i], 0, nx);
        }

        // Solve L * Y = B (Forward substitution)
        for (int k = 0; k < n; k++) {
            for (int i = k + 1; i < n; i++) {
                for (int j = 0; j < nx; j++) {
                    xData[i][j] -= lu[i][k] * xData[k][j];
                }
            }
        }

        // Solve U * X = Y (Back substitution)
        for (int k = n - 1; k >= 0; k--) {
            double invUkk = 1.0 / lu[k][k];
            for (int j = 0; j < nx; j++) {
                xData[k][j] *= invUkk;
            }
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < nx; j++) {
                    xData[i][j] -= lu[i][k] * xData[k][j];
                }
            }
        }

        return new Matrix(xData);
    }

    /**
     * Computes the inverse of the decomposed matrix.
     * 
     * @return inverse Matrix
     */
    public Matrix inverse() {
        // Solve AX = I
        double[][] identityData = new double[n][n];
        for (int i = 0; i < n; i++) {
            identityData[i][i] = 1.0;
        }
        return solve(new Matrix(identityData));
    }
}
