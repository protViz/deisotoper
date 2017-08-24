
/**
 * @author Lucas Schmidt
 * @since 2017-08-22
 */

import java.util.ArrayList;
import java.util.List;

public class MassSpectrometryMeasurement {
    public static List<MassSpectrum> MSMlist = new ArrayList<MassSpectrum>();

    public static List<MassSpectrum> getMSMlist() {
        return MSMlist;
    }

    public static void setMSMlist(List<MassSpectrum> list) {
        MSMlist = list;
    }

    public static class MassSpectrum {
        private String typ;
        private String searchengine;
        private double[] mz;
        private double[] intensity;
        private double peptidmass;
        private double rt;
        private int chargestate;

        public String getTyp() {
            return typ;
        }

        public void setTyp(String typ) {
            this.typ = typ;
        }

        public String getSearchEngine() {
            return searchengine;
        }

        public void setSearchEngine(String searchengine) {
            this.searchengine = searchengine;
        }

        public double[] getMz() {
            return mz;
        }

        public void setMz(double[] mz) {
            this.mz = mz;
        }

        public double[] getIntensity() {
            return intensity;
        }

        public void setIntensity(double[] intensity) {
            this.intensity = intensity;
        }

        public double getPeptidMass() {
            return peptidmass;
        }

        public void setPeptidMass(double peptidmass) {
            this.peptidmass = peptidmass;
        }

        public double getRt() {
            return rt;
        }

        public void setRt(double rt) {
            this.rt = rt;
        }

        public int getChargeState() {
            return chargestate;
        }

        public void setChargeState(int chargestate) {
            this.chargestate = chargestate;
        }
    }

    public static List<MassSpectrum> addMSM(String typ, String searchengine, double[] mz, double[] intensity, double peptidmass, double rt, int chargestate) {
        MassSpectrum MS = new MassSpectrum();

        MS.setTyp(typ);
        MS.setSearchEngine(searchengine);
        MS.setMz(mz);
        MS.setIntensity(intensity);
        MS.setPeptidMass(peptidmass);
        MS.setRt(rt);
        MS.setChargeState(chargestate);

        MSMlist.add(MS);

        return MSMlist;
    }

    // For testing
    public static void main(String[] args) {
        String typ = "MS1 Spectrum";
        String searchengine = "mascot";
        double[] mz = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
        double[] intensity = { 4, 4, 5, 6, 6, 7, 7, 7, 8, 8 };
        double peptidmass = 309.22;
        double rt = 38383.34;
        int chargestate = 2;

        addMSM(typ, searchengine, mz, intensity, peptidmass, rt, chargestate);

        String typ2 = "MS2 Spectrum";
        String searchengine2 = "mascot";
        double[] mz2 = { 2, 2, 4, 4, 5, 6, 8, 8, 10, 11, 15 };
        double[] intensity2 = { 2, 3, 4, 6, 7, 7, 7, 8, 9, 9 };
        double peptidmass2 = 203.23;
        double rt2 = 7473.32;
        int chargestate2 = 2;

        addMSM(typ2, searchengine2, mz2, intensity2, peptidmass2, rt2, chargestate2);

        chargestate2 = 3;
        addMSM(typ2, searchengine2, mz2, intensity2, peptidmass2, rt2, chargestate2);

        MSMlist.remove(2);

        String typ3 = "MS2 Spectrum";
        String searchengine3 = "mascot";
        double[] mz3 = { 2, 2, 4, 4, 5, 5, 5, 5, 10, 11, 15 };
        double[] intensity3 = { 5, 5, 4, 5, 7, 7, 7, 8, 9, 9 };
        double peptidmass3 = 2303.23;
        double rt3 = 74733.32;
        int chargestate3 = 4;

        addMSM(typ3, searchengine3, mz3, intensity3, peptidmass3, rt3, chargestate3);

        for (MassSpectrum element : MSMlist) {
            System.out.print(element.typ + " " + element.searchengine + " " + element.rt + " " + element.peptidmass + " " + element.chargestate);
            System.out.println();
        }

        // SerializeMSM.serializeMSM();
    }
}
