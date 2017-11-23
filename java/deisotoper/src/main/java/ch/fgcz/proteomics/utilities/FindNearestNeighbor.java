package ch.fgcz.proteomics.utilities;

/**
 * @author Lucas Schmidt
 * @since 2017-08-21
 */

import java.util.Arrays;

public class FindNearestNeighbor {
    public static double[] findNearestNeighbor(double[] pattern, double[] data) {
        double[] index = new double[pattern.length];

        for (int i = 0; i < pattern.length; i++) {
            index[i] = ((double) Arrays.binarySearch(data, pattern[i]));
            if (index[i] < 0)
                index[i] = -(index[i]) - 1;

            if (index[i] == 0) {
            } else if (index[i] >= data.length) {
                index[i] = index[i] - 1;
            } else {
                index[i] = (((Math.abs(data[(int) (index[i] - 1)] - pattern[i])) < (Math
                        .abs(data[(int) index[i]] - pattern[i]))) ? index[i] - 1 : index[i]);
            }
        }

        for (int i = 0; i < index.length; i++) {
            index[i] = index[i] + 1;
        }

        return index;
    }
}
