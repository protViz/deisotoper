package ch.fgcz.proteomics.dto;

/**
 * @author Lucas Schmidt
 * @since 2017-08-22
 */

import java.util.ArrayList;
import java.util.List;

public class MassSpectrometryMeasurement {
    private List<MassSpectrum> massspectrumlist = new ArrayList<>();
    private String source;

    public String getSource() {
	return source;
    }

    public void setSource(String source) {
	this.source = source;
    }

    public List<MassSpectrum> getMSlist() {
	return massspectrumlist;
    }

    public void setMSlist(List<MassSpectrum> massspectrumlist) {
	this.massspectrumlist = massspectrumlist;
    }

    public MassSpectrometryMeasurement(String source) {
	this.setSource(source);
	this.setMSlist(new ArrayList<>());
    }

    public void addMS(String typ, String searchEngine, double[] mz, double[] intensity, double peptideMass, double rt,
	    int chargeState, int id) {
	List<Double> mzValues = new ArrayList<>();
	List<Double> intensityValues = new ArrayList<>();

	for (int i = 0; i < mz.length || i < intensity.length; i++) {
	    mzValues.add(mz[i]);
	    intensityValues.add(intensity[i]);
	}

	this.getMSlist()
		.add(new MassSpectrum(typ, searchEngine, mzValues, intensityValues, peptideMass, rt, chargeState, id));
    }

    public void addMS(String typ, String searchengine, List<Double> mz, List<Double> intensity, double peptidmass,
	    double rt, int chargestate, int id) {
	this.getMSlist().add(new MassSpectrum(typ, searchengine, mz, intensity, peptidmass, rt, chargestate, id));
    }

    public void addMS(String typ, String searchengine, List<Double> mz, List<Double> intensity, double peptidmass,
	    double rt, int chargestate, int id, List<Integer> charge, List<Double> isotope) {
	this.getMSlist().add(
		new MassSpectrum(typ, searchengine, mz, intensity, peptidmass, rt, chargestate, id, charge, isotope));
    }
}