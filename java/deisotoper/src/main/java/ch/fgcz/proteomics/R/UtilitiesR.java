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
        return FindNearestNeighbor.findNN(vector, query);
    }

    public static String summaryMSMR(MassSpectrometryMeasurement massspectrometrymeasurement) {
        return Summary.makeSummary(massspectrometrymeasurement);
    }

    public static String summaryMSR(MassSpectrum massspectrum) {
        MassSpectrometryMeasurement msm = new MassSpectrometryMeasurement("");
        msm.getMSlist().add(massspectrum);

        return Summary.makeSummary(msm);
    }
}
