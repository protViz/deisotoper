
/**
 * @author Lucas Schmidt
 * @since 2017-08-30
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MassSpectrum {
    private String typ;
    private String searchengine;
    private List<Peak> peaklist;
    private double peptidmass;
    private double rt;
    private int chargestate;
    private int id;

    public List<Peak> getPeaklist() {
        return peaklist;
    }

    public void setPeaklist(List<Peak> peaklist) {
        this.peaklist = peaklist;
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

    public MassSpectrum(String typ, String searchengine, double[] mz, double[] intensity, double peptidmass, double rt, int chargestate, int id) {
        List<Peak> plist = new ArrayList<>();

        for (int i = 0; i < mz.length && i < intensity.length; i++) {
            plist.add(new Peak(mz[i], intensity[i]));
        }

        Collections.sort(plist, (a, b) -> a.getMz() < b.getMz() ? -1 : a.getMz() == b.getMz() ? 0 : 1);

        this.setPeaklist(plist);
        this.setTyp(typ);
        this.setSearchEngine(searchengine);
        this.setPeptidMass(peptidmass);
        this.setRt(rt);
        this.setChargeState(chargestate);
        this.setId(id);
    }
}