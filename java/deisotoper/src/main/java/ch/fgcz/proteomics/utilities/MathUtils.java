package ch.fgcz.proteomics.utilities;

public class MathUtils {
    public static boolean fuzzyEqual(double a, double b, double tolerance) {
        return Math.abs(a - b) < tolerance;
    }
}
