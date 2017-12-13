package ch.fgcz.proteomics.fbdm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucas Schmidt
 * @since 2017-09-18
 */

import ch.fgcz.proteomics.utilities.MathUtils;

public class IsotopicCluster {
    private List<Peak> iCluster = new ArrayList<Peak>();
    private int charge;
    private int clusterId;
    private String status;
    private PeakList peakList;

    public IsotopicCluster(List<Peak> isotopicCluster, int charge, PeakList peakList, double isotopicPeakDistance,
            double delta) {
        rangeCheck(isotopicCluster, charge, isotopicPeakDistance, delta);
        this.iCluster = isotopicCluster;
        this.charge = charge;
        this.peakList = peakList;
    }

    public IsotopicCluster(String status) {
        this.iCluster = null;
        this.charge = 0;
        this.status = status;
    }

    // TODO not sure if should not moved to isotopicSet.
    static List<IsotopicCluster> removeOverlappingPeaksInClusters(List<IsotopicCluster> isotopicClusters) {
        // If cluster has same peak/peaks as other cluster.
        // Remove this peak in the lowest charged cluster.
        // Aggregate only the non removed cluster and add the remaining peaks from the
        // overlapping cluster to the resultPeakList.

        for (IsotopicCluster isotopicCluster1 : isotopicClusters) {
            for (IsotopicCluster isotopicCluster2 : isotopicClusters) {
                if (isotopicCluster1.equals(isotopicCluster2)) {
                    continue;
                }

                if (isotopicCluster1.hasSamePeaks(isotopicCluster2)) {
                    isotopicCluster1.manipulateWhenHasSamePeaks(isotopicCluster2);
                }
            }
        }
        return isotopicClusters;
    }

    public Peak aggregation(String modus) {
        if (modus.equals("first")) {
            return this.aggregateFirst();
        } else if (modus.equals("highest")) {
            throw new IllegalArgumentException("Modus: " + modus + " is deprecated");
        } else {
            throw new IllegalArgumentException("Modus not found (" + modus + ")");
        }
    }

    public Peak getPeak(int i) {
        return this.iCluster.get(i);
    }

    public int size() {
        return this.iCluster.size();
    }

    public int getClusterID() {
        return clusterId;
    }

    public void setClusterID(int clusterId) {
        this.clusterId = clusterId;
    }

    public String getStatus() {
        return status;
    }

    public int getCharge() {
        return charge;
    }

    public List<Peak> getIsotopicCluster() {
        return iCluster;
    }

    public boolean isNotNull() {
        return this.iCluster != null;
    }

    public boolean isNull() {
        return this.iCluster == null;
    }

    public double sumIntensity() {
        double intensitySum = 0;
        for (Peak peak : this.iCluster) {
            intensitySum += peak.getIntensity();
        }

        return intensitySum;
    }

    public boolean hasSamePeaks(IsotopicCluster isotopicClusterToCompare) {
        for (Peak peak1 : this.getIsotopicCluster()) {
            for (Peak peak2 : isotopicClusterToCompare.getIsotopicCluster()) {
                if (peak1.equalsPeak(peak2)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void manipulateWhenHasSamePeaks(IsotopicCluster isotopicCluster) {
        if (this.getCharge() > isotopicCluster.getCharge()) {
            this.getIsotopicCluster().removeAll(isotopicCluster.getIsotopicCluster());
        } else if (this.getCharge() < isotopicCluster.getCharge()) {
            isotopicCluster.getIsotopicCluster().removeAll(this.getIsotopicCluster());
        } else {
            double intensitySumOfCluster1 = this.sumIntensity();
            double intensitySumOfCluster2 = isotopicCluster.sumIntensity();
            if (intensitySumOfCluster1 > intensitySumOfCluster2) {
                isotopicCluster.getIsotopicCluster().removeAll(this.getIsotopicCluster());
            } else {
                this.getIsotopicCluster().removeAll(isotopicCluster.getIsotopicCluster());
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(" + this.clusterId + ") [ ");
        if (this.iCluster == null) {
            return this.status;
        }
        for (Peak p : this.iCluster) {
            stringBuilder.append(p.getMz() + " ");
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    private Peak aggregateFirst() {
        double intensitySum = this.sumIntensity();
        return this.rearrangeCluster(intensitySum);
    }

    private Peak rearrangeCluster(double intensitySum) {
        return new Peak(this.iCluster.get(0).getMz(), intensitySum, this.charge);

    }

    private static void rangeCheck(List<Peak> peaks, int charge, double isotopicDistance, double delta) {
        for (int i = 0; i < peaks.size() - 1; i++) {
            double distance = peaks.get(i + 1).getMz() - peaks.get(i).getMz();
            double theorDistance = isotopicDistance / charge;
            if (!MathUtils.fuzzyEqual(distance, theorDistance, delta)) {
                throw new IllegalArgumentException("Wrong distance at IsotopicCluster creation! (" + distance
                        + "while only " + theorDistance + " allowed.)");
            }
        }
    }

    /*
     * public void scoreCluster(Configuration config) { Score score = new
     * Score(this.peakList.getPeptideMass(), this.peakList.getChargeState(),
     * config); for (Peak peakX : this.iCluster) {
     * 
     * for (Peak peakY : peakList.getPeakList()) { double scoreResult =
     * score.calculateAggregatedScore(peakX.getMz(), peakY.getMz(),
     * this.getIsotopicCluster());
     * 
     * // double scoreFiveResult = scoreFive.calculateFifthScore(connection);
     * 
     * // scoreSum += scoreResult + scoreFiveResult; } } }
     */
}
