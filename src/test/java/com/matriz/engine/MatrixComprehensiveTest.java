package com.matriz.engine;

import MatrixCalculator.Matrix;
import MatrixCalculator.analyzer.MatrixAnalysis;
import MatrixCalculator.analyzer.MatrixAnalyzer;
import MatrixCalculator.analyzer.MatrixProperty;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive integration and end-to-end test suite verifying all implemented features of MatrixCalculator.
 */
public class MatrixComprehensiveTest {

    @Test
    void testCompleteMatrixWorkflow() {
        // 1. Instantiation & Defensive Copy
        double[][] raw = {
            {2.0, 1.0},
            {1.0, 3.0}
        };
        Matrix a = new Matrix(raw);
        raw[0][0] = 999.0; // modify source array
        assertEquals(2.0, a.get(0, 0), 0.0001, "Defensive copy must protect internal state");

        // 2. Property Analysis
        MatrixAnalysis analysis = MatrixAnalyzer.analyze(a);
        assertTrue(analysis.isSquare());
        assertTrue(analysis.hasProperty(MatrixProperty.SYMMETRIC));
        assertFalse(analysis.hasProperty(MatrixProperty.DIAGONAL));
        assertTrue(analysis.canCalculateSquareOperations());

        // 3. Arithmetic Operations (Addition & Subtraction)
        Matrix b = new Matrix(new double[][]{
            {1.0, 0.0},
            {0.0, 1.0}
        });
        Matrix sum = a.add(b);
        assertEquals(3.0, sum.get(0, 0), 0.0001);
        assertEquals(4.0, sum.get(1, 1), 0.0001);

        Matrix diff = a.subtract(b);
        assertEquals(1.0, diff.get(0, 0), 0.0001);
        assertEquals(2.0, diff.get(1, 1), 0.0001);

        // 4. Matrix Multiplication & Scalar Multiplication
        Matrix prod = a.multiply(b);
        assertEquals(2.0, prod.get(0, 0), 0.0001);
        assertEquals(3.0, prod.get(1, 1), 0.0001);

        Matrix scaled = a.multiply(2.0);
        assertEquals(4.0, scaled.get(0, 0), 0.0001);
        assertEquals(6.0, scaled.get(1, 1), 0.0001);

        // 5. Transpose
        Matrix trans = a.transpose();
        assertEquals(a.get(0, 1), trans.get(1, 0), 0.0001);

        // 6. Trace & Norms
        assertEquals(5.0, a.trace(), 0.0001);
        assertTrue(a.normFrobenius() > 0.0);
        assertTrue(a.normL1() > 0.0);
        assertTrue(a.normInfinity() > 0.0);

        // 7. Determinant via LU Decomposition
        // det([ [2, 1], [1, 3] ]) = 6 - 1 = 5
        assertEquals(5.0, a.determinant(), 0.0001);

        // 8. Linear System Solver (AX = B)
        Matrix rhs = new Matrix(new double[][]{
            {3.0},
            {4.0}
        });
        Matrix x = a.solve(rhs);
        assertNotNull(x);
        assertEquals(2, x.getRows());

        // 9. Matrix Inverse
        Matrix inv = a.inverse();
        Matrix identityCheck = a.multiply(inv);
        assertEquals(1.0, identityCheck.get(0, 0), 0.0001);
        assertEquals(0.0, identityCheck.get(0, 1), 0.0001);
        assertEquals(0.0, identityCheck.get(1, 0), 0.0001);
        assertEquals(1.0, identityCheck.get(1, 1), 0.0001);

        // 10. Matrix Power
        Matrix p0 = a.pow(0);
        assertEquals(1.0, p0.get(0, 0), 0.0001);
        assertEquals(0.0, p0.get(0, 1), 0.0001);

        Matrix p2 = a.pow(2);
        // A^2 = [ [2,1],[1,3] ] * [ [2,1],[1,3] ] = [ [5, 5], [5, 10] ]
        assertEquals(5.0, p2.get(0, 0), 0.0001);
        assertEquals(10.0, p2.get(1, 1), 0.0001);
    }
}
