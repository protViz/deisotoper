
/**
 * @author Lucas Schmidt
 * @since 2017-08-15
 */

import java.util.ArrayList;
import java.util.List;

public class FindPatternLinear {
    private static double[] mZ;
    private static double[] pattern;
    private static double eps;

    public static void setMz(double[] x) {
        FindPatternLinear.mZ = x;
    }

    public static void setPattern(double[] x) {
        FindPatternLinear.pattern = x;
    }

    public static void setEps(double x) {
        FindPatternLinear.eps = x;
    }

    private static double[] findPattern(double[] mZ, double[] pattern, double eps) {
        // long time1 = System.currentTimeMillis();
        List<Double> index = new ArrayList<Double>();
        for (int i = 0; i < mZ.length; i++) {
            for (int j = 0; j < pattern.length; j++) {
                if (pattern[j] - eps <= mZ[i] && mZ[i] <= pattern[j] + eps) {
                    index.add((double) i);
                }
            }
        }

        double[] idx = index.stream().mapToDouble(Double::doubleValue).toArray();

        // long time2 = System.currentTimeMillis();

        // System.out.println("Time needed: " + (time2 - time1) + "ms");

        if (idx.length == 0) {
            return null;
        } else {
            return idx;
        }
    }

    public static double[] getIndex() {
        return findPattern(FindPatternLinear.mZ, FindPatternLinear.pattern, FindPatternLinear.eps);
    }

    // For Testing only
    public static void setAll() {
        double[] mZmain = { 1, 3, 4, 5, 7, 9, 11, 13, 14, 15, 17.5, 18, 20, 22 };
        double[] patternmain = { 0.6, 1, 5, 6, 23 };
        double epsmain = 1;

        FindPatternLinear.mZ = mZmain;
        FindPatternLinear.pattern = patternmain;
        FindPatternLinear.eps = epsmain;

    }

    // For Testing only
    public static void main(String[] args) {
        setAll();

        double[] indexmain = getIndex();

        for (int i = 0; i < indexmain.length; i++) {
            System.out.println("Pattern found at Index: " + (int) indexmain[i]);
        }
    }
}
