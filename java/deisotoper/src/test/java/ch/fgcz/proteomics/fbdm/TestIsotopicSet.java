package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-10-26
 */

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;

public class TestIsotopicSet {
    @Test
    public void testIsotopeSet() {
        String source = "Unit Test Case";
        String typ = "MS2";
        String searchEngine = "mascot";
        double[] mz = { 0.2, 1.001, 2.002, 2.503, 3.003, 10.0 };
        double[] intensity = { 0.5, 2.0, 1.0, 1.0, 1.0, 3.0 };
        double peptidMass = 309.22;
        double rt = 383.34;
        int chargeState = 2;
        int id = 123;

        Configuration config = new Configuration();

        List<Peak> isotopicSet = Arrays.asList(new Peak(1.001, 2.0, 1), new Peak(2.002, 1.0, 2),
                new Peak(2.503, 1.0, 3), new Peak(3.003, 1.0, 4));

        MassSpectrometryMeasurement massSpectrometryMeasurement = new MassSpectrometryMeasurement(source);
        massSpectrometryMeasurement.addMS(typ, searchEngine, mz, intensity, peptidMass, rt, chargeState, id);

        IsotopicSet isotopicSetResult = new IsotopicSet(massSpectrometryMeasurement.getMSlist().get(0), isotopicSet, 0,
                config);

        String A = "(0) [ 1.001 2.002 ]";
        String B = "(1) [ 1.001 2.002 3.003 ]";
        String C = "(2) [ 2.002 2.503 ]";
        String D = "(3) [ 2.002 2.503 3.003 ]";
        String E = "(4) [ 2.002 3.003 ]";
        String F = "(5) [ 2.503 3.003 ]";

        assertEquals(isotopicSetResult.getIsotopicSet().size(), 7);

        assertEquals(isotopicSetResult.getIsotopicSet().get(0).toString(), A);
        assertEquals(isotopicSetResult.getIsotopicSet().get(1).toString(), B);
        assertEquals(isotopicSetResult.getIsotopicSet().get(2).toString(), C);
        assertEquals(isotopicSetResult.getIsotopicSet().get(3).toString(), D);
        assertEquals(isotopicSetResult.getIsotopicSet().get(4).toString(), E);
        assertEquals(isotopicSetResult.getIsotopicSet().get(5).toString(), F);
    }
}
