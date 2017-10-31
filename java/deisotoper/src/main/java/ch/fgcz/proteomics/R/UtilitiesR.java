package ch.fgcz.proteomics.R;

/**
 * @author Lucas Schmidt
 * @since 2017-10-31
 */

import ch.fgcz.proteomics.utilities.FindNN;

public class UtilitiesR {
    public static double[] findNNR(double[] vec, double[] q) {
        return FindNN.findNN(vec, q);
    }
}
