package ch.fgcz.proteomics.R;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author Lucas Schmidt
 * @since 2017-10-31
 */

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public static String getGraphFromIS(IsotopicSet is, MassSpectrum ms, ScoreConfig config) {
        IsotopicClusterGraph icg = new IsotopicClusterGraph(is);

        icg.scoreIsotopicClusterGraph(ms.getPeptideMass(), ms.getChargeState(), config.getErrortolerance(), new Peaklist(ms), config);

        return icg.createDOTIsotopicClusterGraph();
    }

    public static String getStatistic(MassSpectrometryMeasurement msm, ScoreConfig config) {
        int numberms = msm.getMSlist().size();
        int numberis = 0;
        int numberic = 0;
        int numberipeaks = 0;
        int numberpeaks = 0;

        for (MassSpectrum ms : msm.getMSlist()) {
            numberpeaks += ms.getMz().size();

            IsotopicMassSpectrum ims = new IsotopicMassSpectrum(ms, config.getErrortolerance(), config);

            numberis += ims.getIsotopicMassSpectrum().size();

            for (IsotopicSet is : ims.getIsotopicMassSpectrum()) {
                numberic += is.getIsotopicSet().size();
                List<Peak> peakic = new ArrayList<>();

                for (IsotopicCluster ic : is.getIsotopicSet()) {
                    peakic.addAll(ic.getIsotopicCluster());
                }

                Set<Double> titles = new HashSet<Double>();
                List<Peak> result = new ArrayList<Peak>();

                for (Peak p : peakic) {
                    if (titles.add(p.getMz())) {
                        result.add(p);
                    }
                }

                numberipeaks += result.size();
            }
        }

        StringBuilder sb = new StringBuilder();
        String linesep = System.getProperty("line.separator");
        sb.append("NumberOfMassSpectra,NumberOfIsotopicSets,NumberOfIsotopicClusters,NumberOfPeaksInIsotopicClusters,NumberOfPeaks").append(linesep);
        sb.append(numberms).append(",").append(numberis).append(",").append(numberic).append(",").append(numberipeaks).append(",").append(numberpeaks).append(linesep);

        return sb.toString();
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