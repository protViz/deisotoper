package ch.fgcz.proteomics.fdbm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-18
 */

import java.util.ArrayList;
import java.util.List;

public class IsotopicCluster {
    private List<Peak> isotopiccluster = new ArrayList<>();
    private int charge;
    private String status;

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
}
