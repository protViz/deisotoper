package ch.fgcz.proteomics.fdbm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-19
 */

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;

public class Deisotope {
    public static void main(String[] args) {
        String s = "TesterinoData.RData";

        String typ = "MS2 Spectrum";
        String searchengine = "mascot";
        double[] mz = { 0.2, 1.0, 2.0, 2.5, 3.0, 10.0 };
        double[] intensity = { 0.5, 2.0, 1.0, 1.0, 1.0, 3.0 };
        double peptidmass = 309.22;
        double rt = 38383.34;
        int chargestate = 2;
        int id = 123;

        MassSpectrometryMeasurement MSM = new MassSpectrometryMeasurement(s);
        MSM.addMS(typ, searchengine, mz, intensity, peptidmass, rt, chargestate, id);

        IsotopicMassSpectrum test = new IsotopicMassSpectrum(MSM.getMSlist().get(0), 0.01);

        StringBuilder sb = new StringBuilder();
        StringBuilder sbs = new StringBuilder();
        StringBuilder sbe = new StringBuilder();

        String linesep = System.getProperty("line.separator");

        for (IsotopicSet i : test.getIsotopicMassSpectrum()) {
            i.getIsotopicSet().add(new IsotopicCluster("start"));
            i.getIsotopicSet().add(new IsotopicCluster("end"));

            for (IsotopicCluster n : i.getIsotopicSet()) {
                for (IsotopicCluster m : i.getIsotopicSet()) {
                    String color = ch.fgcz.proteomics.fdbm.Connection.calculateConnection(n, m);
                    if (color != null && n.getIsotopicCluster() != null && m.getIsotopicCluster() != null) {
                        for (Peak x : n.getIsotopicCluster()) {
                            sb.append(x.getMz() + " ");
                        }
                        sb.append("--" + color + "-- ");
                        for (Peak x : m.getIsotopicCluster()) {
                            sb.append(x.getMz() + " ");
                        }
                        sb.append(linesep);
                    }
                    if (color != null && n.getIsotopicCluster() == null) {
                        sbs.append("start ");
                        sbs.append("--" + color + "-- ");
                        for (Peak x : m.getIsotopicCluster()) {
                            sbs.append(x.getMz() + " ");
                        }
                        sbs.append(linesep);
                    }
                    if (color != null && m.getIsotopicCluster() == null) {
                        for (Peak x : n.getIsotopicCluster()) {
                            sbe.append(x.getMz() + " ");
                        }
                        sbe.append("--" + color + "-- ");
                        sbe.append("end");
                        sbe.append(linesep);
                    }
                }
            }
        }

        System.out.println(sbs.toString());
        System.out.println(sb.toString());
        System.out.println(sbe.toString());
    }
}
