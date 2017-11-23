package ch.fgcz.proteomics.dto;

/**
 * @author Lucas Schmidt
 * @since 2017-08-30
 */

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MassSpectrum {
    private String typ;
    private String searchEngine;
    private List<Double> mz = new ArrayList<>();
    private List<Double> intensity = new ArrayList<>();
    private List<Integer> charge = new ArrayList<>();
    private List<Double> isotope = new ArrayList<>();
    private double peptideMass;
    private double rt;
    private int chargeState;
    private int id;

    // Helper function for R
    public int[] getChargeArray() {
        int[] chargearray = new int[charge.size()];

        for (int i = 0; i < chargearray.length; i++) {
            chargearray[i] = charge.get(i);
        }

        return chargearray;
    }

    // Helper function for R
    public double[] getMzArray() {
        double[] mzarray = new double[mz.size()];

        for (int i = 0; i < mzarray.length; i++) {
            mzarray[i] = mz.get(i);
        }

        return mzarray;
    }

    // Helper function for R
    public double[] getIntensityArray() {
        double[] intarray = new double[intensity.size()];

        for (int i = 0; i < intarray.length; i++) {
            intarray[i] = intensity.get(i);
        }

        return intarray;
    }

    // Helper function for R
    public double[] getIsotopeArray() {
        double[] isoarray = new double[isotope.size()];

        for (int i = 0; i < isoarray.length; i++) {
            isoarray[i] = isotope.get(i);
        }

        return isoarray;
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

    // public void setId(int id) {
    // this.id = id;
    // }

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

    public MassSpectrum() {
        this.mz = null;
        this.intensity = null;
        this.charge = null;
        this.isotope = null;
        this.typ = null;
        this.searchEngine = null;
        this.id = 0;
        this.rt = 0;
        this.chargeState = 0;
        this.peptideMass = 0;
    }

    /**
     * Why did we use two List<Double> instead of a Map<Double, Double> or a
     * List<Peak>?
     * 
     * Firstly the Map<Double, Double> hasn't index operations and then it's hard to
     * work with it in the future and also there would be problems if a key occurs
     * more than one time. Secondly the List<Peak> would be a very big performance
     * leak. The List<Peak> contains Peaks and each of these Peaks is a Object.
     * Therefore, assumed we have for example 30000 MassSpectrum Objects and 300
     * Peak Objects in one average List<Peak>, there would be 9000000 Objects. In
     * some tests with that structure we got many Java out of memory Errors.
     */
    public MassSpectrum(String typ, String searchEngine, List<Double> mz, List<Double> intensity, double peptidMass,
            double rt, int chargeState, int id) {
        if (!isSorted(mz)) {
            throw new IllegalArgumentException("The mZ-values are not sorted!");
        }

        this.mz = mz;
        this.intensity = intensity;
        this.charge = null;
        this.isotope = null;
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

    private static boolean isSorted(List<Double> list) {
        return list.stream().sorted().collect(Collectors.toList()).equals(list);
    }
}