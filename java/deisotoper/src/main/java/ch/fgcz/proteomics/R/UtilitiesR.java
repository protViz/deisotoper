package ch.fgcz.proteomics.R;

/**
 * @author Lucas Schmidt
 * @since 2017-10-31
 */

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.dto.MassSpectrum;
import ch.fgcz.proteomics.utilities.FindNearestNeighbor;
import ch.fgcz.proteomics.utilities.Summary;

public class UtilitiesR {
    public static double[] findNNR(double[] vector, double[] query) {
        return FindNearestNeighbor.findNearestNeighbor(vector, query);
    }

    public static String summaryMSMR(MassSpectrometryMeasurement massSpectrometryMeasurement) {
        return Summary.makeSummary(massSpectrometryMeasurement);
    }

    public static String summaryMSR(MassSpectrum massSpectrum) {
        MassSpectrometryMeasurement msm = new MassSpectrometryMeasurement("");
        msm.getMSlist().add(massSpectrum);

        return Summary.makeSummary(msm);
    }
}
