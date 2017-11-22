package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-18
 */

import java.util.ArrayList;
import java.util.List;

import ch.fgcz.proteomics.dto.MassSpectrum;

public class IsotopicMassSpectrum {
    private List<IsotopicSet> isotopicmassspectrum = new ArrayList<>();
    private Peaklist aggregatedpeaklist;

    public Peaklist getAggregatedPeaklist() {
	return aggregatedpeaklist;
    }

    public void setAggregatedPeaklist(Peaklist aggregatedpeaklist) {
	this.aggregatedpeaklist = aggregatedpeaklist;
    }

    public List<IsotopicSet> getIsotopicMassSpectrum() {
	return isotopicmassspectrum;
    }

    public IsotopicMassSpectrum(MassSpectrum massspectrum, double delta, Configuration config, Deisotoper deisotoper,
	    String modus) {
	this(massspectrum, new Peaklist(massspectrum), delta, config, deisotoper, modus);
    }

    public IsotopicMassSpectrum(MassSpectrum massspectrum, Peaklist peaklist, double delta, Configuration config,
	    Deisotoper deisotoper, String modus) {
	int id = 0;
	for (int i = 0; i < peaklist.getPeaklist().size(); i++) {
	    List<Peak> isotopicset = new ArrayList<>();

	    while (i < peaklist.getPeaklist().size() - 1) {
		boolean trigger = false;
		double distance = peaklist.getPeaklist().get(i + 1).getMz() - peaklist.getPeaklist().get(i).getMz();

		for (int charge = 1; charge <= 3; charge++) {
		    if ((config.getDistance() / charge) - delta < distance
			    && distance < (config.getDistance() / charge) + delta) {
			if (isotopicset.size() == 0) {
			    isotopicset.add((peaklist.getPeaklist().get(i)));
			}
			isotopicset.add((peaklist.getPeaklist().get(i + 1)));
			trigger = true;
		    }
		}

		if (trigger == false) {
		    break;
		}

		i++;
	    }

	    if (1 < isotopicset.size()) {
		IsotopicSet is = new IsotopicSet(massspectrum, isotopicset, delta, id, config);
		id++;

		this.isotopicmassspectrum.add(is);

		if (isotopicset.size() == peaklist.getPeaklist().size()) {
		    break;
		}
	    }
	}

	saveAnnotatedSpectrum(deisotoper, this);
	this.aggregatedpeaklist = aggregation(deisotoper, massspectrum, modus);
    }

    public Peaklist aggregation(Deisotoper deisotoper, MassSpectrum massspectrumin, String modus) {
	deisotoper.getIsotopicClusterGraphList().removeAll(deisotoper.getIsotopicClusterGraphList());
	Peaklist listmassspectrumaggregated = new Peaklist();
	List<Double> mz = new ArrayList<>();

	for (IsotopicSet isotopicset : this.getIsotopicMassSpectrum()) {

	    List<IsotopicCluster> bestpath = isotopicset.getBestPath();

	    deisotoper.getIsotopicClusterGraphList().add(isotopicset.getIsotopicClusterGraph());

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

    private void saveAnnotatedSpectrum(Deisotoper deisotoper, IsotopicMassSpectrum isotopicmassspectrum) {
	deisotoper.setAnnotatedSpectrum(null);

	StringBuilder stringbuilder = new StringBuilder();
	String linesep = System.getProperty("line.separator");

	stringbuilder.append("IsotopicSet,IsotopicCluster,Peak,Charge,mZ,Intensity").append(linesep);

	for (IsotopicSet isotopicset : isotopicmassspectrum.getIsotopicMassSpectrum()) {
	    for (IsotopicCluster isotopiccluster : isotopicset.getIsotopicSet()) {
		if (isotopiccluster.getIsotopicCluster() != null) {
		    for (Peak peak : isotopiccluster.getIsotopicCluster()) {
			stringbuilder.append(isotopicset.getSetID()).append(",").append(isotopiccluster.getClusterID())
				.append(",").append(peak.getPeakID()).append(",").append(isotopiccluster.getCharge())
				.append(",").append(peak.getMz()).append(",").append(peak.getIntensity())
				.append(linesep);
		    }
		}
	    }
	}

	deisotoper.setAnnotatedSpectrum(stringbuilder.toString());
    }
}
