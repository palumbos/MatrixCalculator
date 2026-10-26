package MatrixCalculator;

/**
 * Represents a mathematical Matrix with double precision values.
 * Contains private attributes, dimension validation, getters, and core operations (addition, subtraction).
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
            throw new IllegalArgumentException("Matrix dimensions must be greater than zero. Provided: " + rows + "x" + cols);
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
