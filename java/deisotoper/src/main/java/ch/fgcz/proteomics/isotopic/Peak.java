package ch.fgcz.proteomics.isotopic;

/**
 * @author Lucas Schmidt
 * @since 2017-09-04
 */

public class Peak {
    private double mz;
    private double intensity;

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
    public Peak(double mz, double intensity) {
        this.mz = mz;
        this.intensity = intensity;
    }
}
