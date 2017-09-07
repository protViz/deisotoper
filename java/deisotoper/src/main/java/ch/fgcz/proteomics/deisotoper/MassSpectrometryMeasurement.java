package ch.fgcz.proteomics.deisotoper;

/**
 * @author Lucas Schmidt
 * @since 2017-08-22
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MassSpectrometryMeasurement {
    private List<MassSpectrum> MSlist = new ArrayList<MassSpectrum>();
    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<MassSpectrum> getMSlist() {
        return MSlist;
    }

    public void setMSlist(List<MassSpectrum> list) {
        this.MSlist = list;
    }

    /**
     * Constructs a MassSpectrometryMeasurement object.
     * 
     * @param src
     */
    public MassSpectrometryMeasurement(String src) {
        List<MassSpectrum> list = new ArrayList<>();
        this.setSource(src);
        this.setMSlist(list);
    }

    /**
     * Fills the generated List<MassSpectrum> with input data.
     * 
     * @param typ
     * @param searchengine
     * @param mz
     * @param intensity
     * @param peptidmass
     * @param rt
     * @param chargestate
     * @param id
     */
    public void addMS(String typ, String searchengine, List<Double> mz, List<Double> intensity, double peptidmass, double rt, int chargestate, int id) {
        this.getMSlist().add(new MassSpectrum(typ, searchengine, mz, intensity, peptidmass, rt, chargestate, id));
    }

    public static void main(String[] args) {
        String s = "TesterinoData.RData";

        String typ = "MS2 Spectrum";
        String searchengine = "mascot";
        List<Double> mz = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0);
        List<Double> intensity = Arrays.asList(4.0, 4.0, 5.0, 6.0, 6.0, 7.0, 7.0, 7.0, 8.0, 8.0);
        double peptidmass = 309.22;
        double rt = 38383.34;
        int chargestate = 2;
        int id = 123;

        String typ2 = "MS2 Spectrum";
        String searchengine2 = "mascot";
        List<Double> mz2 = Arrays.asList(2.0, 4.0, 6.0, 8.0, 10.0, 12.0, 14.0, 16.0, 18.0, 20.0);
        List<Double> intensity2 = Arrays.asList(65.0, 44.0, 23.0, 88.0, 666.0, 451.0, 44.0, 22.0, 111.0, 1000.0);
        double peptidmass2 = 203.23;
        double rt2 = 58333.35;
        int chargestate2 = 2;
        int id2 = 124;

        MassSpectrometryMeasurement test = new MassSpectrometryMeasurement(s);
        test.addMS(typ, searchengine, mz, intensity, peptidmass, rt, chargestate, id);
        test.addMS(typ2, searchengine2, mz2, intensity2, peptidmass2, rt2, chargestate2, id2);

        System.out.println(mz.size() + " " + test.getMSlist().get(0).getMz());

        System.out.println("Source: " + test.getSource());
        for (MassSpectrum i : test.getMSlist()) {
            System.out.println("Type: " + i.getTyp() + ", SearchEngine: " + i.getSearchEngine() + ", PeptidMass " + i.getPeptidMass() + ", Rt: " + i.getRt() + ", ChargeState: " + i.getChargeState()
                    + ", SpectrumId: " + i.getId());
            for (int j = 0; j < i.getMz().size() && j < i.getIntensity().size(); j++) {
                System.out.println("Mz: " + i.getMz().get(j) + ", Intensity: " + i.getIntensity().get(j));
            }
            System.out.println();
        }

        System.out.println(Summary.makeSummary(test));
        System.out.println();
        System.out.println(SerializeMSM.serializeMSMToJson("TestMSM.json", test));
        System.out.println();
        MassSpectrometryMeasurement m = SerializeMSM.deserializeJsonToMSM("TestMSM.json");

        System.out.println("Source: " + m.getSource());
        for (MassSpectrum i : m.getMSlist()) {
            System.out.println("Type: " + i.getTyp() + ", SearchEngine: " + i.getSearchEngine() + ", PeptidMass " + i.getPeptidMass() + ", Rt: " + i.getRt() + ", ChargeState: " + i.getChargeState()
                    + ", SpectrumId: " + i.getId());
            for (int j = 0; j < i.getMz().size() && j < i.getIntensity().size(); j++) {
                System.out.println("Mz: " + i.getMz().get(j) + ", Intensity: " + i.getIntensity().get(j));
            }
            System.out.println();
        }
    }
}