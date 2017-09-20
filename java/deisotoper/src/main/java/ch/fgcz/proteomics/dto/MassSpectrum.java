package ch.fgcz.proteomics.dto;

/**
 * @author Lucas Schmidt
 * @since 2017-08-30
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MassSpectrum {
    private String typ;
    private String searchEngine;

    // TODO (LS) why not double[] mz, intensity etc?
    private List<Double> mz = new ArrayList<>();
    private List<Double> intensity = new ArrayList<>();
    private List<Integer> charge = new ArrayList<>();
    private List<Double> isotope = new ArrayList<>();
    private double peptidmass;
    private double rt;
    private int chargestate;
    private int id;


    // TODO (LS):  not used
    public int[] getChargeArray() {
        int[] chargearray = new int[charge.size()];

        for (int i = 0; i < chargearray.length; i++) {
            chargearray[i] = charge.get(i);
        }

        return chargearray;
    }

    // TODO (LS): not used
    public double[] getMzArray() {
        double[] mzarray = new double[mz.size()];

        for (int i = 0; i < mzarray.length; i++) {
            mzarray[i] = mz.get(i);
        }

        return mzarray;
    }


    // TODO (LS): not used
    public double[] getIntensityArray() {
        double[] intarray = new double[intensity.size()];

        for (int i = 0; i < intarray.length; i++) {
            intarray[i] = intensity.get(i);
        }

        return intarray;
    }

    // TODO (LS): not used
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

    public void setCharge(List<Integer> charge) {
        this.charge = charge;
    }

    public List<Double> getIsotope() {
        return isotope;
    }

    public void setIsotope(List<Double> isotope) {
        this.isotope = isotope;
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

    public void setId(int id) {
        this.id = id;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public double getPeptideMass() {
        return peptidmass;
    }

    public void setPeptideMass(double peptidmass) {
        this.peptidmass = peptidmass;
    }

    public double getRt() {
        return rt;
    }

    public void setRt(double rt) {
        this.rt = rt;
    }

    public int getChargeState() {
        return chargestate;
    }

    public void setChargeState(int chargestate) {
        this.chargestate = chargestate;
    }

    /**
     * Constructs a MassSpectrum Object.
     * 
     * 
     * Why did we use two List<Double> instead of a Map<Double, Double> or a List<Peak>?
     * 
     * Firstly the Map<Double, Double> hasn't index operations and then it's hard to work with it in the future and also there would be problems if a key occurs more than one time. Secondly the
     * List<Peak> would be a very big performance leak. The List<Peak> contains Peaks and each of these Peaks is a Object. Therefore, assumed we have for example 30000 MassSpectrum Objects and 300
     * Peak Objects in one average List<Peak>, there would be 9000000 Objects. In some tests with that structure we got many Java out of memory Errors.
     * 
     * @param typ
     * @param searchEngine
     * @param mz
     * @param intensity
     * @param peptidmass
     * @param rt
     * @param chargestate
     * @param id
     */
    public MassSpectrum(String typ, String searchEngine, List<Double> mz, List<Double> intensity, double peptidmass, double rt, int chargestate, int id) {
        if (!isSorted(mz)) {
            Collections.sort(mz);
        }

        this.setMz(mz);
        this.setIntensity(intensity);
        this.setTyp(typ);
        this.setSearchEngine(searchEngine);
        this.setPeptideMass(peptidmass);
        this.setRt(rt);
        this.setChargeState(chargestate);
        this.setId(id);
    }

    /**
     * Constructs a MassSpectrum Object with a List of isotopes and a List of charges.
     * 
     * @param typ
     * @param searchEngine
     * @param mz
     * @param intensity
     * @param peptidmass
     * @param rt
     * @param chargestate
     * @param id
     * @param charge
     * @param isotope
     */
    public MassSpectrum(String typ, String searchEngine, List<Double> mz, List<Double> intensity, double peptidmass, double rt, int chargestate, int id, List<Integer> charge, List<Double> isotope) {
        if (!isSorted(mz)) {
            Collections.sort(mz);
        }

        this.setMz(mz);
        this.setIntensity(intensity);
        this.setCharge(charge);
        this.setIsotope(isotope);
        this.setTyp(typ);
        this.setSearchEngine(searchEngine);
        this.setPeptideMass(peptidmass);
        this.setRt(rt);
        this.setChargeState(chargestate);
        this.setId(id);
    }

    /**
     * Checks whether a list is sorted or not.
     * 
     * @param list
     * @return boolean
     */
    private static boolean isSorted(List<Double> list) {
        return list.stream().sorted().collect(Collectors.toList()).equals(list);
    }
}