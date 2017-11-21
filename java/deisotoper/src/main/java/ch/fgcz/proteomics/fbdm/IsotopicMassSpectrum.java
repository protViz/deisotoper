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

    public List<IsotopicSet> getIsotopicMassSpectrum() {
	return isotopicmassspectrum;
    }

    public IsotopicMassSpectrum(MassSpectrum massspectrum, double delta, Configuration config, Deisotoper deisotoper) {
	this(massspectrum, new Peaklist(massspectrum), delta, config, deisotoper);
    }

    public IsotopicMassSpectrum(MassSpectrum massspectrum, Peaklist peaklist, double delta, Configuration config,
	    Deisotoper deisotoper) {
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
