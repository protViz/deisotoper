package ch.fgcz.proteomics.dto;

/**
 * @author Lucas Schmidt
 * @since 2017-08-30
 */

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MassSpectrum implements MassSpectrumMetaInformation {
    private String typ;
    private String searchEngine;
    private List<Double> mz = new ArrayList<Double>();
    private List<Double> intensity = new ArrayList<Double>();
    private List<Integer> charge = new ArrayList<Integer>();
    private List<Double> isotope = new ArrayList<Double>();
    private double peptideMass;
    private double rt;
    private int chargeState;
    private int id;

    public MassSpectrum() {
        this.mz = new ArrayList<Double>();
        this.intensity = new ArrayList<Double>();
        this.charge = new ArrayList<Integer>();
        this.isotope = new ArrayList<Double>();
        this.typ = null;
        this.searchEngine = null;
        this.id = 0;
        this.rt = 0;
        this.chargeState = 0;
        this.peptideMass = 0;
    }

    public MassSpectrum(List<Double> mz, List<Double> intensity) {
        this(null, null, mz, intensity, 0, 0, 0, 0);
    }

    public MassSpectrum(String typ, String searchEngine, List<Double> mz, List<Double> intensity, double peptidMass,
            double rt, int chargeState, int id) {
        if (!isSorted(mz)) {
            throw new IllegalArgumentException("The mZ-values are not sorted!");
        }

        this.mz = mz;
        this.intensity = intensity;
        this.charge = new ArrayList<Integer>();
        this.isotope = new ArrayList<Double>();
        this.typ = typ;
        this.searchEngine = searchEngine;
        this.id = id;
        this.rt = rt;
        this.chargeState = chargeState;
        this.peptideMass = peptidMass;
    }

    public MassSpectrum(String typ, String searchEngine, List<Double> mz, List<Double> intensity, double peptidMass,
            double rt, int chargeState, int id, List<Integer> charge, List<Double> isotope) {
        if (!isSorted(mz)) {
            throw new IllegalArgumentException("The mZ-values are not sorted!");
        }

        this.mz = mz;
        this.intensity = intensity;
        this.charge = charge;
        this.isotope = isotope;
        this.typ = typ;
        this.searchEngine = searchEngine;
        this.id = id;
        this.rt = rt;
        this.chargeState = chargeState;
        this.peptideMass = peptidMass;
    }

    public double[] getMzArray() {
        double[] mzarray = new double[mz.size()];

        for (int i = 0; i < mzarray.length; i++) {
            mzarray[i] = mz.get(i);
        }

        return mzarray;
    }

    public double[] getIntensityArray() {
        double[] intarray = new double[intensity.size()];

        for (int i = 0; i < intarray.length; i++) {
            intarray[i] = intensity.get(i);
        }

        return intarray;
    }

    public List<Integer> getCharge() {
        return charge;
    }

    public List<Double> getIsotope() {
        return isotope;
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

    public int getId() {
        return id;
    }

    public String getTyp() {
        return typ;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public double getPeptideMass() {
        return peptideMass;
    }

    public void setPeptideMass(double peptidMass) {
        this.peptideMass = peptidMass;
    }

    public double getRt() {
        return rt;
    }

    public int getChargeState() {
        return chargeState;
    }

    public void setChargeState(int chargeState) {
        this.chargeState = chargeState;
    }

    private static boolean isSorted(List<Double> list) {
        return list.stream().sorted().collect(Collectors.toList()).equals(list);
    }
}