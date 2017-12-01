package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-21
 */

import java.util.ArrayList;
import java.util.Collections;
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

    // TODO: Doesn't work like it should!
    public String getAnnotatedSpectrum() {
        PeakList peaksInSet = collectPeaks();

        if (this.config.isDecharge()) {
            peaksInSet = peaksInSet.dechargePeaks(this.config.getH_MASS());
        }

        PeakList mergedPeakListLocal = new PeakList();

        mergedPeakListLocal = this.peakList.mergePeakLists(peaksInSet);

        if (this.config.getNoise() != 0) {
            mergedPeakListLocal = mergedPeakListLocal.filterNoisePeaks(this.config.getNoise());
        }

        mergedPeakListLocal = mergedPeakListLocal.sortPeakList();

        mergedPeakListLocal.sortForAnnotating();

        mergedPeakListLocal.removeMultiplePeaks();

        return mergedPeakListLocal.saveAnnotatedSpectrum();
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
    public MassSpecMeasure deisotopeMSM(MassSpecMeasure massSpectrometryMeasurementin, Configuration config) {
        MassSpecMeasure massSpectrometryMeasurementOut = new MassSpecMeasure(massSpectrometryMeasurementin.getSource());

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

        PeakList peakListAggregated = aggregate(bestClusters, this.config.getModus());

        if (this.config.isDecharge()) {
            peakListAggregated = peakListAggregated.dechargePeaks(this.config.getH_MASS());
        }

        this.mergedPeakList = this.peakList.mergePeakLists(peakListAggregated);

        if (this.config.getNoise() != 0) {
            this.mergedPeakList = this.mergedPeakList.filterNoisePeaks(this.config.getNoise());
        }

        this.mergedPeakList = mergedPeakList.sortPeakList();

        return this.mergedPeakList.makeResultSpectrum(massSpectrum);
    }

    protected List<IsotopicCluster> getBestClusters() {
        List<IsotopicCluster> bestClusters = new ArrayList<>();

        for (IsotopicSet isotopicSet : this.isotopicSets) {
            bestClusters.addAll(isotopicSet.getBestPath());
        }

        return bestClusters;
    }

    protected PeakList aggregate(List<IsotopicCluster> isotopicClusters, String modus) {
        PeakList resultPeakList = new PeakList();

        double sumBefore = sumAllIntensities(isotopicClusters);

        isotopicClusters = removeOverlappingPeaksInClusters(isotopicClusters);

        for (IsotopicCluster isotopicCluster : isotopicClusters) {
            if (isotopicCluster.size() > 1) {
                IsotopicCluster aggregatedCluster = isotopicCluster.aggregation(modus);
                Peak peak = aggregatedCluster.getPeak(0);
                resultPeakList.add(peak);
            } else if (isotopicCluster.size() == 0) {
            } else {
                Peak peak = isotopicCluster.getPeak(0);
                resultPeakList.add(peak);
            }
        }

        double sumAfter = sumAllIntensities(isotopicClusters);

        try {
            intensityCheck(sumBefore, sumAfter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultPeakList;
    }

    private void intensityCheck(double before, double after) throws Exception {
        if ((double) Math.round(before * 1000d) / 1000d != (double) Math.round(after * 1000d) / 1000d) {
            throw new Exception("Wrong intensities after aggregation (Intensity before aggregation: " + before
                    + " and after aggregation: " + after + "!");
        }
    }

    private double sumAllIntensities(List<IsotopicCluster> isotopicClusters) {
        PeakList peakList = new PeakList();
        double intensitySum = 0;

        for (IsotopicCluster isotopicCluster : isotopicClusters) {
            peakList.addAll(isotopicCluster.getIsotopicCluster());
        }

        peakList.removeMultiplePeaks();

        for (Peak peak : peakList.getPeakList()) {
            intensitySum += peak.getIntensity();
        }

        return intensitySum;
    }

    private List<IsotopicCluster> removeOverlappingPeaksInClusters(List<IsotopicCluster> isotopicClusters) {
        // If cluster has same peak/peaks as other cluster.
        // Remove this peak in the lowest charged cluster.
        // Aggregate only the non removed cluster and add the remaining peaks from the
        // overlapping cluster to the resultPeakList.

        for (IsotopicCluster isotopicCluster1 : isotopicClusters) {
            for (IsotopicCluster isotopicCluster2 : isotopicClusters) {
                if (isotopicCluster1.equals(isotopicCluster2)) {
                    continue;
                }

                if (hasSamePeaks(isotopicCluster1, isotopicCluster2)) {
                    hasSameTrue(isotopicCluster1, isotopicCluster2);
                }
            }
        }

        return isotopicClusters;
    }

    private void hasSameTrue(IsotopicCluster isotopicCluster1, IsotopicCluster isotopicCluster2) {
        if (isotopicCluster1.getCharge() > isotopicCluster2.getCharge()) {
            isotopicCluster1.getIsotopicCluster().removeAll(isotopicCluster2.getIsotopicCluster());
        } else if (isotopicCluster1.getCharge() < isotopicCluster2.getCharge()) {
            isotopicCluster2.getIsotopicCluster().removeAll(isotopicCluster1.getIsotopicCluster());
        } else {
            double intensitySumOfCluster1 = sumIntensity(isotopicCluster1);
            double intensitySumOfCluster2 = sumIntensity(isotopicCluster2);
            if (intensitySumOfCluster1 > intensitySumOfCluster2) {
                isotopicCluster2.getIsotopicCluster().removeAll(isotopicCluster1.getIsotopicCluster());
            } else {
                isotopicCluster1.getIsotopicCluster().removeAll(isotopicCluster2.getIsotopicCluster());
            }
        }
    }

    private double sumIntensity(IsotopicCluster isotopicCluster1) {
        double intensitySum = 0;

        for (Peak peak : isotopicCluster1.getIsotopicCluster()) {
            intensitySum += peak.getIntensity();
        }

        return intensitySum;
    }

    private boolean hasSamePeaks(IsotopicCluster isotopicCluster1, IsotopicCluster isotopicCluster2) {
        for (Peak peak1 : isotopicCluster1.getIsotopicCluster()) {
            for (Peak peak2 : isotopicCluster2.getIsotopicCluster()) {
                if (peak1.equals(peak2)) {
                    return true;
                }
            }
        }

        return false;
    }

    // New version of generateIsotopicSets.
    protected void generateIsotopicSets(MassSpectrum massSpectrum) {
        this.peakList = new PeakList(massSpectrum);

        PeakList allPossiblePeaks = collectAllPossiblePeaks(this.peakList, this.config);
        allPossiblePeaks = allPossiblePeaks.removeMultiplePeaks();
        allPossiblePeaks = allPossiblePeaks.sortPeakList();
        List<PeakList> allPossiblePeaksParts = splitIntoParts(allPossiblePeaks, this.config);
        List<PeakList> listOfIsotopicSets = iterateThroughParts(allPossiblePeaksParts, this.config);

        this.isotopicSets = addSplittedIsotopicSetsToIsotopicSets(listOfIsotopicSets, massSpectrum, this.config);
    }

    private static List<PeakList> iterateThroughParts(List<PeakList> parts, Configuration config) {
        List<PeakList> combinedPeakLists = new ArrayList<>();

        for (PeakList part : parts) {
            Set<PeakList> setOfPeakLists = splitAllPossibleIsotopicSets(new HashSet<Peak>(part.getPeakList()));
            List<PeakList> listOfPeakLists = new ArrayList<>();

            listOfPeakLists = sortAndCheckCorrectnessOfSplittedIsotopicSets(setOfPeakLists, listOfPeakLists, config);

            listOfPeakLists = checkForContainingAndRemoveWrongSets(listOfPeakLists);

            combinedPeakLists.addAll(listOfPeakLists);
        }

        return combinedPeakLists;
    }

    private static List<IsotopicSet> addSplittedIsotopicSetsToIsotopicSets(List<PeakList> listOfIsotopicSets,
            MassSpectrum massSpectrum, Configuration config) {
        List<IsotopicSet> isotopicSets = new ArrayList<>();
        int id = 0;

        for (PeakList isotopicSet : listOfIsotopicSets) {
            IsotopicSet temporaryIsotopicSet = new IsotopicSet(massSpectrum, isotopicSet.getPeakList(), id, config);
            id++;
            isotopicSets.add(temporaryIsotopicSet);
        }

        return isotopicSets;
    }

    private static List<PeakList> sortAndCheckCorrectnessOfSplittedIsotopicSets(Set<PeakList> allPossiblePeaks,
            List<PeakList> correctIsotopicSets, Configuration config) {
        for (PeakList possiblePeaks : allPossiblePeaks) {
            if (1 < possiblePeaks.size()) {
                possiblePeaks = possiblePeaks.sortPeakList();

                possiblePeaks = checkForCorrectRangeOfPeaks(possiblePeaks, config);

                if (possiblePeaks != null) {
                    correctIsotopicSets.add(possiblePeaks);
                }
            }
        }

        return correctIsotopicSets;
    }

    private static PeakList collectAllPossiblePeaks(PeakList peakList, Configuration config) {
        PeakList allPossiblePeaks = new PeakList();
        for (int i = 0; i < peakList.size(); i++) {
            Peak peakI = peakList.get(i);
            for (int j = 0; j < peakList.size(); j++) {
                Peak peakJ = peakList.get(j);
                allPossiblePeaks = collectForEachCharge(allPossiblePeaks, peakI, peakJ, config);
            }
        }

        return allPossiblePeaks;
    }

    private static PeakList collectForEachCharge(PeakList allPossiblePeaks, Peak peakI, Peak peakJ,
            Configuration config) {
        // check if both peaks could be in set.
        for (int charge = 1; charge < 4; charge++) {
            double lowerThreshold = peakI.getMz() + config.getIsotopicPeakDistance() / charge - config.getDelta();
            double higherThreshold = peakI.getMz() + config.getIsotopicPeakDistance() / charge + config.getDelta();

            if (lowerThreshold < peakJ.getMz() && peakJ.getMz() < higherThreshold) {
                peakI.setInSet(true);
                peakJ.setInSet(true);
                allPossiblePeaks.add(peakI);
                allPossiblePeaks.add(peakJ);
            }
        }

        return allPossiblePeaks;
    }

    private static List<PeakList> splitIntoParts(PeakList peaks, Configuration config) {
        List<PeakList> partsOfPeaks = new ArrayList<>();

        int j = 0;
        for (int i = 0; i < peaks.size() - 1; i++) {
            double distance = peaks.get(i + 1).getMz() - peaks.get(i).getMz();
            if (distance > config.getIsotopicPeakDistance() + 1) {
                PeakList peaks2 = new PeakList(peaks.getPeakList().subList(j, i + 1));
                partsOfPeaks.add(peaks2);
                j = i + 1;
            } else if (i == peaks.size() - 2) {
                PeakList peaks2 = new PeakList(peaks.getPeakList().subList(j, i + 2));
                partsOfPeaks.add(peaks2);
            }
        }

        return partsOfPeaks;
    }

    private static List<PeakList> checkForContainingAndRemoveWrongSets(List<PeakList> listOfPeakLists) {
        List<PeakList> tempListOfPeakLists = new ArrayList<>(listOfPeakLists);

        for (PeakList peakList1 : listOfPeakLists) {
            for (PeakList peakList2 : listOfPeakLists) {
                if (peakList1.equals(peakList2)) {
                    continue;
                }
                if (Collections.indexOfSubList(peakList1.getPeakList(), peakList2.getPeakList()) != -1) {
                    if (peakList1.size() > peakList2.size()) {
                        tempListOfPeakLists.remove(peakList2);
                    } else if (peakList1.size() < peakList2.size()) {
                        tempListOfPeakLists.remove(peakList1);
                    }
                }
            }
        }

        tempListOfPeakLists = checkIfRemovedCorrectly(tempListOfPeakLists);

        return tempListOfPeakLists;
    }

    private static List<PeakList> checkIfRemovedCorrectly(List<PeakList> listOfPeakLists) {
        List<PeakList> tempListOfPeakLists = new ArrayList<>(listOfPeakLists);

        for (PeakList peaks1 : listOfPeakLists) {
            for (PeakList peaks2 : listOfPeakLists) {
                if (peaks1.size() > peaks2.size()) {
                    if (peaks1.getPeakList().containsAll(peaks2.getPeakList())) {
                        tempListOfPeakLists.remove(peaks2);
                    }
                } else if (peaks1.size() < peaks2.size()) {
                    if (peaks2.getPeakList().containsAll(peaks1.getPeakList())) {
                        tempListOfPeakLists.remove(peaks1);
                    }
                }
            }
        }

        return tempListOfPeakLists;
    }

    private static PeakList checkForCorrectRangeOfPeaks(PeakList peakList, Configuration config) {
        for (int i = 0; i < peakList.size() - 1; i++) {
            double distance = peakList.get(i + 1).getMz() - peakList.get(i).getMz();

            boolean b = false;
            for (int charge = 1; charge <= 3; charge++) {
                if (((config.getIsotopicPeakDistance() / charge - config.getDelta() < Math.abs(distance)
                        && Math.abs(distance) < config.getIsotopicPeakDistance() / charge + config.getDelta()))) {
                    b = true;
                }
            }

            if (b == false) {
                return null;
            }
        }

        return peakList;
    }

    private static Set<PeakList> splitAllPossibleIsotopicSets(Set<Peak> setOfPeaks) {
        Set<PeakList> allPeaks = new HashSet<PeakList>();

        if (setOfPeaks.isEmpty()) {
            allPeaks.add(new PeakList());
            return allPeaks;
        }

        PeakList listOfPeaks = new PeakList();
        listOfPeaks.setPeakList(new ArrayList<Peak>(setOfPeaks));

        Peak head = listOfPeaks.get(0);
        Set<Peak> rPeaks = new HashSet<Peak>(listOfPeaks.getPeakList().subList(1, listOfPeaks.size()));

        for (PeakList peaks : splitAllPossibleIsotopicSets(rPeaks)) {
            PeakList newPeaks = new PeakList();
            newPeaks.add(head);
            newPeaks.addAll(peaks);
            allPeaks.add(newPeaks);
            allPeaks.add(peaks);
        }

        return allPeaks;
    }

    private PeakList collectPeaks() {
        PeakList peaksInSet = new PeakList();
        for (IsotopicSet isotopicSet : this.isotopicSets) {
            List<IsotopicCluster> isotopicClusters = isotopicSet.getIsotopicSet();
            for (IsotopicCluster isotopicCluster : isotopicClusters) {
                for (Peak peak : isotopicCluster.getIsotopicCluster()) {
                    peaksInSet.add(new Peak(peak.getMz(), peak.getIntensity(), peak.getIsotope(), peak.getCharge(),
                            peak.getPeakID(), peak.getIsotopicClusterID(), peak.getIsotopicSetID()));
                }
            }
        }

        return peaksInSet.removeMultiplePeaks();
    }

    // Old version of generateIsotopicSets
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
}