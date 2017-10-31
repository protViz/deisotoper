package ch.fgcz.proteomics.R;

/**
 * @author Lucas Schmidt
 * @since 2017-10-31
 */

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.dto.MassSpectrum;
import ch.fgcz.proteomics.fdbm.*;

public class FDBMR {
    public MassSpectrometryMeasurement deisotopeMSMR(MassSpectrometryMeasurement input, boolean save, String modus, String file, double percent, double error, double delta) {
        return new Deisotope().deisotopeMSM(input, save, modus, file, percent, error, delta);
    }

    public MassSpectrum deisotopeMSR(MassSpectrum input, boolean save, String modus, ScoreConfig config, double percent, double error, double delta) {
        return new Deisotope().deisotopeMS(input, save, modus, config, percent, error, delta);
    }
}
