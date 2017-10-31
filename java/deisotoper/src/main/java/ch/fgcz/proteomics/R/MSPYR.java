package ch.fgcz.proteomics.R;

/**
 * @author Lucas Schmidt
 * @since 2017-10-31
 */

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.mspy.Convert;

public class MSPYR {
    public MassSpectrometryMeasurement deisotopeMSMR(MassSpectrometryMeasurement input) {
        return Convert.deisotopeordeconvolute(input, "deisotope");
    }

    public MassSpectrometryMeasurement deconvoluteMSMR(MassSpectrometryMeasurement input) {
        return Convert.deisotopeordeconvolute(input, "deconvolute");
    }
}