
/**
 * @author Lucas Schmidt
 * @since 2017-09-05
 */

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class IsotopicTest {
    Peaklist resultpeaklist1;
    Peaklist resultpeaklist2;
    Peaklist peaklist1;
    Peaklist peaklist2;
    IsotopicSets IS1;
    IsotopicSets IS2;

    @Before
    public void setUp() {
        // Example 1
        List<Double> mz1 = Arrays.asList(0.1, 0.2, 1.33, 1.66, 2.0, 2.5, 3.5, 4.0, 5.0, 6.0, 12.0, 15.0);
        List<Double> intensity1 = Arrays.asList(7.0, 4.0, 5.0, 6.0, 6.0, 7.0, 7.0, 7.0, 8.0, 8.0, 2.0, 3.0);
        peaklist1 = new Peaklist(mz1, intensity1);

        // Example 2
        List<Double> mz2 = Arrays.asList(0.2, 1.0, 2.0, 3.0, 3.5, 4.0, 4.5, 5.0, 6.0, 6.5, 7.0, 11.0, 12.3);
        List<Double> intensity2 = Arrays.asList(10.0, 8.0, 3.0, 5.0, 6.0, 1.0, 4.0, 2.0, 1.0, 4.0, 1.0, 2.0, 4.0);
        peaklist2 = new Peaklist(mz2, intensity2);

        // Example 1 Result
        List<Double> resultmz1 = Arrays.asList(1.33, 1.66, 2.0, 2.5, 3.5, 4.0, 5.0, 6.0);
        List<Double> resultintensity1 = Arrays.asList(5.0, 6.0, 6.0, 7.0, 7.0, 7.0, 8.0, 8.0);
        resultpeaklist1 = new Peaklist(resultmz1, resultintensity1);

        // Example 2 Result
        List<Double> resultmz2 = Arrays.asList(1.0, 2.0, 3.0, 3.5, 4.0, 4.5, 5.0, 6.0, 6.5, 7.0);
        List<Double> resultintensity2 = Arrays.asList(8.0, 3.0, 5.0, 6.0, 1.0, 4.0, 2.0, 1.0, 4.0, 1.0);
        resultpeaklist2 = new Peaklist(resultmz2, resultintensity2);

        // Isotopic Sets
        double errortolerance = 0.01;
        IS1 = new IsotopicSets(peaklist1, errortolerance);
        IS2 = new IsotopicSets(peaklist2, errortolerance);
    }

    @Test
    public void testIsotopeSet() {
        // Assert
        assertNotNull(IS1);
        assertEquals("Calculated IS1 must be same size as defined result (resultpreaklist1)!", IS1.getIsotopicsets().get(0).size(), resultpeaklist1.getPeaklist().size());

        assertNotNull(IS2);
        assertEquals("Calculated IS2 must be same size as defined result (resultpreaklist2)!", IS2.getIsotopicsets().get(0).size(), resultpeaklist2.getPeaklist().size());
    }

    @Test
    public void testSpectrumToIsotopeClusters() {
        // Assert
        for (List<Peak> set : IS1.getIsotopicsets()) {
            IsotopicClusters IC = new IsotopicClusters(set, 0.01);
            assertEquals("Calculated IC must be size 9!", IC.getIsotopicclusters().size(), 9);
        }

        for (List<Peak> set : IS2.getIsotopicsets()) {
            IsotopicClusters IC = new IsotopicClusters(set, 0.01);
            assertEquals("Calculated IC must be size 14!", IC.getIsotopicclusters().size(), 14);
        }
    }
}
