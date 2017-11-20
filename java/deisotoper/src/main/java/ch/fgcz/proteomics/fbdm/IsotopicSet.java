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

import org.jgrapht.GraphPath;

import ch.fgcz.proteomics.dto.MassSpectrum;

public class IsotopicSet {
    private List<IsotopicCluster> isotopicset = new ArrayList<>();
    private IsotopicClusterGraph icg;
    private GraphPath<IsotopicCluster, Connection> bestpath;
    private int setID;

    public int getSetID() {
	return setID;
    }

    public List<IsotopicCluster> getIsotopicSet() {
	return isotopicset;
    }

    public IsotopicClusterGraph getIcg() {
	return icg;
    }

    public GraphPath<IsotopicCluster, Connection> getBestpath() {
	return bestpath;
    }

    private void setBestPath(MassSpectrum ms, List<IsotopicCluster> is, Configuration config) {
	IsotopicClusterGraph icg = new IsotopicClusterGraph(is);

	icg.scoreIsotopicClusterGraph(ms.getPeptideMass(), ms.getChargeState(), config.getErrortolerance(),
		new Peaklist(ms.getMz(), ms.getIntensity()), config);

	GraphPath<IsotopicCluster, Connection> bp = icg.bestPath(getStart(icg), getEnd(icg));

	this.icg = icg;
	this.bestpath = bp;
    }

    public IsotopicSet(MassSpectrum massspectrum, List<Peak> isotopicset, double delta, int setid,
	    Configuration config) {
	try {
	    rangeCheck(isotopicset, config);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	List<IsotopicCluster> is = new ArrayList<>();

	is = collectClusterForEachCharge(is, isotopicset, 3, delta, config);

	is = collectClusterForEachCharge(is, isotopicset, 2, delta, config);

	is = collectClusterForEachCharge(is, isotopicset, 1, delta, config);

	is = removeMultipleIsotopicCluster(is);

	is = sortIsotopicSet(is);

	int clusterid = 0;
	for (IsotopicCluster cluster : is) {
	    cluster.setClusterID(clusterid);
	    clusterid++;
	}
	
	setBestPath(massspectrum, is, config);

	this.isotopicset = is;
	this.setID = setid;
    }

    private List<IsotopicCluster> collectClusterForEachCharge(List<IsotopicCluster> is, List<Peak> isotopicset,
	    int charge, double delta, Configuration config) {
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
			is.add(cluster);
		    }
		}
	    }
	}

	return is;
    }

    private static List<IsotopicCluster> sortIsotopicSet(List<IsotopicCluster> list) {
	Collections.sort(list, new Comparator<IsotopicCluster>() {
	    @Override
	    public int compare(IsotopicCluster o1, IsotopicCluster o2) {
		int result = Double.compare(o1.getIsotopicCluster().get(0).getMz(),
			o2.getIsotopicCluster().get(0).getMz());

		if (result == 0) {
		    result = Double.compare(o1.getIsotopicCluster().get(1).getMz(),
			    o2.getIsotopicCluster().get(1).getMz());
		    if (result == 0) {
			if (o1.getIsotopicCluster().size() == 3 && o2.getIsotopicCluster().size() == 3) {
			    result = Double.compare(o1.getIsotopicCluster().get(2).getMz(),
				    o2.getIsotopicCluster().get(2).getMz());
			    return result;
			}
		    }
		}

		return result;
	    }
	});

	return list;
    }

    private static List<IsotopicCluster> removeMultipleIsotopicCluster(List<IsotopicCluster> list) {
	List<IsotopicCluster> result = new ArrayList<>();
	Set<List<Peak>> titles = new HashSet<>();

	for (IsotopicCluster item : list) {
	    if (titles.add(item.getIsotopicCluster())) {
		result.add(item);
	    }
	}

	return result;
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

    private IsotopicCluster getStart(IsotopicClusterGraph icg) {
	for (IsotopicCluster e : icg.getIsotopicclustergraph().vertexSet()) {
	    if (e.getIsotopicCluster() == null && e.getStatus() == "start") {
		return e;
	    }
	}
	return null;
    }

    private IsotopicCluster getEnd(IsotopicClusterGraph icg) {
	for (IsotopicCluster e : icg.getIsotopicclustergraph().vertexSet()) {
	    if (e.getIsotopicCluster() == null && e.getStatus() == "end") {
		return e;
	    }
	}
	return null;
    }
}