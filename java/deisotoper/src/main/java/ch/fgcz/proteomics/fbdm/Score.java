package ch.fgcz.proteomics.fbdm;

import java.util.List;

/**
 * @author Lucas Schmidt
 * @since 2017-09-19
 */

public class Score {
    private double peptidMass;
    private double charge;
    private Configuration config;

    // TODO: Remove dependency on Configuration.
    // LS: I wouldn't recommend to remove it, because there would be too much
    // parameters.
    public Score(double peptidMass, double charge, Configuration config) {
        this.peptidMass = peptidMass;
        this.charge = charge;
        this.config = config;
    }

    // TODO: Integrate 5 th score. Done, see IsotopicClusterGraph.
    public double calculateFirstSecondThirdAndFourthScore(Peak peakX, Peak peakY,
            IsotopicCluster isotopicClusterOfPeakX, Connection connection) {
        return this.config.getF1()
                * firstAminoAcidDistanceScore(peakX, peakY, config.getH_MASS(), config.getAaMass(),
                        config.getAaMassDividedTwo(), config.getAaMassDividedTwo(), config.getMin(), config.getMax(),
                        config.getErrortolerance())
                + this.config.getF2() * secondComplementaryMassScore(peakX, peakY, this.peptidMass, this.charge,
                        isotopicClusterOfPeakX, config.getH_MASS(), config.getH_MASS_MULTIPLIED_TWO(),
                        config.getErrortolerance(), config.getDistance())
                + this.config.getF3() * thirdSideChainLossScore(peakX, peakY, config.getH_MASS(), config.getH2O_MASS(),
                        config.getH2O_MASS_DIVIDED_TWO(), config.getH2O_MASS_DIVIDED_THREE(), config.getNH3_MASS(),
                        config.getNH3_MASS_DIVIDED_TWO(), config.getNH3_MASS_DIVIDED_THREE(),
                        config.getErrortolerance())
                + this.config.getF4() * fourthSupportiveAndZIonScore(peakX, peakY, config.getH_MASS(),
                        config.getNH_MASS(), config.getNH_MASS_DIVIDED_TWO(), config.getNH_MASS_DIVIDED_THREE(),
                        config.getCO_MASS(), config.getCO_MASS_DIVIDED_TWO(), config.getCO_MASS_DIVIDED_THREE(),
                        config.getErrortolerance());
    }

    public static double diff1(Peak x, Peak y) {
        return x.getMz() - y.getMz();
    }

    public static double diff2(Peak x, Peak y, double H_MASS) {
        return x.getMz() - ((y.getMz() + H_MASS) / 2);
    }

    public static double diff3(Peak x, Peak y, double H_MASS) {
        return x.getMz() - ((y.getMz() + (2 * H_MASS)) / 3);
    }

    public static double diff4(Peak x, Peak y, double H_MASS) {
        return x.getMz() - (((y.getMz() * 2) + H_MASS) / 3);
    }

    public static double sum1(Peak x, Peak y) {
        return x.getMz() + y.getMz();
    }

    public static double sum2(Peak x, Peak y, double H_MASS) {
        return x.getMz() + ((y.getMz() + H_MASS) / 2);
    }

    public static double sum3(Peak x, Peak y, double H_MASS) {
        return x.getMz() + ((y.getMz() + (2 * H_MASS)) / 3);
    }

    public static double sum4(Peak x, Peak y, double H_MASS) {
        return x.getMz() + (((y.getMz() * 2) + H_MASS) / 3);
    }

    // TODO : Look into paper and try to find out what is being scored. Done.
    public static int firstAminoAcidDistanceScore(Peak x, Peak y, double H_MASS, List<Double> AA_MASS,
            List<Double> AA_MASS2, List<Double> AA_MASS3, double minimum, double maximum, double errorTolerance) {
        int F1 = 0;

        double d1xy = Math.abs(diff1(x, y));
        double d2xy = Math.abs(diff2(x, y, H_MASS));
        double d2yx = Math.abs(diff2(y, x, H_MASS));
        double d3xy = Math.abs(diff3(x, y, H_MASS));
        double d3yx = Math.abs(diff3(y, x, H_MASS));
        double d4xy = Math.abs(diff4(x, y, H_MASS));
        double d4yx = Math.abs(diff4(y, x, H_MASS));

        double min = minimum - 2;
        double max = maximum + 2;

        if ((min < d1xy && d1xy < max) || (min < d2xy && d2xy < max) || (min < d2yx && d2yx < max)
                || (min < d3xy && d3xy < max) || (min < d3yx && d3yx < max) || (min < d4xy && d4xy < max)
                || (min < d4yx && d4yx < max)) {
            for (int i = 0; i < AA_MASS.size(); i++) {
                double aa = AA_MASS.get(i);
                double aa2 = AA_MASS2.get(i);
                double aa3 = AA_MASS3.get(i);
                double aape = aa + errorTolerance;
                double aame = aa - errorTolerance;
                double aa2pe = aa2 + errorTolerance;
                double aa2me = aa2 - errorTolerance;
                double aa3pe = aa3 + errorTolerance;
                double aa3me = aa3 - errorTolerance;

                // TODO: can very likely just be written as
                // d1xy < aa2me || (same as in paper)

                if ((aame < d1xy && d1xy < aape) || (aa2me < d1xy && d1xy < aa2pe) || (aa3me < d1xy && d1xy < aa3pe)
                        || (aa2me < d2xy && d2xy < aa2pe) || (aa2me < d2yx && d2yx < aa2pe)
                        || (aa3me < d3xy && d3xy < aa3pe) || (aa3me < d3yx && d3yx < aa3pe)
                        || (aa3me < d4xy && d4xy < aa3pe) || (aa3me < d4yx && d4yx < aa3pe)) {
                    F1++;
                }
            }
        }

        return F1;
    }

    // FOR TESTING PURPOSE
    public int calculateAminoAcidDistanceScore(Peak x, PeakList peaklist) {
        int peaklistScore = 0;
        for (Peak y : peaklist.getPeakList()) {
            peaklistScore += firstAminoAcidDistanceScore(x, y, config.getH_MASS(), config.getAaMass(),
                    config.getAaMassDividedTwo(), config.getAaMassDividedThree(), config.getMin(), config.getMax(),
                    config.getErrortolerance());
        }
        return peaklistScore;
    }

    // is based on the number collection of peaks representing fragment ions that
    // complement with fragment ion represented by .
    public static int secondComplementaryMassScore(Peak x, Peak y, double pepidMass, double charge,
            IsotopicCluster isotopicCluster, double H_MASS, double H_MASS2, double errorTolerance, double distance) {
        int F2 = 0;
        int i = 0;
        for (Peak c : isotopicCluster.getIsotopicCluster()) {
            if (c.getMz() == x.getMz() && c.getIntensity() == x.getIntensity()) {
                break;
            }
            i++;
        }
        double s1xy = sum1(x, y);
        double s2xy = sum2(x, y, H_MASS);
        double s2yx = sum2(y, x, H_MASS);
        double s3xy = sum3(x, y, H_MASS);
        double s3yx = sum3(y, x, H_MASS);
        double s4xy = sum4(x, y, H_MASS);
        double s4yx = sum4(y, x, H_MASS);
        double m2i = (pepidMass * charge - charge * H_MASS) + 2 * distance * i;

        if (m2i + H_MASS2 - errorTolerance < s1xy && s1xy < m2i + H_MASS2 + errorTolerance) {
            F2++;
        } else if (m2i / 2 + H_MASS2 - errorTolerance < s1xy && s1xy < m2i / 2 + H_MASS2 + errorTolerance) {
            F2++;
        } else if (m2i / 3 + H_MASS2 - errorTolerance < s1xy && s1xy < m2i / 3 + H_MASS2 + errorTolerance) {
            F2++;
        } else if (m2i / 2 + H_MASS2 - errorTolerance < s2xy && s2xy < m2i / 2 + H_MASS2 + errorTolerance) {
            F2++;
        } else if (m2i / 2 + H_MASS2 - errorTolerance < s2yx && s2yx < m2i / 2 + H_MASS2 + errorTolerance) {
            F2++;
        } else if (m2i / 3 + H_MASS2 - errorTolerance < s3xy && s3xy < m2i / 3 + H_MASS2 + errorTolerance) {
            F2++;
        } else if (m2i / 3 + H_MASS2 - errorTolerance < s3yx && s3yx < m2i / 3 + H_MASS2 + errorTolerance) {
            F2++;
        } else if (m2i / 3 + H_MASS2 - errorTolerance < s4xy && s4xy < m2i / 3 + H_MASS2 + errorTolerance) {
            F2++;
        } else if (m2i / 3 + H_MASS2 - errorTolerance < s4yx && s4yx < m2i / 3 + H_MASS2 + errorTolerance) {
            F2++;
        }

        return F2;
    }

    public static int thirdSideChainLossScore(Peak x, Peak y, double H_MASS, double H2O_MASS, double H2O_MASS2,
            double H2O_MASS3, double NH3_MASS, double NH3_MASS2, double NH3_MASS3, double errorTolerance) {
        int F3 = 0;

        double d1xy = Math.abs(diff1(x, y));
        double d2xy = Math.abs(diff2(x, y, H_MASS));
        double d2yx = Math.abs(diff2(y, x, H_MASS));
        double d3xy = Math.abs(diff3(x, y, H_MASS));
        double d3yx = Math.abs(diff3(y, x, H_MASS));
        double d4xy = Math.abs(diff4(x, y, H_MASS));
        double d4yx = Math.abs(diff4(y, x, H_MASS));

        if (H2O_MASS - errorTolerance < d1xy && d1xy < H2O_MASS + errorTolerance) {
            F3++;
        } else if (NH3_MASS - errorTolerance < d1xy && d1xy < NH3_MASS + errorTolerance) {
            F3++;
        } else if (H2O_MASS2 - errorTolerance < d1xy && d1xy < H2O_MASS2 + errorTolerance) {
            F3++;
        } else if (NH3_MASS2 - errorTolerance < d1xy && d1xy < NH3_MASS2 + errorTolerance) {
            F3++;
        } else if (H2O_MASS3 - errorTolerance < d1xy && d1xy < H2O_MASS3 + errorTolerance) {
            F3++;
        } else if (NH3_MASS3 - errorTolerance < d1xy && d1xy < NH3_MASS3 + errorTolerance) {
            F3++;
        } else if (H2O_MASS2 - errorTolerance < d2xy && d2xy < H2O_MASS2 + errorTolerance) {
            F3++;
        } else if (NH3_MASS2 - errorTolerance < d2xy && d2xy < NH3_MASS2 + errorTolerance) {
            F3++;
        } else if (H2O_MASS2 - errorTolerance < d2yx && d2yx < H2O_MASS2 + errorTolerance) {
            F3++;
        } else if (NH3_MASS2 - errorTolerance < d2yx && d2yx < NH3_MASS2 + errorTolerance) {
            F3++;
        } else if (H2O_MASS3 - errorTolerance < d3xy && d3xy < H2O_MASS3 + errorTolerance) {
            F3++;
        } else if (NH3_MASS3 - errorTolerance < d3xy && d3xy < NH3_MASS3 + errorTolerance) {
            F3++;
        } else if (H2O_MASS3 - errorTolerance < d3yx && d3yx < H2O_MASS3 + errorTolerance) {
            F3++;
        } else if (NH3_MASS3 - errorTolerance < d3yx && d3yx < NH3_MASS3 + errorTolerance) {
            F3++;
        } else if (H2O_MASS3 - errorTolerance < d4xy && d4xy < H2O_MASS3 + errorTolerance) {
            F3++;
        } else if (NH3_MASS3 - errorTolerance < d4xy && d4xy < NH3_MASS3 + errorTolerance) {
            F3++;
        } else if (H2O_MASS3 - errorTolerance < d4yx && d4yx < H2O_MASS3 + errorTolerance) {
            F3++;
        } else if (NH3_MASS3 - errorTolerance < d4yx && d4yx < NH3_MASS3 + errorTolerance) {
            F3++;
        }

        return F3;
    }

    // considers two supportive ions a-ions and z-ions which can be used to indicate
    // the existence of the corresponding b-ions and y-ions.
    public static int fourthSupportiveAndZIonScore(Peak x, Peak y, double H_MASS, double NH_MASS, double NH_MASS2,
            double NH_MASS3, double CO_MASS, double CO_MASS2, double CO_MASS3, double errorTolerance) {
        int F4 = 0;

        double d1xy = Math.abs(diff1(x, y));
        double d2xy = Math.abs(diff2(x, y, H_MASS));
        double d2yx = Math.abs(diff2(y, x, H_MASS));
        double d3xy = Math.abs(diff3(x, y, H_MASS));
        double d3yx = Math.abs(diff3(y, x, H_MASS));
        double d4xy = Math.abs(diff4(x, y, H_MASS));
        double d4yx = Math.abs(diff4(y, x, H_MASS));

        if (NH_MASS - errorTolerance < d1xy && d1xy < NH_MASS + errorTolerance) {
            F4++;
        } else if (CO_MASS - errorTolerance < d1xy && d1xy < CO_MASS + errorTolerance) {
            F4++;
        } else if (NH_MASS2 - errorTolerance < d1xy && d1xy < NH_MASS2 + errorTolerance) {
            F4++;
        } else if (CO_MASS2 - errorTolerance < d1xy && d1xy < CO_MASS2 + errorTolerance) {
            F4++;
        } else if (NH_MASS3 - errorTolerance < d1xy && d1xy < NH_MASS3 + errorTolerance) {
            F4++;
        } else if (CO_MASS3 - errorTolerance < d1xy && d1xy < CO_MASS3 + errorTolerance) {
            F4++;
        } else if (NH_MASS2 - errorTolerance < d2xy && d2xy < NH_MASS2 + errorTolerance) {
            F4++;
        } else if (CO_MASS2 - errorTolerance < d2xy && d2xy < CO_MASS2 + errorTolerance) {
            F4++;
        } else if (NH_MASS2 - errorTolerance < d2yx && d2yx < NH_MASS2 + errorTolerance) {
            F4++;
        } else if (CO_MASS2 - errorTolerance < d2yx && d2yx < CO_MASS2 + errorTolerance) {
            F4++;
        } else if (NH_MASS3 - errorTolerance < d3xy && d3xy < NH_MASS3 + errorTolerance) {
            F4++;
        } else if (CO_MASS3 - errorTolerance < d3xy && d3xy < CO_MASS3 + errorTolerance) {
            F4++;
        } else if (NH_MASS3 - errorTolerance < d3yx && d3yx < NH_MASS3 + errorTolerance) {
            F4++;
        } else if (CO_MASS3 - errorTolerance < d3yx && d3yx < CO_MASS3 + errorTolerance) {
            F4++;
        } else if (NH_MASS3 - errorTolerance < d4xy && d4xy < NH_MASS3 + errorTolerance) {
            F4++;
        } else if (CO_MASS3 - errorTolerance < d4xy && d4xy < CO_MASS3 + errorTolerance) {
            F4++;
        } else if (NH_MASS3 - errorTolerance < d4yx && d4yx < NH_MASS3 + errorTolerance) {
            F4++;
        } else if (CO_MASS3 - errorTolerance < d4yx && d4yx < CO_MASS3 + errorTolerance) {
            F4++;
        }

        return F4;
    }
}
