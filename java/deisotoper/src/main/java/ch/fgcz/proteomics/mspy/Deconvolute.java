package ch.fgcz.proteomics.mspy;

/**
 * @author Lucas Schmidt
 * @since 2017-09-15
 */

public class Deconvolute {
    public static ch.fgcz.proteomics.dto.MassSpectrometryMeasurement deconvoluteMSM(ch.fgcz.proteomics.dto.MassSpectrometryMeasurement input) {
        return Convert.deisotopeordeconvolute(input, "deconvolute");
    }
}
