
/**
 * @author Lucas Schmidt
 * @since 2017-08-23
 */

import java.util.ArrayList;
import java.util.List;

public class XICMSM {
    public static List<String> xicMSM(List<MassSpectrometryMeasurement> list) {
        List<String> slist = new ArrayList<String>();

        for (MassSpectrometryMeasurement e : list) {
            String summary = null;
            double intensitysum = 0;
            double rt = 0;

            for (double i : e.getIntensity()) {
                intensitysum += i;
            }
            rt = e.getRt();

            summary = intensitysum + ", " + rt;
            slist.add(summary);
        }

        return slist;
    }

    public static void main(String[] args) {
        String typ = "MS1 Spectrum";
        String searchengine = "mascot";
        double[] mz = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
        double[] intensity = { 4, 4, 5, 6, 6, 7, 7, 7, 8, 8 };
        double peptidmass = 309.22;
        double rt = 38383.34;
        int chargestate = 2;

        MassSpectrometryMeasurement.addMSM(typ, searchengine, mz, intensity, peptidmass, rt, chargestate);

        String typ2 = "MS2 Spectrum";
        String searchengine2 = "mascot";
        double[] mz2 = { 2, 2, 4, 4, 5, 6, 8, 8, 10, 11, 15 };
        double[] intensity2 = { 2, 3, 4, 6, 7, 7, 7, 8, 9, 9 };
        double peptidmass2 = 203.23;
        double rt2 = 7473.32;
        int chargestate2 = 2;

        MassSpectrometryMeasurement.addMSM(typ2, searchengine2, mz2, intensity2, peptidmass2, rt2, chargestate2);

        chargestate2 = 3;
        MassSpectrometryMeasurement.addMSM(typ2, searchengine2, mz2, intensity2, peptidmass2, rt2, chargestate2);

        String typ3 = "MS2 Spectrum";
        String searchengine3 = "mascot";
        double[] mz3 = { 2, 2, 4, 4, 5, 5, 5, 5, 10, 11, 15 };
        double[] intensity3 = { 5, 5, 4, 5, 7, 7, 7, 8, 9, 9 };
        double peptidmass3 = 2303.23;
        double rt3 = 74733.32;
        int chargestate3 = 4;

        MassSpectrometryMeasurement.addMSM(typ3, searchengine3, mz3, intensity3, peptidmass3, rt3, chargestate3);
        
        List<String> r = xicMSM(MassSpectrometryMeasurement.getMSMlist());
        
        for (String s : r) {
            System.out.println(s);
        }
    }
}
