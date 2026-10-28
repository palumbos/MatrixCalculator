package com.matriz.engine;

import MatrixCalculator.Matrix;
import MatrixCalculator.analyzer.MatrixAnalysis;
import MatrixCalculator.analyzer.MatrixAnalyzer;
import MatrixCalculator.analyzer.MatrixProperty;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MatrixAnalyzerTest {

    @Test
    void testZeroMatrixAnalysis() {
        Matrix m = new Matrix(new double[][]{
            {0.0, 0.0},
            {0.0, 0.0}
        });

        MatrixAnalysis analysis = MatrixAnalyzer.analyze(m);

        assertTrue(analysis.hasProperty(MatrixProperty.ZERO));
        assertTrue(analysis.hasProperty(MatrixProperty.SQUARE));
        assertTrue(analysis.hasProperty(MatrixProperty.DIAGONAL));
        assertTrue(analysis.hasProperty(MatrixProperty.SCALAR));
        assertTrue(analysis.hasProperty(MatrixProperty.SYMMETRIC));
        assertTrue(analysis.hasProperty(MatrixProperty.ANTISYMMETRIC));
        assertTrue(analysis.hasProperty(MatrixProperty.UPPER_TRIANGULAR));
        assertTrue(analysis.hasProperty(MatrixProperty.LOWER_TRIANGULAR));
        assertFalse(analysis.hasProperty(MatrixProperty.IDENTITY));
    }

    @Test
    void testIdentityMatrixAnalysis() {
        Matrix m = new Matrix(new double[][]{
            {1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0}
        });

        MatrixAnalysis analysis = MatrixAnalyzer.analyze(m);

        assertTrue(analysis.hasProperty(MatrixProperty.IDENTITY));
        assertTrue(analysis.hasProperty(MatrixProperty.DIAGONAL));
        assertTrue(analysis.hasProperty(MatrixProperty.SCALAR));
        assertTrue(analysis.hasProperty(MatrixProperty.SYMMETRIC));
        assertTrue(analysis.hasProperty(MatrixProperty.UPPER_TRIANGULAR));
        assertTrue(analysis.hasProperty(MatrixProperty.LOWER_TRIANGULAR));
        assertTrue(analysis.isSquare());
        assertTrue(analysis.canCalculateSquareOperations());
    }

    @Test
    void testScalarAndDiagonalMatrix() {
        Matrix m = new Matrix(new double[][]{
            {5.0, 0.0},
            {0.0, 5.0}
        });

        MatrixAnalysis analysis = MatrixAnalyzer.analyze(m);

        assertTrue(analysis.hasProperty(MatrixProperty.DIAGONAL));
        assertTrue(analysis.hasProperty(MatrixProperty.SCALAR));
        assertFalse(analysis.hasProperty(MatrixProperty.IDENTITY));
        assertTrue(analysis.hasProperty(MatrixProperty.SYMMETRIC));
    }

    @Test
    void testTriangularMatrices() {
        Matrix upper = new Matrix(new double[][]{
            {1.0, 2.0, 3.0},
            {0.0, 4.0, 5.0},
            {0.0, 0.0, 6.0}
        });

        MatrixAnalysis analysisUpper = MatrixAnalyzer.analyze(upper);
        assertTrue(analysisUpper.hasProperty(MatrixProperty.UPPER_TRIANGULAR));
        assertFalse(analysisUpper.hasProperty(MatrixProperty.LOWER_TRIANGULAR));

        Matrix lower = new Matrix(new double[][]{
            {1.0, 0.0, 0.0},
            {2.0, 3.0, 0.0},
            {4.0, 5.0, 6.0}
        });

        MatrixAnalysis analysisLower = MatrixAnalyzer.analyze(lower);
        assertTrue(analysisLower.hasProperty(MatrixProperty.LOWER_TRIANGULAR));
        assertFalse(analysisLower.hasProperty(MatrixProperty.UPPER_TRIANGULAR));
    }

    @Test
    void testSymmetricAndAntisymmetric() {
        Matrix symmetric = new Matrix(new double[][]{
            {1.0, 2.0},
            {2.0, 3.0}
        });
        assertTrue(MatrixAnalyzer.analyze(symmetric).hasProperty(MatrixProperty.SYMMETRIC));

        Matrix antisymmetric = new Matrix(new double[][]{
            {0.0, 2.0},
            {-2.0, 0.0}
        });
        assertTrue(MatrixAnalyzer.analyze(antisymmetric).hasProperty(MatrixProperty.ANTISYMMETRIC));
    }

    @Test
    void testRowAndColumnVectors() {
        Matrix row = new Matrix(new double[][]{{1.0, 2.0, 3.0}});
        MatrixAnalysis analysisRow = MatrixAnalyzer.analyze(row);
        assertTrue(analysisRow.hasProperty(MatrixProperty.ROW));
        assertTrue(analysisRow.hasProperty(MatrixProperty.NON_SQUARE));
        assertFalse(analysisRow.isSquare());

        Matrix col = new Matrix(new double[][]{{1.0}, {2.0}, {3.0}});
        MatrixAnalysis analysisCol = MatrixAnalyzer.analyze(col);
        assertTrue(analysisCol.hasProperty(MatrixProperty.COLUMN));
        assertTrue(analysisCol.hasProperty(MatrixProperty.NON_SQUARE));
    }

    @Test
    void testToleranceAndFloatingPointResiduals() {
        // Value 1e-12 should be treated as zero (within epsilon 1e-11)
        Matrix m = new Matrix(new double[][]{
            {1.0, 1e-12},
            {0.0, 1.0}
        });

        MatrixAnalysis analysis = MatrixAnalyzer.analyze(m);
        assertTrue(analysis.hasProperty(MatrixProperty.UPPER_TRIANGULAR));
        assertTrue(analysis.hasProperty(MatrixProperty.DIAGONAL));
    }

    @Test
    void testSingleElementMatrix1x1() {
        Matrix m = new Matrix(new double[][]{{7.0}});
        MatrixAnalysis analysis = MatrixAnalyzer.analyze(m);

        assertTrue(analysis.hasProperty(MatrixProperty.SQUARE));
        assertTrue(analysis.hasProperty(MatrixProperty.ROW));
        assertTrue(analysis.hasProperty(MatrixProperty.COLUMN));
        assertTrue(analysis.hasProperty(MatrixProperty.DIAGONAL));
        assertTrue(analysis.hasProperty(MatrixProperty.SCALAR));
        assertTrue(analysis.hasProperty(MatrixProperty.SYMMETRIC));
        assertTrue(analysis.hasProperty(MatrixProperty.UPPER_TRIANGULAR));
        assertTrue(analysis.hasProperty(MatrixProperty.LOWER_TRIANGULAR));
    }
}
