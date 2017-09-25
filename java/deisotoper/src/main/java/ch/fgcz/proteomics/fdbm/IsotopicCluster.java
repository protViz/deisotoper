package ch.fgcz.proteomics.fdbm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-18
 */

import java.util.ArrayList;
import java.util.List;

public class IsotopicCluster {
    private final static double H_MASS = 1.008;
    private List<Peak> isotopiccluster = new ArrayList<>();
    private int charge;
    private String status;
    private int clusterID;

    public int getClusterID() {
        return clusterID;
    }

    public void setClusterID(int clusterID) {
        this.clusterID = clusterID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public List<Peak> getIsotopicCluster() {
        return isotopiccluster;
    }

    public void setIsotopicCluster(List<Peak> isotopicCluster) {
        this.isotopiccluster = isotopicCluster;
    }

    public IsotopicCluster(List<Peak> isotopiccluster) {
        this.isotopiccluster = isotopiccluster;
        this.charge = 0;
        this.clusterID = 0;
    }

    public IsotopicCluster(List<Peak> isotopiccluster, int charge) {
        this.isotopiccluster = isotopiccluster;
        this.charge = charge;
    }

    public IsotopicCluster(String status) {
        this.isotopiccluster = null;
        this.charge = 0;
        this.status = status;
    }

    public static IsotopicCluster aggregateFirst(IsotopicCluster cluster) {
        double intensitysum = sumIntensity(cluster);

        cluster.getIsotopicCluster().get(0).setIntensity(intensitysum);
        if (cluster.getIsotopicCluster().size() == 2) {
            cluster.getIsotopicCluster().remove(1);
        } else if (cluster.getIsotopicCluster().size() == 3) {
            cluster.getIsotopicCluster().remove(2);
            cluster.getIsotopicCluster().remove(1);
        }

        cluster.getIsotopicCluster().get(0).setMz((cluster.getIsotopicCluster().get(0).getMz() * cluster.getCharge()) - (cluster.getCharge() - 1) * H_MASS);
        return cluster;
    }

    public static IsotopicCluster aggregateLast(IsotopicCluster cluster) {
        double intensitysum = sumIntensity(cluster);

        cluster.getIsotopicCluster().remove(0);
        if (cluster.getIsotopicCluster().size() == 1) {
            cluster.getIsotopicCluster().get(0).setIntensity(intensitysum);
        } else if (cluster.getIsotopicCluster().size() == 2) {
            cluster.getIsotopicCluster().remove(0);
            cluster.getIsotopicCluster().get(0).setIntensity(intensitysum);
        }

        cluster.getIsotopicCluster().get(0).setMz((cluster.getIsotopicCluster().get(0).getMz() * cluster.getCharge()) - (cluster.getCharge() - 1) * H_MASS);
        return cluster;
    }

    public static IsotopicCluster aggregateMean(IsotopicCluster cluster) {
        double intensitysum = sumIntensity(cluster);

        double mzmean = 0;
        for (Peak p : cluster.getIsotopicCluster()) {
            mzmean += p.getMz();
        }

        mzmean = mzmean / cluster.getIsotopicCluster().size();

        cluster.getIsotopicCluster().get(0).setIntensity(intensitysum);
        cluster.getIsotopicCluster().get(0).setMz(mzmean);
        if (cluster.getIsotopicCluster().size() == 2) {
            cluster.getIsotopicCluster().remove(1);
        } else if (cluster.getIsotopicCluster().size() == 3) {
            cluster.getIsotopicCluster().remove(2);
            cluster.getIsotopicCluster().remove(1);
        }

        cluster.getIsotopicCluster().get(0).setMz((cluster.getIsotopicCluster().get(0).getMz() * cluster.getCharge()) - (cluster.getCharge() - 1) * H_MASS);
        return cluster;
    }

    private static double sumIntensity(IsotopicCluster cluster) {
        double intensitysum = 0;
        for (Peak p : cluster.getIsotopicCluster()) {
            intensitysum += p.getIntensity();
        }

        return intensitysum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(" + this.clusterID + ") [ - ");
        if (this.isotopiccluster == null) {
            return this.status;
        }
        for (Peak p : this.isotopiccluster) {
            sb.append(p.getMz() + " - ");
        }
        sb.append("]");
        return sb.toString();
    }
}
