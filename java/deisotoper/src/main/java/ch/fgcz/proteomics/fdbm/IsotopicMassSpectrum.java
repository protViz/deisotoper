package ch.fgcz.proteomics.fdbm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-18
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.dto.MassSpectrum;

public class IsotopicMassSpectrum {
    private List<IsotopicSet> isotopicmassspectrum = new ArrayList<>();

    public List<IsotopicSet> getIsotopicMassSpectrum() {
        return isotopicmassspectrum;
    }

    // TODO (LS): Who is calling this funciton?
    public void setIsotopicMassSpectrum(List<IsotopicSet> isotopicMassSpectrum) {
        this.isotopicmassspectrum = isotopicMassSpectrum;
    }

    /**
     * Constructor with MassSpectrum input, uses other constructor to create the IsotopicSet and IsotopicClusters of these IsotopicSets.
     * 
     * @param massspectrum
     * @param errortolerance
     */
    public IsotopicMassSpectrum(MassSpectrum massspectrum, double errortolerance, ScoreConfig config) {
        Peaklist peaklist = new Peaklist(massspectrum);

        constructIsotopicMassSpectrum(peaklist, errortolerance, config);
    }

    /**
     * Constructor with Peaklist input.
     * 
     * @param peaklist
     * @param errortolerance
     */
    public IsotopicMassSpectrum(Peaklist peaklist, double errortolerance, ScoreConfig config) {
        constructIsotopicMassSpectrum(peaklist, errortolerance, config);
    }

    // TODO (LS) : what is the 1.003 number here? A: "The space between any pair of adjacent isotopic peaks in each set is 1.003/z (z = 1, 2, 3)"
    private void constructIsotopicMassSpectrum(Peaklist peaklist, double errortolerance, ScoreConfig config) {
        int id = 0;
        for (int i = 0; i < peaklist.getPeaklist().size(); i++) {
            List<Peak> isotopicset = new ArrayList<>();

            while (i < peaklist.getPeaklist().size() - 1) {
                boolean trigger = false;
                double distance = peaklist.getPeaklist().get(i + 1).getMz() - peaklist.getPeaklist().get(i).getMz();

                for (int charge = 1; charge <= 3; charge++) {
                    if ((config.getDISTANCE_BETWEEN_ISOTOPIC_PEAKS() / charge) - errortolerance < distance && distance < (config.getDISTANCE_BETWEEN_ISOTOPIC_PEAKS() / charge) + errortolerance) {
                        if (isotopicset.size() == 0) {
                            isotopicset.add((peaklist.getPeaklist().get(i)));
                        }
                        isotopicset.add((peaklist.getPeaklist().get(i + 1)));
                        trigger = true;
                    }
                }

                if (trigger == false) {
                    break;
                }

                i++;
            }

            if (1 < isotopicset.size()) {
                IsotopicSet is = new IsotopicSet(isotopicset, errortolerance, id, config);
                id++;

                this.isotopicmassspectrum.add(is);

                if (isotopicset.size() == peaklist.getPeaklist().size()) {
                    break;
                }
            }
        }

    }

    // TODO (LS) move to tests.
    public static void main(String[] args) {
        String s = "TesterinoData.RData";

        String typ = "MS2 Spectrum";
        String searchengine = "mascot";
        double[] mz = { 1, 1.5, 2, 3, 3.5, 4.5, 4.83, 5.15, 5.5, 6, 6.5, 7, 8, 9, 10 };
        double[] intensity = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
        double peptidmass = 309.22;
        double rt = 38383.34;
        int chargestate = 2;
        int id = 123;

        MassSpectrometryMeasurement MSM = new MassSpectrometryMeasurement(s);
        MSM.addMS(typ, searchengine, mz, intensity, peptidmass, rt, chargestate, id);

        IsotopicMassSpectrum test = new IsotopicMassSpectrum(MSM.getMSlist().get(0), 0.01, new ScoreConfig(""));

        for (IsotopicSet is : test.getIsotopicMassSpectrum()) {
            System.out.println("(((" + is.getSetID() + ")))");
            for (IsotopicCluster ic : is.getIsotopicSet()) {
                System.out.print("((" + ic.getClusterID() + ")) (" + ic.getCharge() + ") ");
                for (Peak p : ic.getIsotopicCluster()) {
                    System.out.print(p.getMz() + " ");
                }

                System.out.println();
            }
            System.out.println();
        }
    }

    public void makeStatistics(String date, int allpeaks) {
        File file = new File("statistics_" + date.substring(0, 11) + ".csv");

        int isotopicset = this.isotopicmassspectrum.size();
        int isotopiccluster = 0;
        int peaks = 0;

        for (IsotopicSet s : this.isotopicmassspectrum) {
            isotopiccluster += s.getIsotopicSet().size();
            for (IsotopicCluster c : s.getIsotopicSet()) {
                peaks += c.getIsotopicCluster().size();
            }
        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
            out.println(isotopicset + "," + isotopiccluster + "," + peaks + "," + allpeaks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
