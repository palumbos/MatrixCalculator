package MatrixCalculator;

import MatrixCalculator.decomposition.LUDecomposition;
import MatrixCalculator.exception.MatrixDimensionException;

/**
 * Represents a mathematical Matrix with double precision values.
 * Contains private attributes, dimension validation, getters, and core operations (addition, subtraction, multiplication, transpose, determinant, solve, inverse, pow).
 */
public class Matrix {
    private final int rows;
    private final int cols;
    private final double[][] data;

    /**
     * Constructs a matrix with specified dimensions, initialized to zero.
     * 
     * @param rows number of rows (must be > 0)
     * @param cols number of columns (must be > 0)
     */
    public Matrix(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new MatrixDimensionException("Matrix dimensions must be greater than zero. Provided: " + rows + "x" + cols);
        }
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }

    /**
     * Constructs a matrix from a 2D array, validating that it is non-null,
     * non-empty, and rectangular.
     * 
     * @param data 2D array of double values
     */
    public Matrix(double[][] data) {
        if (data == null) {
            throw new IllegalArgumentException("Matrix data cannot be null.");
        }
        if (data.length == 0 || data[0].length == 0) {
            throw new IllegalArgumentException("Matrix dimensions cannot be zero.");
        }

        this.rows = data.length;
        this.cols = data[0].length;
        this.data = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            if (data[i] == null) {
                throw new IllegalArgumentException("Row " + i + " cannot be null.");
            }
            if (data[i].length != cols) {
                throw new IllegalArgumentException("All rows must have the same length. Expected " + cols + ", but row " + i + " has length " + data[i].length);
            }
            System.arraycopy(data[i], 0, this.data[i], 0, cols);
        }
    }

    /**
     * Returns the number of rows in the matrix.
     * 
     * @return number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns the number of columns in the matrix.
     * 
     * @return number of columns
     */
    public int getCols() {
        return cols;
    }

    /**
     * Returns a deep copy of the underlying 2D data array to preserve encapsulation.
     * 
     * @return 2D array copy of matrix data
     */
    public double[][] getData() {
        double[][] copy = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(data[i], 0, copy[i], 0, cols);
        }
        return copy;
    }

    /**
     * Returns the element at the specified position.
     * 
     * @param row row index (0-based)
     * @param col column index (0-based)
     * @return value at (row, col)
     */
    public double get(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("Indices (" + row + ", " + col + ") are out of bounds for matrix of size " + rows + "x" + cols);
        }
        return data[row][col];
    }

    /**
     * Adds another matrix to this matrix (element-wise addition).
     * 
     * @param other the matrix to add
     * @return a new Matrix resulting from the addition
     */
    public Matrix add(Matrix other) {
        return elementWiseOperation(other, true);
    }

    /**
     * Subtracts another matrix from this matrix (element-wise subtraction).
     * 
     * @param other the matrix to subtract
     * @return a new Matrix resulting from the subtraction
     */
    public Matrix subtract(Matrix other) {
        return elementWiseOperation(other, false);
    }

    /**
     * Multiplies this matrix by another matrix (matrix multiplication).
     * 
     * @param other the matrix to multiply with
     * @return a new Matrix resulting from the product
     */
    public Matrix multiply(Matrix other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot multiply with a null matrix.");
        }
        if (this.cols != other.rows) {
            throw new MatrixDimensionException(String.format(
                "Incompatible dimensions for multiplication: (%dx%d) and (%dx%d)",
                this.rows, this.cols, other.rows, other.cols
            ));
        }

        Matrix result = new Matrix(this.rows, other.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                double sum = 0.0;
                for (int k = 0; k < this.cols; k++) {
                    sum += this.data[i][k] * other.data[k][j];
                }
                result.data[i][j] = sum;
            }
        }
        return result;
    }

    /**
     * Multiplies this matrix by a scalar value (scalar multiplication).
     * 
     * @param scalar the scalar value
     * @return a new Matrix resulting from scalar multiplication
     */
    public Matrix multiply(double scalar) {
        Matrix result = new Matrix(this.rows, this.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                result.data[i][j] = this.data[i][j] * scalar;
            }
        }
        return result;
    }

    /**
     * Computes the transpose of this matrix.
     * 
     * @return a new Matrix that is the transpose of this matrix
     */
    public Matrix transpose() {
        Matrix result = new Matrix(this.cols, this.rows);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                result.data[j][i] = this.data[i][j];
            }
        }
        return result;
    }

    /**
     * Calculates the determinant of this matrix using LU decomposition.
     * Matrix must be square.
     * 
     * @return determinant value
     */
    public double determinant() {
        if (rows != cols) {
            throw new MatrixDimensionException("Determinant can only be calculated for square matrices. Provided: " + rows + "x" + cols);
        }
        LUDecomposition lu = new LUDecomposition(this);
        return lu.determinant();
    }

    /**
     * Solves the linear system AX = B, where this matrix is A and rhs is B.
     * 
     * @param rhs right-hand side matrix B
     * @return solution matrix X
     */
    public Matrix solve(Matrix rhs) {
        if (rows != cols) {
            throw new MatrixDimensionException("Linear system solving requires a square coefficient matrix A. Provided: " + rows + "x" + cols);
        }
        LUDecomposition lu = new LUDecomposition(this);
        return lu.solve(rhs);
    }

    /**
     * Computes the inverse of this matrix using LU decomposition.
     * Matrix must be square and non-singular.
     * 
     * @return inverse Matrix
     */
    public Matrix inverse() {
        if (rows != cols) {
            throw new MatrixDimensionException("Matrix inversion requires a square matrix. Provided: " + rows + "x" + cols);
        }
        LUDecomposition lu = new LUDecomposition(this);
        return lu.inverse();
    }

    /**
     * Computes the power of this square matrix for a non-negative integer exponent.
     * Exponent 0 returns the identity matrix.
     * Exponent 1 returns an equivalent matrix.
     * 
     * @param exponent non-negative integer exponent
     * @return resulting Matrix
     */
    public Matrix pow(int exponent) {
        if (rows != cols) {
            throw new MatrixDimensionException("Matrix exponentiation requires a square matrix. Provided: " + rows + "x" + cols);
        }
        if (exponent < 0) {
            throw new IllegalArgumentException("Negative exponents are not supported in this version. Provided: " + exponent);
        }

        // Exponent 0: return identity matrix
        if (exponent == 0) {
            return identity(rows);
        }

        // Exponent 1: return a copy of this matrix
        if (exponent == 1) {
            return new Matrix(this.getData());
        }

        // Exponentiation by squaring
        Matrix base = new Matrix(this.getData());
        Matrix result = identity(rows);
        int exp = exponent;

        while (exp > 0) {
            if ((exp & 1) == 1) {
                result = result.multiply(base);
            }
            base = base.multiply(base);
            exp >>= 1;
        }

        return result;
    }

    /**
     * Helper to create an identity matrix of size n x n.
     */
    private static Matrix identity(int n) {
        double[][] data = new double[n][n];
        for (int i = 0; i < n; i++) {
            data[i][i] = 1.0;
        }
        return new Matrix(data);
    }

    /**
     * Helper method to perform element-wise addition or subtraction while adhering to DRY principles.
     * 
     * @param other the other matrix
     * @param isAddition true for addition, false for subtraction
     * @return resulting Matrix
     */
    private Matrix elementWiseOperation(Matrix other, boolean isAddition) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot operate with a null matrix.");
        }
        if (this.rows != other.rows || this.cols != other.cols) {
            String opName = isAddition ? "addition" : "subtraction";
            throw new IllegalArgumentException(String.format(
                "Matrix dimension mismatch for %s: (%dx%d) vs (%dx%d)",
                opName, this.rows, this.cols, other.rows, other.cols
            ));
        }

        Matrix result = new Matrix(this.rows, this.cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.data[i][j] = isAddition ? this.data[i][j] + other.data[i][j]
                                              : this.data[i][j] - other.data[i][j];
            }
        }
        return result;
    }
}
