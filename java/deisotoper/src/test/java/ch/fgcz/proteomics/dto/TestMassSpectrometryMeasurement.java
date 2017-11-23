package ch.fgcz.proteomics.dto;

/**
 * @author Lucas Schmidt
 * @since 2017-08-31
 */

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestMassSpectrometryMeasurement {
    @Test
    public void testMassSpectrometryMeasurementCreation() {
        String s = "Unit Test Case";

        String typ = "MS2";
        String searchEngine = "mascot";
        double[] mz = { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };
        double[] intensity = { 4.0, 4.0, 5.0, 6.0, 6.0, 7.0, 7.0, 7.0, 8.0, 8.0 };
        double peptidMass = 309.22;
        double rt = 38383.34;
        int chargeState = 2;
        int id = 123;

        String typ2 = "MS2";
        String searchEngine2 = "mascot";
        double[] mz2 = { 2.0, 4.0, 6.0, 8.0, 10.0, 12.0, 14.0, 16.0, 18.0, 20.0 };
        double[] intensity2 = { 65.0, 44.0, 23.0, 88.0, 666.0, 451.0, 44.0, 22.0, 111.0, 1000.0 };
        double peptidMass2 = 203.23;
        double rt2 = 58333.35;
        int chargeState2 = 2;
        int id2 = 124;

        MassSpectrometryMeasurement massSpectrometryMeasurement = new MassSpectrometryMeasurement(s);
        massSpectrometryMeasurement.addMS(typ, searchEngine, mz, intensity, peptidMass, rt, chargeState, id);
        massSpectrometryMeasurement.addMS(typ2, searchEngine2, mz2, intensity2, peptidMass2, rt2, chargeState2, id2);

        assertEquals("Source must be right!", massSpectrometryMeasurement.getSource(), "Unit Test Case");

        for (MassSpectrum i : massSpectrometryMeasurement.getMSlist()) {
            assertEquals("Length of List must be correct", i.getMz().size(), 10);
            assertEquals("Length of List must be correct", i.getIntensity().size(), 10);
        }

        MassSpectrometryMeasurementSummary.makeSummary(massSpectrometryMeasurement);
    }
}
