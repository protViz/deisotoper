package ch.fgcz.proteomics.fbdm;

import java.util.Map;

public interface ScoringConfiguration {
    double getHMass(int multiplier);

    double getErrorTolerance();

    double getNhMass(int charge);

    double getCoMass(int charge);

    double getIsotopicPeakDistance();

    double getMin();

    double getMax();

    Map<String, Double> getAaMass();

    Map<String, Double> getAaMassDividedTwo();

    Map<String, Double> getAaMassDividedThree();

    double getF(int f);

    void setF(int f, double value);

    double getH2oMass(int charge);

    double getNh3Mass(int charge);
}
