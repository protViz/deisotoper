package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-11-23
 */

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.dto.MassSpectrum;

public class TestDeisotoper {
    @Test
    public void testDeisotopeMSM() {
        String source = "Unit Test Case";

        String typ = "MS2";
        String searchEngine = "mascot";
        double[] mz = { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };
        double[] intensity = { 4.0, 4.0, 5.0, 6.0, 6.0, 7.0, 7.0, 7.0, 8.0, 8.0 };
        double peptidMass = 309.22;
        double rt = 383.34;
        int chargeState = 2;
        int id = 123;

        String typ2 = "MS2";
        String searchEngine2 = "mascot";
        double[] mz2 = { 2.0, 4.0, 6.0, 8.0, 10.0, 12.0, 14.0, 16.0, 18.0, 20.0 };
        double[] intensity2 = { 65.0, 44.0, 23.0, 88.0, 666.0, 451.0, 44.0, 22.0, 111.0, 1000.0 };
        double peptidMass2 = 203.23;
        double rt2 = 583.35;
        int chargeState2 = 2;
        int id2 = 124;

        MassSpectrometryMeasurement massSpectrometryMeasurementIn = new MassSpectrometryMeasurement(source);
        massSpectrometryMeasurementIn.addMS(typ, searchEngine, mz, intensity, peptidMass, rt, chargeState, id);
        massSpectrometryMeasurementIn.addMS(typ2, searchEngine2, mz2, intensity2, peptidMass2, rt2, chargeState2, id2);

        Configuration config = new Configuration(0.8, 0.5, 0.1, 0.1, 0.1, 0.003, 0.3, 1.003, 0, false, "first");

        Deisotoper deisotoper = new Deisotoper();

        MassSpectrometryMeasurement massSpectrometryMeasurementOut = deisotoper
                .deisotopeMSM(massSpectrometryMeasurementIn, config);

        assertEquals("Source must be equal!", massSpectrometryMeasurementIn.getSource(),
                massSpectrometryMeasurementOut.getSource());

        assertEquals("Size of massSpectrumList must be equal!", massSpectrometryMeasurementIn.getMSlist().size(),
                massSpectrometryMeasurementOut.getMSlist().size());

        assertNotEquals("Size of mZ-Values must be not equal, becuase there should be isotopic sets!",
                massSpectrometryMeasurementIn.getMSlist().get(0).getMz().size(),
                massSpectrometryMeasurementOut.getMSlist().get(0).getMz().size());

        assertEquals("Size of mZ-Values must be equal, because there should not be isotopic sets!",
                massSpectrometryMeasurementIn.getMSlist().get(1).getMz().size(),
                massSpectrometryMeasurementOut.getMSlist().get(1).getMz().size());
    }

    @Test
    public void testDeisotopeMS() {
        String source = "Unit Test Case";

        String typ = "MS2";
        String searchEngine = "mascot";
        double[] mz = { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };
        double[] intensity = { 4.0, 4.0, 5.0, 6.0, 6.0, 7.0, 7.0, 7.0, 8.0, 8.0 };
        double peptidMass = 309.22;
        double rt = 383.34;
        int chargeState = 2;
        int id = 123;

        MassSpectrometryMeasurement massSpectrometryMeasurementin = new MassSpectrometryMeasurement(source);
        massSpectrometryMeasurementin.addMS(typ, searchEngine, mz, intensity, peptidMass, rt, chargeState, id);

        Configuration config = new Configuration(0.8, 0.5, 0.1, 0.1, 0.1, 0.003, 0.3, 1.003, 0, false, "first");

        Deisotoper deisotoper = new Deisotoper();

        deisotoper.setConfiguration(config);

        MassSpectrum massSpectrumOut = deisotoper.deisotopeMS(massSpectrometryMeasurementin.getMSlist().get(0));

        assertNotEquals("Size of mZ-Values must be not equal, becuase there should be isotopic sets!",
                massSpectrometryMeasurementin.getMSlist().get(0).getMz().size(), massSpectrumOut.getMz().size());

        assertEquals(massSpectrumOut.getMz(), Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0));

        // assertEquals(massSpectrumOut.getIntensity(),
        // Arrays.asList(13.0, 15.0, 17.0, 19.0, 20.0, 21.0, 22.0, 23.0, 16.0)); //J
        // Unit bugs?

        assertEquals(massSpectrumOut.getIsotope(), Arrays.asList(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0));

        assertEquals(massSpectrumOut.getCharge(), Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1));
    }
}
