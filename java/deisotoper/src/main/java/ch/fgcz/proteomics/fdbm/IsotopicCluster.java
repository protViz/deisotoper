package ch.fgcz.proteomics.fdbm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-18
 */

import java.util.ArrayList;
import java.util.List;

public class IsotopicCluster {
    private List<Peak> isotopiccluster = new ArrayList<>();
    private double distance;

    public List<Peak> getIsotopicCluster() {
        return isotopiccluster;
    }

    public void setIsotopicCluster(List<Peak> isotopicCluster) {
        this.isotopiccluster = isotopicCluster;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public IsotopicCluster(List<Peak> isotopiccluster) {
        this.isotopiccluster = isotopiccluster;
        this.distance = 0;
    }

    public IsotopicCluster(List<Peak> isotopiccluster, double distance) {
        this.isotopiccluster = isotopiccluster;
        this.distance = distance;
    }
}
