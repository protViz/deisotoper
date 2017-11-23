package ch.fgcz.proteomics.mgf;

/**
 * @author Lucas Schmidt
 * @since 2017-11-03
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.dto.MassSpectrum;

public class WriteMGF {
    public static boolean write(String fileName, MassSpectrometryMeasurement massSpectrometryMeasurement) {
        File f = new File(fileName);

        if (f.exists()) {
            System.err.println("WARNING: The requested output (" + fileName + ") exists already!");
            return false;
        }

        return writeConnect(f, massSpectrometryMeasurement);
    }

    private static boolean writeHeader(File fileName, MassSpectrometryMeasurement massSpectrometryMeasurement) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)))) {
            out.println(
                    "# deisotoped by fbdm algorithm at " + new SimpleDateFormat("yyyy-MM-dd:HH-mm").format(new Date()));
            out.println("COM=" + massSpectrometryMeasurement.getSource());
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private static boolean writeLocal(File fileName, MassSpectrometryMeasurement massSpectrometryMeasurement) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)))) {
            for (MassSpectrum MS : massSpectrometryMeasurement.getMSlist()) {
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

    private static boolean writeConnect(File fileName, MassSpectrometryMeasurement massSpectrometryMeasurement) {
        boolean header = writeHeader(fileName, massSpectrometryMeasurement);
        boolean local = writeLocal(fileName, massSpectrometryMeasurement);

        if (local == false || header == false) {
            return false;
        } else {
            return true;
        }
    }
}
