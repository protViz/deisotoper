package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-04
 */

import java.util.ArrayList;
import java.util.List;

import ch.fgcz.proteomics.dto.MassSpectrum;

public class Peaklist {
    private List<Peak> peaklist = new ArrayList<>();

    private List<Double> mz = new ArrayList<>();
    private List<Double> intensity = new ArrayList<>();
    private List<Double> isotope = new ArrayList<>();
    private List<Integer> charge = new ArrayList<>();

    public List<Peak> getPeaklist() {
	return peaklist;
    }

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

    public Peaklist() {
    }

    public Peaklist(MassSpectrum massspectrum) {
	List<Peak> plist = new ArrayList<>();

	for (int i = 0; i < massspectrum.getMz().size() || i < massspectrum.getIntensity().size(); i++) {
	    plist.add(new Peak(massspectrum.getMz().get(i), massspectrum.getIntensity().get(i), i));
	}

	this.peaklist = plist;
    }

    public Peaklist(List<Double> mz, List<Double> intensity) {
	List<Peak> plist = new ArrayList<>();

	for (int i = 0; i < mz.size() || i < intensity.size(); i++) {
	    plist.add(new Peak(mz.get(i), intensity.get(i), i));
	}

	this.peaklist = plist;
    }

    public Peaklist(List<Double> mz, List<Double> intensity, List<Double> isotope, List<Integer> charge) {
	List<Peak> plist = new ArrayList<>();

	for (int i = 0; i < mz.size() || i < intensity.size(); i++) {
	    plist.add(new Peak(mz.get(i), intensity.get(i), isotope.get(i), charge.get(i), i));
	}

	this.peaklist = plist;
    }
}
