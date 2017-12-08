package ch.fgcz.proteomics.fbdm;

import ch.fgcz.proteomics.utilities.MathUtils;

import java.util.List;

/**
 * @author Witold Wolski, Lucas Schmidt
 * @since 2017-09-19
 */

public class Score {
    private double peptidMassValue;
    private double chargeValue;
    // private DefaultDirectedWeightedGraph<IsotopicCluster, Connection>
    // isotopicClusterGraph;
    private ScoringConfiguration config;

    public Score(double peptidMass, int charge, ScoringConfiguration config) {
        this.peptidMassValue = peptidMass;
        this.chargeValue = charge;
        this.config = config;
    }

    // same charge
    public static double diff1(double x, double y) {
        return x - y;
    }

    // single versus double
    public static double diff2(double x, double y, double H_MASS) {
        return x - ((y + H_MASS) / 2);
    }

    // single versus triply charged
    public static double diff3(double x, double y, double H_MASS) {
        return x - ((y + (2 * H_MASS)) / 3);
    }

    public static double diff4(double x, double y, double H_MASS) {
        return x - (((y * 2) + H_MASS) / 3);
    }

    public static double sum1(double x, double y) {
        return x + y;
    }

    public static double sum2(double x, double y, double H_MASS) {
        return x + ((y + H_MASS) / 2);
    }

    public static double sum3(double x, double y, double H_MASS) {
        return x + ((y + (2 * H_MASS)) / 3);
    }

    public static double sum4(double x, double y, double H_MASS) {
        return x + (((y * 2) + H_MASS) / 3);
    }

    // TODO : Look into paper and try to find out what is being scored.
    // is based on the number collection of peaks whose mass differences with
    // approximate the residue mass of one of the twenty amino acids.
    public static int firstAminoAcidDistanceScore(double x, double y, ScoringConfiguration config) {
        int F1 = 0;
        double error = config.getErrorTolerance();
        double H_MASS = config.getH_MASS(1);

        Range aminoAcidMassRange = new Range(config.getMin() - 2, config.getMax() + 2);

        double d1xy = Math.abs(diff1(x, y));
        double d2xy = Math.abs(diff2(x, y, H_MASS));
        double d2yx = Math.abs(diff2(y, x, H_MASS));
        double d3xy = Math.abs(diff3(x, y, H_MASS));
        double d3yx = Math.abs(diff3(y, x, H_MASS));
        double d4xy = Math.abs(diff4(x, y, H_MASS));
        double d4yx = Math.abs(diff4(y, x, H_MASS));

        if (aminoAcidMassRange.in(d1xy) || aminoAcidMassRange.in(d2xy) || aminoAcidMassRange.in(d2yx)
                || aminoAcidMassRange.in(d3xy) || aminoAcidMassRange.in(d3yx) || aminoAcidMassRange.in(d4xy)
                || aminoAcidMassRange.in(d4yx)) {

            for (int i = 0; i < config.getAaMass().size(); i++) {

                double aa = config.getAaMass().get(i);
                double aa2 = config.getAaMassDividedTwo().get(i);
                double aa3 = config.getAaMassDividedThree().get(i);

                if (MathUtils.fuzzyEqual(d1xy, aa, error) || MathUtils.fuzzyEqual(d1xy, aa2, error)
                        || MathUtils.fuzzyEqual(d1xy, aa3, error) || MathUtils.fuzzyEqual(d2xy, aa2, error)
                        || MathUtils.fuzzyEqual(d2yx, aa2, error) || MathUtils.fuzzyEqual(d3xy, aa3, error)
                        || MathUtils.fuzzyEqual(d3yx, aa3, error) || MathUtils.fuzzyEqual(d4xy, aa3, error)
                        || MathUtils.fuzzyEqual(d4yx, aa3, error)) {
                    F1++;
                }
            }
        }

        return F1;
    }

    // is based on the number collection of peaks representing fragment ions that
    // complement with fragment ion represented by .
    public static int secondComplementaryMassScore(double x, double y, double pepidMass, double charge,
            List<Peak> isotopicCluster, ScoringConfiguration config) {
        int F2 = 0;
        int i = 0;
        for (Peak c : isotopicCluster) {
            if (c.getMz() == x) {
                break;
            }
            i++;
        }
        double H_MASS = config.getH_MASS(1);
        double s1xy = sum1(x, y);
        double s2xy = sum2(x, y, H_MASS);
        double s2yx = sum2(y, x, H_MASS);
        double s3xy = sum3(x, y, H_MASS);
        double s3yx = sum3(y, x, H_MASS);
        double s4xy = sum4(x, y, H_MASS);
        double s4yx = sum4(y, x, H_MASS);
        double m2i = (pepidMass * charge - charge * config.getH_MASS(1)) + 2 * config.getIsotopicPeakDistance() * i;

        if (MathUtils.fuzzyEqual(s1xy, m2i + config.getH_MASS(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(s1xy, m2i / 2 + config.getH_MASS(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(s1xy, m2i / 3 + config.getH_MASS(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(s2xy, m2i / 2 + config.getH_MASS(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(s2yx, m2i / 2 + config.getH_MASS(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(s3xy, m2i / 3 + config.getH_MASS(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(s3yx, m2i / 3 + config.getH_MASS(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(s4xy, m2i / 3 + config.getH_MASS(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(s4yx, m2i / 3 + config.getH_MASS(2), config.getErrorTolerance())) {
            F2++;
        }

        return F2;
    }

    public static int thirdSideChainLossScore(double x, double y, ScoringConfiguration config) {
        int F3 = 0;
        double d1xy = Math.abs(diff1(x, y));
        double d2xy = Math.abs(diff2(x, y, config.getH_MASS(1)));
        double d2yx = Math.abs(diff2(y, x, config.getH_MASS(1)));
        double d3xy = Math.abs(diff3(x, y, config.getH_MASS(1)));
        double d3yx = Math.abs(diff3(y, x, config.getH_MASS(1)));
        double d4xy = Math.abs(diff4(x, y, config.getH_MASS(1)));
        double d4yx = Math.abs(diff4(y, x, config.getH_MASS(1)));

        if (MathUtils.fuzzyEqual(d1xy, config.getH2O_MASS(1), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d1xy, config.getNH3_MASS(1), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d1xy, config.getH2O_MASS(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d1xy, config.getNH3_MASS(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d1xy, config.getH2O_MASS(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d1xy, config.getNH3_MASS(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d2xy, config.getH2O_MASS(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d2xy, config.getNH3_MASS(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d2yx, config.getH2O_MASS(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d2yx, config.getNH3_MASS(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d3xy, config.getH2O_MASS(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d3xy, config.getNH3_MASS(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d3yx, config.getH2O_MASS(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d3yx, config.getNH3_MASS(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d4xy, config.getH2O_MASS(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d4xy, config.getNH3_MASS(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d4yx, config.getH2O_MASS(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d4yx, config.getNH3_MASS(3), config.getErrorTolerance())) {
            F3++;
        }

        return F3;
    }

    // considers two supportive ions a-ions and z-ions which can be used to indicate
    // the existence of the corresponding b-ions and y-ions.
    public static int fourthSupportiveAZIonsScore(double x, double y, ScoringConfiguration config) {
        int F4 = 0;

        double d1xy = Math.abs(diff1(x, y));
        double d2xy = Math.abs(diff2(x, y, config.getH_MASS(1)));
        double d2yx = Math.abs(diff2(y, x, config.getH_MASS(1)));
        double d3xy = Math.abs(diff3(x, y, config.getH_MASS(1)));
        double d3yx = Math.abs(diff3(y, x, config.getH_MASS(1)));
        double d4xy = Math.abs(diff4(x, y, config.getH_MASS(1)));
        double d4yx = Math.abs(diff4(y, x, config.getH_MASS(1)));

        if (MathUtils.fuzzyEqual(d1xy, config.getNH_MASS(1), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d1xy, config.getCO_MASS(1), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d1xy, config.getNH_MASS(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d1xy, config.getCO_MASS(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d1xy, config.getNH_MASS(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d1xy, config.getCO_MASS(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d2xy, config.getNH_MASS(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d2xy, config.getCO_MASS(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d2yx, config.getNH_MASS(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d2yx, config.getCO_MASS(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d3xy, config.getNH_MASS(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d3xy, config.getCO_MASS(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d3yx, config.getNH_MASS(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d3yx, config.getCO_MASS(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d4xy, config.getNH_MASS(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d4xy, config.getCO_MASS(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d4yx, config.getNH_MASS(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d4yx, config.getCO_MASS(3), config.getErrorTolerance())) {
            F4++;
        }

        return F4;
    }

    public int firstAminoAcidDistanceScore(double x, PeakList peaklist, ScoringConfiguration config) {
        int peaklistScore = 0;
        for (Peak y : peaklist.getPeakList()) {
            peaklistScore += firstAminoAcidDistanceScore(x, y.getMz(), config);
        }
        return peaklistScore;
    }

    // TODO integrate 5 th score
    public double calculateAggregatedScore(double xMz, double yMz, List<Peak> isotopicClusterOfPeakX) {
        return this.config.getF1() * firstAminoAcidDistanceScore(xMz, yMz, this.config)
                + this.config.getF2() * secondComplementaryMassScore(xMz, yMz, this.peptidMassValue, this.chargeValue,
                        isotopicClusterOfPeakX, this.config)
                + this.config.getF3() * thirdSideChainLossScore(xMz, yMz, this.config)
                + this.config.getF4() * fourthSupportiveAZIonsScore(xMz, yMz, this.config);
    }

    public static class Range {
        double min, max;

        Range(double min, double max) {
            this.min = min;
            this.max = max;
        }

        public boolean in(double value) {
            return (min < value && value < max);
        }
    }

}
