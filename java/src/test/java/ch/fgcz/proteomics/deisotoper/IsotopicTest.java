
/**
 * @author Lucas Schmidt
 * @since 2017-09-05
 * THIS CODE IS UNDER CONSTRUCTION
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
    MassSpectrometryMeasurement MSM;

    @Before
    public void setUp() {
        String source = "IsotopicSetTestData";

        // Example 1
        String typ1 = "MS2 Spectrum";
        String searchengine1 = "mascot";
        List<Double> mz1 = Arrays.asList(0.1, 0.2, 1.33, 1.66, 2.0, 2.5, 3.5, 4.0, 5.0, 6.0, 12.0, 15.0);
        List<Double> intensity1 = Arrays.asList(7.0, 4.0, 5.0, 6.0, 6.0, 7.0, 7.0, 7.0, 8.0, 8.0, 2.0, 3.0);
        double peptidmass1 = 309.22;
        double rt1 = 383.34;
        int chargestate1 = 2;
        int id1 = 1;

        // Example 2
        String typ2 = "MS2 Spectrum";
        String searchengine2 = "mascot";
        List<Double> mz2 = Arrays.asList(0.2, 1.0, 2.0, 3.0, 3.5, 4.0, 4.5, 5.0, 6.0, 6.5, 7.0, 11.0, 12.3);
        List<Double> intensity2 = Arrays.asList(10.0, 8.0, 3.0, 5.0, 6.0, 1.0, 4.0, 2.0, 1.0, 4.0, 1.0, 2.0, 4.0);
        double peptidmass2 = 203.23;
        double rt2 = 583.35;
        int chargestate2 = 2;
        int id2 = 2;

        // Example 1 Result
        List<Double> resultmz1 = Arrays.asList(1.33, 1.66, 2.0, 2.5, 3.5, 4.0, 5.0, 6.0);
        List<Double> resultintensity1 = Arrays.asList(5.0, 6.0, 6.0, 7.0, 7.0, 7.0, 8.0, 8.0);
        resultpeaklist1 = new Peaklist(resultmz1, resultintensity1);

        // Example 2 Result
        List<Double> resultmz2 = Arrays.asList(1.0, 2.0, 3.0, 3.5, 4.0, 4.5, 5.0, 6.0, 6.5, 7.0);
        List<Double> resultintensity2 = Arrays.asList(8.0, 3.0, 5.0, 6.0, 1.0, 4.0, 2.0, 1.0, 4.0, 1.0);
        resultpeaklist2 = new Peaklist(resultmz2, resultintensity2);

        // Create MSM
        MSM = new MassSpectrometryMeasurement(source);
        MSM.addMS(typ1, searchengine1, mz1, intensity1, peptidmass1, rt1, chargestate1, id1);
        MSM.addMS(typ2, searchengine2, mz2, intensity2, peptidmass2, rt2, chargestate2, id2);

        // Create Peaklist of the two MS of MSM
        peaklist1 = new Peaklist(MSM.getMSlist().get(0));
        peaklist2 = new Peaklist(MSM.getMSlist().get(1));
    }

    @Test
    public void testIsotopeSet() {
        // setUp();
        double errortolerance = 0.01;

        // Calculate IsotopicSet
        IsotopicSets IS1 = new IsotopicSets(peaklist1, errortolerance);
        assertEquals("Calculated IS1 must be same size as defined result (resultpreaklist1)!", IS1.getIsotopicsets().get(0).size(), resultpeaklist1.getPeaklist().size());

        IsotopicSets IS2 = new IsotopicSets(peaklist2, errortolerance);
        assertEquals("Calculated IS2 must be same size as defined result (resultpreaklist2)!", IS2.getIsotopicsets().get(0).size(), resultpeaklist2.getPeaklist().size());
    }

    @Test
    public void testSpectrumToIsotopeClusters() {

    }
}
