package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-21
 */

import java.util.ArrayList;
import java.util.List;

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.dto.MassSpectrum;

// TODO: Adjust tests!
public class Deisotoper {
    private Configuration config;
    private String annotatedSpectrum = null;
    private List<IsotopicSet> isotopicSets = new ArrayList<>();

    public Configuration getConfiguration() {
        return config;
    }

    public void setConfiguration(Configuration config) {
        this.config = config;
    }

    public String getAnnotatedSpectrum() {
        return annotatedSpectrum;
    }

    public void setAnnotatedSpectrum(String annotatedSpectrum) {
        this.annotatedSpectrum = annotatedSpectrum;
    }

    public List<String> getDotGraphs() {
        List<String> graph = new ArrayList<>();
        for (IsotopicSet isotopicSet : this.isotopicSets) {
            graph.add(isotopicSet.getDot());
        }
        return graph;
    }

    public List<IsotopicSet> getIsotopicSets() {
        return isotopicSets;
    }

    // Will be used to deisotope entire mgf files from Java.
    public MassSpectrometryMeasurement deisotopeMSM(MassSpectrometryMeasurement massSpectrometryMeasurementin,
            Configuration config) {
        MassSpectrometryMeasurement massSpectrometryMeasurementOut = new MassSpectrometryMeasurement(
                massSpectrometryMeasurementin.getSource());

        this.config = config;

        for (MassSpectrum massSpectrum : massSpectrometryMeasurementin.getMSlist()) {
            massSpectrometryMeasurementOut.getMSlist().add(deisotopeMS(massSpectrum));
        }

        return massSpectrometryMeasurementOut;
    }

    public MassSpectrum deisotopeMS(MassSpectrum massSpectrum) {
        this.annotatedSpectrum = null;
        //this.dotGraphs = new ArrayList<>();
        this.isotopicSets = new ArrayList<>();

        Peaklist peaklistIn = new Peaklist(massSpectrum);

        generateIsotopicSets(massSpectrum);

        Peaklist peaklistAggregated = aggregatePeaks(peaklistIn);

        if(config.isDecharge()) {
            peaklistAggregated = Peaklist.dechargePeaks(peaklistAggregated, config.getH_MASS());
        }
        if (this.config.getNoise() != 0) {
            peaklistAggregated = Peaklist.filterNoisePeaks(peaklistAggregated, config.getNoise());
        }

        Peaklist peaklistOut = Peaklist.sortPeaks(peaklistAggregated);

        return makeResultSpectrum(massSpectrum, peaklistOut);
    }

    private MassSpectrum makeResultSpectrum(MassSpectrum massSpectrum, Peaklist peaklist) {
        List<Double> mz = new ArrayList<>();
        List<Double> intensity = new ArrayList<>();
        List<Double> isotope = new ArrayList<>();
        List<Integer> charge = new ArrayList<>();
        for (int i = 0; i < peaklist.getPeaklist().size(); i++) {
            mz.add(peaklist.getPeaklist().get(i).getMz());
            intensity.add(peaklist.getPeaklist().get(i).getIntensity());
            isotope.add(peaklist.getPeaklist().get(i).getIsotope());
            charge.add(peaklist.getPeaklist().get(i).getCharge());
        }

        return new MassSpectrum(massSpectrum.getTyp(), massSpectrum.getSearchEngine(), mz, intensity,
                massSpectrum.getPeptideMass(), massSpectrum.getRt(), massSpectrum.getChargeState(),
                massSpectrum.getId(), charge, isotope);
    }

    private void generateIsotopicSets(MassSpectrum massSpectrum) {
        Peaklist peaklist = new Peaklist(massSpectrum);

        int id = 0;
        for (int i = 0; i < peaklist.getPeaklist().size(); i++) {
            List<Peak> isotopicSet = new ArrayList<>();

            while (i < peaklist.getPeaklist().size() - 1) {
                boolean trigger = false;
                double distance = peaklist.getPeaklist().get(i + 1).getMz() - peaklist.getPeaklist().get(i).getMz();

                for (int charge = 1; charge <= 3; charge++) {
                    if ((config.getDistance() / charge) - config.getDelta() < distance
                            && distance < (config.getDistance() / charge) + config.getDelta()) {
                        if (isotopicSet.size() == 0) {
                            isotopicSet.add((peaklist.getPeaklist().get(i)));
                        }
                        isotopicSet.add((peaklist.getPeaklist().get(i + 1)));
                        trigger = true;
                    }
                }

                if (trigger == false) {
                    break;
                }

                i++;
            }

            if (1 < isotopicSet.size()) {
                IsotopicSet temporaryIsotopicSet = new IsotopicSet(massSpectrum, isotopicSet, id, config);
                id++;

                this.isotopicSets.add(temporaryIsotopicSet);

                if (isotopicSet.size() == peaklist.getPeaklist().size()) {
                    break;
                }
            }
        }

        Peaklist peaklistIsotopicSets = getPeaklistFromIsotopicSets(peaklist);

        this.annotatedSpectrum = Peaklist.saveAnnotatedSpectrum(peaklistIsotopicSets);
    }

    private Peaklist getPeaklistFromIsotopicSets(Peaklist peaklist) {
        return getPeaklistFromIsotopicSets(peaklist, false);
    }

    private Peaklist aggregatePeaks(Peaklist peaklist) {
        return getPeaklistFromIsotopicSets(peaklist, true);
    }

    private Peaklist getPeaklistFromIsotopicSets(Peaklist peaklistIn, boolean aggregation) {
        Peaklist peaklistOut = new Peaklist();
        List<Double> mz = new ArrayList<>();

        for (IsotopicSet isotopicSet : this.isotopicSets) {

            List<IsotopicCluster> bestpath = isotopicSet.getBestPath();

            Peaklist peaklistAggregatedInside = new Peaklist();

            List<Double> mzInside = new ArrayList<>();

            for (IsotopicCluster cluster : bestpath) {
                if (cluster.getIsotopicCluster() != null) {
                    for (Peak peak : cluster.getIsotopicCluster()) {
                        mzInside.add(peak.getMz());
                    }

                    if (aggregation) {
                        cluster.aggregation(config.getModus());
                    }

                    int position = 1;
                    for (Peak peak : cluster.getIsotopicCluster()) {
                        peaklistAggregatedInside.getPeaklist()
                                .add(new Peak(peak.getMz(), peak.getIntensity(), (double) position, cluster.getCharge(),
                                        peak.getPeakID(), cluster.getClusterID(), isotopicSet.getSetID()));
                        position++;
                    }
                }
            }

            peaklistOut.getPeaklist().addAll(peaklistAggregatedInside.getPeaklist());

            mz.addAll(mzInside);
        }

        for (int i = 0; i < peaklistIn.getPeaklist().size(); i++) {
            if (!mz.contains(peaklistIn.getPeaklist().get(i).getMz())) {
                peaklistOut.getPeaklist().add(new Peak(peaklistIn.getPeaklist().get(i).getMz(),
                        peaklistIn.getPeaklist().get(i).getIntensity(), peaklistIn.getPeaklist().get(i).getPeakID()));
            }
        }

        return Peaklist.sortPeaks(peaklistOut);
    }

}