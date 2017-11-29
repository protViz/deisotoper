package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-19
 */

public class Score {
    private double peptidMassValue;
    private double chargeValue;
    //private DefaultDirectedWeightedGraph<IsotopicCluster, Connection> isotopicClusterGraph;
    private Configuration config;

    // @TODO remove depedency on config.
    public Score(double peptidMass, int charge, Configuration config) {
        this.peptidMassValue = peptidMass;
        this.chargeValue = charge;
        this.config = config;
    }

    // TODO integrate 5 th score
    public double calculateAminoAcidDistanceScore(Peak peakX, Peak peakY, IsotopicCluster isotopicClusterOfPeakX,
                                                  Connection connection) {
        return this.config.getF1() * firstAminoAcidDistanceScore(peakX, peakY, this.config)
                + this.config.getF2() * secondComplementaryMassScore(peakX, peakY, this.peptidMassValue,
                        this.chargeValue, isotopicClusterOfPeakX, this.config)
                + this.config.getF3() * thirdSideChainLossScore(peakX, peakY, this.config)
                + this.config.getF4() * fourthSupportiveAndZIonScore(peakX, peakY, this.config);
    }

    public static double diff1(Peak x, Peak y) {
        return x.getMz() - y.getMz();
    }

    public static double diff2(Peak x, Peak y, double H_MASS) {
        return x.getMz() - ((y.getMz() + H_MASS) / 2);
    }

    public static double diff3(Peak x, Peak y, Configuration config) {
        return x.getMz() - ((y.getMz() + (2 * config.getH_MASS())) / 3);
    }

    public static double diff4(Peak x, Peak y, Configuration config) {
        return x.getMz() - (((y.getMz() * 2) + config.getH_MASS()) / 3);
    }

    public static double sum1(Peak x, Peak y, Configuration config) {
        return x.getMz() + y.getMz();
    }

    public static double sum2(Peak x, Peak y, Configuration config) {
        return x.getMz() + ((y.getMz() + config.getH_MASS()) / 2);
    }

    public static double sum3(Peak x, Peak y, Configuration config) {
        return x.getMz() + ((y.getMz() + (2 * config.getH_MASS())) / 3);
    }

    public static double sum4(Peak x, Peak y, Configuration config) {
        return x.getMz() + (((y.getMz() * 2) + config.getH_MASS()) / 3);
    }

    // TODO : Look into paper and try to find out what is being scored.
    // is based on the number collection of peaks  whose mass differences with approximate the residue mass of one of the twenty amino acids.
    public static int firstAminoAcidDistanceScore(Peak x, Peak y, Configuration config) {
        int F1 = 0;

        double d1xy = Math.abs(diff1(x, y));
        double d2xy = Math.abs(diff2(x, y, config.getH_MASS()));
        double d2yx = Math.abs(diff2(y, x, config.getH_MASS()));
        double d3xy = Math.abs(diff3(x, y, config));
        double d3yx = Math.abs(diff3(y, x, config));
        double d4xy = Math.abs(diff4(x, y, config));
        double d4yx = Math.abs(diff4(y, x, config));

        double min = config.getMin() - 2;
        double max = config.getMax() + 2;

        if ((min < d1xy && d1xy < max) || (min < d2xy && d2xy < max) || (min < d2yx && d2yx < max)
                || (min < d3xy && d3xy < max) || (min < d3yx && d3yx < max) || (min < d4xy && d4xy < max)
                || (min < d4yx && d4yx < max)) {
            for (int i = 0; i < config.getAaMass().size(); i++) {
                double aa = config.getAaMass().get(i);
                double aa2 = config.getAaMassDividedTwo().get(i);
                double aa3 = config.getAaMassDividedThree().get(i);


                double aape = aa + config.getErrortolerance();
                double aame = aa - config.getErrortolerance();
                double aa2pe = aa2 + config.getErrortolerance();
                double aa2me = aa2 - config.getErrortolerance();
                double aa3pe = aa3 + config.getErrortolerance();
                double aa3me = aa3 - config.getErrortolerance();


                // TODO: can very likely just be written as
                // d1xy < aa2me || (same as in paper)

                if ((aame < d1xy && d1xy < aape) ||
                        (aa2me < d1xy && d1xy < aa2pe) ||
                        (aa3me < d1xy && d1xy < aa3pe) ||
                        (aa2me < d2xy && d2xy < aa2pe) ||
                        (aa2me < d2yx && d2yx < aa2pe) ||
                        (aa3me < d3xy && d3xy < aa3pe) ||
                        (aa3me < d3yx && d3yx < aa3pe) ||
                        (aa3me < d4xy && d4xy < aa3pe) ||
                        (aa3me < d4yx && d4yx < aa3pe)) {
                    F1++;
                }
            }
        }

        return F1;
    }

    public int firstAminoAcidDistanceScore(Peak x, PeakList peaklist, Configuration config) {
        int peaklistScore = 0;
        for(Peak y : peaklist.getPeakList()) {
            peaklistScore += firstAminoAcidDistanceScore(x, y , config);
        }
        return peaklistScore;
    }
    // is based on the number collection of peaks  representing fragment ions that complement with fragment ion represented by .
    public static int secondComplementaryMassScore(Peak x, Peak y, double pepidMass, double charge,
                                                   IsotopicCluster isotopicCluster, Configuration config) {
        int F2 = 0;
        int i = 0;
        for (Peak c : isotopicCluster.getIsotopicCluster()) {
            if (c.getMz() == x.getMz() && c.getIntensity() == x.getIntensity()) {
                break;
            }
            i++;
        }
        double s1xy = sum1(x, y, config);
        double s2xy = sum2(x, y, config);
        double s2yx = sum2(y, x, config);
        double s3xy = sum3(x, y, config);
        double s3yx = sum3(y, x, config);
        double s4xy = sum4(x, y, config);
        double s4yx = sum4(y, x, config);
        double m2i = (pepidMass * charge - charge * config.getH_MASS()) + 2 * config.getDistance() * i;

        if (m2i + config.getH_MASS_MULTIPLIED_TWO() - config.getErrortolerance() < s1xy
                && s1xy < m2i + config.getH_MASS_MULTIPLIED_TWO() + config.getErrortolerance()) {
            F2++;
        } else if (m2i / 2 + config.getH_MASS_MULTIPLIED_TWO() - config.getErrortolerance() < s1xy
                && s1xy < m2i / 2 + config.getH_MASS_MULTIPLIED_TWO() + config.getErrortolerance()) {
            F2++;
        } else if (m2i / 3 + config.getH_MASS_MULTIPLIED_TWO() - config.getErrortolerance() < s1xy
                && s1xy < m2i / 3 + config.getH_MASS_MULTIPLIED_TWO() + config.getErrortolerance()) {
            F2++;
        } else if (m2i / 2 + config.getH_MASS_MULTIPLIED_TWO() - config.getErrortolerance() < s2xy
                && s2xy < m2i / 2 + config.getH_MASS_MULTIPLIED_TWO() + config.getErrortolerance()) {
            F2++;
        } else if (m2i / 2 + config.getH_MASS_MULTIPLIED_TWO() - config.getErrortolerance() < s2yx
                && s2yx < m2i / 2 + config.getH_MASS_MULTIPLIED_TWO() + config.getErrortolerance()) {
            F2++;
        } else if (m2i / 3 + config.getH_MASS_MULTIPLIED_TWO() - config.getErrortolerance() < s3xy
                && s3xy < m2i / 3 + config.getH_MASS_MULTIPLIED_TWO() + config.getErrortolerance()) {
            F2++;
        } else if (m2i / 3 + config.getH_MASS_MULTIPLIED_TWO() - config.getErrortolerance() < s3yx
                && s3yx < m2i / 3 + config.getH_MASS_MULTIPLIED_TWO() + config.getErrortolerance()) {
            F2++;
        } else if (m2i / 3 + config.getH_MASS_MULTIPLIED_TWO() - config.getErrortolerance() < s4xy
                && s4xy < m2i / 3 + config.getH_MASS_MULTIPLIED_TWO() + config.getErrortolerance()) {
            F2++;
        } else if (m2i / 3 + config.getH_MASS_MULTIPLIED_TWO() - config.getErrortolerance() < s4yx
                && s4yx < m2i / 3 + config.getH_MASS_MULTIPLIED_TWO() + config.getErrortolerance()) {
            F2++;
        }
        return F2;
    }

    public static int thirdSideChainLossScore(Peak x, Peak y, Configuration config) {
        int F3 = 0;
        double d1xy = Math.abs(diff1(x, y));
        double d2xy = Math.abs(diff2(x, y, config.getH_MASS()));
        double d2yx = Math.abs(diff2(y, x, config.getH_MASS()));
        double d3xy = Math.abs(diff3(x, y, config));
        double d3yx = Math.abs(diff3(y, x, config));
        double d4xy = Math.abs(diff4(x, y, config));
        double d4yx = Math.abs(diff4(y, x, config));

        if (config.getH2O_MASS() - config.getErrortolerance() < d1xy
                && d1xy < config.getH2O_MASS() + config.getErrortolerance()) {
            F3++;
        } else if (config.getNH3_MASS() - config.getErrortolerance() < d1xy
                && d1xy < config.getNH3_MASS() + config.getErrortolerance()) {
            F3++;
        } else if (config.getH2O_MASS_DIVIDED_TWO() - config.getErrortolerance() < d1xy
                && d1xy < config.getH2O_MASS_DIVIDED_TWO() + config.getErrortolerance()) {
            F3++;
        } else if (config.getNH3_MASS_DIVIDED_TWO() - config.getErrortolerance() < d1xy
                && d1xy < config.getNH3_MASS_DIVIDED_TWO() + config.getErrortolerance()) {
            F3++;
        } else if (config.getH2O_MASS_DIVIDED_THREE() - config.getErrortolerance() < d1xy
                && d1xy < config.getH2O_MASS_DIVIDED_THREE() + config.getErrortolerance()) {
            F3++;
        } else if (config.getNH3_MASS_DIVIDED_THREE() - config.getErrortolerance() < d1xy
                && d1xy < config.getNH3_MASS_DIVIDED_THREE() + config.getErrortolerance()) {
            F3++;
        } else if (config.getH2O_MASS_DIVIDED_TWO() - config.getErrortolerance() < d2xy
                && d2xy < config.getH2O_MASS_DIVIDED_TWO() + config.getErrortolerance()) {
            F3++;
        } else if (config.getNH3_MASS_DIVIDED_TWO() - config.getErrortolerance() < d2xy
                && d2xy < config.getNH3_MASS_DIVIDED_TWO() + config.getErrortolerance()) {
            F3++;
        } else if (config.getH2O_MASS_DIVIDED_TWO() - config.getErrortolerance() < d2yx
                && d2yx < config.getH2O_MASS_DIVIDED_TWO() + config.getErrortolerance()) {
            F3++;
        } else if (config.getNH3_MASS_DIVIDED_TWO() - config.getErrortolerance() < d2yx
                && d2yx < config.getNH3_MASS_DIVIDED_TWO() + config.getErrortolerance()) {
            F3++;
        } else if (config.getH2O_MASS_DIVIDED_THREE() - config.getErrortolerance() < d3xy
                && d3xy < config.getH2O_MASS_DIVIDED_THREE() + config.getErrortolerance()) {
            F3++;
        } else if (config.getNH3_MASS_DIVIDED_THREE() - config.getErrortolerance() < d3xy
                && d3xy < config.getNH3_MASS_DIVIDED_THREE() + config.getErrortolerance()) {
            F3++;
        } else if (config.getH2O_MASS_DIVIDED_THREE() - config.getErrortolerance() < d3yx
                && d3yx < config.getH2O_MASS_DIVIDED_THREE() + config.getErrortolerance()) {
            F3++;
        } else if (config.getNH3_MASS_DIVIDED_THREE() - config.getErrortolerance() < d3yx
                && d3yx < config.getNH3_MASS_DIVIDED_THREE() + config.getErrortolerance()) {
            F3++;
        } else if (config.getH2O_MASS_DIVIDED_THREE() - config.getErrortolerance() < d4xy
                && d4xy < config.getH2O_MASS_DIVIDED_THREE() + config.getErrortolerance()) {
            F3++;
        } else if (config.getNH3_MASS_DIVIDED_THREE() - config.getErrortolerance() < d4xy
                && d4xy < config.getNH3_MASS_DIVIDED_THREE() + config.getErrortolerance()) {
            F3++;
        } else if (config.getH2O_MASS_DIVIDED_THREE() - config.getErrortolerance() < d4yx
                && d4yx < config.getH2O_MASS_DIVIDED_THREE() + config.getErrortolerance()) {
            F3++;
        } else if (config.getNH3_MASS_DIVIDED_THREE() - config.getErrortolerance() < d4yx
                && d4yx < config.getNH3_MASS_DIVIDED_THREE() + config.getErrortolerance()) {
            F3++;
        }
        return F3;
    }

    // considers two supportive ions a-ions and z-ions which can be used to indicate the existence of the corresponding b-ions and y-ions.
    public static int fourthSupportiveAndZIonScore(Peak x, Peak y, Configuration config) {
        int F4 = 0;

        double d1xy = Math.abs(diff1(x, y));
        double d2xy = Math.abs(diff2(x, y, config.getH_MASS()));
        double d2yx = Math.abs(diff2(y, x, config.getH_MASS()));
        double d3xy = Math.abs(diff3(x, y, config));
        double d3yx = Math.abs(diff3(y, x, config));
        double d4xy = Math.abs(diff4(x, y, config));
        double d4yx = Math.abs(diff4(y, x, config));

        if (config.getNH_MASS() - config.getErrortolerance() < d1xy
                && d1xy < config.getCO_MASS() + config.getErrortolerance()) {
            F4++;
        } else if (config.getCO_MASS() - config.getErrortolerance() < d1xy
                && d1xy < config.getCO_MASS() + config.getErrortolerance()) {
            F4++;
        } else if (config.getNH_MASS_DIVIDED_TWO() - config.getErrortolerance() < d1xy
                && d1xy < config.getNH_MASS_DIVIDED_TWO() + config.getErrortolerance()) {
            F4++;
        } else if (config.getCO_MASS_DIVIDED_TWO() - config.getErrortolerance() < d1xy
                && d1xy < config.getCO_MASS_DIVIDED_TWO() + config.getErrortolerance()) {
            F4++;
        } else if (config.getNH_MASS_DIVIDED_THREE() - config.getErrortolerance() < d1xy
                && d1xy < config.getNH_MASS_DIVIDED_THREE() + config.getErrortolerance()) {
            F4++;
        } else if (config.getCO_MASS_DIVIDED_THREE() - config.getErrortolerance() < d1xy
                && d1xy < config.getCO_MASS_DIVIDED_THREE() + config.getErrortolerance()) {
            F4++;
        } else if (config.getNH_MASS_DIVIDED_TWO() - config.getErrortolerance() < d2xy
                && d2xy < config.getNH_MASS_DIVIDED_TWO() + config.getErrortolerance()) {
            F4++;
        } else if (config.getCO_MASS_DIVIDED_TWO() - config.getErrortolerance() < d2xy
                && d2xy < config.getCO_MASS_DIVIDED_TWO() + config.getErrortolerance()) {
            F4++;
        } else if (config.getNH_MASS_DIVIDED_TWO() - config.getErrortolerance() < d2yx
                && d2yx < config.getNH_MASS_DIVIDED_TWO() + config.getErrortolerance()) {
            F4++;
        } else if (config.getCO_MASS_DIVIDED_TWO() - config.getErrortolerance() < d2yx
                && d2yx < config.getCO_MASS_DIVIDED_TWO() + config.getErrortolerance()) {
            F4++;
        } else if (config.getNH_MASS_DIVIDED_THREE() - config.getErrortolerance() < d3xy
                && d3xy < config.getNH_MASS_DIVIDED_THREE() + config.getErrortolerance()) {
            F4++;
        } else if (config.getCO_MASS_DIVIDED_THREE() - config.getErrortolerance() < d3xy
                && d3xy < config.getCO_MASS_DIVIDED_THREE() + config.getErrortolerance()) {
            F4++;
        } else if (config.getNH_MASS_DIVIDED_THREE() - config.getErrortolerance() < d3yx
                && d3yx < config.getNH_MASS_DIVIDED_THREE() + config.getErrortolerance()) {
            F4++;
        } else if (config.getCO_MASS_DIVIDED_THREE() - config.getErrortolerance() < d3yx
                && d3yx < config.getCO_MASS_DIVIDED_THREE() + config.getErrortolerance()) {
            F4++;
        } else if (config.getNH_MASS_DIVIDED_THREE() - config.getErrortolerance() < d4xy
                && d4xy < config.getNH_MASS_DIVIDED_THREE() + config.getErrortolerance()) {
            F4++;
        } else if (config.getCO_MASS_DIVIDED_THREE() - config.getErrortolerance() < d4xy
                && d4xy < config.getCO_MASS_DIVIDED_THREE() + config.getErrortolerance()) {
            F4++;
        } else if (config.getNH_MASS_DIVIDED_THREE() - config.getErrortolerance() < d4yx
                && d4yx < config.getNH_MASS_DIVIDED_THREE() + config.getErrortolerance()) {
            F4++;
        } else if (config.getCO_MASS_DIVIDED_THREE() - config.getErrortolerance() < d4yx
                && d4yx < config.getCO_MASS_DIVIDED_THREE() + config.getErrortolerance()) {
            F4++;
        }

        return F4;
    }


}
