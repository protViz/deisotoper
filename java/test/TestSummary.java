
/**
 * @author Lucas Schmidt
 * @since 2017-08-29
 */

import static org.junit.Assert.*;
import org.junit.Test;

public class TestSummary {
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

        System.out.println(main.java.Summary.makeSummary(main.java.MassSpectrometryMeasurement.getMSlist()));
        String result = "SpectrumID, Attribute, Value\n" + "122,spectrum_type,MS1 Spectrum\n" + "122,nr_of_peaks,11\n" + "122,min_intensity,4.0\n" + "122,max_intensity,8.0\n"
                + "122,sum_intensity,62.0\n" + "122,min_mass,1.0\n" + "122,max_mass,11.0\n" + "122,min_peak_distance,1.0\n" + "122,max_peak_distance,1.0\n" + "122,precursor_charge,2\n"
                + "122,precursor_mass,309.22\n" + "122,rt_in_seconds,38383.34\n" + "122,spectrum_type,MS2 Spectrum\n" + "122,nr_of_peaks,11\n" + "122,min_intensity,2.0\n" + "122,max_intensity,9.0\n"
                + "122,sum_intensity,62.0\n" + "122,min_mass,2.0\n" + "122,max_mass,15.0\n" + "122,min_peak_distance,0.0\n" + "122,max_peak_distance,4.0\n" + "122,precursor_charge,2\n"
                + "122,precursor_mass,203.23\n" + "122,rt_in_seconds,7473.32\n" + "122,spectrum_type,MS2 Spectrum\n" + "122,nr_of_peaks,11\n" + "122,min_intensity,4.0\n" + "122,max_intensity,9.0\n"
                + "122,sum_intensity,66.0\n" + "122,min_mass,2.0\n" + "122,max_mass,15.0\n" + "122,min_peak_distance,0.0\n" + "122,max_peak_distance,5.0\n" + "122,precursor_charge,4\n"
                + "122,precursor_mass,2303.23\n" + "122,rt_in_seconds,74733.32\n";
        assertEquals("Summary must be correct!", main.java.Summary.makeSummary(main.java.MassSpectrometryMeasurement.getMSlist()), result);
    }
}
