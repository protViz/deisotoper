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
    public static double[] findNNR(double[] vec, double[] q) {
	return FindNearestNeighbor.findNN(vec, q);
    }

    public static String summaryMSMR(MassSpectrometryMeasurement m) {
	return Summary.makeSummary(m);
    }

    public static String summaryMSR(MassSpectrum m) {
	MassSpectrometryMeasurement msm = new MassSpectrometryMeasurement("");
	msm.getMSlist().add(m);

	return Summary.makeSummary(msm);
    }
}
