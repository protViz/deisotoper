package ch.fgcz.proteomics.fdbm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-26
 */

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;

import org.junit.Test;

public class ScoreTest {
    @Test
    public void testF1() {
        ScoreConfig config = new ScoreConfig("nofile");
        Score s = new Score();
        Peak x1 = new Peak(123.0, 550.42, 0);
        Peak y1 = new Peak(125.86, 467.55, 0);
        Peak x2 = new Peak(113.0, 6699.498, 0);
        Peak y2 = new Peak(117.0, 335.565, 0);
        Peak x3 = new Peak(155.0, 5855.542, 0);
        Peak y3 = new Peak(189.86, 325.58, 0);
        double e = 0.3;

        double score1 = s.firstNonintensityFeature(x1, y1, e, config);
        double score2 = s.firstNonintensityFeature(x2, y3, e, config);
        double score3 = s.firstNonintensityFeature(x3, y2, e, config);

        assertEquals(score1, 3.0, 0);
        assertEquals(score2, 1.0, 0);
        assertEquals(score3, 1.0, 0);
    }

    @Test
    public void testF2() {
        ScoreConfig config = new ScoreConfig("nofile");
        Score s = new Score();
        Peak x = new Peak(123.0, 550.42, 0);
        Peak y = new Peak(128.0, 467.55, 0);
        double pepmass = 333.038;
        int charge = 2;
        double e = 0.3;
        IsotopicCluster ic = new IsotopicCluster(Arrays.asList(new Peak(123.0, 550.42, 0), new Peak(124.0, 233.2, 0), new Peak(125.0, 112.02, 0)), 1);

        double score = s.secondNonintensityFeature(x, y, e, pepmass, charge, ic, config);

        assertEquals(score, 0, 0);
    }

    @Test
    public void testF3() {
        ScoreConfig config = new ScoreConfig("nofile");
        Score s = new Score();
        Peak x = new Peak(123.0, 550.42, 0);
        Peak y = new Peak(141.0, 467.55, 0);
        double e = 0.3;

        double score = s.thirdNonintensityFeature(x, y, e, config);

        assertEquals(score, 1, 0);
    }

    @Test
    public void testF4() {
        ScoreConfig config = new ScoreConfig("nofile");
        Score s = new Score();
        Peak x = new Peak(123.0, 550.42, 0);
        Peak y = new Peak(138.0, 467.55, 0);
        double e = 0.3;

        double score = s.fourthNonintensityFeature(x, y, e, config);

        assertEquals(score, 1, 0);
    }

    @Test
    public void testF5() {
        ScoreConfig config = new ScoreConfig("nofile");

        Score s = new Score();
        IsotopicClusterGraph icg = new IsotopicClusterGraph(
                new IsotopicSet(Arrays.asList(new Peak(123.0, 473.23, 0), new Peak(124.0, 333.23, 0), new Peak(125.0, 342.23, 0), new Peak(125.5, 173.243, 0)), 0.01, 0));
        Object[] a = icg.getIsotopicclustergraph().edgeSet().toArray();

        double score = s.fifthIntensityFeature((Connection) a[0], icg.getIsotopicclustergraph(), config);

        assertEquals(score, 0, 0);
    }

    @Test
    public void testScoreConfig() {
        File file = new File("AminoAcidMassesTest.properties");

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("X=12345");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ScoreConfig config = new ScoreConfig(file.getName());

        assertEquals(config.getAA_MASS().get(0), 12345, 0);

        file.delete();
    }
}
