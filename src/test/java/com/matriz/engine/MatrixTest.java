package com.matriz.engine;

import MatrixCauculator.Matrix;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Matrix class.
 * Validates instantiation, dimension validations, and immutability via defensive copying.
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
}
