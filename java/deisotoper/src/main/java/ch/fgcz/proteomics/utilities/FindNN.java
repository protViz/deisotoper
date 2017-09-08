package ch.fgcz.proteomics.utilities;

/**
 * @author Lucas Schmidt
 * @since 2017-08-21
 */

import java.util.Arrays;

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;

public class FindNN {
    private static double[] q;
    private static double[] vec;

    public static void setQ(double[] x) {
        FindNN.q = x;
    }

    public static void setVec(double[] x) {
        FindNN.vec = x;
    }

    /**
     * Finds the nearest neighbors. Applies the pattern on the data to find the nearest neighbors of the pattern.
     * 
     * @param pattern
     * @param data
     * @return index array
     * @see MassSpectrometryMeasurement
     */
    public static double[] findNN(double[] pattern, double[] data) {
        double[] index = new double[pattern.length];

        for (int i = 0; i < pattern.length; i++) {
            index[i] = ((double) Arrays.binarySearch(data, pattern[i]));
            if (index[i] < 0)
                index[i] = -(index[i]) - 1;

            if (index[i] == 0) {
            } else if (index[i] >= data.length) {
                index[i] = index[i] - 1;
            } else {
                index[i] = (((Math.abs(data[(int) (index[i] - 1)] - pattern[i])) < (Math.abs(data[(int) index[i]] - pattern[i]))) ? index[i] - 1 : index[i]);
            }
        }

        return index;
    }

    /**
     * @return index array
     */
    public static double[] getIndex() {
        return FindNN.findNN(FindNN.q, FindNN.vec);
    }
}
