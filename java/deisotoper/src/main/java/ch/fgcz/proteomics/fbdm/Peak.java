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
    private boolean inSet = false;

    public Peak(double mz, double intensity, int peakId) {
        this(mz, intensity, -1.0, -1, peakId, -1, -1);
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

    public void setIsotope(double isotope) {
        this.isotope = isotope;
    }

    public void setInSet(boolean inSet) {
        this.inSet = inSet;
    }

    public boolean isInSet() {
        return inSet;
    }

    public int getIsotopicClusterID() {
        return isotopicClusterId;
    }

    public void setIsotopicClusterID(int clusterId) {
        this.isotopicClusterId = clusterId;
    }

    public int getIsotopicSetID() {
        return isotopicSetId;
    }

    public void setIsotopicSetID(int setId) {
        this.isotopicSetId = setId;
    }

    public double getIsotope() {
        return isotope;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
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

    public boolean equalsPeak(Peak peak) {
        return peak != null && (this.getMz() == peak.getMz() && this.getIntensity() == peak.getIntensity()
                && this.getCharge() == peak.getCharge() && this.getIsotope() == peak.getIsotope()
                && this.getPeakID() == peak.getPeakID() && this.isInSet() == peak.isInSet());
    }

    @Override
    public String toString() {
        return "(" + mz + ", " + intensity + ", " + isotope + ", " + charge + ", " + isotopicClusterId + ")";
    }
}