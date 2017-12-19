package ch.fgcz.proteomics.r;

/**
 * @author Lucas Schmidt
 * @since 2017-10-31
 */

import ch.fgcz.proteomics.utilities.FindNearestNeighbor;

public class Utilities {
    public static double[] findNNR(double[] vector, double[] query) {
        return FindNearestNeighbor.findNearestNeighbor(vector, query);
    }

    private Utilities() {
        // Utility class
    }
}
