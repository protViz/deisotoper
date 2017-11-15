package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-11-15
 */

import java.util.ArrayList;
import java.util.List;

public class ListMassSpectrum {
    private List<Double> mz = new ArrayList<>();
    private List<Double> intensity = new ArrayList<>();
    private List<Double> isotope = new ArrayList<>();
    private List<Integer> charge = new ArrayList<>();

    public List<Double> getMz() {
	return mz;
    }

    public void setMz(List<Double> mz) {
	this.mz = mz;
    }

    public List<Double> getIntensity() {
	return intensity;
    }

    public void setIntensity(List<Double> intensity) {
	this.intensity = intensity;
    }

    public List<Double> getIsotope() {
	return isotope;
    }

    public void setIsotope(List<Double> isotope) {
	this.isotope = isotope;
    }

    public List<Integer> getCharge() {
	return charge;
    }

    public void setCharge(List<Integer> charge) {
	this.charge = charge;
    }

}
