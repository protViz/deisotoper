package ch.fgcz.proteomics.mgf;

/**
 * @author Lucas Schmidt
 * @since 2017-11-03
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.dto.MassSpectrum;

public class WriteMGF {
    // FILE
    private static boolean writeHeader(File file, MassSpectrometryMeasurement MSM) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
            out.println("# deisotoped by fbdm algorithm at " + new SimpleDateFormat("yyyy-MM-dd:HH-mm").format(new Date()));
            out.println("COM=" + MSM.getSource());
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    // FILE
    private static boolean writeLocal(File file, MassSpectrometryMeasurement MSM) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
            for (MassSpectrum MS : MSM.getMSlist()) {
                out.println("BEGIN IONS");

                out.println("TITLE=" + MS.getTyp());
                out.println("PEPMASS=" + MS.getPeptideMass());
                out.println("CHARGE=" + MS.getChargeState() + "+");
                out.println("RTINSECONDS=" + MS.getRt());
                int i = 0;
                for (i = 0; i < MS.getMz().size(); i++) {
                    out.println(MS.getMz().get(i) + " " + MS.getIntensity().get(i));
                }
                out.println("END IONS");
            }
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    // FILE
    private static boolean writeConnect(File file, MassSpectrometryMeasurement MSM) {
        boolean header = writeHeader(file, MSM);
        boolean local = writeLocal(file, MSM);

        if (local == false || header == false) {
            return false;
        } else {
            return true;
        }
    }

    // STDOUT
    private static boolean writeHeader(MassSpectrometryMeasurement MSM) {
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        out.println("# deisotoped by fbdm algorithm at " + new SimpleDateFormat("yyyy-MM-dd:HH-mm").format(new Date()));
        out.println("COM=" + MSM.getSource());

        return true;
    }

    // STDOUT
    private static boolean writeLocal(MassSpectrometryMeasurement MSM) {
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        for (MassSpectrum MS : MSM.getMSlist()) {
            out.println("BEGIN IONS");

            out.println("TITLE=" + MS.getTyp());
            out.println("PEPMASS=" + MS.getPeptideMass());
            out.println("CHARGE=" + MS.getChargeState() + "+");
            out.println("RTINSECONDS=" + MS.getRt());
            int i = 0;
            for (i = 0; i < MS.getMz().size(); i++) {
                out.println(MS.getMz().get(i) + " " + MS.getIntensity().get(i));
            }
            out.println("END IONS");
        }

        return true;
    }

    // STDOUT
    private static boolean writeConnect(MassSpectrometryMeasurement MSM) {
        boolean header = writeHeader(MSM);
        boolean local = writeLocal(MSM);

        if (local == false || header == false) {
            return false;
        } else {
            return true;
        }
    }

    // FILE
    public static boolean write(String file, MassSpectrometryMeasurement MSM) {
        File f = new File(file);

        if (f.exists()) {
            System.err.println("WARNING: The requested output (" + file + ") exists already!");
            return false;
        }

        return writeConnect(f, MSM);
    }

    // STDOUT
    public static boolean write(MassSpectrometryMeasurement MSM) {
        return writeConnect(MSM);
    }
}
