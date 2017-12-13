package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-18
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.fgcz.proteomics.utilities.MathUtils;

// TODO: Fix id's of clusters...
public class IsotopicSet {
    private final PeakList peakList;
    private List<IsotopicCluster> iSet = null;
    private List<IsotopicCluster> bestPath = null;
    private List<Peak> peaksInSet = null;
    private String dot = null;
    private int setId;

    public IsotopicSet(PeakList peakList, List<Peak> peaksInSet, int setId, Configuration config) {
        this.peakList = peakList;
        rangeCheck(peaksInSet, config);

        this.peaksInSet = peaksInSet;

        List<IsotopicCluster> tempIsotopicSet = collectClusters(config, setId);

        this.iSet = tempIsotopicSet;
        setBestPath(peakList, tempIsotopicSet, config);
    }

    // TODO Looks to me like an isotopicSet method.
    public static PeakList checkForCorrectRangeOfPeaks(PeakList peakList, Configuration config) {
        if (peakList.isSortedByMass()) {
            // first check if sorted.
            // do we want to limit us to charge 3?
            // max charge in config.
            for (int i = 0; i < peakList.size() - 1; i++) {
                double distance = peakList.get(i + 1).getMz() - peakList.get(i).getMz();

                boolean b = false;
                for (int charge = 1; charge <= 3; charge++) {
                    if (MathUtils.fuzzyEqual(config.getIsotopicPeakDistance() / charge, Math.abs(distance),
                            config.getDelta())) {
                        b = true;
                    }
                }

                if (!b) {
                    return null;
                }
            }

            return peakList;
        } else {
            throw new IllegalArgumentException("Not Sorted");
        }
    }

    public List<Peak> getPeaksInSet() {
        return peaksInSet;
    }

    public int getSetId() {
        return setId;
    }

    public void setSetId(int setId) {
        this.setId = setId;
    }

    public List<IsotopicCluster> getIsotopicSet() {
        return iSet;
    }

    public String getDot() {
        return dot;
    }

    public List<IsotopicCluster> getBestPath() {
        List<IsotopicCluster> bestClusters = new ArrayList<IsotopicCluster>();
        for (IsotopicCluster isotopicCluster : bestPath) {
            if (isotopicCluster.isNotNull()) {
                bestClusters.add(isotopicCluster);
            }
        }

        return bestClusters;
    }

    private List<IsotopicCluster> collectClusters(Configuration config, int setId) {
        List<IsotopicCluster> isotopicClusters = new ArrayList<IsotopicCluster>();

        for (int charge = 3; 0 < charge; charge--) {
            collectClusterForEachCharge(isotopicClusters, this.peaksInSet, charge, config);
        }

        isotopicClusters = removeMultipleIsotopicCluster(isotopicClusters);

        sortIsotopicSet(isotopicClusters);

        this.setId = setId;

        setPositions(isotopicClusters);

        return isotopicClusters;
    }

    private List<IsotopicCluster> setPositions(List<IsotopicCluster> isotopicClusters) {
        int clusterId = 0;
        for (IsotopicCluster isotopicCluster : isotopicClusters) {
            isotopicCluster.setClusterID(clusterId);
            clusterId++;
        }

        for (IsotopicCluster isotopicCluster : isotopicClusters) {
            if (isotopicCluster.isNotNull()) {
                int position = 1;
                for (Peak peak : isotopicCluster.getIsotopicCluster()) {
                    peak.setIsotopicSetID(setId);
                    peak.setIsotopicClusterID(isotopicCluster.getClusterID());
                    peak.setIsotope(position);
                    position++;
                }
            }
        }

        return isotopicClusters;
    }

    private void setBestPath(PeakList peaklist, List<IsotopicCluster> isotopicClusters, Configuration config) {
        // FIRST GRAPH AND BEST PATH
        List<IsotopicCluster> isotopicClustersForBestPath = new ArrayList<IsotopicCluster>(isotopicClusters);

        IsotopicClusterGraph isotopicClusterGraphForBestPath = new IsotopicClusterGraph(
                removeDoubleClusterLeaveTripleCluster(isotopicClustersForBestPath));

        isotopicClusterGraphForBestPath.scoreIsotopicClusterGraph(peaklist.getPeptideMass(), peaklist.getChargeState(),
                peaklist, config);

        this.bestPath = isotopicClusterGraphForBestPath
                .bestPath(isotopicClusterGraphForBestPath.getStart(), isotopicClusterGraphForBestPath.getEnd())
                .getVertexList();

        // THEN GRAPH AND DOT
        List<IsotopicCluster> isotopicClustersForDot = new ArrayList<IsotopicCluster>(isotopicClusters);

        IsotopicClusterGraph isotopicClusterGraphForDot = new IsotopicClusterGraph(isotopicClustersForDot);

        isotopicClusterGraphForDot.scoreIsotopicClusterGraph(peaklist.getPeptideMass(), peaklist.getChargeState(),
                peaklist, config);

        this.dot = isotopicClusterGraphForDot.toDOTGraph();
    }

    // TODO : write a few tests.
    protected static List<IsotopicCluster> removeDoubleClusterLeaveTripleCluster(
            List<IsotopicCluster> isotopicClusters) {
        List<IsotopicCluster> isotopicClusters2 = new ArrayList<IsotopicCluster>();

        for (IsotopicCluster cluster1 : isotopicClusters) {
            for (IsotopicCluster cluster2 : isotopicClusters) {
                innerIfStatementsOfRemoveDoubleCluster(isotopicClusters2, cluster1, cluster2);
            }
        }

        isotopicClusters.removeAll(removeMultipleIsotopicCluster(isotopicClusters2));

        return isotopicClusters;
    }

    private static void innerIfStatementsOfRemoveDoubleCluster(List<IsotopicCluster> isotopicClusters,
            IsotopicCluster cluster1, IsotopicCluster cluster2) {
        if (cluster1.size() == 3 && cluster2.size() == 2 && (cluster1.getPeak(1).equalsPeak(cluster2.getPeak(0))
                && cluster1.getPeak(2).equalsPeak(cluster2.getPeak(1)))) {
            isotopicClusters.add(cluster2);
        } else if (cluster1.size() == 2 && cluster2.size() == 3 && (cluster1.getPeak(0).equalsPeak(cluster2.getPeak(0))
                && cluster1.getPeak(1).equalsPeak(cluster2.getPeak(1)))) {
            isotopicClusters.add(cluster1);
        }
    }

    private List<IsotopicCluster> collectClusterForEachCharge(List<IsotopicCluster> isotopicClusters,
            List<Peak> isotopicSet, int charge, Configuration config) {
        for (Peak a : isotopicSet) {
            for (Peak b : isotopicSet) {
                double distanceab = b.getMz() - a.getMz();
                for (Peak c : isotopicSet) {

                    innerIfStatementsOfCollectCluster(isotopicClusters, a, b, c, charge, config, distanceab);
                }
            }
        }

        return isotopicClusters;
    }

    private void innerIfStatementsOfCollectCluster(List<IsotopicCluster> isotopicClusters, Peak a, Peak b, Peak c,
            int charge, Configuration config, double distanceab) {
        List<Peak> ic = new ArrayList<Peak>();
        double distanceac = c.getMz() - a.getMz();
        double distancebc = c.getMz() - b.getMz();

        if ((config.getIsotopicPeakDistance() / charge) - config.getDelta() < distanceab
                && distanceab < (config.getIsotopicPeakDistance() / charge) + config.getDelta()) {
            a.setCharge(charge);
            b.setCharge(charge);
            ic.add(a);
            ic.add(b);
        }

        if ((config.getIsotopicPeakDistance() / charge) - config.getDelta() < distancebc
                && distancebc < (config.getIsotopicPeakDistance() / charge) + config.getDelta()
                && ((config.getIsotopicPeakDistance() / charge) - config.getDelta()) * 2 < distanceac
                && distanceac < ((config.getIsotopicPeakDistance() / charge) + config.getDelta()) * 2) {
            c.setCharge(charge);
            ic.add(c);
        }

        if (ic.size() == 2 || ic.size() == 3) {
            IsotopicCluster cluster = new IsotopicCluster(ic, charge, this.peakList, config.getIsotopicPeakDistance(),
                    config.getDelta());
            isotopicClusters.add(cluster);
        }
    }

    private static List<IsotopicCluster> removeMultipleIsotopicCluster(List<IsotopicCluster> isotopicClusters) {
        List<IsotopicCluster> result = new ArrayList<IsotopicCluster>();
        Set<List<Peak>> set = new HashSet<List<Peak>>();

        for (IsotopicCluster cluster : isotopicClusters) {
            if (set.add(cluster.getIsotopicCluster())) {
                result.add(cluster);
            }
        }

        return result;
    }

    private static void rangeCheck(List<Peak> peaks, Configuration config) {
        for (int i = 0; i < peaks.size() - 1; i++) {
            double distance = peaks.get(i + 1).getMz() - peaks.get(i).getMz();

            boolean b = false;
            for (int charge = 1; charge <= 3; charge++) {
                if ((config.getIsotopicPeakDistance() / charge - config.getDelta() < Math.abs(distance)
                        && Math.abs(distance) < config.getIsotopicPeakDistance() / charge + config.getDelta())) {
                    b = true;
                }
            }

            if (!b) {
                throw new IllegalArgumentException("Wrong distance at IsotopicSet creation! (" + distance + ")");
            }
        }
    }

    private static List<IsotopicCluster> sortIsotopicSet(List<IsotopicCluster> isotopicClusters) {
        Collections.sort(isotopicClusters, new Comparator<IsotopicCluster>() {
            @Override
            public int compare(IsotopicCluster cluster1, IsotopicCluster cluster2) {
                int result = Double.compare(cluster1.getPeak(0).getMz(), cluster2.getPeak(0).getMz());

                if (result == 0) {
                    result = Double.compare(cluster1.getPeak(1).getMz(), cluster2.getPeak(1).getMz());
                    if (result == 0 && (cluster1.size() == 3 && cluster2.size() == 3)) {
                        result = Double.compare(cluster1.getPeak(2).getMz(), cluster2.getPeak(2).getMz());
                        return result;
                    }
                }

                return result;
            }
        });

        return isotopicClusters;
    }
}