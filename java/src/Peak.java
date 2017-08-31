
/**
 * @author Lucas Schmidt
 * @since 2017-08-31
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

    public Peak(double mz, double intensity) {
        this.setMz(mz);
        this.setIntensity(intensity);
    }
}
