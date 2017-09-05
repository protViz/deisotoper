
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

    /**
     * Sets up the starting parameters.
     * 
     * The examples are constructed as Peaklist Objects. From those Peaklist Objects you can construct the IsotopicSets with a given error tolerance.
     * 
     * Also there is a result-pattern for each example, to compare the output (IsotopicSets) with the result-pattern.
     */
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

    /**
     * Checks if the calculated IsotopicSets are not empty and if they match the result-pattern.
     */
    @Test
    public void testIsotopeSetConstruction() {
        // Assert
        assertNotNull(IS1);
        assertEquals("Calculated IS1 must be same size as defined result (resultpreaklist1)!", IS1.getIsotopicsets().get(0).size(), resultpeaklist1.getPeaklist().size());
        assertPeaklistEquals(IS1.getIsotopicsets().get(0), resultpeaklist1.getPeaklist());

        assertNotNull(IS2);
        assertEquals("Calculated IS2 must be same size as defined result (resultpreaklist2)!", IS2.getIsotopicsets().get(0).size(), resultpeaklist2.getPeaklist().size());
        assertPeaklistEquals(IS2.getIsotopicsets().get(0), resultpeaklist2.getPeaklist());
    }

    /**
     * Checks if the calculated IsotopicClusters not empty and if there is the correct number of IsotopicCluster.
     */
    @Test
    public void testIsotopeClustersConstruction() {
        // Assert
        for (List<Peak> set : IS1.getIsotopicsets()) {
            IsotopicClusters IC = new IsotopicClusters(set, 0.01);
            assertNotNull(IC);
            assertEquals("Calculated IC must be size 9!", IC.getIsotopicclusters().size(), 9);
        }

        for (List<Peak> set : IS2.getIsotopicsets()) {
            IsotopicClusters IC = new IsotopicClusters(set, 0.01);
            assertNotNull(IC);
            assertEquals("Calculated IC must be size 14!", IC.getIsotopicclusters().size(), 14);
        }
    }

    /**
     * Compares two lists of Peaks with each other.
     * 
     * The function is a additional function to the existing assert functions from the JUnit package.
     * 
     * @param list
     * @param list2
     */
    private void assertPeaklistEquals(List<Peak> list, List<Peak> list2) {
        for (int i = 0; i < list.size() || i < list2.size(); i++) {
            boolean b = false;

            if (list.get(i).getMz() == list2.get(i).getMz() && list.get(i).getIntensity() == list2.get(i).getIntensity()) {
                b = true;
            }

            assertTrue(b);
        }
    }
}
