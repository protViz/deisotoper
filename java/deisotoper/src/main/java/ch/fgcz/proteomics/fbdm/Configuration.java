package ch.fgcz.proteomics.fbdm;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Configuration implements ScoringConfiguration {
    private Map<String, Double> aaMass = new HashMap<String, Double>();
    private Map<String, Double> aaMassDividedTwo = new HashMap<String, Double>();
    private Map<String, Double> aaMassDividedThree = new HashMap<String, Double>();
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

    @Override
    public double getF(int f) {
        switch (f) {
        case 1:
            return this.f1;
        case 2:
            return this.f2;
        case 3:
            return this.f3;
        case 4:
            return this.f4;
        case 5:
            return this.f5;
        default:
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void setF(int f, double value) {
        switch (f) {
        case 1:
            this.f1 = value;
            break;
        case 2:
            this.f2 = value;
            break;
        case 3:
            this.f3 = value;
            break;
        case 4:
            this.f4 = value;
            break;
        case 5:
            this.f5 = value;
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

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
    public Map<String, Double> getAaMass() {
        return aaMass;
    }

    @Override
    public Map<String, Double> getAaMassDividedTwo() {
        return aaMassDividedTwo;
    }

    @Override
    public Map<String, Double> getAaMassDividedThree() {
        return aaMassDividedThree;
    }

    public Configuration() {
        this(0.003, 0.3, 1.00048, 0, false, "first");
    }

    public Configuration(double delta, double errortolerance, double distance, double noise, boolean decharge,
            String modus) {
        this(initializeStandartAminoAcidMasses(), delta, errortolerance, distance, noise, decharge, modus);
    }

    public Configuration(Map<String, Double> aaMass, double delta, double errortolerance, double distance, double noise,
            boolean decharge, String modus) {
        this.delta = delta;
        this.errortolerance = errortolerance;
        this.distance = distance;
        this.noise = noise;
        this.decharge = decharge;
        this.aaMass = aaMass;
        this.modus = modus;

        for (Map.Entry<String, Double> entry : aaMass.entrySet()) {
            this.aaMassDividedTwo.put(entry.getKey(), entry.getValue() / 2);
            this.aaMassDividedThree.put(entry.getKey(), entry.getValue() / 3);
        }

        this.minimum = Collections.min(this.aaMassDividedThree.values());
        this.maximum = Collections.max(this.aaMass.values());
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

    private String print(Map<String, Double> aaMass) {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append("[");

        for (Map.Entry<String, Double> entry : aaMass.entrySet()) {
            stringbuilder.append(" " + entry.getKey() + "=" + entry.getValue());
        }

        stringbuilder.append(" ]");

        return stringbuilder.toString();
    }

    private static Map<String, Double> initializeStandartAminoAcidMasses() {
        Map<String, Double> aaMass = new HashMap<String, Double>();
        aaMass.put("A", 71.03711);
        aaMass.put("R", 156.10111);
        aaMass.put("N", 114.04293);
        aaMass.put("D", 115.02694);
        aaMass.put("C", 103.00919);
        aaMass.put("E", 129.04259);
        aaMass.put("Q", 128.05858);
        aaMass.put("G", 57.02146);
        aaMass.put("H", 137.05891);
        aaMass.put("I", 113.08406);
        aaMass.put("L", 113.08406);
        aaMass.put("K", 128.09496);
        aaMass.put("M", 131.04049);
        aaMass.put("F", 147.06841);
        aaMass.put("P", 97.05276);
        aaMass.put("S", 87.03203);
        aaMass.put("T", 101.04768);
        aaMass.put("W", 186.07931);
        aaMass.put("Y", 163.06333);
        aaMass.put("V", 99.06841);

        return aaMass;
    }
}
