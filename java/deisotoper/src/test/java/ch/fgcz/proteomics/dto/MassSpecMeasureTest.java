package ch.fgcz.proteomics.dto;

/**
 * @author Lucas Schmidt
 * @since 2017-08-31
 */

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MassSpecMeasureTest {
    @Test
    public void testMassSpectrometryMeasurementCreation() {
        String s = "Unit Test Case";

        double[] mz = { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };
        double[] intensity = { 4.0, 4.0, 5.0, 6.0, 6.0, 7.0, 7.0, 7.0, 8.0, 8.0 };
        double peptidMass = 309.22;
        int chargeState = 2;
        int id = 123;

        double[] mz2 = { 2.0, 4.0, 6.0, 8.0, 10.0, 12.0, 14.0, 16.0, 18.0, 20.0 };
        double[] intensity2 = { 65.0, 44.0, 23.0, 88.0, 666.0, 451.0, 44.0, 22.0, 111.0, 1000.0 };
        double peptidMass2 = 203.23;
        int chargeState2 = 2;
        int id2 = 124;

        MassSpecMeasure massSpectrometryMeasurement = new MassSpecMeasure(s);
        massSpectrometryMeasurement.addMS(mz, intensity, peptidMass, chargeState, id);
        massSpectrometryMeasurement.addMS(mz2, intensity2, peptidMass2, chargeState2, id2);

        assertEquals("Source must be right!", "Unit Test Case", massSpectrometryMeasurement.getSource());

        for (MassSpectrum i : massSpectrometryMeasurement.getMSlist()) {
            assertEquals("Length of List must be correct", 10, i.getMz().size());
            assertEquals("Length of List must be correct", 10, i.getIntensity().size());
        }

        MassSpecMeasureSummary.summarize(massSpectrometryMeasurement);
    }
}
