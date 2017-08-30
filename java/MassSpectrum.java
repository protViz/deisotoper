
/**
 * @author Lucas Schmidt
 * @since 2017-08-30
 */

public class MassSpectrum {
    private String typ;
    private String searchengine;
    private double[] mz;
    private double[] intensity;
    private double peptidmass;
    private double rt;
    private int chargestate;
    private int id;

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

    public double[] getMz() {
        return mz;
    }

    public void setMz(double[] mz) {
        this.mz = mz;
    }

    public double[] getIntensity() {
        return intensity;
    }

    public void setIntensity(double[] intensity) {
        this.intensity = intensity;
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
}
