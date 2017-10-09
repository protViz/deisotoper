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
import java.util.List;

public class ScoreConfig {
    private List<Double> AA_MASS = new ArrayList<>();

    public List<Double> getAA_MASS() {
        return AA_MASS;
    }

    public void setAA_MASS(List<Double> aA_MASS) {
        AA_MASS = aA_MASS;
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
    }
}