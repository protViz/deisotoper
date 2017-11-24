package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-18
 */

import java.util.ArrayList;
import java.util.List;

public class IsotopicCluster {
    private List<Peak> isotopicCluster = new ArrayList<>();
    private int charge;
    private int clusterId;
    private String status;

    public Peak getPeak(int i) {
        return this.isotopicCluster.get(i);
    }

    public int size() {
        return this.isotopicCluster.size();
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
        return isotopicCluster;
    }

    public IsotopicCluster(List<Peak> isotopicCluster, int charge, Configuration config) {
        try {
            rangeCheck(isotopicCluster, config, charge);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.isotopicCluster = isotopicCluster;
        this.charge = charge;
    }

    public IsotopicCluster(String status) {
        this.isotopicCluster = null;
        this.charge = 0;
        this.status = status;
    }

    public IsotopicCluster aggregation(String modus) {
        if (modus.equals("first")) {
            return this.aggregateFirst();
        } else if (modus.equals("highest")) {
            return this.aggregateHighest();
        } else {
            throw new IllegalArgumentException("Modus not found (" + modus + ")");
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(" + this.clusterId + ") [ ");
        if (this.isotopicCluster == null) {
            return this.status;
        }
        for (Peak p : this.isotopicCluster) {
            stringBuilder.append(p.getMz() + " ");
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public boolean isNotNull() {
        if (this.isotopicCluster == null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isNull() {
        if (this.isotopicCluster == null) {
            return true;
        } else {
            return false;
        }
    }

    private IsotopicCluster aggregateFirst() {
        double intensitySum = this.sumIntensity();

        return this.rearrangeCluster(intensitySum);
    }

    private IsotopicCluster aggregateHighest() {
        double intensitySum = this.sumIntensity();
        double minIntensity = 0;
        double minMz = 0;

        for (Peak peak : this.isotopicCluster) {
            if (peak.getIntensity() > minIntensity) {
                minIntensity = peak.getIntensity();
                minMz = peak.getMz();
            }
        }

        this.isotopicCluster.get(0).setMz(minMz);

        return this.rearrangeCluster(intensitySum);
    }

    private IsotopicCluster rearrangeCluster(double intensitySum) {
        this.isotopicCluster.get(0).setIntensity(intensitySum);
        if (this.isotopicCluster.size() == 2) {
            this.isotopicCluster.remove(1);
        } else if (this.isotopicCluster.size() == 3) {
            this.isotopicCluster.remove(2);
            this.isotopicCluster.remove(1);
        }
        return this;
    }

    private double sumIntensity() {
        double intensitySum = 0;
        for (Peak peak : this.isotopicCluster) {
            intensitySum += peak.getIntensity();
        }

        return intensitySum;
    }

    private static void rangeCheck(List<Peak> peaks, Configuration config, int charge) throws Exception {
        for (int i = 0; i < peaks.size() - 1; i++) {
            double distance = peaks.get(i + 1).getMz() - peaks.get(i).getMz();
            if (!((config.getDistance() / charge - config.getDelta() < Math.abs(distance)
                    && Math.abs(distance) < config.getDistance() / charge + config.getDelta()))) {
                throw new Exception("Wrong distance at IsotopicCluster creation! (" + distance + ")");
            }
        }
    }
}
