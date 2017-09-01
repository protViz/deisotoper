package main.java;

/**
 * @author Lucas Schmidt
 * @since 2017-08-29
 */

import java.util.ArrayList;
import java.util.List;

public class Peaklist {
    public static List<Peak> peaklist = new ArrayList<Peak>();

    public static class Peak {
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

    }

    public static List<Peak> createPeak(double mz, double intensity, int charge, double isotope) {
        Peak p = new Peak();

        p.setIntensity(intensity);
        p.setCharge(charge);
        p.setIsotope(isotope);
        p.setMz(mz);

        peaklist.add(p);

        return peaklist;
    }
}
