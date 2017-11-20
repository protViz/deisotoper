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

    public IsotopicMassSpectrum(MassSpectrum massspectrum, double delta, Configuration config) {
	this(new Peaklist(massspectrum), delta, config);
    }

    public IsotopicMassSpectrum(Peaklist peaklist, double delta, Configuration config) {
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
		IsotopicSet is = new IsotopicSet(isotopicset, delta, id, config);
		id++;

		this.isotopicmassspectrum.add(is);

		if (isotopicset.size() == peaklist.getPeaklist().size()) {
		    break;
		}
	    }
	}
    }
}
