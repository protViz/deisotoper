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
    private List<IsotopicCluster> isotopicset = new ArrayList<>();
    private IsotopicClusterGraph isotopicclustergraph;
    private List<IsotopicCluster> bestpath;
    private int setID;

    public int getSetID() {
	return setID;
    }

    public List<IsotopicCluster> getIsotopicSet() {
	return isotopicset;
    }

    // TODO (LS) replace with method returning the dot string.
    public IsotopicClusterGraph getIsotopicClusterGraph() {
	return isotopicclustergraph;
    }

    public List<IsotopicCluster> getBestPath() {
	return bestpath;
    }


    public IsotopicSet(MassSpectrum massspectrum, List<Peak> isotopicset, double delta, int setid,
	    Configuration config) {
	try {
	    rangeCheck(isotopicset, config);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	List<IsotopicCluster> isotopicclusters = new ArrayList<>();

	isotopicclusters = collectClusterForEachCharge(isotopicclusters, isotopicset, 3, delta, config);

	isotopicclusters = collectClusterForEachCharge(isotopicclusters, isotopicset, 2, delta, config);

	isotopicclusters = collectClusterForEachCharge(isotopicclusters, isotopicset, 1, delta, config);

	isotopicclusters = removeMultipleIsotopicCluster(isotopicclusters);

	isotopicclusters = sortIsotopicSet(isotopicclusters);

	int clusterid = 0;
	for (IsotopicCluster cluster : isotopicclusters) {
	    cluster.setClusterID(clusterid);
	    clusterid++;
	}

	setBestPath(massspectrum, isotopicclusters, config);

	this.isotopicset = isotopicclusters;
	this.setID = setid;
    }


	private void setBestPath(MassSpectrum massspectrum, List<IsotopicCluster> isotopicclusters, Configuration config) {
		IsotopicClusterGraph isotopicclustergraph = new IsotopicClusterGraph(isotopicclusters);

		isotopicclustergraph.scoreIsotopicClusterGraph(massspectrum.getPeptideMass(), massspectrum.getChargeState(),
				config.getErrortolerance(), new Peaklist(massspectrum.getMz(), massspectrum.getIntensity()), config);

		this.isotopicclustergraph = isotopicclustergraph;
		this.bestpath = isotopicclustergraph.bestPath(getStart(isotopicclustergraph), getEnd(isotopicclustergraph))
				.getVertexList();
	}
    private List<IsotopicCluster> collectClusterForEachCharge(List<IsotopicCluster> isotopicclusters,
	    List<Peak> isotopicset, int charge, double delta, Configuration config) {
	for (Peak a : isotopicset) {
	    for (Peak b : isotopicset) {
		double distanceab = b.getMz() - a.getMz();
		for (Peak c : isotopicset) {
		    List<Peak> ic = new ArrayList<>();
		    double distanceac = c.getMz() - a.getMz();
		    double distancebc = c.getMz() - b.getMz();

		    if ((config.getDistance() / charge) - delta < distanceab
			    && distanceab < (config.getDistance() / charge) + delta) {
			ic.add(a);
			ic.add(b);
		    }

		    if ((config.getDistance() / charge) - delta < distancebc
			    && distancebc < (config.getDistance() / charge) + delta
			    && ((config.getDistance() / charge) - delta) * 2 < distanceac
			    && distanceac < ((config.getDistance() / charge) + delta) * 2) {
			ic.add(c);
		    }

		    if (ic.size() == 2 || ic.size() == 3) {
			IsotopicCluster cluster = new IsotopicCluster(ic, charge, config);
			isotopicclusters.add(cluster);
		    }
		}
	    }
	}

	return isotopicclusters;
    }

    private static List<IsotopicCluster> sortIsotopicSet(List<IsotopicCluster> isotopicclusters) {
	Collections.sort(isotopicclusters, new Comparator<IsotopicCluster>() {
	    @Override
	    public int compare(IsotopicCluster cluster1, IsotopicCluster cluster2) {
		int result = Double.compare(cluster1.getIsotopicCluster().get(0).getMz(),
			cluster2.getIsotopicCluster().get(0).getMz());

		if (result == 0) {
		    result = Double.compare(cluster1.getIsotopicCluster().get(1).getMz(),
			    cluster2.getIsotopicCluster().get(1).getMz());
		    if (result == 0) {
			if (cluster1.getIsotopicCluster().size() == 3 && cluster2.getIsotopicCluster().size() == 3) {
			    result = Double.compare(cluster1.getIsotopicCluster().get(2).getMz(),
				    cluster2.getIsotopicCluster().get(2).getMz());
			    return result;
			}
		    }
		}

		return result;
	    }
	});

	return isotopicclusters;
    }

    private static List<IsotopicCluster> removeMultipleIsotopicCluster(List<IsotopicCluster> isotopicclusters) {
	List<IsotopicCluster> result = new ArrayList<>();
	Set<List<Peak>> set = new HashSet<>();

	for (IsotopicCluster cluster : isotopicclusters) {
	    if (set.add(cluster.getIsotopicCluster())) {
		result.add(cluster);
	    }
	}

	return result;
    }

    private IsotopicCluster getStart(IsotopicClusterGraph isotopicclustergraph) {
	for (IsotopicCluster cluster : isotopicclustergraph.getIsotopicclustergraph().vertexSet()) {
	    if (cluster.getIsotopicCluster() == null && cluster.getStatus() == "start") {
		return cluster;
	    }
	}
	return null;
    }

    private IsotopicCluster getEnd(IsotopicClusterGraph isotopicclustergraph) {
	for (IsotopicCluster cluster : isotopicclustergraph.getIsotopicclustergraph().vertexSet()) {
	    if (cluster.getIsotopicCluster() == null && cluster.getStatus() == "end") {
		return cluster;
	    }
	}
	return null;
    }

    private static void rangeCheck(List<Peak> peaks, Configuration config) throws Exception {
	for (int i = 0; i < peaks.size() - 1; i++) {
	    double distance = peaks.get(i + 1).getMz() - peaks.get(i).getMz();

	    boolean b = false;
	    for (int charge = 1; charge <= 3; charge++) {
		if (((config.getDistance() / charge - config.getDelta() < Math.abs(distance)
			&& Math.abs(distance) < config.getDistance() / charge + config.getDelta()))) {
		    b = true;
		}
	    }

	    if (b == false) {
		throw new Exception("Wrong distance at IsotopicSet creation! (" + distance + ")");
	    }
	}
    }
}