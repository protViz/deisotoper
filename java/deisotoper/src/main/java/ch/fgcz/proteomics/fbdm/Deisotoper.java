package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-21
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.dto.MassSpectrum;
import ch.fgcz.proteomics.utilities.Sort;

public class Deisotoper {
    private Configuration config;
    private List<IsotopicClusterGraph> isotopicclustergraphlist = new ArrayList<>();
    private String annotatedspectrum;

    public Configuration getConfiguration() {
	return config;
    }

    public void setConfiguration(Configuration config) {
	this.config = config;
    }

    public List<IsotopicClusterGraph> getIsotopicClusterGraphList() {
	return isotopicclustergraphlist;
    }

    public String getAnnotatedSpectrum() {
	return annotatedspectrum;
    }

    public void setAnnotatedSpectrum(String annotatedspectrum) {
	this.annotatedspectrum = annotatedspectrum;
    }

    public MassSpectrometryMeasurement deisotopeMSM(MassSpectrometryMeasurement massspectrometrymeasurementin,
	    String modus, Configuration config) {
	MassSpectrometryMeasurement massspectrometrymeasurementout = new MassSpectrometryMeasurement(
		massspectrometrymeasurementin.getSource());

	this.config = config;

	for (MassSpectrum massspectrum : massspectrometrymeasurementin.getMSlist()) {
	    massspectrometrymeasurementout.getMSlist().add(deisotopeMS(massspectrum, modus));
	}

	return massspectrometrymeasurementout;
    }

    public MassSpectrum deisotopeMS(MassSpectrum massspectrum, String modus) {
	IsotopicMassSpectrum isotopicmassspectrum = new IsotopicMassSpectrum(massspectrum, this.config.getDelta(),
		this.config, this);

	// TODO (LS) : move this method to IsotopicSet
	Peaklist listmassspectrumaggregated = aggregation(massspectrum, isotopicmassspectrum, modus);

	Peaklist listmassspectrumdecharged = decharge(listmassspectrumaggregated, this.config);

	Sort.keySort(listmassspectrumdecharged.getMz(), listmassspectrumdecharged.getMz(),
		listmassspectrumdecharged.getIntensity(), listmassspectrumdecharged.getIsotope(),
		listmassspectrumdecharged.getCharge());

	MassSpectrum massspectrumdeisotoped = noiseFiltering(massspectrum, listmassspectrumdecharged, this.config);

	return massspectrumdeisotoped;
    }

    private MassSpectrum noiseFiltering(MassSpectrum massspectrum, Peaklist listmassspectrum, Configuration config) {
	MassSpectrum massspectrundeisotoped;

	if (config.getNoise() != 0) {
	    double threshold = Collections.max(listmassspectrum.getIntensity()) * config.getNoise() / 100;

	    List<Double> mz5 = new ArrayList<>();
	    List<Double> intensity5 = new ArrayList<>();
	    List<Double> isotope5 = new ArrayList<>();
	    List<Integer> charge5 = new ArrayList<>();

	    for (int i = 0; i < listmassspectrum.getIntensity().size(); i++) {
		if (threshold < listmassspectrum.getIntensity().get(i)) {
		    mz5.add(listmassspectrum.getMz().get(i));
		    intensity5.add(listmassspectrum.getIntensity().get(i));
		    isotope5.add(listmassspectrum.getIsotope().get(i));
		    charge5.add(listmassspectrum.getCharge().get(i));
		}
	    }

	    massspectrundeisotoped = new MassSpectrum(massspectrum.getTyp(), massspectrum.getSearchEngine(), mz5,
		    intensity5, massspectrum.getPeptideMass(), massspectrum.getRt(), massspectrum.getChargeState(),
		    massspectrum.getId(), charge5, isotope5);
	} else {
	    massspectrundeisotoped = new MassSpectrum(massspectrum.getTyp(), massspectrum.getSearchEngine(),
		    listmassspectrum.getMz(), listmassspectrum.getIntensity(), massspectrum.getPeptideMass(),
		    massspectrum.getRt(), massspectrum.getChargeState(), massspectrum.getId(),
		    listmassspectrum.getCharge(), listmassspectrum.getIsotope());
	}

	return massspectrundeisotoped;
    }

    private Peaklist aggregation(MassSpectrum massspectrumin, IsotopicMassSpectrum isotopicmassspectrum, String modus) {
	this.isotopicclustergraphlist.removeAll(this.isotopicclustergraphlist);

	Peaklist listmassspectrumaggregated = new Peaklist();
	List<Double> mz = new ArrayList<>();

	for (IsotopicSet isotopicset : isotopicmassspectrum.getIsotopicMassSpectrum()) {

	    List<IsotopicCluster> bestpath = isotopicset.getBestPath();

	    this.isotopicclustergraphlist.add(isotopicset.getIsotopicClusterGraph());

	    Peaklist listmassspectrumaggregated2 = new Peaklist();

	    List<Double> mz2 = new ArrayList<>();

	    for (IsotopicCluster cluster : bestpath) {
		if (cluster.getIsotopicCluster() != null) {
		    for (Peak peak : cluster.getIsotopicCluster()) {
			mz2.add(peak.getMz());
		    }

		    cluster.aggregation(modus);

		    int position = 1;
		    for (Peak peak : cluster.getIsotopicCluster()) {
			listmassspectrumaggregated2.getMz().add(peak.getMz());
			listmassspectrumaggregated2.getIntensity().add(peak.getIntensity());
			listmassspectrumaggregated2.getIsotope().add((double) position);
			listmassspectrumaggregated2.getCharge().add(cluster.getCharge());
			position++;
		    }
		}
	    }

	    listmassspectrumaggregated.getMz().addAll(listmassspectrumaggregated2.getMz());
	    listmassspectrumaggregated.getIntensity().addAll(listmassspectrumaggregated2.getIntensity());
	    listmassspectrumaggregated.getIsotope().addAll(listmassspectrumaggregated2.getIsotope());
	    listmassspectrumaggregated.getCharge().addAll(listmassspectrumaggregated2.getCharge());

	    mz.addAll(mz2);
	}

	for (int i = 0; i < massspectrumin.getMz().size(); i++) {
	    if (!mz.contains(massspectrumin.getMz().get(i))) {
		listmassspectrumaggregated.getMz().add(massspectrumin.getMz().get(i));
		listmassspectrumaggregated.getIntensity().add(massspectrumin.getIntensity().get(i));
		listmassspectrumaggregated.getIsotope().add(-1.0);
		listmassspectrumaggregated.getCharge().add(-1);
	    }
	}

	return listmassspectrumaggregated;
    }

    private Peaklist decharge(Peaklist listmassspectrum, Configuration config) {
	Peaklist listmassspectrumdecharged = new Peaklist();

	if (config.isDecharge() == true) {
	    for (int i = 0; i < listmassspectrum.getMz().size(); i++) {
		if (listmassspectrum.getCharge().get(i) > 1) {
		    listmassspectrumdecharged.getMz()
			    .add(listmassspectrum.getMz().get(i) * listmassspectrum.getCharge().get(i)
				    - (listmassspectrum.getCharge().get(i) - 1) * config.getH_MASS());
		    listmassspectrumdecharged.getIntensity().add(listmassspectrum.getIntensity().get(i));
		    listmassspectrumdecharged.getIsotope().add(listmassspectrum.getIsotope().get(i));
		    listmassspectrumdecharged.getCharge().add(1);
		} else {
		    listmassspectrumdecharged.getMz().add(listmassspectrum.getMz().get(i));
		    listmassspectrumdecharged.getIntensity().add(listmassspectrum.getIntensity().get(i));
		    listmassspectrumdecharged.getIsotope().add(listmassspectrum.getIsotope().get(i));
		    listmassspectrumdecharged.getCharge().add(listmassspectrum.getCharge().get(i));
		}
	    }
	} else {
	    listmassspectrumdecharged.getMz().addAll(listmassspectrum.getMz());
	    listmassspectrumdecharged.getIntensity().addAll(listmassspectrum.getIntensity());
	    listmassspectrumdecharged.getIsotope().addAll(listmassspectrum.getIsotope());
	    listmassspectrumdecharged.getCharge().addAll(listmassspectrum.getCharge());
	}

	return listmassspectrumdecharged;
    }
}