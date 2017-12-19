package ch.fgcz.proteomics.fbdm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.fgcz.proteomics.utilities.MathUtils;

/**
 * @author Witold Wolski, Lucas Schmidt
 * @since 2017-09-19
 */

public class Score {
    private double peptidMassValue;
    private double chargeValue;
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
    public static double diff2(double x, double y, double hMass) {
        return x - ((y + hMass) / 2);
    }

    // single versus triply charged
    public static double diff3(double x, double y, double hMass) {
        return x - ((y + (2 * hMass)) / 3);
    }

    public static double diff4(double x, double y, double hMass) {
        return x - (((y * 2) + hMass) / 3);
    }

    public static double sum1(double x, double y) {
        return x + y;
    }

    public static double sum2(double x, double y, double hMass) {
        return x + ((y + hMass) / 2);
    }

    public static double sum3(double x, double y, double hMass) {
        return x + ((y + (2 * hMass)) / 3);
    }

    public static double sum4(double x, double y, double hMass) {
        return x + (((y * 2) + hMass) / 3);
    }

    // TODO : Look into paper and try to find out what is being scored.
    // is based on the number collection of peaks whose mass differences with
    // approximate the residue mass of one of the twenty amino acids.
    public static int firstAminoAcidDistanceScore(double x, double y, ScoringConfiguration config) {
        int f1 = 0;
        double error = config.getErrorTolerance();
        double hMass = config.getHMass(1);

        Range aminoAcidMassRange = new Range(config.getMin() - 2, config.getMax() + 2);

        double d1xy = Math.abs(diff1(x, y));
        double d2xy = Math.abs(diff2(x, y, hMass));
        double d2yx = Math.abs(diff2(y, x, hMass));
        double d3xy = Math.abs(diff3(x, y, hMass));
        double d3yx = Math.abs(diff3(y, x, hMass));
        double d4xy = Math.abs(diff4(x, y, hMass));
        double d4yx = Math.abs(diff4(y, x, hMass));

        List<Double> aaMassValues = new ArrayList<Double>();
        List<Double> aaMassValuesDividedTwo = new ArrayList<Double>();
        List<Double> aaMassValuesDividedThree = new ArrayList<Double>();

        for (Map.Entry<String, Double> entry : config.getAaMass().entrySet()) {
            aaMassValues.add(entry.getValue());
        }

        for (Map.Entry<String, Double> entry : config.getAaMassDividedTwo().entrySet()) {
            aaMassValuesDividedTwo.add(entry.getValue());
        }

        for (Map.Entry<String, Double> entry : config.getAaMassDividedThree().entrySet()) {
            aaMassValuesDividedThree.add(entry.getValue());
        }

        if (aminoAcidMassRange.in(d1xy) || aminoAcidMassRange.in(d2xy) || aminoAcidMassRange.in(d2yx)
                || aminoAcidMassRange.in(d3xy) || aminoAcidMassRange.in(d3yx) || aminoAcidMassRange.in(d4xy)
                || aminoAcidMassRange.in(d4yx)) {

            for (int i = 0; i < config.getAaMass().size(); i++) {

                double aa = aaMassValues.get(i);
                double aa2 = aaMassValuesDividedTwo.get(i);
                double aa3 = aaMassValuesDividedThree.get(i);

                if (MathUtils.fuzzyEqual(d1xy, aa, error) || MathUtils.fuzzyEqual(d1xy, aa2, error)
                        || MathUtils.fuzzyEqual(d1xy, aa3, error) || MathUtils.fuzzyEqual(d2xy, aa2, error)
                        || MathUtils.fuzzyEqual(d2yx, aa2, error) || MathUtils.fuzzyEqual(d3xy, aa3, error)
                        || MathUtils.fuzzyEqual(d3yx, aa3, error) || MathUtils.fuzzyEqual(d4xy, aa3, error)
                        || MathUtils.fuzzyEqual(d4yx, aa3, error)) {
                    f1++;
                }
            }
        }

        return f1;
    }

    // is based on the number collection of peaks representing fragment ions that
    // complement with fragment ion represented by .
    public static int secondComplementaryMassScore(double x, double y, double pepidMass, double charge,
            List<Peak> isotopicCluster, ScoringConfiguration config) {
        int f2 = 0;
        int i = 0;
        for (Peak c : isotopicCluster) {
            if (c.getMz() == x) {
                break;
            }
            i++;
        }
        double hMass = config.getHMass(1);
        double s1xy = sum1(x, y);
        double s2xy = sum2(x, y, hMass);
        double s2yx = sum2(y, x, hMass);
        double s3xy = sum3(x, y, hMass);
        double s3yx = sum3(y, x, hMass);
        double s4xy = sum4(x, y, hMass);
        double s4yx = sum4(y, x, hMass);
        double m2i = (pepidMass * charge - charge * config.getHMass(1)) + 2 * config.getIsotopicPeakDistance() * i;

        if (MathUtils.fuzzyEqual(s1xy, m2i + config.getHMass(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(s1xy, m2i / 2 + config.getHMass(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(s1xy, m2i / 3 + config.getHMass(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(s2xy, m2i / 2 + config.getHMass(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(s2yx, m2i / 2 + config.getHMass(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(s3xy, m2i / 3 + config.getHMass(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(s3yx, m2i / 3 + config.getHMass(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(s4xy, m2i / 3 + config.getHMass(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(s4yx, m2i / 3 + config.getHMass(2), config.getErrorTolerance())) {
            f2++;
        }

        return f2;
    }

    public static int thirdSideChainLossScore(double x, double y, ScoringConfiguration config) {
        int f3 = 0;
        double d1xy = Math.abs(diff1(x, y));
        double d2xy = Math.abs(diff2(x, y, config.getHMass(1)));
        double d2yx = Math.abs(diff2(y, x, config.getHMass(1)));
        double d3xy = Math.abs(diff3(x, y, config.getHMass(1)));
        double d3yx = Math.abs(diff3(y, x, config.getHMass(1)));
        double d4xy = Math.abs(diff4(x, y, config.getHMass(1)));
        double d4yx = Math.abs(diff4(y, x, config.getHMass(1)));

        if (MathUtils.fuzzyEqual(d1xy, config.getH2oMass(1), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d1xy, config.getNh3Mass(1), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d1xy, config.getH2oMass(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d1xy, config.getNh3Mass(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d1xy, config.getH2oMass(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d1xy, config.getNh3Mass(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d2xy, config.getH2oMass(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d2xy, config.getNh3Mass(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d2yx, config.getH2oMass(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d2yx, config.getNh3Mass(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d3xy, config.getH2oMass(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d3xy, config.getNh3Mass(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d3yx, config.getH2oMass(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d3yx, config.getNh3Mass(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d4xy, config.getH2oMass(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d4xy, config.getNh3Mass(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d4yx, config.getH2oMass(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d4yx, config.getNh3Mass(3), config.getErrorTolerance())) {
            f3++;
        }

        return f3;
    }

    // considers two supportive ions a-ions and z-ions which can be used to indicate
    // the existence of the corresponding b-ions and y-ions.
    public static int fourthSupportiveAZIonsScore(double x, double y, ScoringConfiguration config) {
        int f4 = 0;

        double d1xy = Math.abs(diff1(x, y));
        double d2xy = Math.abs(diff2(x, y, config.getHMass(1)));
        double d2yx = Math.abs(diff2(y, x, config.getHMass(1)));
        double d3xy = Math.abs(diff3(x, y, config.getHMass(1)));
        double d3yx = Math.abs(diff3(y, x, config.getHMass(1)));
        double d4xy = Math.abs(diff4(x, y, config.getHMass(1)));
        double d4yx = Math.abs(diff4(y, x, config.getHMass(1)));

        if (MathUtils.fuzzyEqual(d1xy, config.getNhMass(1), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d1xy, config.getCoMass(1), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d1xy, config.getNhMass(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d1xy, config.getCoMass(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d1xy, config.getNhMass(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d1xy, config.getCoMass(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d2xy, config.getNhMass(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d2xy, config.getCoMass(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d2yx, config.getNhMass(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d2yx, config.getCoMass(2), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d3xy, config.getNhMass(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d3xy, config.getCoMass(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d3yx, config.getNhMass(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d3yx, config.getCoMass(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d4xy, config.getNhMass(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d4xy, config.getCoMass(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d4yx, config.getNhMass(3), config.getErrorTolerance())
                || MathUtils.fuzzyEqual(d4yx, config.getCoMass(3), config.getErrorTolerance())) {
            f4++;
        }

        return f4;
    }

    public int firstAminoAcidDistanceScore(double x, PeakList peaklist, ScoringConfiguration config) {
        int peaklistScore = 0;
        for (Peak y : peaklist.getPeakList()) {
            peaklistScore += firstAminoAcidDistanceScore(x, y.getMz(), config);
        }
        return peaklistScore;
    }

    // TODO integrate 5 th score. LS: is integrated see: connectClusters
    // (isotopicSetGraph)
    public double calculateAggregatedScore(double xMz, double yMz, List<Peak> isotopicClusterOfPeakX) {
        return this.config.getF(1) * firstAminoAcidDistanceScore(xMz, yMz, this.config)
                + this.config.getF(2) * secondComplementaryMassScore(xMz, yMz, this.peptidMassValue, this.chargeValue,
                        isotopicClusterOfPeakX, this.config)
                + this.config.getF(3) * thirdSideChainLossScore(xMz, yMz, this.config)
                + this.config.getF(4) * fourthSupportiveAZIonsScore(xMz, yMz, this.config);
    }

    public static class Range {
        double min;
        double max;

        Range(double min, double max) {
            this.min = min;
            this.max = max;
        }

        public boolean in(double value) {
            return (min < value && value < max);
        }
    }

}
