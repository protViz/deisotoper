package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-11-28
 */

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ScoreTest {
    Configuration config;
    Score score;
    Peak x1;
    Peak x2;

    @Before
    public void initialize() {

        config = new Configuration();

        x1 = new Peak(100, 10, 1);
        x2 = new Peak(100 + config.getAaMass().get(1), 10, 2);
        score = new Score(x1.getMz() + x2.getMz(), 1, config);

    }

    @Test
    public void testDiff() {
        Configuration config = new Configuration();
        Peak x = new Peak(120.0, 550.42, 0);
        Peak y = new Peak(150.0, 467.55, 1);
        double d1 = Score.diff1(x, y);
        double d2 = Score.diff2(x, y, config.getH_MASS());
        double d3 = Score.diff3(x, y, config.getH_MASS());
        double d4 = Score.diff4(x, y, config.getH_MASS());

        assertEquals(d1, -30, 0);
        assertEquals(d2, 44.495999999999995, 0);
        assertEquals(d3, 69.328, 0);
        assertEquals(d4, 19.664, 0);
    }

    @Test
    public void testSum() {
        Configuration config = new Configuration();
        Peak x = new Peak(120.0, 550.42, 0);
        Peak y = new Peak(150.0, 467.55, 0);
        double s1 = Score.sum1(x, y);
        double s2 = Score.sum2(x, y, config.getH_MASS());
        double s3 = Score.sum3(x, y, config.getH_MASS());
        double s4 = Score.sum4(x, y, config.getH_MASS());

        assertEquals(s1, 270.0, 0);
        assertEquals(s2, 195.50400000000002, 0);
        assertEquals(s3, 170.672, 0);
        assertEquals(s4, 220.336, 0);
    }

    /**
     * Tests the firstNonintensityFeature function.
     *
     * With given x(123.0, 1) and y(125.86, 1) peaks, it is possible to precalculate
     * the results.
     *
     * So now we precalculate the diff functions:
     *
     * diff1: |123 - 125.86| = 2.86 diff2: |123 - (125.86 + 1.008) / 2| = 59.566 and
     * |125.86 - (123 + 1.008) / 2| = 63.856 diff3: |123 - (125.86 + 2 * 1.008) / 3|
     * = 80.3747 and |125.86 - (123 + 2 * 1.008) / 3| = 84.188 diff4: |123 - (2 *
     * 125.86 + 1.008) / 3| = 38.7573 and |125.86 - (2 * 123 + 1.008) / 3| = 43.524
     *
     * In the standart configurated amino acid masses (from charge 1 to 3) there
     * would be matches (with error tolerance of 0.3) at 131.04049 (charge 3,
     * matches with diff4(y, x)), 128.09496 (charge 2, matches with diff2(y, x)) and
     * 128.05858 (charge 2 matches with diff2(y, x)).
     */
    @Test
    public void firstAminoAcidDistanceScore() {
        Configuration config = new Configuration();
        Peak x = new Peak(123.0, 1, 0);
        Peak y = new Peak(125.86, 1, 1);
        double score1 = Score.firstAminoAcidDistanceScore(x, y, config);

        assertEquals(3, score1, 0);

    }

    @Test
    public void firstAminoAcidDistanceScorePeakList() {
        int charge = 1;

        Configuration config = new Configuration();
        Peak x1 = new Peak(100, 10, 1);
        Peak x2 = new Peak(100 + config.getAaMass().get(1), 10, 1);
        Peak x3 = new Peak(100 + config.getAaMass().get(1) + config.getAaMass().get(2), 10, 1);
        Peak x4 = new Peak(x3.getMz() + config.getAaMass().get(10), 10, 1);
        Peak x5 = new Peak(x4.getMz() + config.getAaMass().get(11), 10, 1);

        PeakList peaklist = new PeakList();
        peaklist.add(x1).add(x2).add(x3).add(x4).add(x5);

        Score score = new Score(x5.getMz(), charge, config);

        int score1 = score.firstAminoAcidDistanceScore(x1, peaklist, config);
        int score2 = score.firstAminoAcidDistanceScore(x2, peaklist, config);
        int score3 = score.firstAminoAcidDistanceScore(x3, peaklist, config);
        int score4 = score.firstAminoAcidDistanceScore(x4, peaklist, config);
        int score5 = score.firstAminoAcidDistanceScore(x5, peaklist, config);

        // TODO : make a meaningfull test here.
        //assertEquals(0, 1);
    }

    /**
     * Tests the secondNonintensityFeature function.
     *
     * With given x(123.0, 550.42) and y(253.0, 467.55) peaks, it is possible to
     * precalculate the results.
     *
     * sum1: 123 + 253 = 376
     *
     * And the m2i value is: 188 * 2 - 2 * 1.008 = 373.986
     *
     * Therefore we have: 373.986 + 2 * 1.008 +- 0.3 = 375.7 and 376.3
     *
     * The value of sum1 (376 is in the range of 375.7 and 376.3, therefore the
     * result of the function is 1.
     */

    @Test
    public void secondComplementaryMassScore() {
        Configuration config = new Configuration();
        Peak x = new Peak(123.0, 550.42, 0);
        Peak y = new Peak(253.0, 467.55, 1);
        double pepmass = 188.038;
        int charge = 2;

        List<Peak> cluster = Arrays.asList(
                new Peak(123.0, 1, 0),
                new Peak(124.0, 1, 0),
                new Peak(125.0, 1, 0)
        );

        double score = Score.secondComplementaryMassScore(x, y, pepmass, charge, cluster, config);

        //assertEquals(score, 1, 0);
    }

    /**
     * Tests the thirdNonintensityFeature function.
     *
     * With given x(123.0, 550.42) and y(141.0, 467.55) peaks, it is possible to
     * precalculate the results.
     *
     * The NH3 mass is 17.03052 and the H2O mass is 18.01528. Therefore the value of
     * diff1, diff2, diff3 and diff4 must be in the range calculated from NH3 and
     * H2O.
     *
     * The result from diff1 (18) matches the range from H2O (18.01528 +- 0.3).
     */
    @Test
    public void thirdSideChainLossScore() {
        Configuration config = new Configuration();
        Peak x = new Peak(123.0, 550.42, 0);
        Peak y = new Peak(141.0, 467.55, 1);

        int score = Score.thirdSideChainLossScore(x, y, config);

        assertEquals(1, score);
    }

    /**
     * Tests the fourthNonintensityFeature function.
     *
     * With given x(123.0, 550.42) and y(138.0, 467.55) peaks, it is possible to
     * precalculate the results.
     *
     * This function is nearly the same as in the thirdNonintensityFeature function.
     *
     * The NH mass is 15.01464 and the CO mass is 28.0101. Therefore the value of
     * diff1, diff2, diff3 and diff4 must be in the range calculated from NH and CO.
     *
     * The result from diff1 (15) matches the range from NH (15.01464 +- 0.3).
     */
    @Test
    public void fourthSupportiveAndZIonScore() {
        Configuration config = new Configuration();
        Peak x = new Peak(123.0, 550.42, 0);
        Peak y = new Peak(138.0, 467.55, 1);

        double score = Score.fourthSupportiveAZIonsScore(x, y, config);

        assertEquals(score, 1, 0);
    }

    @Test
    public void calculateAminoAcidDistanceScore() {
    }

}