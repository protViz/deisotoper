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
    private List<Double> AA_MASS = new ArrayList<Double>();
    private List<Double> AA_MASS2 = new ArrayList<>();
    private List<Double> AA_MASS3 = new ArrayList<>();
    private double min = 0;
    private double max = Double.MAX_VALUE;
    private double FM1 = 0.8;
    private double FM2 = 0.5;
    private double FM3 = 0.1;
    private double FM4 = 0.1;
    private double FM5 = 0.1;
    private double Distance = 1.003;
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

    public void setDistance(double dISTANCE_BETWEEN_ISOTOPIC_PEAKS) {
	Distance = dISTANCE_BETWEEN_ISOTOPIC_PEAKS;
    }

    public double getNH_MASSd2() {
	return NH_MASSd2;
    }

    public double getDistance() {
	return Distance;
    }

    public double getFM1() {
	return FM1;
    }

    public void setFM1(double fM1) {
	FM1 = fM1;
    }

    public double getFM2() {
	return FM2;
    }

    public void setFM2(double fM2) {
	FM2 = fM2;
    }

    public double getFM3() {
	return FM3;
    }

    public void setFM3(double fM3) {
	FM3 = fM3;
    }

    public double getFM4() {
	return FM4;
    }

    public void setFM4(double fM4) {
	FM4 = fM4;
    }

    public double getFM5() {
	return FM5;
    }

    public void setFM5(double fM5) {
	FM5 = fM5;
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
	return min;
    }

    public void setMin(double min) {
	this.min = min;
    }

    public double getMax() {
	return max;
    }

    public void setMax(double max) {
	this.max = max;
    }

    public List<Double> getAA_MASS() {
	return AA_MASS;
    }

    public void setAA_MASS(List<Double> aA_MASS) {
	AA_MASS = aA_MASS;
    }

    public List<Double> getAA_MASS2() {
	return AA_MASS2;
    }

    public void setAA_MASS2(List<Double> aA_MASS2) {
	AA_MASS2 = aA_MASS2;
    }

    public List<Double> getAA_MASS3() {
	return AA_MASS3;
    }

    public void setAA_MASS3(List<Double> aA_MASS3) {
	AA_MASS3 = aA_MASS3;
    }

    public Configuration() {
	this(Arrays.asList(71.03711, 156.10111, 114.04293, 115.02694, 103.00919, 129.04259, 128.05858, 57.02146,
		137.05891, 113.08406, 113.08406, 128.09496, 131.04049, 147.06841, 97.05276, 87.03203, 101.04768,
		186.07931, 163.06333, 99.06841), 0.8, 0.5, 0.1, 0.1, 0.1, 0.003, 0.3, 1.003, 0, false, "first");
    }

    public Configuration(double F1, double F2, double F3, double F4, double F5, double DELTA, double ERRORTOLERANCE,
	    double DISTANCE, double NOISE, boolean DECHARGE, String MODUS) {
	this(Arrays.asList(71.03711, 156.10111, 114.04293, 115.02694, 103.00919, 129.04259, 128.05858, 57.02146,
		137.05891, 113.08406, 113.08406, 128.09496, 131.04049, 147.06841, 97.05276, 87.03203, 101.04768,
		186.07931, 163.06333, 99.06841), F1, F2, F3, F4, F5, DELTA, ERRORTOLERANCE, DISTANCE, NOISE, DECHARGE,
		MODUS);
    }

    public Configuration(List<Double> AA_MASS, double F1, double F2, double F3, double F4, double F5, double DELTA,
	    double ERRORTOLERANCE, double DISTANCE, double NOISE, boolean DECHARGE, String MODUS) {
	this.FM1 = F1;
	this.FM2 = F2;
	this.FM3 = F3;
	this.FM4 = F4;
	this.FM5 = F5;
	this.delta = DELTA;
	this.errortolerance = ERRORTOLERANCE;
	this.Distance = DISTANCE;
	this.noise = NOISE;
	this.decharge = DECHARGE;
	this.AA_MASS = AA_MASS;
	this.modus = MODUS;

	for (Double x : this.AA_MASS) {
	    this.AA_MASS2.add(x / 2);
	    this.AA_MASS3.add(x / 3);
	}

	this.min = Collections.min(this.AA_MASS3);
	this.max = Collections.max(this.AA_MASS);
    }

    @Override
    public String toString() {
	return "Configuration [AA_MASS=" + AA_MASS + ", AA_MASS2=" + AA_MASS2 + ", AA_MASS3=" + AA_MASS3 + ", min="
		+ min + ", max=" + max + ", FM1=" + FM1 + ", FM2=" + FM2 + ", FM3=" + FM3 + ", FM4=" + FM4 + ", FM5="
		+ FM5 + ", Distance=" + Distance + ", errortolerance=" + errortolerance + ", delta=" + delta
		+ ", noise=" + noise + ", decharge=" + decharge + ", H_MASS=" + H_MASS + ", NH3_MASS=" + NH3_MASS
		+ ", H2O_MASS=" + H2O_MASS + ", NH_MASS=" + NH_MASS + ", CO_MASS=" + CO_MASS + ", PHE_MASS=" + PHE_MASS
		+ ", ASP_MASS=" + ASP_MASS + ", AVE_UPDATED_MASS=" + AVE_UPDATED_MASS + ", H_MASSx2=" + H_MASSx2
		+ ", H2O_MASSd2=" + H2O_MASSd2 + ", H2O_MASSd3=" + H2O_MASSd3 + ", NH3_MASSd2=" + NH3_MASSd2
		+ ", NH3_MASSd3=" + NH3_MASSd3 + ", NH_MASSd2=" + NH_MASSd2 + ", NH_MASSd3=" + NH_MASSd3
		+ ", CO_MASSd2=" + CO_MASSd2 + ", CO_MASSd3=" + CO_MASSd3 + "]";
    }
}
