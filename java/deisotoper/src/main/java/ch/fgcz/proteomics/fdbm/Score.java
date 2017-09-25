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
    private final static double PHE_MASS = 165.192;
    private final static double ASP_MASS = 133.104;
    private final static double AVE_UPDATED_MASS = 111.125;
    // private final static List<Peak> PHE_PATTERN = Arrays.asList(new Peak(147.06842, 100), new Peak(148.07178, 10.2), new Peak(149.07513, 0.6));
    // private final static List<Peak> ASP_PATTERN = Arrays.asList(new Peak(115.02696, 100), new Peak(116.03032, 4.9), new Peak(117.0312, 0.7));
    // private final static List<Peak> AVE_UPDATED_PATTERN = Arrays.asList(new Peak(0, 0));

    public static double score(Peak x, Peak y, double error, double mspepmass, double mscharge, IsotopicCluster icofx, Connection e, IsotopicClusterGraph ICG) {
        return 0.8 * firstNonintensityFeature(x, y, error) + 0.5 * secondNonintensityFeature(x, y, error, mspepmass, mscharge, icofx) + 0.1 * thirdNonintensityFeature(x, y, error)
                + 0.1 * fourthNonintensityFeature(x, y, error) + 0.1 * fifthIntensityFeature(e, ICG);
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

    // TODO (LS) write brief docu - you can also copy paste from paper.
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
    private static int fifthIntensityFeature(Connection e, IsotopicClusterGraph ICG) {
        List<Peak> F5 = new ArrayList<>();
        double threshold = 0.3;

        int i = 0;
        for (Peak p : ICG.getIsotopicclustergraph().getEdgeTarget(e).getIsotopicCluster()) {
            // System.out.println("PEAK: " + p.getMz() + " MZ, " + p.getIntensity() + " INTENSITY");
            double T_MIN = (p.getMz() / ASP_MASS) * p.getIntensity();
            // System.out.println("T_MIN: " + T_MIN);
            double T_MEAN = (p.getMz() / AVE_UPDATED_MASS) * p.getIntensity();
            // System.out.println("T_MEAN: " + T_MEAN);
            double T_MEAN_OVERLAP = 0;
            if (ICG.getIsotopicclustergraph().getEdgeSource(e).getIsotopicCluster() != null) {
                if (i < ICG.getIsotopicclustergraph().getEdgeSource(e).getIsotopicCluster().size()) {
                    T_MEAN_OVERLAP = (ICG.getIsotopicclustergraph().getEdgeSource(e).getIsotopicCluster().get(i).getMz() / AVE_UPDATED_MASS) * p.getIntensity();
                } else {
                    T_MEAN_OVERLAP = (ICG.getIsotopicclustergraph().getEdgeSource(e).getIsotopicCluster().get(ICG.getIsotopicclustergraph().getEdgeSource(e).getIsotopicCluster().size() - 1).getMz()
                            / AVE_UPDATED_MASS) * p.getIntensity();
                }
            }
            // System.out.println("T_MEAN_OVERLAP: " + T_MEAN_OVERLAP);
            double T_MAX = (p.getMz() / PHE_MASS) * p.getIntensity();
            // System.out.println("T_MAX: " + T_MAX);

            if (e.getColor() == "black") {
                // System.out.println("black: " + Math.min(Math.abs(p.getIntensity() - T_MIN), Math.abs(p.getIntensity() - T_MAX)) / T_MEAN);
                if (Math.min(Math.abs(p.getIntensity() - T_MIN), Math.abs(p.getIntensity() - T_MAX)) / T_MEAN <= threshold) {
                    F5.add(p);
                }
            }

            if (e.getColor() == "red") {
                // System.out.println("red: " + Math.min(Math.abs((p.getIntensity() - T_MEAN_OVERLAP) - T_MIN), Math.abs((p.getIntensity() - T_MEAN_OVERLAP) - T_MAX)));
                if (Math.min(Math.abs((p.getIntensity() - T_MEAN_OVERLAP) - T_MIN), Math.abs((p.getIntensity() - T_MEAN_OVERLAP) - T_MAX)) / T_MEAN <= threshold) {
                    F5.add(p);
                    // System.out.println("RED ADDED");
                }
            }
            i++;
        }

        // System.out.println("SCORE: " + F5.size());
        return F5.size();
    }
}
