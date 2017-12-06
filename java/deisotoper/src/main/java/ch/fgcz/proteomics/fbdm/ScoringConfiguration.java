package ch.fgcz.proteomics.fbdm;

import java.util.List;

public interface ScoringConfiguration {
    double getH_MASS(int multiplier);

    double getErrorTolerance();

    double getNH_MASS(int charge);

    double getCO_MASS(int charge);

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

    double getH2O_MASS(int charge);

    double getNH3_MASS(int charge);
}
