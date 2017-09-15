package ch.fgcz.proteomics.dto;

/**
 * @author Lucas Schmidt
 * @since 2017-08-22
 */

import java.util.ArrayList;
import java.util.List;

public class MassSpectrometryMeasurement {
    private List<MassSpectrum> MSlist = new ArrayList<MassSpectrum>();
    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<MassSpectrum> getMSlist() {
        return MSlist;
    }

    public void setMSlist(List<MassSpectrum> list) {
        this.MSlist = list;
    }

    /**
     * Constructs a MassSpectrometryMeasurement object.
     * 
     * @param src
     */
    public MassSpectrometryMeasurement(String src) {
        List<MassSpectrum> list = new ArrayList<>();
        this.setSource(src);
        this.setMSlist(list);
    }

    /**
     * Fills the generated List<MassSpectrum> with input data.
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
    public void addMS(String typ, String searchengine, double[] mz, double[] intensity, double peptidmass, double rt, int chargestate, int id) {
        List<Double> mzlist = new ArrayList<>();
        List<Double> intlist = new ArrayList<>();

        for (int i = 0; i < mz.length || i < intensity.length; i++) {
            mzlist.add(mz[i]);
            intlist.add(intensity[i]);
        }

        this.getMSlist().add(new MassSpectrum(typ, searchengine, mzlist, intlist, peptidmass, rt, chargestate, id));
    }

    /**
     * Fills the generated List<MassSpectrum> with input data.
     * 
     * @param typ
     * @param searchengine
     * @param mz
     * @param intensity
     * @param peptidmass
     * @param rt
     * @param chargestate
     * @param id
     * @param charge
     * @param isotope
     */
    public void addMS(String typ, String searchengine, List<Double> mz, List<Double> intensity, double peptidmass, double rt, int chargestate, int id, List<Integer> charge, List<Double> isotope) {
        this.getMSlist().add(new MassSpectrum(typ, searchengine, mz, intensity, peptidmass, rt, chargestate, id, charge, isotope));
    }

    /**
     * @return version
     */
    public String version() {
        return "2017-09-15 (1) Lucas Schmidt";
    }
}