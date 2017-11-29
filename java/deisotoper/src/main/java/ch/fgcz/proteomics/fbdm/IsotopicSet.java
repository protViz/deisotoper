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

import ch.fgcz.proteomics.dto.MassSpectrum;

public class IsotopicSet {
    private String dot;
    private List<IsotopicCluster> isotopicSet = new ArrayList<>();
    private List<IsotopicCluster> bestPath;
    private List<Peak> peaksInSet;
    private int setId;

    public List<Peak> getPeaksInSet() {
        return peaksInSet;
    }

    public int getSetID() {
        return setId;
    }

    public List<IsotopicCluster> getIsotopicSet() {
        return isotopicSet;
    }

    public String getDot() {
        return dot;
    }

    public List<IsotopicCluster> getBestPath() {
        List<IsotopicCluster> bestClusters = new ArrayList<>();
        for (IsotopicCluster isotopicCluster : bestPath) {
            if (isotopicCluster.isNotNull()) {
                bestClusters.add(isotopicCluster);
            }
        }

        return bestClusters;
    }

    public IsotopicSet(MassSpectrum massSpectrum, List<Peak> isotopicSet, int setId, Configuration config) {
        this.peaksInSet = isotopicSet;
        try {
            rangeCheck(isotopicSet, config);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<IsotopicCluster> isotopicClusters = new ArrayList<>();

        isotopicClusters = collectClusterForEachCharge(isotopicClusters, isotopicSet, 3, config);

        isotopicClusters = collectClusterForEachCharge(isotopicClusters, isotopicSet, 2, config);

        isotopicClusters = collectClusterForEachCharge(isotopicClusters, isotopicSet, 1, config);

        isotopicClusters = removeMultipleIsotopicCluster(isotopicClusters);

        isotopicClusters = sortIsotopicSet(isotopicClusters);

        int clusterId = 0;
        for (IsotopicCluster isotopicCluster : isotopicClusters) {
            isotopicCluster.setClusterID(clusterId);
            clusterId++;
        }

        this.setId = setId;

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

        this.isotopicSet = isotopicClusters;

        setBestPath(massSpectrum, isotopicClusters, config);
    }

    private void setBestPath(MassSpectrum massSpectrum, List<IsotopicCluster> isotopicClusters, Configuration config) {
        List<IsotopicCluster> isotopicClusters2 = removeDoubleClusterLeaveTripleCluster(isotopicClusters);

        IsotopicClusterGraph isotopicClusterGraph = new IsotopicClusterGraph(isotopicClusters2);

        isotopicClusterGraph.scoreIsotopicClusterGraph(massSpectrum.getPeptideMass(), massSpectrum.getChargeState(),
                new PeakList(massSpectrum.getMz(), massSpectrum.getIntensity()), config);

        this.dot = isotopicClusterGraph.toDOTGraph();
        this.bestPath = isotopicClusterGraph.bestPath(isotopicClusterGraph.getStart(), isotopicClusterGraph.getEnd())
                .getVertexList();
    }

    private List<IsotopicCluster> removeDoubleClusterLeaveTripleCluster(List<IsotopicCluster> isotopicClusters) {
        List<IsotopicCluster> isotopicClusters2 = new ArrayList<>();

        for (IsotopicCluster cluster1 : isotopicClusters) {
            for (IsotopicCluster cluster2 : isotopicClusters) {
                if (cluster1.size() == 3 && cluster2.size() == 2) {
                    if (cluster1.getPeak(1).equals(cluster2.getPeak(0))
                            && cluster1.getPeak(2).equals(cluster2.getPeak(1))) {
                        isotopicClusters2.add(cluster2);
                    }
                }
                if (cluster1.size() == 2 && cluster2.size() == 3) {
                    if (cluster1.getPeak(0).equals(cluster2.getPeak(0))
                            && cluster1.getPeak(1).equals(cluster2.getPeak(1))) {
                        isotopicClusters2.add(cluster1);
                    }
                }
            }
        }

        isotopicClusters.removeAll(removeMultipleIsotopicCluster(isotopicClusters2));

        return isotopicClusters;
    }

    private List<IsotopicCluster> collectClusterForEachCharge(List<IsotopicCluster> isotopicClusters,
            List<Peak> isotopicSet, int charge, Configuration config) {
        for (Peak a : isotopicSet) {
            for (Peak b : isotopicSet) {
                double distanceab = b.getMz() - a.getMz();
                for (Peak c : isotopicSet) {
                    List<Peak> ic = new ArrayList<>();
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
                        IsotopicCluster cluster = new IsotopicCluster(ic, charge, config);
                        isotopicClusters.add(cluster);
                    }
                }
            }
        }

        return isotopicClusters;
    }

    private static List<IsotopicCluster> sortIsotopicSet(List<IsotopicCluster> isotopicClusters) {
        Collections.sort(isotopicClusters, new Comparator<IsotopicCluster>() {
            @Override
            public int compare(IsotopicCluster cluster1, IsotopicCluster cluster2) {
                int result = Double.compare(cluster1.getPeak(0).getMz(), cluster2.getPeak(0).getMz());

                if (result == 0) {
                    result = Double.compare(cluster1.getPeak(1).getMz(), cluster2.getPeak(1).getMz());
                    if (result == 0) {
                        if (cluster1.size() == 3 && cluster2.size() == 3) {
                            result = Double.compare(cluster1.getPeak(2).getMz(), cluster2.getPeak(2).getMz());
                            return result;
                        }
                    }
                }

                return result;
            }
        });

        return isotopicClusters;
    }

    private static List<IsotopicCluster> removeMultipleIsotopicCluster(List<IsotopicCluster> isotopicClusters) {
        List<IsotopicCluster> result = new ArrayList<>();
        Set<List<Peak>> set = new HashSet<>();

        for (IsotopicCluster cluster : isotopicClusters) {
            if (set.add(cluster.getIsotopicCluster())) {
                result.add(cluster);
            }
        }

        return result;
    }

    private static void rangeCheck(List<Peak> peaks, Configuration config) throws Exception {
        for (int i = 0; i < peaks.size() - 1; i++) {
            double distance = peaks.get(i + 1).getMz() - peaks.get(i).getMz();

            boolean b = false;
            for (int charge = 1; charge <= 3; charge++) {
                if (((config.getIsotopicPeakDistance() / charge - config.getDelta() < Math.abs(distance)
                        && Math.abs(distance) < config.getIsotopicPeakDistance() / charge + config.getDelta()))) {
                    b = true;
                }
            }

            if (b == false) {
                throw new Exception("Wrong distance at IsotopicSet creation! (" + distance + ")");
            }
        }
    }
}