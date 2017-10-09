package ch.fgcz.proteomics.mspy;

/**
 * @author Lucas Schmidt
 * @since 2017-09-15
 */

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;

public class Deconvolute {
    public static MassSpectrometryMeasurement deconvoluteMSM(MassSpectrometryMeasurement input) {
        return Convert.deisotopeordeconvolute(input, "deconvolute");
    }
}
