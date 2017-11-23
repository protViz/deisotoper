package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-10-09
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Configuration {
    private List<Double> aaMass = new ArrayList<Double>();
    private List<Double> aaMassDividedTwo = new ArrayList<>();
    private List<Double> aaMassDividedThree = new ArrayList<>();
    private double minimum = 0;
    private double maximum = Double.MAX_VALUE;
    private double F1 = 0.8;
    private double F2 = 0.5;
    private double F3 = 0.1;
    private double F4 = 0.1;
    private double F5 = 0.1;
    private double distance = 1.003;
    private double errortolerance = 0.3;
    private double delta = 0.003;
    private double noise = 0;
    private boolean decharge = false;
    private String modus = "first";
    private final double H_MASS = 1.008;
    private final double NH3_MASS = 17.03052;
    private final double H2O_MASS = 18.01528;
    private final double NH_MASS = 15.01464;
    private final double CO_MASS = 28.0101;
    private final double PHE_MASS = 165.192;
    private final double ASP_MASS = 133.104;
    private final double AVE_UPDATED_MASS = 111.125;
    private final double H_MASS_MULTIPLIED_TWO = 2 * H_MASS;
    private final double H2O_MASS_DIVIDED_TWO = H2O_MASS / 2;
    private final double H2O_MASS_DIVIDED_THREE = H2O_MASS / 3;
    private final double NH3_MASS_DIVIDED_TWO = NH3_MASS / 2;
    private final double NH3_MASS_DIVIDED_THREE = NH3_MASS / 3;
    private final double NH_MASS_DIVIDED_TWO = NH_MASS / 2;
    private final double NH_MASS_DIVIDED_THREE = NH_MASS / 2;
    private final double CO_MASS_DIVIDED_TWO = CO_MASS / 2;
    private final double CO_MASS_DIVIDED_THREE = CO_MASS / 3;

    public boolean isDecharge() {
        return decharge;
    }

    public String getModus() {
        return modus;
    }

    public double getErrortolerance() {
        return errortolerance;
    }

    public double getDelta() {
        return delta;
    }

    public double getNoise() {
        return noise;
    }

    public double getNH_MASS_DIVIDED_TWO() {
        return NH_MASS_DIVIDED_TWO;
    }

    public double getDistance() {
        return distance;
    }

    public double getF1() {
        return F1;
    }

    public double getF2() {
        return F2;
    }

    public double getF3() {
        return F3;
    }

    public double getF4() {
        return F4;
    }

    public double getF5() {
        return F5;
    }

    public double getNH_MASS_DIVIDED_THREE() {
        return NH_MASS_DIVIDED_THREE;
    }

    public double getCO_MASS_DIVIDED_TWO() {
        return CO_MASS_DIVIDED_TWO;
    }

    public double getCO_MASS_DIVIDED_THREE() {
        return CO_MASS_DIVIDED_THREE;
    }

    public double getH2O_MASS_DIVIDED_TWO() {
        return H2O_MASS_DIVIDED_TWO;
    }

    public double getH2O_MASS_DIVIDED_THREE() {
        return H2O_MASS_DIVIDED_THREE;
    }

    public double getNH3_MASS_DIVIDED_TWO() {
        return NH3_MASS_DIVIDED_TWO;
    }

    public double getNH3_MASS_DIVIDED_THREE() {
        return NH3_MASS_DIVIDED_THREE;
    }

    public double getH_MASS_MULTIPLIED_TWO() {
        return H_MASS_MULTIPLIED_TWO;
    }

    public double getH_MASS() {
        return H_MASS;
    }

    public double getNH3_MASS() {
        return NH3_MASS;
    }

    public double getH2O_MASS() {
        return H2O_MASS;
    }

    public double getNH_MASS() {
        return NH_MASS;
    }

    public double getCO_MASS() {
        return CO_MASS;
    }

    public double getPHE_MASS() {
        return PHE_MASS;
    }

    public double getASP_MASS() {
        return ASP_MASS;
    }

    public double getAVE_UPDATED_MASS() {
        return AVE_UPDATED_MASS;
    }

    public double getMin() {
        return minimum;
    }

    public double getMax() {
        return maximum;
    }

    public List<Double> getAaMass() {
        return aaMass;
    }

    public List<Double> getAaMassDividedTwo() {
        return aaMassDividedTwo;
    }

    public List<Double> getAaMassDividedThree() {
        return aaMassDividedThree;
    }

    public Configuration() {
        this(Arrays.asList(71.03711, 156.10111, 114.04293, 115.02694, 103.00919, 129.04259, 128.05858, 57.02146,
                137.05891, 113.08406, 113.08406, 128.09496, 131.04049, 147.06841, 97.05276, 87.03203, 101.04768,
                186.07931, 163.06333, 99.06841), 0.8, 0.5, 0.1, 0.1, 0.1, 0.003, 0.3, 1.003, 0, false, "first");
    }

    public Configuration(double F1, double F2, double F3, double F4, double F5, double delta, double errortolerance,
            double distance, double noise, boolean decharge, String modus) {
        this(Arrays.asList(71.03711, 156.10111, 114.04293, 115.02694, 103.00919, 129.04259, 128.05858, 57.02146,
                137.05891, 113.08406, 113.08406, 128.09496, 131.04049, 147.06841, 97.05276, 87.03203, 101.04768,
                186.07931, 163.06333, 99.06841), F1, F2, F3, F4, F5, delta, errortolerance, distance, noise, decharge,
                modus);
    }

    public Configuration(List<Double> aaMass, double F1, double F2, double F3, double F4, double F5, double delta,
            double errortolerance, double distance, double noise, boolean decharge, String modus) {
        this.F1 = F1;
        this.F2 = F2;
        this.F3 = F3;
        this.F4 = F4;
        this.F5 = F5;
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
        return "Configuration,Value\nF1," + F1 + "\nF2," + F2 + "\nF3," + F3 + "\nF4," + F4 + "\nF5," + F5
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
