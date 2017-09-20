package ch.fgcz.proteomics.fdbm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-19
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Score {
    private final static List<Double> AA_MASS = Arrays.asList(71.03711, 156.10111, 114.04293, 115.02694, 103.00919, 129.04259, 128.05858, 57.02146, 137.05891, 113.08406, 113.08406, 128.09496,
            131.04049, 147.06841, 97.05276, 87.03203, 101.04768, 186.07931, 163.06333, 99.06841);
    private final static double H_MASS = 1.008;
    private final static double NH3_MASS = 17.03052;
    private final static double H2O_MASS = 18.01528;
    private final static double NH_MASS = 15.01464;
    private final static double CO_MASS = 28.0101;

    private static double score(Peak x, Peak y, double error, double mspepmass, double mscharge, IsotopicCluster icofx) {
        return 0.8 * firstNonintensityFeature(x, y, error) + 0.5 * secondNonintensityFeature(x, y, error, mspepmass, mscharge, icofx)
                + 0.1 * thirdNonintensityFeature(x, y, error + 0.1 * fifthIntensityFeature(icofx)) + 0.1 * fourthNonintensityFeature(x, y, error);

    }

    private static double diff1(Peak x, Peak y) {
        return x.getMz() - y.getMz();
    }

    private static double diff2(Peak x, Peak y) {
        return x.getMz() - ((y.getMz() + H_MASS) / 2);
    }

    private static double diff3(Peak x, Peak y) {
        return x.getMz() - ((y.getMz() + (2 * H_MASS)) / 3);
    }

    private static double diff4(Peak x, Peak y) {
        return x.getMz() - (((y.getMz() * 2) + H_MASS) / 3);
    }

    private static double sum1(Peak x, Peak y) {
        return x.getMz() + y.getMz();
    }

    private static double sum2(Peak x, Peak y) {
        return x.getMz() + ((y.getMz() + H_MASS) / 2);
    }

    private static double sum3(Peak x, Peak y) {
        return x.getMz() + ((y.getMz() + (2 * H_MASS)) / 3);
    }

    private static double sum4(Peak x, Peak y) {
        return x.getMz() + (((y.getMz() * 2) + H_MASS) / 3);
    }

    private static int firstNonintensityFeature(Peak x, Peak y, double e) {
        List<Peak> F1 = new ArrayList<>();

        for (Double aa : AA_MASS) {
            if (aa - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < aa + e) {
                F1.add(y);
            } else if (aa / 2 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < aa / 2 + e) {
                F1.add(y);
            } else if (aa / 3 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < aa / 3 + e) {
                F1.add(y);
            } else if (aa / 2 - e < Math.abs(diff2(x, y)) && Math.abs(diff2(x, y)) < aa / 2 + e) {
                F1.add(y);
            } else if (aa / 2 - e < Math.abs(diff2(y, x)) && Math.abs(diff2(y, x)) < aa / 2 + e) {
                F1.add(y);
            } else if (aa / 3 - e < Math.abs(diff3(x, y)) && Math.abs(diff3(x, y)) < aa / 3 + e) {
                F1.add(y);
            } else if (aa / 3 - e < Math.abs(diff3(y, x)) && Math.abs(diff3(y, x)) < aa / 3 + e) {
                F1.add(y);
            } else if (aa / 3 - e < Math.abs(diff4(x, y)) && Math.abs(diff4(x, y)) < aa / 3 + e) {
                F1.add(y);
            } else if (aa / 3 - e < Math.abs(diff4(y, x)) && Math.abs(diff4(y, x)) < aa / 3 + e) {
                F1.add(y);
            }
        }

        return F1.size();
    }

    private static int secondNonintensityFeature(Peak x, Peak y, double e, double pepmass, double charge, IsotopicCluster ic) {
        List<Peak> F2 = new ArrayList<>();
        double M = pepmass * charge;

        int i = 1;
        for (Peak c : ic.getIsotopicCluster()) {
            if (c.getMz() == x.getMz() && c.getIntensity() == x.getIntensity()) {
                break;
            }
            i++;
        }

        if (M + 2 * i + 2 * H_MASS - e < sum1(x, y) && sum1(x, y) < M + 2 * i + 2 * H_MASS + e) {
            F2.add(y);
        } else if ((M + 2 * i) / 2 + 2 * H_MASS - e < sum1(x, y) && sum1(x, y) < (M + 2 * i) / 2 + 2 * H_MASS + e) {
            F2.add(y);
        } else if ((M + 2 * i) / 3 + 2 * H_MASS - e < sum1(x, y) && sum1(x, y) < (M + 2 * i) / 3 + 2 * H_MASS + e) {
            F2.add(y);
        } else if ((M + 2 * i) / 2 + 2 * H_MASS - e < sum2(x, y) && sum2(x, y) < (M + 2 * i) / 2 + 2 * H_MASS + e) {
            F2.add(y);
        } else if ((M + 2 * i) / 2 + 2 * H_MASS - e < sum2(y, x) && sum2(y, x) < (M + 2 * i) / 2 + 2 * H_MASS + e) {
            F2.add(y);
        } else if ((M + 2 * i) / 3 + 2 * H_MASS - e < sum3(x, y) && sum3(x, y) < (M + 2 * i) / 3 + 2 * H_MASS + e) {
            F2.add(y);
        } else if ((M + 2 * i) / 3 + 2 * H_MASS - e < sum3(y, x) && sum3(y, x) < (M + 2 * i) / 3 + 2 * H_MASS + e) {
            F2.add(y);
        } else if ((M + 2 * i) / 3 + 2 * H_MASS - e < sum4(x, y) && sum4(x, y) < (M + 2 * i) / 3 + 2 * H_MASS + e) {
            F2.add(y);
        } else if ((M + 2 * i) / 3 + 2 * H_MASS - e < sum4(y, x) && sum4(y, x) < (M + 2 * i) / 3 + 2 * H_MASS + e) {
            F2.add(y);
        }

        return F2.size();
    }

    private static int thirdNonintensityFeature(Peak x, Peak y, double e) {
        List<Peak> F3 = new ArrayList<>();

        if (H2O_MASS - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < H2O_MASS + e) {
            F3.add(y);
        } else if (NH3_MASS - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < NH3_MASS + e) {
            F3.add(y);
        } else if (H2O_MASS / 2 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < H2O_MASS / 2 + e) {
            F3.add(y);
        } else if (NH3_MASS / 2 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < NH3_MASS / 2 + e) {
            F3.add(y);
        } else if (H2O_MASS / 3 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < H2O_MASS / 3 + e) {
            F3.add(y);
        } else if (NH3_MASS / 3 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < NH3_MASS / 3 + e) {
            F3.add(y);
        } else if (H2O_MASS / 2 - e < Math.abs(diff2(x, y)) && Math.abs(diff2(x, y)) < H2O_MASS / 2 + e) {
            F3.add(y);
        } else if (NH3_MASS / 2 - e < Math.abs(diff2(x, y)) && Math.abs(diff2(x, y)) < NH3_MASS / 2 + e) {
            F3.add(y);
        } else if (H2O_MASS / 2 - e < Math.abs(diff2(y, x)) && Math.abs(diff2(y, x)) < H2O_MASS / 2 + e) {
            F3.add(y);
        } else if (NH3_MASS / 2 - e < Math.abs(diff2(y, x)) && Math.abs(diff2(y, x)) < NH3_MASS / 2 + e) {
            F3.add(y);
        } else if (H2O_MASS / 3 - e < Math.abs(diff3(x, y)) && Math.abs(diff3(x, y)) < H2O_MASS / 3 + e) {
            F3.add(y);
        } else if (NH3_MASS / 3 - e < Math.abs(diff3(x, y)) && Math.abs(diff3(x, y)) < NH3_MASS / 3 + e) {
            F3.add(y);
        } else if (H2O_MASS / 3 - e < Math.abs(diff3(y, x)) && Math.abs(diff3(y, x)) < H2O_MASS / 3 + e) {
            F3.add(y);
        } else if (NH3_MASS / 3 - e < Math.abs(diff3(y, x)) && Math.abs(diff3(y, x)) < NH3_MASS / 3 + e) {
            F3.add(y);
        } else if (H2O_MASS / 3 - e < Math.abs(diff4(x, y)) && Math.abs(diff4(x, y)) < H2O_MASS / 3 + e) {
            F3.add(y);
        } else if (NH3_MASS / 3 - e < Math.abs(diff4(x, y)) && Math.abs(diff4(x, y)) < NH3_MASS / 3 + e) {
            F3.add(y);
        } else if (H2O_MASS / 3 - e < Math.abs(diff4(y, x)) && Math.abs(diff4(y, x)) < H2O_MASS / 3 + e) {
            F3.add(y);
        } else if (NH3_MASS / 3 - e < Math.abs(diff4(y, x)) && Math.abs(diff4(y, x)) < NH3_MASS / 3 + e) {
            F3.add(y);
        }

        return F3.size();
    }

    private static int fourthNonintensityFeature(Peak x, Peak y, double e) {
        List<Peak> F4 = new ArrayList<>();

        if (NH_MASS - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < NH_MASS + e) {
            F4.add(y);
        } else if (CO_MASS - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < CO_MASS + e) {
            F4.add(y);
        } else if (NH_MASS / 2 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < NH_MASS / 2 + e) {
            F4.add(y);
        } else if (CO_MASS / 2 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < CO_MASS / 2 + e) {
            F4.add(y);
        } else if (NH_MASS / 3 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < NH_MASS / 3 + e) {
            F4.add(y);
        } else if (CO_MASS / 3 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < CO_MASS / 3 + e) {
            F4.add(y);
        } else if (NH_MASS / 2 - e < Math.abs(diff2(x, y)) && Math.abs(diff2(x, y)) < NH_MASS / 2 + e) {
            F4.add(y);
        } else if (CO_MASS / 2 - e < Math.abs(diff2(x, y)) && Math.abs(diff2(x, y)) < CO_MASS / 2 + e) {
            F4.add(y);
        } else if (NH_MASS / 2 - e < Math.abs(diff2(y, x)) && Math.abs(diff2(y, x)) < NH_MASS / 2 + e) {
            F4.add(y);
        } else if (CO_MASS / 2 - e < Math.abs(diff2(y, x)) && Math.abs(diff2(y, x)) < CO_MASS / 2 + e) {
            F4.add(y);
        } else if (NH_MASS / 3 - e < Math.abs(diff3(x, y)) && Math.abs(diff3(x, y)) < NH_MASS / 3 + e) {
            F4.add(y);
        } else if (CO_MASS / 3 - e < Math.abs(diff3(x, y)) && Math.abs(diff3(x, y)) < CO_MASS / 3 + e) {
            F4.add(y);
        } else if (NH_MASS / 3 - e < Math.abs(diff3(y, x)) && Math.abs(diff3(y, x)) < NH_MASS / 3 + e) {
            F4.add(y);
        } else if (CO_MASS / 3 - e < Math.abs(diff3(y, x)) && Math.abs(diff3(y, x)) < CO_MASS / 3 + e) {
            F4.add(y);
        } else if (NH_MASS / 3 - e < Math.abs(diff4(x, y)) && Math.abs(diff4(x, y)) < NH_MASS / 3 + e) {
            F4.add(y);
        } else if (CO_MASS / 3 - e < Math.abs(diff4(x, y)) && Math.abs(diff4(x, y)) < CO_MASS / 3 + e) {
            F4.add(y);
        } else if (NH_MASS / 3 - e < Math.abs(diff4(y, x)) && Math.abs(diff4(y, x)) < NH_MASS / 3 + e) {
            F4.add(y);
        } else if (CO_MASS / 3 - e < Math.abs(diff4(y, x)) && Math.abs(diff4(y, x)) < CO_MASS / 3 + e) {
            F4.add(y);
        }

        return F4.size();
    }

    // NOT FINISHED YET
    private static int fifthIntensityFeature(IsotopicCluster ic) {
        List<Peak> F5 = new ArrayList<>();
        double T_MIN = 0; // Missing because don't know how to calculate
        double T_MEAN = 0; // Missing because don't know how to calculate
        double T_MEAN_OVERLAP = 0; // Missing because don't know how to calculate
        double T_MAX = 0; // Missing because don't know how to calculate
        double threshold = 0.3;
        // Peak x = ic.getIsotopicCluster().get(0);
        // Peak y1 = ic.getIsotopicCluster().get(1);
        // if (ic.getIsotopicCluster().size() == 3) {
        // Peak y2 = ic.getIsotopicCluster().get(2);
        // }

        for (Peak p : ic.getIsotopicCluster()) {
            // if black
            if (Math.min(Math.abs(p.getIntensity() - T_MIN), Math.abs(p.getIntensity() - T_MAX)) / T_MEAN <= threshold) {
                F5.add(p);
            }
            // if red
            if (Math.min(Math.abs((p.getIntensity() - T_MEAN_OVERLAP) - T_MIN), Math.abs((p.getIntensity() - T_MEAN_OVERLAP) - T_MAX)) / T_MEAN <= threshold) {
                F5.add(p);
            }
        }

        return F5.size();
    }
}
