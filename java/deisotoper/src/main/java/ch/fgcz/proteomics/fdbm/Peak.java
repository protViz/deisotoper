package ch.fgcz.proteomics.fdbm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-04
 */

public class Peak {
    private double mz;
    private double intensity;
    private double isotope;
    private int charge;
    private int peakID;

    public double getIsotope() {
        return isotope;
    }

    public void setIsotope(double isotope) {
        this.isotope = isotope;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public int getPeakID() {
        return peakID;
    }

    public double getMz() {
        return mz;
    }

    public void setMz(double mz) {
        this.mz = mz;
    }

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    public Peak(double mz, double intensity, int peakID) {
        this.mz = mz;
        this.intensity = intensity;
        this.peakID = peakID;
        this.charge = -1;
        this.isotope = -1;
    }

    public Peak(double mz, double intensity, double isotope, int charge, int peakID) {
        this.mz = mz;
        this.intensity = intensity;
        this.peakID = peakID;
        this.charge = charge;
        this.isotope = isotope;
    }
}
