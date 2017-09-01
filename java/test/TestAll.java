
/**
 * @author Lucas Schmidt
 * @since 2017-08-31
 */

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class TestAll {
    @Test
    public void test() {
        String s = "TesterinoData.RData";

        String typ = "MS2 Spectrum";
        String searchengine = "mascot";
        double[] mz = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        double[] intensity = { 4, 4, 5, 6, 6, 7, 7, 7, 8, 8 };
        double peptidmass = 309.22;
        double rt = 38383.34;
        int chargestate = 2;
        int id = 123;

        String typ2 = "MS2 Spectrum";
        String searchengine2 = "mascot";
        double[] mz2 = { 2, 4, 6, 8, 10, 12, 14, 16, 18, 20 };
        double[] intensity2 = { 1.2, 2.3, 5.5, 6.5, 6.8, 8.1, 8.2, 9, 10, 11 };
        double peptidmass2 = 203.23;
        double rt2 = 58333.35;
        int chargestate2 = 2;
        int id2 = 124;

        List<Object> l1 = RJavaMassSpectrometryMeasurement.putArgsIntoList(typ, searchengine, mz, intensity, peptidmass, rt, chargestate, id);
        List<Object> l2 = RJavaMassSpectrometryMeasurement.putArgsIntoList(typ2, searchengine2, mz2, intensity2, peptidmass2, rt2, chargestate2, id2);

        RJavaMassSpectrometryMeasurement.putListIntoList(l1);
        RJavaMassSpectrometryMeasurement.putListIntoList(l2);

        MassSpectrometryMeasurement test = MassSpectrometryMeasurement.createMSM(s, RJavaMassSpectrometryMeasurement.getListlist());

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
