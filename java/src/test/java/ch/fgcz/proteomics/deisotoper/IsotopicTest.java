
/**
 * @author Lucas Schmidt
 * @since 2017-09-05
 * THIS CODE IS UNDER CONSTRUCTION
 */

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class IsotopicTest {
    @Test
    public void testIsotopeSet() {
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

        // Create MSM
        MassSpectrometryMeasurement MSM = new MassSpectrometryMeasurement(source);
        MSM.addMS(typ1, searchengine1, mz1, intensity1, peptidmass1, rt1, chargestate1, id1);
        MSM.addMS(typ2, searchengine2, mz2, intensity2, peptidmass2, rt2, chargestate2, id2);

        // Create IsotopicSets for this MSM (Expected results)
        List<List<Double>> resultlist1 = new ArrayList<>();
        List<Double> resultresultlist1 = Arrays.asList(1.33, 1.66, 2.0, 2.5, 3.5, 4.0, 5.0, 6.0);
        resultlist1.add(resultresultlist1);
        List<List<Double>> resultlist2 = new ArrayList<>();
        List<Double> resultresultlist2 = Arrays.asList(1.0, 2.0, 3.0, 3.5, 4.0, 4.5, 5.0, 6.0, 6.5, 7.0);
        resultlist2.add(resultresultlist2);

        // Create Peaklist of the two MS
        double errortolerance = 0.01;
        Peaklist peaklist1 = new Peaklist(MSM.getMSlist().get(0));
        Peaklist peaklist2 = new Peaklist(MSM.getMSlist().get(1));

        // Calculate IsotopicSet
        IsotopicSets IS1 = new IsotopicSets(peaklist1, errortolerance);
        assertEquals("Calculated IsotopicSet must be same size as defined Result!", IS1.getIsotopicsets().size(), resultlist1.size());

        IsotopicSets IS2 = new IsotopicSets(peaklist2, errortolerance);
        assertEquals("Calculated IsotopicSet must be same size as defined Result!", IS2.getIsotopicsets().size(), resultlist2.size());

    }

    @Test
    public void testSpectrumToIsotopeClusters() {

    }

}
