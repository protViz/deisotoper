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
    private String searchengine;
    private List<Double> mz = new ArrayList<>();
    private List<Double> intensity = new ArrayList<>();
    private List<Double> charge = new ArrayList<>();
    private List<Double> isotope = new ArrayList<>();
    private double peptidmass;
    private double rt;
    private int chargestate;
    private int id;

    public List<Double> getCharge() {
        return charge;
    }

    public void setCharge(List<Double> charge) {
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
        return searchengine;
    }

    public void setSearchEngine(String searchengine) {
        this.searchengine = searchengine;
    }

    public double getPeptidMass() {
        return peptidmass;
    }

    public void setPeptidMass(double peptidmass) {
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
     * @param searchengine
     * @param mz
     * @param intensity
     * @param peptidmass
     * @param rt
     * @param chargestate
     * @param id
     */
    public MassSpectrum(String typ, String searchengine, List<Double> mz, List<Double> intensity, double peptidmass, double rt, int chargestate, int id) {
        if (!isSorted(mz)) {
            Collections.sort(mz);
        }

        this.setMz(mz);
        this.setIntensity(intensity);
        this.setTyp(typ);
        this.setSearchEngine(searchengine);
        this.setPeptidMass(peptidmass);
        this.setRt(rt);
        this.setChargeState(chargestate);
        this.setId(id);
    }

    // public MassSpectrum(String typ, String searchengine, List<Double> mz, List<Double> intensity, double peptidmass, double rt, int chargestate, int id, List<Double> charge, List<Double> isotope) {
    // if (!isSorted(mz)) {
    // Collections.sort(mz);
    // }
    //
    // this.setMz(mz);
    // this.setIntensity(intensity);
    // this.setCharge(charge);
    // this.setIsotope(isotope);
    // this.setTyp(typ);
    // this.setSearchEngine(searchengine);
    // this.setPeptidMass(peptidmass);
    // this.setRt(rt);
    // this.setChargeState(chargestate);
    // this.setId(id);
    // }

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