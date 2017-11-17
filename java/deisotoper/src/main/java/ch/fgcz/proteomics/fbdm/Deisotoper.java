package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-21
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.GraphPath;

import ch.fgcz.proteomics.dto.MassSpectrum;
import ch.fgcz.proteomics.fbdm.IsotopicClusterGraph;
import ch.fgcz.proteomics.utilities.Sort;

public class Deisotoper {
    private Configuration config;
    private List<IsotopicClusterGraph> icglist = new ArrayList<>();
    private String annotatedSpectrum;

    public Configuration getConfiguration() {
	return config;
    }

    public void setConfiguration(Configuration config) {
	this.config = config;
    }

    public List<IsotopicClusterGraph> getIcgList() {
	return icglist;
    }

    public String getAnnotatedSpectrum() {
	return annotatedSpectrum;
    }

    // public MassSpectrometryMeasurement deisotopeMSM(MassSpectrometryMeasurement
    // input, String modus,
    // Configuration config) {
    //
    //
    // String date = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());
    //
    // for (MassSpectrum ms : input.getMSlist()) {
    //
    // output.getMSlist().add(deisotopeMS(ms, modus, config, date));
    // }
    //
    // return output;
    // }

    public MassSpectrum deisotopeMS(MassSpectrum input, String modus) {
	IsotopicMassSpectrum ims = new IsotopicMassSpectrum(input, this.config.getDelta(), this.config);

	saveAnnotatedSpectrum(ims);

	ListMassSpectrum list = makeGraph(input, ims, modus, this.config);

	ListMassSpectrum list3 = decharge(list, this.config);

	Sort.keySort(list3.getMz(), list3.getMz(), list3.getIntensity(), list3.getIsotope(), list3.getCharge());

	MassSpectrum msdeisotoped = noiseFiltering(input, list3, this.config);

	return msdeisotoped;
    }

    private MassSpectrum noiseFiltering(MassSpectrum ms, ListMassSpectrum list, Configuration config) {
	MassSpectrum msdeisotoped;
	if (config.getNoise() != 0) {
	    double threshold = Collections.max(list.getIntensity()) * config.getNoise() / 100;

	    List<Double> mz5 = new ArrayList<>();
	    List<Double> intensity5 = new ArrayList<>();
	    List<Double> isotope5 = new ArrayList<>();
	    List<Integer> charge5 = new ArrayList<>();

	    for (int i = 0; i < list.getIntensity().size(); i++) {
		if (threshold < list.getIntensity().get(i)) {
		    mz5.add(list.getMz().get(i));
		    intensity5.add(list.getIntensity().get(i));
		    isotope5.add(list.getIsotope().get(i));
		    charge5.add(list.getCharge().get(i));
		}
	    }

	    msdeisotoped = new MassSpectrum(ms.getTyp(), ms.getSearchEngine(), mz5, intensity5, ms.getPeptideMass(),
		    ms.getRt(), ms.getChargeState(), ms.getId(), charge5, isotope5);
	} else {
	    msdeisotoped = new MassSpectrum(ms.getTyp(), ms.getSearchEngine(), list.getMz(), list.getIntensity(),
		    ms.getPeptideMass(), ms.getRt(), ms.getChargeState(), ms.getId(), list.getCharge(),
		    list.getIsotope());
	}
	return msdeisotoped;
    }

    private ListMassSpectrum makeGraph(MassSpectrum input, IsotopicMassSpectrum ims, String modus,
	    Configuration config) {
	this.icglist.removeAll(this.icglist);

	ListMassSpectrum list = new ListMassSpectrum();
	List<Double> mz = new ArrayList<>();

	for (IsotopicSet is : ims.getIsotopicMassSpectrum()) {
	    IsotopicClusterGraph icg = new IsotopicClusterGraph(is);

	    icg.scoreIsotopicClusterGraph(input.getPeptideMass(), input.getChargeState(), config.getErrortolerance(),
		    new Peaklist(input.getMz(), input.getIntensity()), config);

	    this.icglist.add(icg);

	    GraphPath<IsotopicCluster, Connection> bp = icg.bestPath(getStart(icg), getEnd(icg));

	    ListMassSpectrum list2 = new ListMassSpectrum();

	    List<Double> mz2 = new ArrayList<>();

	    for (IsotopicCluster cluster : bp.getVertexList()) {
		if (cluster.getIsotopicCluster() != null) {
		    for (Peak p : cluster.getIsotopicCluster()) {
			mz2.add(p.getMz());
		    }

		    System.out.println("bp " + cluster.toString()); // REMOVE AFTER DEBUG

		    aggregation(cluster, modus);

		    int position = 1;
		    for (Peak p : cluster.getIsotopicCluster()) {
			list2.getMz().add(p.getMz());
			list2.getIntensity().add(p.getIntensity());
			list2.getIsotope().add((double) position);
			list2.getCharge().add(cluster.getCharge());
			position++;
		    }
		}
	    }

	    list.getMz().addAll(list2.getMz());
	    list.getIntensity().addAll(list2.getIntensity());
	    list.getIsotope().addAll(list2.getIsotope());
	    list.getCharge().addAll(list2.getCharge());

	    mz.addAll(mz2);
	}

	for (int i = 0; i < input.getMz().size(); i++) {
	    if (!mz.contains(input.getMz().get(i))) {
		list.getMz().add(input.getMz().get(i));
		list.getIntensity().add(input.getIntensity().get(i));
		list.getIsotope().add(-1.0);
		list.getCharge().add(-1);
	    }
	}

	return list;
    }

    private void saveAnnotatedSpectrum(IsotopicMassSpectrum ims) {
	StringBuilder sb = new StringBuilder();
	String linesep = System.getProperty("line.separator");

	sb.append("IsotopicSet,IsotopicCluster,Peak,Charge,mZ,Intensity").append(linesep);

	for (IsotopicSet is : ims.getIsotopicMassSpectrum()) {
	    for (IsotopicCluster ic : is.getIsotopicSet()) {
		for (Peak p : ic.getIsotopicCluster()) {
		    sb.append(is.getSetID()).append(",").append(ic.getClusterID()).append(",").append(p.getPeakID())
			    .append(",").append(ic.getCharge()).append(",").append(p.getMz()).append(",")
			    .append(p.getIntensity()).append(linesep);
		}
	    }
	}

	this.annotatedSpectrum = sb.toString();
    }

    private ListMassSpectrum decharge(ListMassSpectrum list, Configuration config) {
	ListMassSpectrum list2 = new ListMassSpectrum();

	if (config.isDecharge() == true) {
	    for (int i = 0; i < list.getMz().size(); i++) {
		if (list.getCharge().get(i) > 1) {
		    list2.getMz().add(list.getMz().get(i) * list.getCharge().get(i)
			    - (list.getCharge().get(i) - 1) * config.getH_MASS());
		    list2.getIntensity().add(list.getIntensity().get(i));
		    list2.getIsotope().add(list.getIsotope().get(i));
		    list2.getCharge().add(1);
		} else {
		    list2.getMz().add(list.getMz().get(i));
		    list2.getIntensity().add(list.getIntensity().get(i));
		    list2.getIsotope().add(list.getIsotope().get(i));
		    list2.getCharge().add(list.getCharge().get(i));
		}
	    }
	} else {
	    list2.getMz().addAll(list.getMz());
	    list2.getIntensity().addAll(list.getIntensity());
	    list2.getIsotope().addAll(list.getIsotope());
	    list2.getCharge().addAll(list.getCharge());
	}

	return list2;
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

    private IsotopicCluster aggregation(IsotopicCluster cluster, String modus) {
	if (modus.equals("first")) {
	    return cluster.aggregateFirst();
	} else if (modus.equals("highest")) {
	    return cluster.aggregateHighest();
	} else if (modus.equals("none")) {
	    return cluster;
	} else {
	    throw new IllegalArgumentException("Modus not found (" + modus + ")");
	}
    }
}