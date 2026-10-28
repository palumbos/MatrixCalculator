package com.matriz.engine;

import MatrixCalculator.Matrix;
import MatrixCalculator.exception.MatrixDimensionException;
import MatrixCalculator.exception.SingularMatrixException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Matrix class.
 * Validates instantiation, dimension validations, immutability, and arithmetic operations (addition, subtraction).
 */
public class MatrixTest {

    @Test
    void testValidMatrixInstantiationByDimensions() {
        int rows = 3;
        int cols = 4;
        Matrix matrix = new Matrix(rows, cols);

        assertEquals(rows, matrix.getRows(), "Number of rows should match");
        assertEquals(cols, matrix.getCols(), "Number of columns should match");

        // Verify elements are initialized to zero
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                assertEquals(0.0, matrix.get(i, j), 0.0001, "Element at (" + i + ", " + j + ") should be 0.0");
            }
        }
    }

    @Test
    void testValidMatrixInstantiationBy2DArray() {
        double[][] inputData = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0}
        };
        Matrix matrix = new Matrix(inputData);

        assertEquals(2, matrix.getRows());
        assertEquals(3, matrix.getCols());
        assertEquals(1.0, matrix.get(0, 0), 0.0001);
        assertEquals(6.0, matrix.get(1, 2), 0.0001);
    }

    @Test
    void testInvalidDimensionsThrowException() {
        // Zero or negative dimensions
        assertThrows(IllegalArgumentException.class, () -> new Matrix(0, 3), "Rows <= 0 should throw IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> new Matrix(3, 0), "Cols <= 0 should throw IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> new Matrix(-1, 3), "Negative rows should throw IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> new Matrix(3, -2), "Negative cols should throw IllegalArgumentException");
    }

    @Test
    void testInvalid2DArrayThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Matrix(null), "Null array should throw IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> new Matrix(new double[0][0]), "Empty array should throw IllegalArgumentException");
        
        // Jagged array
        double[][] jaggedData = {
            {1.0, 2.0},
            {3.0}
        };
        assertThrows(IllegalArgumentException.class, () -> new Matrix(jaggedData), "Jagged array should throw IllegalArgumentException");
    }

    @Test
    void testImmutabilityViaDefensiveCopy() {
        double[][] inputData = {
            {10.0, 20.0},
            {30.0, 40.0}
        };
        Matrix matrix = new Matrix(inputData);

        // Modify original input array after instantiation
        inputData[0][0] = 999.0;

        // Matrix internal data should remain unchanged due to defensive copy in constructor
        assertEquals(10.0, matrix.get(0, 0), 0.0001, "Matrix should be immune to external modifications of source array");

        // Modify array retrieved from getData()
        double[][] retrievedData = matrix.getData();
        retrievedData[0][0] = 888.0;

        // Matrix internal data should remain unchanged due to defensive copy in getter
        assertEquals(10.0, matrix.get(0, 0), 0.0001, "Matrix should be immune to modifications on retrieved data array");
    }

    @Test
    void testMatrixAddition() {
        Matrix a = new Matrix(new double[][]{
            {1.0, 2.0},
            {3.0, 4.0}
        });
        Matrix b = new Matrix(new double[][]{
            {5.0, 6.0},
            {7.0, 8.0}
        });

        Matrix result = a.add(b);

        assertEquals(2, result.getRows());
        assertEquals(2, result.getCols());
        assertEquals(6.0, result.get(0, 0), 0.0001);
        assertEquals(8.0, result.get(0, 1), 0.0001);
        assertEquals(10.0, result.get(1, 0), 0.0001);
        assertEquals(12.0, result.get(1, 1), 0.0001);
    }

    @Test
    void testMatrixSubtraction() {
        Matrix a = new Matrix(new double[][]{
            {5.0, 7.0},
            {9.0, 11.0}
        });
        Matrix b = new Matrix(new double[][]{
            {1.0, 2.0},
            {3.0, 4.0}
        });

        Matrix result = a.subtract(b);

        assertEquals(2, result.getRows());
        assertEquals(2, result.getCols());
        assertEquals(4.0, result.get(0, 0), 0.0001);
        assertEquals(5.0, result.get(0, 1), 0.0001);
        assertEquals(6.0, result.get(1, 0), 0.0001);
        assertEquals(7.0, result.get(1, 1), 0.0001);
    }

    @Test
    void testMatrixMultiplication() {
        // (2x3) * (3x2) = (2x2)
        Matrix a = new Matrix(new double[][]{
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0}
        });
        Matrix b = new Matrix(new double[][]{
            {7.0, 8.0},
            {9.0, 1.0},
            {2.0, 3.0}
        });

        Matrix result = a.multiply(b);

        assertEquals(2, result.getRows());
        assertEquals(2, result.getCols());
        assertEquals(31.0, result.get(0, 0), 0.0001);
        assertEquals(19.0, result.get(0, 1), 0.0001);
        assertEquals(85.0, result.get(1, 0), 0.0001);
        assertEquals(55.0, result.get(1, 1), 0.0001);
    }

    @Test
    void testScalarMultiplication() {
        Matrix a = new Matrix(new double[][]{
            {1.0, -2.0},
            {3.0, 4.5}
        });

        Matrix result = a.multiply(2.0);

        assertEquals(2.0, result.get(0, 0), 0.0001);
        assertEquals(-4.0, result.get(0, 1), 0.0001);
        assertEquals(6.0, result.get(1, 0), 0.0001);
        assertEquals(9.0, result.get(1, 1), 0.0001);
    }

    @Test
    void testDeterminant() {
        // 2x2 matrix: det([ [4, 2], [1, 3] ]) = 4*3 - 2*1 = 10
        Matrix m2 = new Matrix(new double[][]{
            {4.0, 2.0},
            {1.0, 3.0}
        });
        assertEquals(10.0, m2.determinant(), 0.0001);

        // 3x3 matrix requiring partial pivoting
        Matrix m3 = new Matrix(new double[][]{
            {1.0, 2.0, 3.0},
            {0.0, 1.0, 4.0},
            {5.0, 6.0, 0.0}
        });
        // det = 1*(0 - 24) - 2*(0 - 20) + 3*(0 - 5) = -24 + 40 - 15 = 1
        assertEquals(1.0, m3.determinant(), 0.0001);
    }

    @Test
    void testLinearSystemSolve() {
        // Solve AX = B
        // A = [ [2, 1], [1, 3] ], B = [ [5], [5] ]
        // Solution X = [ [2], [1] ] because 2(2)+1(1) = 5, 1(2)+3(1) = 5
        Matrix a = new Matrix(new double[][]{
            {2.0, 1.0},
            {1.0, 3.0}
        });
        Matrix b = new Matrix(new double[][]{
            {5.0},
            {5.0}
        });

        Matrix x = a.solve(b);

        assertEquals(2, x.getRows());
        assertEquals(1, x.getCols());
        assertEquals(2.0, x.get(0, 0), 0.0001);
        assertEquals(1.0, x.get(1, 0), 0.0001);
    }

    @Test
    void testMatrixInverse() {
        // A = [ [4, 7], [2, 6] ]
        // det(A) = 24 - 14 = 10
        // A^-1 = (1/10) * [ [6, -7], [-2, 4] ] = [ [0.6, -0.7], [-0.2, 0.4] ]
        Matrix a = new Matrix(new double[][]{
            {4.0, 7.0},
            {2.0, 6.0}
        });

        Matrix inv = a.inverse();

        assertEquals(0.6, inv.get(0, 0), 0.0001);
        assertEquals(-0.7, inv.get(0, 1), 0.0001);
        assertEquals(-0.2, inv.get(1, 0), 0.0001);
        assertEquals(0.4, inv.get(1, 1), 0.0001);

        // Verify A * A^-1 = I
        Matrix product = a.multiply(inv);
        assertEquals(1.0, product.get(0, 0), 0.0001);
        assertEquals(0.0, product.get(0, 1), 0.0001);
        assertEquals(0.0, product.get(1, 0), 0.0001);
        assertEquals(1.0, product.get(1, 1), 0.0001);
    }

    @Test
    void testMatrixPower() {
        Matrix a = new Matrix(new double[][]{
            {1.0, 2.0},
            {3.0, 4.0}
        });

        // A^0 should be Identity
        Matrix p0 = a.pow(0);
        assertEquals(1.0, p0.get(0, 0), 0.0001);
        assertEquals(0.0, p0.get(0, 1), 0.0001);
        assertEquals(0.0, p0.get(1, 0), 0.0001);
        assertEquals(1.0, p0.get(1, 1), 0.0001);

        // A^1 should be A
        Matrix p1 = a.pow(1);
        assertEquals(1.0, p1.get(0, 0), 0.0001);
        assertEquals(2.0, p1.get(0, 1), 0.0001);
        assertEquals(3.0, p1.get(1, 0), 0.0001);
        assertEquals(4.0, p1.get(1, 1), 0.0001);

        // A^2 = A * A = [ [1*1 + 2*3, 1*2 + 2*4], [3*1 + 4*3, 3*2 + 4*4] ] = [ [7, 10], [15, 22] ]
        Matrix p2 = a.pow(2);
        assertEquals(7.0, p2.get(0, 0), 0.0001);
        assertEquals(10.0, p2.get(0, 1), 0.0001);
        assertEquals(15.0, p2.get(1, 0), 0.0001);
        assertEquals(22.0, p2.get(1, 1), 0.0001);
    }

    @Test
    void testSingularMatrixExceptions() {
        // Singular matrix (row of zeros or proportional rows)
        Matrix singular = new Matrix(new double[][]{
            {1.0, 2.0},
            {2.0, 4.0}
        });

        assertThrows(SingularMatrixException.class, () -> singular.determinant());
        assertThrows(SingularMatrixException.class, () -> singular.inverse());
        assertThrows(SingularMatrixException.class, () -> singular.solve(new Matrix(new double[][]{{1.0}, {1.0}})));
    }

    @Test
    void testAdvancedDimensionExceptions() {
        Matrix rectangular = new Matrix(new double[][]{
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0}
        });

        assertThrows(MatrixDimensionException.class, () -> rectangular.determinant());
        assertThrows(MatrixDimensionException.class, () -> rectangular.inverse());
        assertThrows(MatrixDimensionException.class, () -> rectangular.solve(new Matrix(new double[][]{{1.0}})));
        assertThrows(MatrixDimensionException.class, () -> rectangular.pow(2));
        assertThrows(IllegalArgumentException.class, () -> rectangular.pow(-1));
    }

    @Test
    void testArithmeticExceptions() {
        Matrix a = new Matrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        Matrix incompatibleAddSub = new Matrix(new double[][]{{1.0, 2.0, 3.0}});
        Matrix incompatibleMul = new Matrix(new double[][]{{1.0, 2.0, 3.0}});

        assertThrows(IllegalArgumentException.class, () -> a.add(null), "Adding null should throw IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> a.subtract(null), "Subtracting null should throw IllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> a.multiply(null), "Multiplying null matrix should throw IllegalArgumentException");

        assertThrows(MatrixDimensionException.class, () -> a.add(incompatibleAddSub), "Adding incompatible dimensions should throw MatrixDimensionException");
        assertThrows(MatrixDimensionException.class, () -> a.subtract(incompatibleAddSub), "Subtracting incompatible dimensions should throw MatrixDimensionException");
        assertThrows(MatrixDimensionException.class, () -> a.multiply(incompatibleMul), "Multiplying incompatible dimensions should throw MatrixDimensionException");
    }
}
