package main.java;

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

    /**
     * Adds a MassSpectrum to a List of MassSpectrum.
     * 
     * @param typ
     * @param searchengine
     * @param mz
     * @param intensity
     * @param peptidmass
     * @param rt
     * @param chargestate
     * @param id
     * @param scans
     * @param title
     * @return List of MassSpectrum
     */
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
}
