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
