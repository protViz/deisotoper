package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-21
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.fgcz.proteomics.dto.MassSpecMeasure;
import ch.fgcz.proteomics.dto.MassSpectrum;

public class Deisotoper {
    private boolean running = false;
    private PeakList peakList;
    private PeakList mergedPeakList;
    private Configuration config;
    private List<IsotopicSet> isotopicSets = new ArrayList<>();

    public Deisotoper() {
        config = new Configuration();
    }

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
    public MassSpecMeasure deisotopeMSM(MassSpecMeasure massSpectrometryMeasurementin,
            Configuration config) {
        MassSpecMeasure massSpectrometryMeasurementOut = new MassSpecMeasure(
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

    // NEW VERSION OF GENERATEISOTOPICSETS
    protected void generateIsotopicSets(MassSpectrum massSpectrum) {
        this.peakList = new PeakList(massSpectrum);

        List<Peak> allPossiblePeaksForIsotopicSets = new ArrayList<>();
        for (int i = 0; i < peakList.size(); i++) {
            Peak peakI = peakList.get(i);
            for (int j = 0; j < peakList.size(); j++) {
                Peak peakJ = peakList.get(j);
                for (int charge = 1; charge < 4; charge++) {
                    double lowerThreshold = peakI.getMz() + config.getDistance() / charge - config.getDelta();
                    double higherThreshold = peakI.getMz() + config.getDistance() / charge + config.getDelta();

                    if (lowerThreshold < peakJ.getMz() && peakJ.getMz() < higherThreshold) {
                        peakI.setInSet(true);
                        peakJ.setInSet(true);
                        allPossiblePeaksForIsotopicSets.add(peakI);
                        allPossiblePeaksForIsotopicSets.add(peakJ);
                    }
                }
            }
        }

        allPossiblePeaksForIsotopicSets = removeMultiplePeaks(allPossiblePeaksForIsotopicSets);

        allPossiblePeaksForIsotopicSets = sortListOfPeaks(allPossiblePeaksForIsotopicSets);

        List<List<Peak>> allPossiblePeaksForIsotopicSetsParts = splitWhenOverOne(allPossiblePeaksForIsotopicSets);

        for (List<Peak> part : allPossiblePeaksForIsotopicSetsParts) {
            Set<List<Peak>> allIsotopicSets = splitAllPossibleIsotopicSets(new HashSet<Peak>(part));
            List<List<Peak>> splittedIsotopicSets = new ArrayList<>();

            for (List<Peak> peaks : allIsotopicSets) {
                if (peaks.size() > 1) {
                    peaks = sortListOfPeaks(peaks);

                    peaks = checkForCorrectRangeOfPeaks(peaks, this.config);

                    if (peaks != null) {
                        splittedIsotopicSets.add(peaks);
                    }
                }
            }

            splittedIsotopicSets = checkForContainingAndRemove(splittedIsotopicSets);

            int id = 0;
            for (List<Peak> peaks : splittedIsotopicSets) {
                IsotopicSet temporaryIsotopicSet = new IsotopicSet(massSpectrum, peaks, id, config);
                id++;

                this.isotopicSets.add(temporaryIsotopicSet);
            }
        }
    }

    private List<List<Peak>> splitWhenOverOne(List<Peak> peaks) {
        List<List<Peak>> allPeaks = new ArrayList<>();

        int j = 0;
        for (int i = 0; i < peaks.size() - 1; i++) {
            double distance = peaks.get(i + 1).getMz() - peaks.get(i).getMz();
            if (distance > 0.2 + config.getDistance()) {
                allPeaks.add(peaks.subList(j, i + 1));
                j = i + 1;
            } else if (i == peaks.size() - 2) {
                allPeaks.add(peaks.subList(j, i + 2));
            }
        }

        return allPeaks;
    }

    private static List<List<Peak>> checkForContainingAndRemove(List<List<Peak>> isotopicSets) {
        List<List<Peak>> temp = new ArrayList<>(isotopicSets);

        for (List<Peak> peaks1 : isotopicSets) {
            for (List<Peak> peaks2 : isotopicSets) {
                if (peaks1.equals(peaks2)) {
                    continue;
                }
                if (Collections.indexOfSubList(peaks1, peaks2) != -1) {
                    if (peaks1.size() > peaks2.size()) {
                        temp.remove(peaks2);
                    } else if (peaks1.size() < peaks2.size()) {
                        temp.remove(peaks1);
                    }
                }
            }
        }

        return temp;
    }

    private static List<Peak> sortListOfPeaks(List<Peak> peaks) {
        Collections.sort(peaks, new Comparator<Peak>() {
            @Override
            public int compare(Peak peakOne, Peak peakTwo) {
                return Double.compare(peakOne.getMz(), peakTwo.getMz());
            }
        });

        return peaks;
    }

    private static List<Peak> checkForCorrectRangeOfPeaks(List<Peak> peaks, Configuration config) {
        for (int i = 0; i < peaks.size() - 1; i++) {
            double distance = peaks.get(i + 1).getMz() - peaks.get(i).getMz();

            boolean b = false;
            for (int charge = 1; charge <= 3; charge++) {
                if (((config.getDistance() / charge - config.getDelta() < Math.abs(distance)
                        && Math.abs(distance) < config.getDistance() / charge + config.getDelta()))) {
                    b = true;
                }
            }

            if (b == false) {
                return null;
            }
        }

        return peaks;
    }

    private static Set<List<Peak>> splitAllPossibleIsotopicSets(Set<Peak> originalPeaks) {
        Set<List<Peak>> allPeaks = new HashSet<List<Peak>>();

        if (originalPeaks.isEmpty()) {
            allPeaks.add(new ArrayList<Peak>());
            return allPeaks;
        }

        List<Peak> listOfPeaks = new ArrayList<Peak>(originalPeaks);
        Peak head = listOfPeaks.get(0);
        Set<Peak> rPeaks = new HashSet<Peak>(listOfPeaks.subList(1, listOfPeaks.size()));

        for (List<Peak> peaks : splitAllPossibleIsotopicSets(rPeaks)) {
            List<Peak> newPeaks = new ArrayList<Peak>();
            newPeaks.add(head);
            newPeaks.addAll(peaks);
            allPeaks.add(newPeaks);
            allPeaks.add(peaks);
        }

        return allPeaks;
    }

    private static List<Peak> removeMultiplePeaks(List<Peak> peaks) {
        List<Peak> result = new ArrayList<>();
        Set<Peak> set = new HashSet<>();

        for (Peak peak : peaks) {
            if (set.add(peak)) {
                result.add(peak);
            }
        }

        return result;
    }

    // OLD VERSION OF GENERATEISOTOPICSETS
    // protected void generateIsotopicSets(MassSpectrum massSpectrum) {
    // this.peakList = new PeakList(massSpectrum);
    //
    // int id = 0;
    // for (int i = 0; i < peakList.size(); i++) {
    // List<Peak> isotopicSet = new ArrayList<>();
    //
    // while (i < peakList.size() - 1) {
    // boolean trigger = false;
    // double distance = peakList.get(i + 1).getMz() - peakList.get(i).getMz();
    //
    // for (int charge = 1; charge <= 3; charge++) {
    // if ((config.getDistance() / charge) - config.getDelta() < distance
    // && distance < (config.getDistance() / charge) + config.getDelta()) {
    // if (isotopicSet.size() == 0) {
    // Peak peak = peakList.get(i);
    // peak.setInSet(true);
    // isotopicSet.add(peak);
    // }
    // Peak peak = peakList.get(i + 1);
    // peak.setInSet(true);
    // isotopicSet.add(peak);
    // trigger = true;
    // }
    // }
    //
    // if (trigger == false) {
    // break;
    // }
    //
    // i++;
    // }
    //
    // if (1 < isotopicSet.size()) {
    // IsotopicSet temporaryIsotopicSet = new IsotopicSet(massSpectrum, isotopicSet,
    // id, config);
    // id++;
    //
    // this.isotopicSets.add(temporaryIsotopicSet);
    //
    // if (isotopicSet.size() == peakList.size()) {
    // break;
    // }
    // }
    // }
    // }

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
}