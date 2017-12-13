package ch.fgcz.proteomics.fbdm;

import java.util.List;

public interface ScoringConfiguration {
    double getHMass(int multiplier);

    double getErrorTolerance();

    double getNhMass(int charge);

    double getCoMass(int charge);

    double getIsotopicPeakDistance();

    double getMin();

    double getMax();

    List<Double> getAaMass();

    List<Double> getAaMassDividedTwo();

    List<Double> getAaMassDividedThree();

    double getF1();

    double getF2();

    double getF3();

    double getF4();

    double getF5();

    double getH2oMass(int charge);

    double getNh3Mass(int charge);
}
