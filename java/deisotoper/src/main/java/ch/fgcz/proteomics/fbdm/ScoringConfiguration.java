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

    // TODO (LS) merge into one function - and check that they add up to 1.
    // LS: Check is useless.
    double getF(int f);

    void setF(int f, double value);
    // TODO (LS)

    double getH2oMass(int charge);

    double getNh3Mass(int charge);
}
