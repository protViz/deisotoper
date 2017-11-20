package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-21
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.GraphPath; // TODO (LS): - remove this dependency

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.dto.MassSpectrum;
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

	// TODO (LS): do not use acronyms
    public List<IsotopicClusterGraph> getIcgList() {
	return icglist;
    }

    public String getAnnotatedSpectrum() {
	return annotatedSpectrum;
    }

    // TODO (LS): not used remove
    public MassSpectrometryMeasurement deisotopeMSM(MassSpectrometryMeasurement input, String modus,
	    Configuration config) {
	MassSpectrometryMeasurement output = new MassSpectrometryMeasurement(input.getSource());

	this.config = config;

	for (MassSpectrum ms : input.getMSlist()) {
	    output.getMSlist().add(deisotopeMS(ms, modus));
	}

	return output;
    }

    // TODO (LS) :
    public MassSpectrum deisotopeMS(MassSpectrum massSpectrum, String modus) {
	IsotopicMassSpectrum ims = new IsotopicMassSpectrum(massSpectrum, this.config.getDelta(), this.config);

	saveAnnotatedSpectrum(ims);

	//TODO (LS) : move this method to IsotopicSet
	ListMassSpectrum list = makeGraph(massSpectrum, ims, modus);

	ListMassSpectrum list3 = decharge(list, this.config);

	Sort.keySort(list3.getMz(), list3.getMz(), list3.getIntensity(), list3.getIsotope(), list3.getCharge());

	MassSpectrum msdeisotoped = noiseFiltering(massSpectrum, list3, this.config);

	return msdeisotoped;
    }

    private MassSpectrum noiseFiltering(MassSpectrum massSpectrum, ListMassSpectrum list, Configuration config) {
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

	    msdeisotoped = new MassSpectrum(massSpectrum.getTyp(), massSpectrum.getSearchEngine(), mz5, intensity5, massSpectrum.getPeptideMass(),
		    massSpectrum.getRt(), massSpectrum.getChargeState(), massSpectrum.getId(), charge5, isotope5);
	} else {
	    msdeisotoped = new MassSpectrum(massSpectrum.getTyp(), massSpectrum.getSearchEngine(), list.getMz(), list.getIntensity(),
		    massSpectrum.getPeptideMass(), massSpectrum.getRt(), massSpectrum.getChargeState(), massSpectrum.getId(), list.getCharge(),
		    list.getIsotope());
	}
	return msdeisotoped;
    }


    // TODO (LS) operations on paths should only happen in IsotopicSet.
    private ListMassSpectrum makeGraph(MassSpectrum input, IsotopicMassSpectrum ims, String modus) {
	this.icglist.removeAll(this.icglist);

	ListMassSpectrum list = new ListMassSpectrum();
	List<Double> mz = new ArrayList<>();

	for (IsotopicSet is : ims.getIsotopicMassSpectrum()) {

	    GraphPath<IsotopicCluster, Connection> bp = is.getBestPath();

	    this.icglist.add(is.getIcg());

	    ListMassSpectrum list2 = new ListMassSpectrum();

	    List<Double> mz2 = new ArrayList<>();

	    for (IsotopicCluster cluster : bp.getVertexList()) {
		if (cluster.getIsotopicCluster() != null) {
		    for (Peak p : cluster.getIsotopicCluster()) {
			mz2.add(p.getMz());
		    }

		    cluster.aggregation(modus);

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

    // TODO (LS) This is a method of the class IsotopicMassSpectrum
    private void saveAnnotatedSpectrum(IsotopicMassSpectrum ims) {
	this.annotatedSpectrum = null;

	StringBuilder sb = new StringBuilder();
	String linesep = System.getProperty("line.separator");

	sb.append("IsotopicSet,IsotopicCluster,Peak,Charge,mZ,Intensity").append(linesep);

	for (IsotopicSet is : ims.getIsotopicMassSpectrum()) {
	    for (IsotopicCluster ic : is.getIsotopicSet()) {
		if (ic.getIsotopicCluster() != null) {
		    for (Peak p : ic.getIsotopicCluster()) {
			sb.append(is.getSetID()).append(",").append(ic.getClusterID()).append(",").append(p.getPeakID())
				.append(",").append(ic.getCharge()).append(",").append(p.getMz()).append(",")
				.append(p.getIntensity()).append(linesep);
		    }
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
}