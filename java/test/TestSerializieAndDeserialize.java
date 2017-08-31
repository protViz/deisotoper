
/**
 * @author Lucas Schmidt
 * @since 2017-08-29
 */

import static org.junit.Assert.*;
import java.util.List;
import org.junit.Test;

public class TestSerializieAndDeserialize {
    @Test
    public void test() {
        String source = "tersterino";
        int id = 122;
        String typ = "MS1 Spectrum";
        String searchengine = "mascot";
        double[] mz = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
        double[] intensity = { 4, 4, 5, 6, 6, 7, 7, 7, 8, 8 };
        double peptidmass = 309.22;
        double rt = 38383.34;
        int chargestate = 2;

        main.java.MassSpectrometryMeasurement.createMSM(source, typ, searchengine, mz, intensity, peptidmass, rt, chargestate, id);

        String typ2 = "MS2 Spectrum";
        String searchengine2 = "mascot";
        double[] mz2 = { 2, 2, 4, 4, 5, 6, 8, 8, 10, 11, 15 };
        double[] intensity2 = { 2, 3, 4, 6, 7, 7, 7, 8, 9, 9 };
        double peptidmass2 = 203.23;
        double rt2 = 7473.32;
        int chargestate2 = 2;

        main.java.MassSpectrometryMeasurement.createMSM(source, typ2, searchengine2, mz2, intensity2, peptidmass2, rt2, chargestate2, id);
        main.java.MassSpectrometryMeasurement.createMSM(source, typ2, searchengine2, mz2, intensity2, peptidmass2, rt2, chargestate2, id);

        main.java.MassSpectrometryMeasurement.getMSlist().remove(2);

        String typ3 = "MS2 Spectrum";
        String searchengine3 = "mascot";
        double[] mz3 = { 2, 2, 4, 4, 5, 5, 5, 5, 10, 11, 15 };
        double[] intensity3 = { 5, 5, 4, 5, 7, 7, 7, 8, 9, 9 };
        double peptidmass3 = 2303.23;
        double rt3 = 74733.32;
        int chargestate3 = 4;

        main.java.MassSpectrometryMeasurement.createMSM(source, typ3, searchengine3, mz3, intensity3, peptidmass3, rt3, chargestate3, id);

        main.java.SerializeMSM.serializeMSMToJson("unittest.json", main.java.MassSpectrometryMeasurement.getMSlist());

        List<main.java.MassSpectrum> resultlist = main.java.SerializeMSM.deserializeJsonToMSM("unittest.json");

        assertEquals("Size must be correct!", resultlist.size(), main.java.MassSpectrometryMeasurement.getMSlist().size());
        assertEquals("Mz length must be correct!", resultlist.get(0).getMz().length, main.java.MassSpectrometryMeasurement.getMSlist().get(0).getMz().length);
    }
}
