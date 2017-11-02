package ch.fgcz.proteomics.utilities;

/**
 * @author Lucas Schmidt
 * @since 2017-08-21
 */

import java.util.Arrays;

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;

// TODO (LS) rename  Use of acronyms for class names should be avoided see: http://www.oracle.com/technetwork/java/codeconventions-135099.html
public class FindNN {
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
}
