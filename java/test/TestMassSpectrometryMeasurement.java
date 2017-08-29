package test.java;

/**
 * @author Lucas Schmidt
 * @since 2017-08-29
 */

import static org.junit.Assert.*;
import org.junit.Test;

public class TestMassSpectrometryMeasurement {
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

        String typ2 = "MS2 Spectrum";
        String searchengine2 = "mascot";
        double[] mz2 = { 2, 2, 4, 4, 5, 6, 8, 8, 10, 11, 15 };
        double[] intensity2 = { 2, 3, 4, 6, 7, 7, 7, 8, 9, 9 };
        double peptidmass2 = 203.23;
        double rt2 = 7473.32;
        int chargestate2 = 2;

        main.java.MassSpectrometryMeasurement.addMSM(typ2, searchengine2, mz2, intensity2, peptidmass2, rt2, chargestate2, id, scans, title);
        main.java.MassSpectrometryMeasurement.addMSM(typ2, searchengine2, mz2, intensity2, peptidmass2, rt2, chargestate2, id, scans, title);

        main.java.MassSpectrometryMeasurement.getMSMlist().remove(2);

        String typ3 = "MS2 Spectrum";
        String searchengine3 = "mascot";
        double[] mz3 = { 2, 2, 4, 4, 5, 5, 5, 5, 10, 11, 15 };
        double[] intensity3 = { 5, 5, 4, 5, 7, 7, 7, 8, 9, 9 };
        double peptidmass3 = 2303.23;
        double rt3 = 74733.32;
        int chargestate3 = 4;

        main.java.MassSpectrometryMeasurement.addMSM(typ3, searchengine3, mz3, intensity3, peptidmass3, rt3, chargestate3, id, scans, title);

        assertEquals("Length of List<MassSpectrum> must be 3!", main.java.MassSpectrometryMeasurement.getMSMlist().size(), 3);
        assertEquals("Mz must be correct!", mz, main.java.MassSpectrometryMeasurement.getMSMlist().get(0).getMz());
        assertEquals("Intensity must be correct!", intensity, main.java.MassSpectrometryMeasurement.getMSMlist().get(0).getIntensity());
        assertEquals("Mz must be correct!", mz3, main.java.MassSpectrometryMeasurement.getMSMlist().get(2).getMz());
        assertEquals("Intensity must be correct!", intensity3, main.java.MassSpectrometryMeasurement.getMSMlist().get(2).getIntensity());

    }
}
