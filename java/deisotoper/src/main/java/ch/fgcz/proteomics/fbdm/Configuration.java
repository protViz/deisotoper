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
    private List<Double> aaMassd2 = new ArrayList<>();
    private List<Double> aaMassd3 = new ArrayList<>();
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
    private final double H_MASSx2 = 2 * H_MASS;
    private final double H2O_MASSd2 = H2O_MASS / 2;
    private final double H2O_MASSd3 = H2O_MASS / 3;
    private final double NH3_MASSd2 = NH3_MASS / 2;
    private final double NH3_MASSd3 = NH3_MASS / 3;
    private final double NH_MASSd2 = NH_MASS / 2;
    private final double NH_MASSd3 = NH_MASS / 2;
    private final double CO_MASSd2 = CO_MASS / 2;
    private final double CO_MASSd3 = CO_MASS / 3;

    // public Map<String, Object> getScoreConfigAsMap() {
    // Map<String, Object> configmap = new HashMap<>();
    //
    // // final
    // configmap.put("H_MASS", this.H_MASS);
    // configmap.put("NH3_MASS", this.NH3_MASS);
    // configmap.put("H2O_MASS", this.H2O_MASS);
    // configmap.put("NH_MASS", this.NH_MASS);
    // configmap.put("CO_MASS", this.CO_MASS);
    // configmap.put("PHE_MASS", this.PHE_MASS);
    // configmap.put("ASP_MASS", this.ASP_MASS);
    // configmap.put("AVE_UPDATED_MASS", this.AVE_UPDATED_MASS);
    // configmap.put("H_MASSx2", this.H_MASSx2);
    // configmap.put("H2O_MASSd2", this.H2O_MASSd2);
    // configmap.put("H2O_MASSd3", this.H2O_MASSd3);
    // configmap.put("NH3_MASSd2", this.NH3_MASSd2);
    // configmap.put("NH3_MASSd3", this.NH3_MASSd3);
    // configmap.put("NH_MASSd2", this.NH_MASSd2);
    // configmap.put("NH_MASSd3", this.NH_MASSd3);
    // configmap.put("CO_MASSd2", this.CO_MASSd2);
    // configmap.put("CO_MASSd3", this.CO_MASSd3);
    //
    // // non final
    // configmap.put("DISTANCE_BETWEEN_ISOTOPIC_PEAKS", this.Distance);
    // configmap.put("F1", this.FM1);
    // configmap.put("F2", this.FM2);
    // configmap.put("F3", this.FM3);
    // configmap.put("F4", this.FM4);
    // configmap.put("F5", this.FM5);
    // configmap.put("AA_MASS", this.AA_MASS);
    // configmap.put("AA_MASSd2", this.AA_MASS2);
    // configmap.put("AA_MASSd3", this.AA_MASS3);
    //
    // return configmap;
    // }

    public boolean isDecharge() {
        return decharge;
    }

    public String getModus() {
        return modus;
    }

    public void setModus(String modus) {
        this.modus = modus;
    }

    public void setDecharge(boolean decharging) {
        this.decharge = decharging;
    }

    public double getErrortolerance() {
        return errortolerance;
    }

    public void setErrortolerance(double errortolerance) {
        this.errortolerance = errortolerance;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public double getNoise() {
        return noise;
    }

    public void setNoise(double noise) {
        this.noise = noise;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getNH_MASSd2() {
        return NH_MASSd2;
    }

    public double getDistance() {
        return distance;
    }

    public double getFM1() {
        return F1;
    }

    public void setFM1(double f1) {
        F1 = f1;
    }

    public double getFM2() {
        return F2;
    }

    public void setFM2(double f2) {
        F2 = f2;
    }

    public double getFM3() {
        return F3;
    }

    public void setFM3(double f3) {
        F3 = f3;
    }

    public double getFM4() {
        return F4;
    }

    public void setFM4(double f4) {
        F4 = f4;
    }

    public double getFM5() {
        return F5;
    }

    public void setFM5(double f5) {
        F5 = f5;
    }

    public double getNH_MASSd3() {
        return NH_MASSd3;
    }

    public double getCO_MASSd2() {
        return CO_MASSd2;
    }

    public double getCO_MASSd3() {
        return CO_MASSd3;
    }

    public double getH2O_MASSd2() {
        return H2O_MASSd2;
    }

    public double getH2O_MASSd3() {
        return H2O_MASSd3;
    }

    public double getNH3_MASSd2() {
        return NH3_MASSd2;
    }

    public double getNH3_MASSd3() {
        return NH3_MASSd3;
    }

    public double getH_MASSx2() {
        return H_MASSx2;
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

    public void setMin(double min) {
        this.minimum = min;
    }

    public double getMax() {
        return maximum;
    }

    public void setMax(double max) {
        this.maximum = max;
    }

    public List<Double> getAA_MASS() {
        return aaMass;
    }

    public void setAA_MASS(List<Double> aaMass) {
        this.aaMass = aaMass;
    }

    public List<Double> getAA_MASS2() {
        return aaMassd2;
    }

    public void setAA_MASS2(List<Double> aaMassd2) {
        this.aaMassd2 = aaMassd2;
    }

    public List<Double> getAA_MASS3() {
        return aaMassd3;
    }

    public void setAA_MASS3(List<Double> aaMassd3) {
        this.aaMassd3 = aaMassd3;
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
            this.aaMassd2.add(x / 2);
            this.aaMassd3.add(x / 3);
        }

        this.minimum = Collections.min(this.aaMassd3);
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
