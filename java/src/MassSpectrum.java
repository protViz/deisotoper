
/**
 * @author Lucas Schmidt
 * @since 2017-08-30
 */

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MassSpectrum {
    private String typ;
    private String searchengine;
    private List<Double> mz = new ArrayList<>();
    private List<Double> intensity = new ArrayList<>();
    private double peptidmass;
    private double rt;
    private int chargestate;
    private int id;

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

    public MassSpectrum(String typ, String searchengine, double[] mz, double[] intensity, double peptidmass, double rt, int chargestate, int id) throws Exception {
        List<Double> mzlist = new ArrayList<>();
        List<Double> intensitylist = new ArrayList<>();

        for (int i = 0; i < mz.length && i < intensity.length; i++) {
            mzlist.add(mz[i]);
            intensitylist.add(intensity[i]);
        }

        if (!isSorted(mzlist) || mz.length != intensity.length) {
            throw new Exception();
        }

        this.setMz(mzlist);
        this.setIntensity(intensitylist);
        this.setTyp(typ);
        this.setSearchEngine(searchengine);
        this.setPeptidMass(peptidmass);
        this.setRt(rt);
        this.setChargeState(chargestate);
        this.setId(id);
    }

    private static boolean isSorted(List<Double> list) {
        return list.stream().sorted().collect(Collectors.toList()).equals(list);
    }
}