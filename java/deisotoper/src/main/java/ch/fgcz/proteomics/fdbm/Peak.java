package ch.fgcz.proteomics.fdbm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-04
 */

public class Peak {
    private double mz;
    private double intensity;
    private int peakID;

    public int getPeakID() {
        return peakID;
    }

    // TODO (LS) is this function used?
    public void setPeakID(int peakID) {
        this.peakID = peakID;
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

    /**
     * Constructs the Peak Object.
     * 
     * @param mz
     * @param intensity
     */
    public Peak(double mz, double intensity, int peakID) {
        this.mz = mz;
        this.intensity = intensity;
        this.peakID = peakID;
    }
}
