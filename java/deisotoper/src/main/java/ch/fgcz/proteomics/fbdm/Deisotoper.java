package ch.fgcz.proteomics.fbdm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Lucas Schmidt
 * @since 2017-09-21
 */

import ch.fgcz.proteomics.utilities.MathUtils;

public class Deisotoper {
    private boolean running = false;
    private PeakList peakList;
    private Configuration config;
    private List<IsotopicSet> isotopicSets = new ArrayList<IsotopicSet>();

    public Deisotoper() {
        this(new Configuration());
    }

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
            peakListAggregated = peakListAggregated.dechargePeaks(this.config.getHMass(1));
        }

        PeakList mergedPeakList = this.peakList.mergePeakLists(peakListAggregated);
        if (this.config.getNoise() != 0) {
            mergedPeakList = mergedPeakList.filterNoisePeaks(this.config.getNoise());
        }
        mergedPeakList = mergedPeakList.sortByMZ();
        PeakList.checkForIntensityCorrectness(peakList, mergedPeakList);
        return mergedPeakList;
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

        isotopicClusters = IsotopicSet.removeOverlappingPeaksInClusters(isotopicClusters);

        for (IsotopicCluster isotopicCluster : isotopicClusters) {
            if (isotopicCluster.size() > 1) {
                Peak peak = isotopicCluster.aggregation(modus);
                resultPeakList.add(peak);
            } else if (isotopicCluster.size() == 0) {
                // do nothing
            } else {
                Peak peak = isotopicCluster.getPeak(0);
                resultPeakList.add(peak);
            }
        }

        double sumAfter = sumAllIntensities(isotopicClusters);
        intensityCheck(sumBefore, sumAfter);
        return resultPeakList;
    }

    // New version of generateIsotopicSets.
    protected static List<IsotopicSet> generateIsotopicSets(PeakList peakList, Configuration config) {
        PeakList allPossiblePeaks = isoSetCollectAllPossiblePeaks(peakList, config);
        allPossiblePeaks = allPossiblePeaks.removeMultiplePeaks();
        allPossiblePeaks = allPossiblePeaks.sortByMZ();
        List<PeakList> allPossiblePeaksParts = splitIntoParts(allPossiblePeaks, config);
        List<PeakList> listOfIsotopicSets = iterateThroughParts(allPossiblePeaksParts, config);
        return addSplittedIsotopicSetsToIsotopicSets(listOfIsotopicSets, peakList, config);
    }

    private String createAnnotatedSpectrum(PeakList peakList) {
        PeakList peaksInSet = collectPeaksFromSets(peakList);

        if (this.config.isDecharge()) {
            peaksInSet = peaksInSet.dechargePeaks(this.config.getHMass(1));
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

            sortAndCheckCorrectnessOfSplittedIsotopicSets(setOfPeakLists, listOfPeakLists, config);

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

    private static PeakList isoSetCollectAllPossiblePeaks(PeakList peakList, Configuration config) {
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
                if (peaks1.size() > peaks2.size() && peaks1.getPeakList().containsAll(peaks2.getPeakList())) {
                    tempListOfPeakLists.remove(peaks2);
                } else if (peaks1.size() < peaks2.size() && peaks2.getPeakList().containsAll(peaks1.getPeakList())) {
                    tempListOfPeakLists.remove(peaks1);
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

    private static void intensityCheck(double before, double after) {
        if (!MathUtils.fuzzyEqual(before, after, 0.001)) {
            // throw new IllegalStateException("Wrong intensities after aggregation
            // (Intensity before aggregation: "
            // + before + " and after aggregation: " + after + "!");
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
}
