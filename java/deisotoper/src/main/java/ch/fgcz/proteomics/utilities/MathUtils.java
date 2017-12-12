package ch.fgcz.proteomics.utilities;

public class MathUtils {
    private MathUtils() {
        throw new IllegalStateException("Mathematic utilities class");
    }

    public static boolean fuzzyEqual(double a, double b, double tolerance) {
        return Math.abs(a - b) < tolerance;
    }
}
