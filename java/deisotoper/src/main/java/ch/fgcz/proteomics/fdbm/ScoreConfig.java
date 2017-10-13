package ch.fgcz.proteomics.fdbm;

/**
 * @author Lucas Schmidt
 * @since 2017-10-09
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreConfig {
    private List<Double> AA_MASS = new ArrayList<>();
    private List<Double> AA_MASS2 = new ArrayList<>();
    private List<Double> AA_MASS3 = new ArrayList<>();
    private double min = 0;
    private double max = Double.MAX_VALUE;
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
    // private final static List<Peak> PHE_PATTERN = Arrays.asList(new Peak(147.06842, 100), new Peak(148.07178, 10.2), new Peak(149.07513, 0.6));
    // private final static List<Peak> ASP_PATTERN = Arrays.asList(new Peak(115.02696, 100), new Peak(116.03032, 4.9), new Peak(117.0312, 0.7));
    // private final static List<Peak> AVE_UPDATED_PATTERN = Arrays.asList(new Peak(0, 0));

    public double getNH_MASSd2() {
        return NH_MASSd2;
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
        this.AA_MASS.removeAll(this.AA_MASS);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();

            while (line != null) {
                String[] parts = line.split("=");
                this.AA_MASS.add(Double.parseDouble(parts[1]));
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            this.AA_MASS.add(71.03711);
            this.AA_MASS.add(156.10111);
            this.AA_MASS.add(114.04293);
            this.AA_MASS.add(115.02694);
            this.AA_MASS.add(103.00919);
            this.AA_MASS.add(129.04259);
            this.AA_MASS.add(128.05858);
            this.AA_MASS.add(57.02146);
            this.AA_MASS.add(137.05891);
            this.AA_MASS.add(113.08406);
            this.AA_MASS.add(113.08406);
            this.AA_MASS.add(128.09496);
            this.AA_MASS.add(131.04049);
            this.AA_MASS.add(147.06841);
            this.AA_MASS.add(97.05276);
            this.AA_MASS.add(87.03203);
            this.AA_MASS.add(101.04768);
            this.AA_MASS.add(186.07931);
            this.AA_MASS.add(163.06333);
            this.AA_MASS.add(99.06841);
            System.err.println("WARNING: File not found, using standart amino acid masses instead!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Double x : this.AA_MASS) {
            this.AA_MASS2.add(x / 2);
            this.AA_MASS3.add(x / 3);
        }

        this.min = Collections.min(this.AA_MASS3);
        this.max = Collections.max(this.AA_MASS);
    }
}