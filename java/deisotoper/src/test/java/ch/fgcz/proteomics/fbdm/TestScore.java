package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-26
 */

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

//TODO: Finish tests for Score!
public class TestScore {
    @Test
    public void testScore() {
    }

    /**
     * Tests the firstNonintensityFeature function.
     * 
     * With given x(123.0, 550.42) and y(125.86, 467.55) peaks, it is possible to
     * precalculate the results.
     * 
     * So now we precalculate the diff functions:
     * 
     * diff1: |123 - 125.86| = 2.86 // * // * diff2: |123 - (125.86 + 1.008) / 2| =
     * 59.566 and |125.86 - (123 + 1.008) / 2| // * = 63.856 // * // * diff3: |123 -
     * (125.86 + 2 * 1.008) / 3| = 80.3747 and |125.86 - (123 + 2 * // * 1.008) / 3|
     * = 84.188 // * // * diff4: |123 - (2 * 125.86 + 1.008) / 3| = 38.7573 and
     * |125.86 - (2 * 123 + // * 1.008) / 3| = 43.524 // * // * In the standart
     * configurated amino acid masses (from charge 1 to 3) there // * would be
     * matches (with error tolerance of 0.3) at 131.04049 (charge 3, // * matches
     * with diff4(y, x)), 128.09496 (charge 2, matches with diff2(y, x)) and // *
     * 128.05858 (charge 2 matches with diff2(y, x)). //
     */
    // @Test
    // public void testF1() {
    // Configuration config = new Configuration();
    // Peak x = new Peak(123.0, 550.42, 0);
    // Peak y = new Peak(125.86, 467.55, 1);
    // double e = 0.3;
    //
    // double score1 = Score.calculateFirstScoringFeature(x, y, e, config);
    //
    // assertEquals(score1, 3, 0);
    // }
    //
    // /**
    // * Tests the secondNonintensityFeature function.
    // *
    // * With given x(123.0, 550.42) and y(253.0, 467.55) peaks, it is possible to
    // * precalculate the results.
    // *
    // * sum1: 123 + 253 = 376
    // *
    // * And the m2i value is: 188 * 2 - 2 * 1.008 = 373.986
    // *
    // * Therefore we have: 373.986 + 2 * 1.008 +- 0.3 = 375.7 and 376.3
    // *
    // * The value of sum1 (376 is in the range of 375.7 and 376.3, therefore the
    // * result of the function is 1.
    // */
    // @Test
    // public void testF2() {
    // Configuration config = new Configuration();
    // Peak x = new Peak(123.0, 550.42, 0);
    // Peak y = new Peak(253.0, 467.55, 1);
    // double pepmass = 188.038;
    // int charge = 2;
    // double e = 0.3;
    // IsotopicCluster ic = new IsotopicCluster(
    // Arrays.asList(new Peak(123.0, 550.42, 0), new Peak(124.0, 233.2, 0), new
    // Peak(125.0, 112.02, 0)), 1,
    // config);
    //
    // double score = Score.calculateSecondScoringFeature(x, y, e, pepmass, charge,
    // ic, config);
    //
    // assertEquals(score, 1, 0);
    // }
    //
    // /**
    // * Tests the thirdNonintensityFeature function.
    // *
    // * With given x(123.0, 550.42) and y(141.0, 467.55) peaks, it is possible to
    // * precalculate the results.
    // *
    // * The NH3 mass is 17.03052 and the H2O mass is 18.01528. Therefore the value
    // of
    // * diff1, diff2, diff3 and diff4 must be in the range calculated from NH3 and
    // * H2O.
    // *
    // * The result from diff1 (18) matches the range from H2O (18.01528 +- 0.3).
    // */
    // @Test
    // public void testF3() {
    // Configuration config = new Configuration();
    // Peak x = new Peak(123.0, 550.42, 0);
    // Peak y = new Peak(141.0, 467.55, 1);
    // double e = 0.3;
    //
    // double score = Score.calculateThirdScoringFeature(x, y, e, config);
    //
    // assertEquals(score, 1, 0);
    // }
    //
    // /**
    // * Tests the fourthNonintensityFeature function.
    // *
    // * With given x(123.0, 550.42) and y(138.0, 467.55) peaks, it is possible to
    // * precalculate the results.
    // *
    // * This function is nearly the same as in the thirdNonintensityFeature
    // function.
    // *
    // * The NH mass is 15.01464 and the CO mass is 28.0101. Therefore the value of
    // * diff1, diff2, diff3 and diff4 must be in the range calculated from NH and
    // CO.
    // *
    // * The result from diff1 (15) matches the range from NH (15.01464 +- 0.3).
    // */
    // @Test
    // public void testF4() {
    // Configuration config = new Configuration();
    // Peak x = new Peak(123.0, 550.42, 0);
    // Peak y = new Peak(138.0, 467.55, 1);
    // double e = 0.3;
    //
    // double score = Score.calculateFourthScoringFeature(x, y, e, config);
    //
    // assertEquals(score, 1, 0);
    // }
    //
    // /**
    // * A simple test to check if the ScoreConfig class works.
    // *
    // * A file is generated with a test value in it and then this file is used to
    // * initialize the ScoreConfig object. After all the file is deleted.
    // */
    // @Test
    // public void testScoreConfig() {
    // Configuration config = new Configuration();
    //
    // assertEquals(config.getDelta(), 0.003, 0);
    // }

    /**
     * Tests diff1, diff2, diff3 and diff4. Results can be calculated by hand and
     * matched with the results.
     * 
     * With given x(120.0, 550.42) and y(150.0, 467.55) peaks, you can calculate
     * what the different diff functions do.
     * 
     * diff1: 120 - 150 = 30
     * 
     * diff2: 120 - (150 + 1.008) / 2 = 44.496
     * 
     * diff3: 120 - (150 + 2 * 1.008) / 3 = 69.328
     * 
     * diff4: 120 - (2 * 150 + 1.008) / 3 = 19.664
     */
    // @Test
    // public void testDiff() {
    // Configuration config = new Configuration();
    // Peak x = new Peak(120.0, 550.42, 0);
    // Peak y = new Peak(150.0, 467.55, 1);
    // double d1 = Score.diff1(x, y, config);
    // double d2 = Score.diff2(x, y, config);
    // double d3 = Score.diff3(x, y, config);
    // double d4 = Score.diff4(x, y, config);
    //
    // assertEquals(d1, -30, 0);
    // assertEquals(d2, 44.495999999999995, 0);
    // assertEquals(d3, 69.328, 0);
    // assertEquals(d4, 19.664, 0);
    // }

    /**
     * Tests sum1, sum2, sum3 and sum4. Results can be calculated by hand and
     * matched with the results.
     * 
     * With given x(120.0, 550.42) and y(150.0, 467.55) peaks, you can calculate
     * what the different sum functions do.
     * 
     * sum1: 120 + 150 = 270
     * 
     * sum2: 120 + (150 + 1.008) / 2 = 195.504
     * 
     * sum3: 120 + (150 + 2 * 1.008) / 3 = 170.672
     * 
     * sum4: 120 + (2 * 150 + 1.008) / 3 = 220.336
     */
    // @Test
    // public void testSum() {
    // Configuration config = new Configuration();
    // Peak x = new Peak(120.0, 550.42, 0);
    // Peak y = new Peak(150.0, 467.55, 0);
    // double s1 = Score.sum1(x, y, config);
    // double s2 = Score.sum2(x, y, config);
    // double s3 = Score.sum3(x, y, config);
    // double s4 = Score.sum4(x, y, config);
    //
    // assertEquals(s1, 270.0, 0);
    // assertEquals(s2, 195.50400000000002, 0);
    // assertEquals(s3, 170.672, 0);
    // assertEquals(s4, 220.336, 0);
    // }
}
