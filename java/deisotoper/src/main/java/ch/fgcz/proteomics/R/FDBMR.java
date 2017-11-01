package ch.fgcz.proteomics.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Lucas Schmidt
 * @since 2017-10-31
 */

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.dto.MassSpectrum;
import ch.fgcz.proteomics.fdbm.*;

public class FDBMR {
    public static MassSpectrometryMeasurement deisotopeMSMR(MassSpectrometryMeasurement input, String modus, String file) {
        return Deisotoper.deisotopeMSM(input, modus, file);
    }

    public static MassSpectrum deisotopeMSR(MassSpectrum input, String modus, ScoreConfig config) {
        return Deisotoper.deisotopeMS(input, modus, config);
    }

    public static IsotopicMassSpectrum getIMS(MassSpectrum input, double errortolerance, String file) {
        ScoreConfig config = new ScoreConfig(file);
        return new IsotopicMassSpectrum(input, errortolerance, config);
    }

    public static String getGraphFromIS(IsotopicSet is) {
        IsotopicClusterGraph icg = new IsotopicClusterGraph(is);

        return icg.createDOTIsotopicClusterGraph();
    }

    public static String getScoreConfigAsCSV(ScoreConfig config) {
        return makeCSV(config);
    }

    @SuppressWarnings("unchecked")
    private static String makeCSV(ScoreConfig config) {
        Map<String, Object> map = config.getScoreConfigAsMap();

        StringBuilder sb = new StringBuilder();
        String linesep = System.getProperty("line.separator");
        sb.append("Key,Value").append(linesep);

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof List<?>) {
                List<Double> value = (List<Double>) entry.getValue();
                for (Double x : value) {
                    sb.append(entry.getKey()).append(",").append(x).append(linesep);
                }
            } else {
                sb.append(entry.getKey()).append(",").append(entry.getValue()).append(linesep);
            }
        }

        return sb.toString();
    }
}