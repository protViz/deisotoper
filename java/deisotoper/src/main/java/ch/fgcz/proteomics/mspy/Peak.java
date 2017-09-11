package ch.fgcz.proteomics.mspy;

/**
 * @author Lucas Schmidt
 * @since 2017-09-11
 */

public class Peak {
    private double mz;
    private double intensity;
    private int charge;
    private double isotope;

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

    /**
     * Constructs the Peak Object.
     * 
     * @param mz
     * @param intensity
     */
    public Peak(double mz, double intensity) {
        this.mz = mz;
        this.intensity = intensity;
        this.isotope = 0;
        this.charge = 1;
    }
}
