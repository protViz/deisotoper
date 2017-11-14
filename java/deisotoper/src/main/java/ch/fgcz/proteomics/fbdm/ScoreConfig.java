package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-10-09
 */

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ScoreConfig {
    @SuppressWarnings("serial")
    private List<Double> AA_MASS = new ArrayList<Double>() {
        {
            add(71.03711);
            add(156.10111);
            add(114.04293);
            add(115.02694);
            add(103.00919);
            add(129.04259);
            add(128.05858);
            add(57.02146);
            add(137.05891);
            add(113.08406);
            add(113.08406);
            add(128.09496);
            add(131.04049);
            add(147.06841);
            add(97.05276);
            add(87.03203);
            add(101.04768);
            add(186.07931);
            add(163.06333);
            add(99.06841);
        }
    };
    private List<Double> AA_MASS2 = new ArrayList<>();
    private List<Double> AA_MASS3 = new ArrayList<>();
    private double min = 0;
    private double max = Double.MAX_VALUE;
    private double FM1 = 0.8;
    private double FM2 = 0.5;
    private double FM3 = 0.1;
    private double FM4 = 0.1;
    private double FM5 = 0.1;
    private double DISTANCE_BETWEEN_ISOTOPIC_PEAKS = 1.003;
    private double errortolerance = 0.3;
    private double delta = 0.003;
    private double noise = 0;
    private boolean decharging = false;
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

    public Map<String, Object> getScoreConfigAsMap() {
        Map<String, Object> configmap = new HashMap<>();

        // final
        configmap.put("H_MASS", this.H_MASS);
        configmap.put("NH3_MASS", this.NH3_MASS);
        configmap.put("H2O_MASS", this.H2O_MASS);
        configmap.put("NH_MASS", this.NH_MASS);
        configmap.put("CO_MASS", this.CO_MASS);
        configmap.put("PHE_MASS", this.PHE_MASS);
        configmap.put("ASP_MASS", this.ASP_MASS);
        configmap.put("AVE_UPDATED_MASS", this.AVE_UPDATED_MASS);
        configmap.put("H_MASSx2", this.H_MASSx2);
        configmap.put("H2O_MASSd2", this.H2O_MASSd2);
        configmap.put("H2O_MASSd3", this.H2O_MASSd3);
        configmap.put("NH3_MASSd2", this.NH3_MASSd2);
        configmap.put("NH3_MASSd3", this.NH3_MASSd3);
        configmap.put("NH_MASSd2", this.NH_MASSd2);
        configmap.put("NH_MASSd3", this.NH_MASSd3);
        configmap.put("CO_MASSd2", this.CO_MASSd2);
        configmap.put("CO_MASSd3", this.CO_MASSd3);

        // non final
        configmap.put("DISTANCE_BETWEEN_ISOTOPIC_PEAKS", this.DISTANCE_BETWEEN_ISOTOPIC_PEAKS);
        configmap.put("F1", this.FM1);
        configmap.put("F2", this.FM2);
        configmap.put("F3", this.FM3);
        configmap.put("F4", this.FM4);
        configmap.put("F5", this.FM5);
        configmap.put("AA_MASS", this.AA_MASS);
        configmap.put("AA_MASSd2", this.AA_MASS2);
        configmap.put("AA_MASSd3", this.AA_MASS3);

        return configmap;
    }

    public boolean isDecharging() {
        return decharging;
    }

    public void setDecharging(boolean decharging) {
        this.decharging = decharging;
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

    public void setDISTANCE_BETWEEN_ISOTOPIC_PEAKS(double dISTANCE_BETWEEN_ISOTOPIC_PEAKS) {
        DISTANCE_BETWEEN_ISOTOPIC_PEAKS = dISTANCE_BETWEEN_ISOTOPIC_PEAKS;
    }

    public double getNH_MASSd2() {
        return NH_MASSd2;
    }

    public double getDISTANCE_BETWEEN_ISOTOPIC_PEAKS() {
        return DISTANCE_BETWEEN_ISOTOPIC_PEAKS;
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

    public ScoreConfig(String file) {
        Properties properties = new Properties();
        try (BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));) {
            properties.load(stream);
            stream.close();
            if (!properties.isEmpty()) {
                this.AA_MASS.removeAll(this.AA_MASS);
                this.AA_MASS2.removeAll(this.AA_MASS2);
                this.AA_MASS3.removeAll(this.AA_MASS3);
                System.out.println("SUCCESS: File found...");
            } else {
                System.err.println("WARNING: File is empty, using standart amino acid masses instead!");
            }
        } catch (FileNotFoundException e) {
            System.err.println("WARNING: File not found, using standart amino acid masses instead!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Enumeration<?> e = properties.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String value = properties.getProperty(key);
            double vdouble = Double.parseDouble(value);

            if (key.equals("F1")) {
                this.FM1 = vdouble;
                System.out.println("F1 set to " + vdouble);
            } else if (key.equals("F2")) {
                this.FM2 = vdouble;
                System.out.println("F2 set to " + vdouble);
            } else if (key.equals("F3")) {
                this.FM3 = vdouble;
                System.out.println("F3 set to " + vdouble);
            } else if (key.equals("F4")) {
                this.FM4 = vdouble;
                System.out.println("F4 set to " + vdouble);
            } else if (key.equals("F5")) {
                this.FM5 = vdouble;
                System.out.println("F5 set to " + vdouble);
            } else if (key.equals("DISTANCE")) {
                this.DISTANCE_BETWEEN_ISOTOPIC_PEAKS = vdouble;
                System.out.println("DISTANCE set to " + vdouble);
            } else if (key.equals("DELTA")) {
                this.delta = vdouble;
                System.out.println("DELTA set to " + vdouble);
            } else if (key.equals("ERRORTOLERANCE")) {
                this.errortolerance = vdouble;
                System.out.println("ERRORTOLERANCE set to " + vdouble);
            } else if (key.equals("NOISE")) {
                this.noise = vdouble;
                System.out.println("NOISE set to " + vdouble);
            } else if (key.equals("DECHARGE")) {
                if (value.equals("1")) {
                    this.decharging = true;
                } else if (value.equals("0")) {
                    this.decharging = false;
                }
                System.out.println("DECHARGE set to " + this.decharging);
            } else {
                this.AA_MASS.add(vdouble);
            }
        }

        for (Double x : this.AA_MASS) {
            this.AA_MASS2.add(x / 2);
            this.AA_MASS3.add(x / 3);
        }

        this.min = Collections.min(this.AA_MASS3);
        this.max = Collections.max(this.AA_MASS);
    }
}
