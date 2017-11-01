package ch.fgcz.proteomics.R;

/**
 * @author Lucas Schmidt
 * @since 2017-10-31
 */

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.dto.MassSpectrum;
import ch.fgcz.proteomics.fdbm.*;

public class FDBMR {
    public MassSpectrometryMeasurement deisotopeMSMR(MassSpectrometryMeasurement input, String modus, String file, double percent, double error, double delta) {
        return new Deisotoper().deisotopeMSM(input, modus, file, percent, error, delta);
    }

    public MassSpectrum deisotopeMSR(MassSpectrum input, String modus, ScoreConfig config, double percent, double error, double delta) {
        return new Deisotoper().deisotopeMS(input, modus, config, percent, error, delta);
    }

    public IsotopicMassSpectrum getIMS(MassSpectrum input, double errortolerance, String file) {
        ScoreConfig config = new ScoreConfig(file);
        return new IsotopicMassSpectrum(input, errortolerance, config);
    }

    public String getGraphFromIS(IsotopicSet is) {
        IsotopicClusterGraph icg = new IsotopicClusterGraph(is);

        return icg.createDOTIsotopicClusterGraph();
    }
}
