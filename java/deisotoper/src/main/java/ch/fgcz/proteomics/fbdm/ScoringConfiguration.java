package ch.fgcz.proteomics.fbdm;

import java.util.List;

public interface ScoringConfiguration {
    double getH_MASS();


    double getErrorTolerance();

    double getNH(int charge);

    double getCO_MASS();

    double getCO_MASS_DIVIDED_TWO();

    double getCO_MASS_DIVIDED_THREE();

    double getIsotopicPeakDistance();

    double getH_MASS_MULTIPLIED_TWO();

    double getMin();

    double getMax();

    List<Double> getAaMass();

    List<Double>  getAaMassDividedTwo();

    List<Double>  getAaMassDividedThree();

    double getF1();

    double getF2();

    double getF3();

    double getF4();

    double getH2O_MASS();

    double getNH3_MASS();

    double getH2O_MASS_DIVIDED_TWO();

    double getNH3_MASS_DIVIDED_TWO();

    double getH2O_MASS_DIVIDED_THREE();

    double getNH3_MASS_DIVIDED_THREE();
}
