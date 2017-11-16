package ch.fgcz.proteomics.R;

import java.util.ArrayList;
import java.util.List;

import ch.fgcz.proteomics.dto.MassSpectrum;
import ch.fgcz.proteomics.fbdm.Configuration;
import ch.fgcz.proteomics.fbdm.Deisotoper;

/**
 * @author Lucas Schmidt
 * @since 2017-11-16
 */

public class FeaturesBasedDeisotopingR {
    private Deisotoper deisotoper;
    private MassSpectrum massSpectrum;
    private MassSpectrum resultSpectrum;

    public void setConfiguration(double[] AA_MASS, double F1, double F2, double F3, double F4, double F5, double DELTA,
	    double ERRORTOLERANCE, double DISTANCE, double NOISE, boolean DECHARGE) {
	Configuration config;
	if (AA_MASS.length > 1) {
	    List<Double> AA_MASS_LIST = new ArrayList<>();
	    for (int i = 0; i < AA_MASS.length; i++) {
		AA_MASS_LIST.add(AA_MASS[i]);
	    }

	    config = new Configuration(AA_MASS_LIST, F1, F2, F3, F4, F5, DELTA, ERRORTOLERANCE, DISTANCE, NOISE,
		    DECHARGE);
	} else {
	    config = new Configuration(F1, F2, F3, F4, F5, DELTA, ERRORTOLERANCE, DISTANCE, NOISE, DECHARGE);
	}

	this.deisotoper.setConfiguration(config);
    }

    public String getConfiguration() {
	return this.deisotoper.getConfiguration().toString(); // TODO: override toString
    }

    public void setMz(double[] mz) {
	List<Double> mzlist = new ArrayList<>();
	for (int i = 0; i < mz.length; i++) {
	    mzlist.add(mz[i]);
	}

	this.massSpectrum.setMz(mzlist);
    }

    public void setIntensity(double[] intensity) {
	List<Double> intensitylist = new ArrayList<>();
	for (int i = 0; i < intensity.length; i++) {
	    intensitylist.add(intensity[i]);
	}

	this.massSpectrum.setIntensity(intensitylist);
    }

    public void setPepMass(double pepmass) {
	this.massSpectrum.setPeptideMass(pepmass);
    }

    public void setCharge(int charge) {
	this.massSpectrum.setChargeState(charge);
    }

    public double[] getMz() {
	return this.resultSpectrum.getMzArray();
    }

    public double[] getIntensity() {
	return this.resultSpectrum.getIntensityArray();
    }

    public void deisotope(String modus) {
	this.resultSpectrum = this.deisotoper.deisotopeMS(massSpectrum, modus);
    }
}
