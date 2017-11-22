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

    public IsotopicCluster(List<Peak> isotopiccluster, int charge, Configuration config) {
	try {
	    rangeCheck(isotopiccluster, config, charge);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	this.isotopiccluster = isotopiccluster;
	this.charge = charge;
    }

    public IsotopicCluster(String status) {
	this.isotopiccluster = null;
	this.charge = 0;
	this.status = status;
    }

	private IsotopicCluster aggregateFirst() {
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

    private IsotopicCluster aggregateHighest() {
	double intensitysum = this.sumIntensity();
	double minint = 0;
	double minmz = 0;

	for (Peak peak : this.isotopiccluster) {
	    if (peak.getIntensity() > minint) {
		minint = peak.getIntensity();
		minmz = peak.getMz();
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
	for (Peak peak : this.isotopiccluster) {
	    intensitysum += peak.getIntensity();
	}

	return intensitysum;
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
	StringBuilder stringbuilder = new StringBuilder();
	stringbuilder.append("(" + this.clusterID + ") [ ");
	if (this.isotopiccluster == null) {
	    return this.status;
	}
	for (Peak p : this.isotopiccluster) {
	    stringbuilder.append(p.getMz() + " ");
	}
	stringbuilder.append("]");
	return stringbuilder.toString();
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
