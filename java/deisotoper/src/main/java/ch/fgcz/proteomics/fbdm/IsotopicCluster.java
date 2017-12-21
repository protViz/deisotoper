package ch.fgcz.proteomics.fbdm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucas Schmidt
 * @since 2017-09-18
 */

import ch.fgcz.proteomics.utilities.MathUtils;

public class IsotopicCluster {
    private List<Peak> clusterPeaks = new ArrayList<>();
    private int charge;
    private int clusterId;
    private String status;
    private PeakList peakList;
    private double score;

    public IsotopicCluster(List<Peak> isotopicCluster, int charge, PeakList peakList, double isotopicPeakDistance,
            double delta) {
        rangeCheck(isotopicCluster, charge, isotopicPeakDistance, delta);
        this.clusterPeaks = isotopicCluster;
        this.charge = charge;
        this.peakList = peakList;
    }

    public IsotopicCluster(String status) {
        this.clusterPeaks = null;
        this.charge = 0;
        this.status = status;
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

    public double getScore() {
        return score;
    }

    public Peak getPeak(int i) {
        return this.clusterPeaks.get(i);
    }

    public int size() {
        return this.clusterPeaks.size();
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
        return clusterPeaks;
    }

    public boolean isNotNull() {
        return this.clusterPeaks != null;
    }

    public boolean isNull() {
        return this.clusterPeaks == null;
    }

    public double sumIntensity() {
        double intensitySum = 0;
        for (Peak peak : this.clusterPeaks) {
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
        if (this.clusterPeaks == null) {
            return this.status;
        }
        for (Peak p : this.clusterPeaks) {
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
        return new Peak(this.clusterPeaks.get(0).getMz(), intensitySum, this.charge);

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

    public void scoreCluster(Configuration config) {
        Score sObj = new Score(this.peakList.getPeptideMass(), this.peakList.getChargeState(), config);
        double scoreSum = 0;
        if (this.isNotNull()) {
            for (Peak peakX : this.clusterPeaks) {
                for (Peak peakY : peakList.getPeakList()) {
                    if (!peakX.equalsPeak(peakY)) {
                        double scoreResult = sObj.calculateAggregatedScore(peakX.getMz(), peakY.getMz(),
                                this.getIsotopicCluster());
                        scoreSum += scoreResult;
                    }
                }
            }
        }

        // Clusters need to have weight so that there is a beth path in the Graph.
        if (scoreSum == 0) {
            scoreSum = this.clusterPeaks.size() * peakList.getPeakList().size() * 0.000001;
        }

        this.score = scoreSum;
    }
}
