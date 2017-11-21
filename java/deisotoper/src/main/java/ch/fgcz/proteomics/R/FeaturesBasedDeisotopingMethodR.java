package ch.fgcz.proteomics.R;

/**
 * @author Lucas Schmidt
 * @since 2017-10-31
 */

import java.util.ArrayList;

import java.util.List;

import ch.fgcz.proteomics.fbdm.Configuration;

public class FeaturesBasedDeisotopingMethodR {
    // public static MassSpectrometryMeasurement
    // deisotopeMSMR(MassSpectrometryMeasurement input, String modus,
    // Configuration config) {
    // MassSpectrometryMeasurement output = new
    // MassSpectrometryMeasurement(input.getSource() + "_output");
    //
    // String date = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());
    //
    // for (MassSpectrum ms : input.getMSlist()) {
    //
    // output.getMSlist().add(deisotopeMS(ms, modus, config, date));
    // }
    //
    // return new Deisotoper().deisotopeMSM(input, modus, config);
    // }

    // public static MassSpectrum deisotopeMSR(MassSpectrum input, String modus,
    // Configuration config) {
    // return new Deisotoper().deisotopeMS(input, modus, config);
    // }

    // public static IsotopicMassSpectrum getIMS(MassSpectrum input, String modus,
    // Configuration config) {
    // return Deisotoper.deisotopeMS(input, modus, config);
    // }

    // public static String getGraphFromIS(IsotopicSet is, MassSpectrum ms,
    // Configuration config) {
    // IsotopicClusterGraph icg = new IsotopicClusterGraph(is);
    //
    // icg.scoreIsotopicClusterGraph(ms.getPeptideMass(), ms.getChargeState(),
    // config.getErrortolerance(),
    // new Peaklist(ms), config);
    //
    // return icg.toDOTGraph();
    // }

    // public static String getStatistic(MassSpectrometryMeasurement msm,
    // Configuration config) {
    // int numberms = msm.getMSlist().size();
    // int numberis = 0;
    // int numberic = 0;
    // int numberipeaks = 0;
    // int numberpeaks = 0;
    //
    // for (MassSpectrum ms : msm.getMSlist()) {
    // numberpeaks += ms.getMz().size();
    //
    // IsotopicMassSpectrum ims = new IsotopicMassSpectrum(ms, config.getDelta(),
    // config);
    //
    // numberis += ims.getIsotopicMassSpectrum().size();
    //
    // for (IsotopicSet is : ims.getIsotopicMassSpectrum()) {
    // numberic += is.getIsotopicSet().size();
    // List<Peak> peakic = new ArrayList<>();
    //
    // for (IsotopicCluster ic : is.getIsotopicSet()) {
    // peakic.addAll(ic.getIsotopicCluster());
    // }
    //
    // Set<Double> titles = new HashSet<Double>();
    // List<Peak> result = new ArrayList<Peak>();
    //
    // for (Peak p : peakic) {
    // if (titles.add(p.getMz())) {
    // result.add(p);
    // }
    // }
    //
    // numberipeaks += result.size();
    // }
    // }
    //
    // StringBuilder sb = new StringBuilder();
    // String linesep = System.getProperty("line.separator");
    // sb.append(
    // "NumberOfMassSpectra,NumberOfIsotopicSets,NumberOfIsotopicClusters,NumberOfPeaksInIsotopicClusters,NumberOfPeaks")
    // .append(linesep);
    // sb.append(numberms).append(",").append(numberis).append(",").append(numberic).append(",").append(numberipeaks)
    // .append(",").append(numberpeaks).append(linesep);
    //
    // return sb.toString();
    // }

    public static Configuration createConfigurationR(double[] aa, double F1, double F2, double F3, double F4, double F5,
	    double DELTA, double ERRORTOLERANCE, double DISTANCE, double NOISE, boolean DECHARGE) {
	List<Double> AA_MASS = new ArrayList<>();

	for (int i = 0; i < aa.length; i++) {
	    AA_MASS.add(aa[i]);
	}

	return new Configuration(AA_MASS, F1, F2, F3, F4, F5, DELTA, ERRORTOLERANCE, DISTANCE, NOISE, DECHARGE);
    }

    public static Configuration createConfigurationR(double F1, double F2, double F3, double F4, double F5,
	    double DELTA, double ERRORTOLERANCE, double DISTANCE, double NOISE, boolean DECHARGE) {
	return new Configuration(F1, F2, F3, F4, F5, DELTA, ERRORTOLERANCE, DISTANCE, NOISE, DECHARGE);
    }

    // public static String getScoreConfigAsCSV(Configuration config) {
    // return makeCSV(config);
    // }
    //
    // @SuppressWarnings("unchecked")
    // private static String makeCSV(Configuration config) {
    // Map<String, Object> map = config.getScoreConfigAsMap();
    //
    // StringBuilder sb = new StringBuilder();
    // String linesep = System.getProperty("line.separator");
    // sb.append("Key,Value").append(linesep);
    //
    // for (Map.Entry<String, Object> entry : map.entrySet()) {
    // if (entry.getValue() instanceof List<?>) {
    // List<Double> value = (List<Double>) entry.getValue();
    // for (Double x : value) {
    // sb.append(entry.getKey()).append(",").append(x).append(linesep);
    // }
    // } else {
    // sb.append(entry.getKey()).append(",").append(entry.getValue()).append(linesep);
    // }
    // }
    //
    // return sb.toString();
    // }
}