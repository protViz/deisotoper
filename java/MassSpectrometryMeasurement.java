
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

    public static MassSpectrometryMeasurement createMSM(String src, List<List<Object>> data) {
        MassSpectrometryMeasurement MSM = new MassSpectrometryMeasurement();

        List<MassSpectrum> msmlist = new ArrayList<>();

        for (List<Object> l : data) {
            String typ = (String) l.get(0);
            String searchengine = (String) l.get(1);
            double[] mz = (double[]) l.get(2);
            double[] intensity = (double[]) l.get(3);
            double peptidmass = (double) l.get(4);
            double rt = (double) l.get(5);
            int chargestate = (int) l.get(6);
            int id = (int) l.get(7);
            msmlist.add(new MassSpectrum(typ, searchengine, mz, intensity, peptidmass, rt, chargestate, id));
        }

        MSM.setSource(src);
        MSM.setMSlist(msmlist);

        return MSM;
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
        double[] intensity2 = { 1.2, 2.3, 5.5, 6.5, 6.8, 8.1, 8.2, 9, 10, 11 };
        double peptidmass2 = 203.23;
        double rt2 = 58333.35;
        int chargestate2 = 2;
        int id2 = 124;

        List<Object> l1 = RJavaMassSpectrometryMeasurement.putArgsIntoList(typ, searchengine, mz, intensity, peptidmass, rt, chargestate, id);
        List<Object> l2 = RJavaMassSpectrometryMeasurement.putArgsIntoList(typ2, searchengine2, mz2, intensity2, peptidmass2, rt2, chargestate2, id2);

        RJavaMassSpectrometryMeasurement.putListIntoList(l1);
        RJavaMassSpectrometryMeasurement.putListIntoList(l2);

        MassSpectrometryMeasurement test = createMSM(s, RJavaMassSpectrometryMeasurement.getListlist());

        System.out.println(mz.length + " " + (test.getMSlist().get(0).getPeaklist().size() + 1));

        System.out.println("Source: " + test.getSource());
        for (MassSpectrum i : test.getMSlist()) {
            System.out.println("Type: " + i.getTyp() + ", SearchEngine: " + i.getSearchEngine() + ", PeptidMass " + i.getPeptidMass() + ", Rt: " + i.getRt() + ", ChargeState: " + i.getChargeState()
                    + ", SpectrumId: " + i.getId());
            for (Peak j : i.getPeaklist()) {
                System.out.println("Intensity: " + j.getIntensity() + ", Mz: " + j.getMz());
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
            for (Peak j : i.getPeaklist()) {
                System.out.println("Intensity: " + j.getIntensity() + ", Mz: " + j.getMz());
            }
            System.out.println();
        }
    }
}