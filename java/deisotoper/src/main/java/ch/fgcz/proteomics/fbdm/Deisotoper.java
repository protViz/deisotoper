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

public class Deisotoper {
    private boolean running = false;
    private PeakList peakList;
    private PeakList mergedPeakList;
    private Configuration config;
    private List<IsotopicSet> isotopicSets = new ArrayList<IsotopicSet>();
    // private MassSpectrum massSpectrum;

    public Deisotoper() {
        this(new Configuration());
    }

    // TODO constructor with configuration.
    public Deisotoper(Configuration config) {
        this.config = config;
    }

    public PeakList deisotopeMS(PeakList peakList) {
        this.peakList = peakList;
        this.running = true;
        this.isotopicSets = new ArrayList<IsotopicSet>();

        isotopicSets = generateIsotopicSets(this.peakList, config);
        List<IsotopicCluster> bestClusters = getBestClusters();

        PeakList peakListAggregated = aggregate(bestClusters, this.config.getModus());

        if (this.config.isDecharge()) {
            peakListAggregated = peakListAggregated.dechargePeaks(this.config.getH_MASS(1));
        }

        this.mergedPeakList = this.peakList.mergePeakLists(peakListAggregated);
        if (this.config.getNoise() != 0) {
            this.mergedPeakList = this.mergedPeakList.filterNoisePeaks(this.config.getNoise());
        }
        this.mergedPeakList = mergedPeakList.sortByMZ();
        PeakList.checkForIntensityCorrectness(peakList, this.mergedPeakList);
        return this.mergedPeakList;
    }

    public boolean wasRunning() {
        return this.running;

    }

    public Configuration getConfiguration() {
        return config;
    }

    // OK.
    public void setConfiguration(Configuration config) {
        this.config = config;
    }

    public String getAnnotatedSpectrum() {
        // generate it on request.
        return createAnnotatedSpectrum(this.peakList);
    }

    public List<String> getDotGraphs() {
        if (!this.wasRunning()) {
            throw new IllegalStateException("Deisotope spectrum first");
        }

        List<String> graph = new ArrayList<String>();

        for (IsotopicSet isotopicSet : this.isotopicSets) {
            graph.add(isotopicSet.getDot());
        }

        return graph;
    }

    public List<IsotopicSet> getIsotopicSets() {
        return isotopicSets;
    }

    protected List<IsotopicCluster> getBestClusters() {
        List<IsotopicCluster> bestClusters = new ArrayList<IsotopicCluster>();

        for (IsotopicSet isotopicSet : this.isotopicSets) {
            bestClusters.addAll(isotopicSet.getBestPath());
        }

        return bestClusters;
    }

    protected PeakList aggregate(List<IsotopicCluster> isotopicClusters, String modus) {
        PeakList resultPeakList = new PeakList();

        double sumBefore = sumAllIntensities(isotopicClusters);

        isotopicClusters = IsotopicCluster.removeOverlappingPeaksInClusters(isotopicClusters);

        for (IsotopicCluster isotopicCluster : isotopicClusters) {
            if (isotopicCluster.size() > 1) {
                Peak peak = isotopicCluster.aggregation(modus);
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

    // New version of generateIsotopicSets.
    static protected List<IsotopicSet> generateIsotopicSets(PeakList peakList, Configuration config) {
        PeakList allPossiblePeaks = isoSet_collectAllPossiblePeaks(peakList, config);
        allPossiblePeaks = allPossiblePeaks.removeMultiplePeaks();
        allPossiblePeaks = allPossiblePeaks.sortByMZ();
        List<PeakList> allPossiblePeaksParts = splitIntoParts(allPossiblePeaks, config);
        List<PeakList> listOfIsotopicSets = iterateThroughParts(allPossiblePeaksParts, config);
        return addSplittedIsotopicSetsToIsotopicSets(listOfIsotopicSets, peakList, config);
    }

    private String createAnnotatedSpectrum(PeakList peakList) {
        PeakList peaksInSet = collectPeaksFromSets(peakList);

        if (this.config.isDecharge()) {
            peaksInSet = peaksInSet.dechargePeaks(this.config.getH_MASS(1));
        }

        PeakList mergedPeakListLocal = this.peakList.mergePeakLists(peaksInSet);

        if (this.config.getNoise() != 0) {
            mergedPeakListLocal = mergedPeakListLocal.filterNoisePeaks(this.config.getNoise());
        }

        mergedPeakListLocal = mergedPeakListLocal.sortByMZ();
        mergedPeakListLocal.sortByPeakID();
        mergedPeakListLocal.removeMultiplePeaks();

        return mergedPeakListLocal.saveAnnotatedSpectrum();
    }

    private PeakList collectPeaksFromSets(PeakList peakList) {
        this.isotopicSets = new ArrayList<IsotopicSet>();
        generateIsotopicSets(peakList, config);
        PeakList peaksInSet = new PeakList();

        // IDs are recreated
        int setID = 0;
        for (IsotopicSet isotopicSet : this.isotopicSets) {
            int clusterID = 0;
            List<IsotopicCluster> isotopicClusters = isotopicSet.getIsotopicSet();
            for (IsotopicCluster isotopicCluster : isotopicClusters) {
                for (Peak peak : isotopicCluster.getIsotopicCluster()) {
                    peaksInSet.add(new Peak(peak.getMz(), peak.getIntensity(), peak.getIsotope(), peak.getCharge(),
                            peak.getPeakID(), clusterID, setID));
                }
                clusterID++;
            }
            setID++;
        }

        return peaksInSet.removeMultiplePeaks();
    }

    private static List<PeakList> iterateThroughParts(List<PeakList> parts, Configuration config) {
        List<PeakList> combinedPeakLists = new ArrayList<PeakList>();

        for (PeakList part : parts) {
            Set<PeakList> setOfPeakLists = splitAllPossibleIsotopicSets(new HashSet<Peak>(part.getPeakList()));
            List<PeakList> listOfPeakLists = new ArrayList<PeakList>();

            listOfPeakLists = sortAndCheckCorrectnessOfSplittedIsotopicSets(setOfPeakLists, listOfPeakLists, config);

            listOfPeakLists = checkForContainingAndRemoveWrongSets(listOfPeakLists);

            combinedPeakLists.addAll(listOfPeakLists);
        }

        return combinedPeakLists;
    }

    private static List<IsotopicSet> addSplittedIsotopicSetsToIsotopicSets(List<PeakList> listOfIsotopicSets,
            PeakList peakList, Configuration config) {
        List<IsotopicSet> isotopicSets = new ArrayList<IsotopicSet>();
        int id = 0;

        for (PeakList isotopicSet : listOfIsotopicSets) {
            IsotopicSet temporaryIsotopicSet = new IsotopicSet(peakList, isotopicSet.getPeakList(), id, config);
            id++;
            isotopicSets.add(temporaryIsotopicSet);
        }

        return isotopicSets;
    }

    private static List<PeakList> sortAndCheckCorrectnessOfSplittedIsotopicSets(Set<PeakList> allPossiblePeaks,
            List<PeakList> correctIsotopicSets, Configuration config) {
        for (PeakList possiblePeaks : allPossiblePeaks) {
            if (1 < possiblePeaks.size()) {
                possiblePeaks = possiblePeaks.sortByMZ();

                possiblePeaks = IsotopicSet.checkForCorrectRangeOfPeaks(possiblePeaks, config);

                if (possiblePeaks != null) {
                    correctIsotopicSets.add(possiblePeaks);
                }
            }
        }
        return correctIsotopicSets;
    }

    private static PeakList isoSet_collectAllPossiblePeaks(PeakList peakList, Configuration config) {
        PeakList allPossiblePeaks = new PeakList();
        for (int i = 0; i < peakList.size(); i++) {
            Peak peakI = peakList.get(i);
            for (int j = 0; j < peakList.size(); j++) {
                Peak peakJ = peakList.get(j);
                allPossiblePeaks = allPossiblePeaks.collectForEachCharge(peakI, peakJ, config);
            }
        }

        return allPossiblePeaks;
    }

    private static List<PeakList> splitIntoParts(PeakList peaks, Configuration config) {
        List<PeakList> partsOfPeaks = new ArrayList<PeakList>();

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
        List<PeakList> tempListOfPeakLists = new ArrayList<PeakList>(listOfPeakLists);

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
        List<PeakList> tempListOfPeakLists = new ArrayList<PeakList>(listOfPeakLists);

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

    private static void intensityCheck(double before, double after) throws Exception {
        if ((double) Math.round(before * 1000d) / 1000d != (double) Math.round(after * 1000d) / 1000d) {
            throw new Exception("Wrong intensities after aggregation (Intensity before aggregation: " + before
                    + " and after aggregation: " + after + "!");
        }
    }

    private static double sumAllIntensities(List<IsotopicCluster> isotopicClusters) {
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

    // Old version of generateIsotopicSets
    // protected void generateIsotopicSets(MassSpectrum massSpectrum) {
    // this.peakList = new PeakList(massSpectrum);
    //
    // int id = 0;
    // for (int i = 0; i < peakList.size(); i++) {
    // List<Peak> isotopicSet = new ArrayList<Peak>();
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