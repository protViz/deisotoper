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
    private int peakId;
    private int isotopicClusterId;
    private int isotopicSetId;

    public int getIsotopicClusterID() {
        return isotopicClusterId;
    }

    public int getIsotopicSetID() {
        return isotopicSetId;
    }

    public double getIsotope() {
        return isotope;
    }

    public int getCharge() {
        return charge;
    }

    public int getPeakID() {
        return peakId;
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

    public Peak(double mz, double intensity, int peakId) {
        this.mz = mz;
        this.intensity = intensity;
        this.peakId = peakId;
        this.charge = -1;
        this.isotope = -1;
        this.isotopicClusterId = -1;
        this.isotopicSetId = -1;
    }

    public Peak(double mz, double intensity, double isotope, int charge, int peakID, int isotopicClusterId,
            int isotopicSetId) {
        this.mz = mz;
        this.intensity = intensity;
        this.peakId = peakID;
        this.charge = charge;
        this.isotope = isotope;
        this.isotopicClusterId = isotopicClusterId;
        this.isotopicSetId = isotopicSetId;
    }

    public boolean equals(Peak peak) {
        if (this.getMz() == peak.getMz() && this.getIntensity() == peak.getIntensity()
                && this.getCharge() == peak.getCharge() && this.getIsotope() == peak.getIsotope()) {
            return true;
        } else {
            return false;
        }
    }
}
