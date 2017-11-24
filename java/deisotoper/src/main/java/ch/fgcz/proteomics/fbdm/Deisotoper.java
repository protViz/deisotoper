package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-21
 */

import java.util.ArrayList;
import java.util.List;

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.dto.MassSpectrum;

public class Deisotoper {
    private boolean running = false;
    private PeakList peakList;
    private PeakList mergedPeakList;
    private Configuration config;
    private List<IsotopicSet> isotopicSets = new ArrayList<>();

    public void wasRunning() throws Exception {
        if (running == false) {
            throw new Exception(
                    "You must run the deisotope method before you can get a annotated spectrum/dot graphs/summary");
        }
    }

    public Configuration getConfiguration() {
        return config;
    }

    public void setConfiguration(Configuration config) {
        this.config = config;
    }

    public String getAnnotatedSpectrum() {
        return mergedPeakList.saveAnnotatedSpectrum();
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
        this.running = true;
        this.isotopicSets = new ArrayList<>();

        generateIsotopicSets(massSpectrum);

        List<IsotopicCluster> bestClusters = getBestClusters();

        PeakList peakListAggregated = aggregate(bestClusters, config.getModus());

        if (config.isDecharge()) {
            peakListAggregated = peakListAggregated.dechargePeaks(config.getH_MASS());
        }

        mergedPeakList = peakList.mergePeakLists(peakListAggregated);

        if (this.config.getNoise() != 0) {
            mergedPeakList = mergedPeakList.filterNoisePeaks(config.getNoise());
        }

        this.mergedPeakList = mergedPeakList.sortPeakList();

        return mergedPeakList.makeResultSpectrum(massSpectrum);
    }

    private List<IsotopicCluster> getBestClusters() {
        List<IsotopicCluster> bestClusters = new ArrayList<>();
        for (IsotopicSet isotopicSet : this.isotopicSets) {
            bestClusters.addAll(isotopicSet.getBestPath());
        }
        return bestClusters;
    }

    private PeakList aggregate(List<IsotopicCluster> isotopicClusters, String modus) {
        PeakList resultPeakList = new PeakList();

        for (IsotopicCluster istotopicCluster : isotopicClusters) {
            IsotopicCluster aggregatedCluster = istotopicCluster.aggregation(modus);
            Peak peak = aggregatedCluster.getPeak(0);
            resultPeakList.add(peak);
        }

        return resultPeakList;
    }

    private void generateIsotopicSets(MassSpectrum massSpectrum) {
        this.peakList = new PeakList(massSpectrum);

        int id = 0;
        for (int i = 0; i < peakList.size(); i++) {
            List<Peak> isotopicSet = new ArrayList<>();

            while (i < peakList.size() - 1) {
                boolean trigger = false;
                double distance = peakList.get(i + 1).getMz() - peakList.get(i).getMz();

                for (int charge = 1; charge <= 3; charge++) {
                    if ((config.getDistance() / charge) - config.getDelta() < distance
                            && distance < (config.getDistance() / charge) + config.getDelta()) {
                        if (isotopicSet.size() == 0) {
                            Peak peak = peakList.get(i);
                            peak.setInSet(true);
                            isotopicSet.add(peak);
                        }
                        Peak peak = peakList.get(i + 1);
                        peak.setInSet(true);
                        isotopicSet.add(peak);
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

                if (isotopicSet.size() == peakList.size()) {
                    break;
                }
            }
        }
    }
}