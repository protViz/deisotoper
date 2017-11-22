package ch.fgcz.proteomics.fbdm;

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
    private int isotopicclusterID;
    private int isotopicsetID;

    public int getIsotopicClusterID() {
        return isotopicclusterID;
    }

    public void setIsotopicClusterID(int isotopicclusterID) {
        this.isotopicclusterID = isotopicclusterID;
    }

    public int getIsotopicSetID() {
        return isotopicsetID;
    }

    public void setIsotopicSetID(int isotopicsetID) {
        this.isotopicsetID = isotopicsetID;
    }

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
        this.isotopicclusterID = -1;
        this.isotopicsetID = -1;
    }

    public Peak(double mz, double intensity, double isotope, int charge, int peakID, int isotopicclusterID,
            int isotopicsetID) {
        this.mz = mz;
        this.intensity = intensity;
        this.peakID = peakID;
        this.charge = charge;
        this.isotope = isotope;
        this.isotopicclusterID = isotopicclusterID;
        this.isotopicsetID = isotopicsetID;
    }

    public boolean equals(Peak p) {
        if (this.getMz() == p.getMz() && this.getIntensity() == p.getIntensity() && this.getCharge() == p.getCharge()
                && this.getIsotope() == p.getIsotope()) {
            return true;
        } else {
            return false;
        }
    }
}
