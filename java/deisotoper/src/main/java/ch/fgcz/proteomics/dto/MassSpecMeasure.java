package ch.fgcz.proteomics.dto;

/**
 * @author Lucas Schmidt
 * @since 2017-08-22
 */

import java.util.ArrayList;
import java.util.List;

public class MassSpecMeasure {
    private List<MassSpectrum> massSpectrumList = new ArrayList<MassSpectrum>();
    private String source;

    public String getSource() {
        return source;
    }

    public List<MassSpectrum> getMSlist() {
        return massSpectrumList;
    }

    public MassSpecMeasure(String source) {
        this.source = source;
        this.massSpectrumList = new ArrayList<MassSpectrum>();
    }

    public void addMS(String typ, String searchEngine, double[] mz, double[] intensity, double peptideMass, double rt,
            int chargeState, int id) {
        List<Double> mzValues = new ArrayList<Double>();
        List<Double> intensityValues = new ArrayList<Double>();

        for (int i = 0; i < mz.length || i < intensity.length; i++) {
            mzValues.add(mz[i]);
            intensityValues.add(intensity[i]);
        }

        this.getMSlist()
                .add(new MassSpectrum(typ, searchEngine, mzValues, intensityValues, peptideMass, rt, chargeState, id));
    }

    public void addMS(String typ, String searchEngine, List<Double> mz, List<Double> intensity, double peptidMass,
            double rt, int chargeState, int id) {
        this.getMSlist().add(new MassSpectrum(typ, searchEngine, mz, intensity, peptidMass, rt, chargeState, id));
    }

    public void addMS(String typ, String searchEngine, List<Double> mz, List<Double> intensity, double peptidMass,
            double rt, int chargeState, int id, List<Integer> charge, List<Double> isotope) {
        this.getMSlist().add(
                new MassSpectrum(typ, searchEngine, mz, intensity, peptidMass, rt, chargeState, id, charge, isotope));
    }
}