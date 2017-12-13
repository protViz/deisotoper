package ch.fgcz.proteomics.mspy;

/**
 * @deprecated isn't up to date anymore.
 */
@Deprecated
public class Peak {
    private double mz;
    private double intensity;
    private int charge;
    private double isotope;
    private double fwhm;

    public double getFwhm() {
        return fwhm;
    }

    public void setFwhm(double fwhm) {
        this.fwhm = fwhm;
    }

    public double getIsotope() {
        return isotope;
    }

    public void setIsotope(double isotope) {
        this.isotope = isotope;
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

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public Peak(double mz, double intensity) {
        this.mz = mz;
        this.intensity = intensity;
        this.isotope = -1;
        this.charge = -1;
        this.fwhm = -1;
    }

    public Peak(double mz, double intensity, double isotope, int charge) {
        this.mz = mz;
        this.intensity = intensity;
        this.isotope = isotope;
        this.charge = charge;
        this.fwhm = -1;
    }

    public Peak(double mz, double intensity, double isotope, int charge, double fwhm) {
        this.mz = mz;
        this.intensity = intensity;
        this.isotope = isotope;
        this.charge = charge;
        this.fwhm = fwhm;
    }
}
