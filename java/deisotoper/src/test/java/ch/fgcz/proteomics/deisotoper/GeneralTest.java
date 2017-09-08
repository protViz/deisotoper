package ch.fgcz.proteomics.deisotoper;

/**
 * @author Lucas Schmidt
 * @since 2017-08-31
 */

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class GeneralTest {
    @Test
    public void test() {
        // Literally the same as in the main of MassSpectrometryMeasurement
        String s = "TesterinoData.RData";

        String typ = "MS2 Spectrum";
        String searchengine = "mascot";
        double[] mz = { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };
        double[] intensity = { 4.0, 4.0, 5.0, 6.0, 6.0, 7.0, 7.0, 7.0, 8.0, 8.0 };
        double peptidmass = 309.22;
        double rt = 38383.34;
        int chargestate = 2;
        int id = 123;

        String typ2 = "MS2 Spectrum";
        String searchengine2 = "mascot";
        double[] mz2 = { 2.0, 4.0, 6.0, 8.0, 10.0, 12.0, 14.0, 16.0, 18.0, 20.0 };
        double[] intensity2 = { 65.0, 44.0, 23.0, 88.0, 666.0, 451.0, 44.0, 22.0, 111.0, 1000.0 };
        double peptidmass2 = 203.23;
        double rt2 = 58333.35;
        int chargestate2 = 2;
        int id2 = 124;

        MassSpectrometryMeasurement test = new MassSpectrometryMeasurement(s);
        test.addMS(typ, searchengine, mz, intensity, peptidmass, rt, chargestate, id);
        test.addMS(typ2, searchengine2, mz2, intensity2, peptidmass2, rt2, chargestate2, id2);

        assertEquals("Source must be right!", test.getSource(), "TesterinoData.RData");

        for (MassSpectrum i : test.getMSlist()) {
            assertEquals("Length of List must be correct", i.getMz().size(), 10);
            assertEquals("Length of List must be correct", i.getIntensity().size(), 10);
        }

        Summary.makeSummary(test);
        SerializeMSM.serializeMSMToJson("TestMSM.json", test);
        MassSpectrometryMeasurement m = SerializeMSM.deserializeJsonToMSM("TestMSM.json");

        int n = 0;
        for (MassSpectrum i : m.getMSlist()) {
            assertEquals("MSM must be same!", (String) i.getTyp(), (String) test.getMSlist().get(n).getTyp());
            assertEquals("MSM must be same!", (String) i.getSearchEngine(), (String) test.getMSlist().get(n).getSearchEngine());
            n++;
        }
    }
}
