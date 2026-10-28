package MatrixCalculator.analyzer;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Represents the immutable result of a matrix property analysis,
 * including identified properties and GUI-ready capability flags.
 */
public class MatrixAnalysis {
    private final Set<MatrixProperty> properties;
    private final int rows;
    private final int cols;

    public MatrixAnalysis(Set<MatrixProperty> properties, int rows, int cols) {
        this.properties = Collections.unmodifiableSet(EnumSet.copyOf(properties));
        this.rows = rows;
        this.cols = cols;
    }

    /**
     * Returns an unmodifiable set of all properties identified in the matrix.
     * 
     * @return set of MatrixProperty
     */
    public Set<MatrixProperty> getProperties() {
        return properties;
    }

    /**
     * Checks if the matrix possesses a specific property.
     * 
     * @param property the MatrixProperty to check
     * @return true if present, false otherwise
     */
    public boolean hasProperty(MatrixProperty property) {
        return properties.contains(property);
    }

    /**
     * Indicates whether the matrix is square (rows == cols).
     */
    public boolean isSquare() {
        return properties.contains(MatrixProperty.SQUARE);
    }

    /**
     * Indicates whether the matrix is a zero matrix.
     */
    public boolean isZero() {
        return properties.contains(MatrixProperty.ZERO);
    }

    /**
     * Indicates whether the matrix is symmetric.
     */
    public boolean isSymmetric() {
        return properties.contains(MatrixProperty.SYMMETRIC);
    }

    // --- GUI Capability Flags (Decoupled from math logic) ---

    /**
     * Determines if operations requiring square matrices (like determinant, inverse, power) are permitted.
     */
    public boolean canCalculateSquareOperations() {
        return isSquare();
    }

    /**
     * Determines if inversion / linear system resolution is structurally eligible (square matrix).
     * Note: Non-singularity must still be verified during execution.
     */
    public boolean canInvertOrSolve() {
        return isSquare();
    }
}
