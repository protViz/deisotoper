package test.java;

/**
 * @author Lucas Schmidt
 * @since 2017-08-29
 */

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import main.java.MassSpectrometryMeasurement.MassSpectrum;

public class TestSerializieAndDeserialize {
    @Test
    public void test() {
        String typ = "MS1 Spectrum";
        String searchengine = "mascot";
        double[] mz = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
        double[] intensity = { 4, 4, 5, 6, 6, 7, 7, 7, 8, 8 };
        double peptidmass = 309.22;
        double rt = 38383.34;
        int chargestate = 2;
        int id = 10;
        String scans = "2727";
        String title = "rdom";

        main.java.MassSpectrometryMeasurement.addMSM(typ, searchengine, mz, intensity, peptidmass, rt, chargestate, id, scans, title);

        main.java.GsonMSM.serializeMSMToJson("unittest.json", main.java.MassSpectrometryMeasurement.getMSMlist());

        List<MassSpectrum> resultlist = main.java.GsonMSM.deserializeJsonToMSM("unittest.json");

        assertEquals("Size must be correct!", resultlist.size(), main.java.MassSpectrometryMeasurement.getMSMlist().size());
        assertEquals("Mz length must be correct!", resultlist.get(0).getMz().length, main.java.MassSpectrometryMeasurement.getMSMlist().get(0).getMz().length);
    }
}
