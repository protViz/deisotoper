package ch.fgcz.proteomics.dto;

/*
  @author Lucas Schmidt
 * @since 2017-08-22
 */

import java.util.ArrayList;
import java.util.List;

public class MassSpecMeasure {
    private List<MassSpectrum> massSpectrumList;
    private String source;

    public String getSource() {
        return source;
    }

    public List<MassSpectrum> getMSlist() {
        return massSpectrumList;
    }

    public MassSpecMeasure(String source) {
        this.source = source;
        this.massSpectrumList = new ArrayList<>();
    }

    public void addMS(double[] mz, double[] intensity, double peptideMass, int chargeState, int id) {
        List<Double> mzValues = new ArrayList<>();
        List<Double> intensityValues = new ArrayList<>();

        for (int i = 0; i < mz.length || i < intensity.length; i++) {
            mzValues.add(mz[i]);
            intensityValues.add(intensity[i]);
        }

        massSpectrumList.add(new MassSpectrum(mzValues, intensityValues, peptideMass, chargeState, id));
    }

    public void addMS(List<Double> mz, List<Double> intensity, double peptidMass, int chargeState, int id) {
        massSpectrumList.add(new MassSpectrum(mz, intensity, peptidMass, chargeState, id));
    }

    public void addMS(List<Double> mz, List<Double> intensity, double peptidMass, int chargeState, int id,
            List<Integer> charge, List<Double> isotope) {
        massSpectrumList.add(new MassSpectrum(mz, intensity, peptidMass, chargeState, id, charge, isotope));
    }
}