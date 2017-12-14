package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-10-09
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Configuration implements ScoringConfiguration {
    // TODO (LS) as map.
    private List<Double> aaMass = new ArrayList<Double>();
    private List<Double> aaMassDividedTwo = new ArrayList<Double>();
    private List<Double> aaMassDividedThree = new ArrayList<Double>();
    private double minimum = 0;
    private double maximum = Double.MAX_VALUE;
    private double f1 = 0.8;
    private double f2 = 0.5;
    private double f3 = 0.1;
    private double f4 = 0.1;
    private double f5 = 0.1;
    private double distance = 1.00048;
    private double errortolerance = 0.3;
    private double delta = 0.003;
    private double noise = 0;
    private boolean decharge = false;
    private String modus = "first";
    private static final double H_MASS = 1.008;
    private static final double NH3_MASS = 17.03052;
    private static final double H2O_MASS = 18.01528;
    private static final double NH_MASS = 15.01464;
    private static final double CO_MASS = 28.0101;
    private static final double PHE_MASS = 165.192;
    private static final double ASP_MASS = 133.104;
    private static final double AVE_UPDATED_MASS = 111.125;

    public boolean isDecharge() {
        return decharge;
    }

    public String getModus() {
        return modus;
    }

    @Override
    public double getErrorTolerance() {
        return errortolerance;
    }

    public double getDelta() {
        return delta;
    }

    public double getNoise() {
        return noise;
    }

    @Override
    public double getIsotopicPeakDistance() {
        return distance;
    }

    @Override
    public double getF1() {
        return f1;
    }

    @Override
    public double getF2() {
        return f2;
    }

    @Override
    public double getF3() {
        return f3;
    }

    @Override
    public double getF4() {
        return f4;
    }

    @Override
    public double getF5() {
        return f5;
    }

    @Override
    public double getHMass(int multiplier) {
        return H_MASS * multiplier;
    }

    @Override
    public double getNh3Mass(int charge) {
        return NH3_MASS / charge;
    }

    @Override
    public double getH2oMass(int charge) {
        return H2O_MASS / charge;
    }

    @Override
    public double getNhMass(int charge) {
        return NH_MASS / charge;
    }

    @Override
    public double getCoMass(int charge) {
        return CO_MASS / charge;
    }

    public double getPheMass() {
        return PHE_MASS;
    }

    public double getAspMass() {
        return ASP_MASS;
    }

    public double getAveUpdatedMass() {
        return AVE_UPDATED_MASS;
    }

    @Override
    public double getMin() {
        return minimum;
    }

    @Override
    public double getMax() {
        return maximum;
    }

    @Override
    public List<Double> getAaMass() {
        return aaMass;
    }

    @Override
    public List<Double> getAaMassDividedTwo() {
        return aaMassDividedTwo;
    }

    @Override
    public List<Double> getAaMassDividedThree() {
        return aaMassDividedThree;
    }

    public Configuration() {
        this(0.8, 0.5, 0.1, 0.1, 0.1, 0.003, 0.3, 1.00048, 0, false, "first");
    }

    public Configuration(double f1, double f2, double f3, double f4, double f5, double delta, double errortolerance,
            double distance, double noise, boolean decharge, String modus) {
        this(Arrays.asList(71.03711, 156.10111, 114.04293, 115.02694, 103.00919, 129.04259, 128.05858, 57.02146,
                137.05891, 113.08406, 113.08406, 128.09496, 131.04049, 147.06841, 97.05276, 87.03203, 101.04768,
                186.07931, 163.06333, 99.06841), f1, f2, f3, f4, f5, delta, errortolerance, distance, noise, decharge,
                modus);
    }

    public Configuration(List<Double> aaMass, double f1, double f2, double f3, double f4, double f5, double delta,
            double errortolerance, double distance, double noise, boolean decharge, String modus) {
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
        this.f4 = f4;
        this.f5 = f5;
        this.delta = delta;
        this.errortolerance = errortolerance;
        this.distance = distance;
        this.noise = noise;
        this.decharge = decharge;
        this.aaMass = aaMass;
        this.modus = modus;

        for (Double x : this.aaMass) {
            this.aaMassDividedTwo.add(x / 2);
            this.aaMassDividedThree.add(x / 3);
        }

        this.minimum = Collections.min(this.aaMassDividedThree);
        this.maximum = Collections.max(this.aaMass);
    }

    @Override
    public String toString() {
        return "Configuration,Value\nF1," + f1 + "\nF2," + f2 + "\nF3," + f3 + "\nF4," + f4 + "\nF5," + f5
                + "\nDistance," + distance + "\nErrortolerance," + errortolerance + "\nDelta," + delta + "\nNoise,"
                + noise + "\nDecharge," + decharge + "\nModus," + modus + "\nH Mass," + H_MASS + "\nNH3 Mass,"
                + NH3_MASS + "\nH2O Mass," + H2O_MASS + "\nNH Mass," + NH_MASS + "\nCO Mass," + CO_MASS
                + "\nPhenylalanine Mass," + PHE_MASS + "\nAspartic Acid Mass," + ASP_MASS + "\nUpdated Averagine Mass,"
                + AVE_UPDATED_MASS + "\nAmino Acid Masses," + print(aaMass);
    }

    private String print(List<Double> aaMass) {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append("[");
        for (Double d : aaMass) {
            stringbuilder.append(d).append(" ");
        }
        stringbuilder.append(" ]");

        return stringbuilder.toString();
    }
}
