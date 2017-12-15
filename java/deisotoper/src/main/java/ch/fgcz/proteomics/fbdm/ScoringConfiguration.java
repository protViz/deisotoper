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

    double getF1();

    double getF2();

    double getF3();

    double getF4();

    double getF5();

    void setF1(double f1);

    void setF2(double f2);

    void setF3(double f3);

    void setF4(double f4);

    void setF5(double f5);

    double getH2oMass(int charge);

    double getNh3Mass(int charge);
}
