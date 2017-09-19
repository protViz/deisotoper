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

        // for (IsotopicSet i : test.getIsotopicMassSpectrum()) {
        // for (IsotopicCluster n : i.getIsotopicSet()) {
        // for (IsotopicCluster m : i.getIsotopicSet()) {
        // String color = ch.fgcz.proteomics.fdbm.Connection.calculateConnection(n, m);
        //
        // if (color != null) {
        // n.addConnection(new Connection(n, m, color));
        // }
        // }
        // }
        // }
        //
        // for (IsotopicSet i : test.getIsotopicMassSpectrum()) {
        // for (IsotopicCluster n : i.getIsotopicSet()) {
        // for (Connection c : n.getConnection()) {
        // System.out.print("[");
        // for (Peak x : c.getTail().getIsotopicCluster()) {
        // System.out.print(" " + x.getMz() + " ");
        // }
        // System.out.print("] ---" + c.getColor() + "--- [ ");
        // for (Peak x : c.getHead().getIsotopicCluster()) {
        // System.out.print(" " + x.getMz() + " ");
        // }
        // System.out.println("]");
        // }
        // }
        // }
    }
}
