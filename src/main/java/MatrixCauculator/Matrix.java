package MatrixCauculator;

/**
 * Represents a mathematical Matrix with double precision values.
 * Phase 1 implementation containing private attributes, dimension validation, and getters.
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
}
