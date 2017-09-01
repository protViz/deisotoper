
/**
 * @author Lucas Schmidt
 * @since 2017-08-22
 */

import java.util.ArrayList;
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

    public MassSpectrometryMeasurement(String src) {
        List<MassSpectrum> list = new ArrayList<>();
        this.setSource(src);
        this.setMSlist(list);
    }

    // public static MassSpectrometryMeasurement createMSM(String src) {
    // MassSpectrometryMeasurement msm = new MassSpectrometryMeasurement(src);
    // return msm;
    // }

    public void addMS(String typ, String searchengine, double[] mz, double[] intensity, double peptidmass, double rt, int chargestate, int id) {
        this.getMSlist().add(new MassSpectrum(typ, searchengine, mz, intensity, peptidmass, rt, chargestate, id));
    }

    public static void main(String[] args) {
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
        double[] intensity2 = { 65, 44, 23, 88, 666, 451, 44, 22, 111, 1000 };
        double peptidmass2 = 203.23;
        double rt2 = 58333.35;
        int chargestate2 = 2;
        int id2 = 124;

        MassSpectrometryMeasurement test = new MassSpectrometryMeasurement(s);
        test.addMS(typ, searchengine, mz, intensity, peptidmass, rt, chargestate, id);
        test.addMS(typ2, searchengine2, mz2, intensity2, peptidmass2, rt2, chargestate2, id2);

        System.out.println(mz.length + " " + test.getMSlist().get(0).getMz());

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