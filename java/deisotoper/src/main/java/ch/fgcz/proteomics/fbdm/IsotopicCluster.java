package ch.fgcz.proteomics.fbdm;

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
    private int clusterID;

    public int getClusterID() {
	return clusterID;
    }

    public void setClusterID(int clusterid) {
	this.clusterID = clusterid;
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

    public IsotopicCluster(List<Peak> isotopiccluster, int charge) {
	this.isotopiccluster = isotopiccluster;
	this.charge = charge;
    }

    public IsotopicCluster(String status) {
	this.isotopiccluster = null;
	this.charge = 0;
	this.status = status;
    }

    public IsotopicCluster aggregateFirst() {
	double intensitysum = this.sumIntensity();

	this.isotopiccluster.get(0).setIntensity(intensitysum);
	if (this.isotopiccluster.size() == 2) {
	    this.isotopiccluster.remove(1);
	} else if (this.isotopiccluster.size() == 3) {
	    this.isotopiccluster.remove(2);
	    this.isotopiccluster.remove(1);
	}

	return this;
    }

    public IsotopicCluster aggregateHighest() {
	double intensitysum = this.sumIntensity();
	double minint = 0;
	double minmz = 0;

	for (Peak p : this.isotopiccluster) {
	    if (p.getIntensity() > minint) {
		minint = p.getIntensity();
		minmz = p.getMz();
	    }
	}

	this.isotopiccluster.get(0).setIntensity(intensitysum);
	this.isotopiccluster.get(0).setMz(minmz);
	if (this.isotopiccluster.size() == 2) {
	    this.isotopiccluster.remove(1);
	} else if (this.isotopiccluster.size() == 3) {
	    this.isotopiccluster.remove(2);
	    this.isotopiccluster.remove(1);
	}

	return this;
    }

    private double sumIntensity() {
	double intensitysum = 0;
	for (Peak p : this.isotopiccluster) {
	    intensitysum += p.getIntensity();
	}

	return intensitysum;
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append("(" + this.clusterID + ") [ ");
	if (this.isotopiccluster == null) {
	    return this.status;
	}
	for (Peak p : this.isotopiccluster) {
	    sb.append(p.getMz() + " ");
	}
	sb.append("]");
	return sb.toString();
    }
}
