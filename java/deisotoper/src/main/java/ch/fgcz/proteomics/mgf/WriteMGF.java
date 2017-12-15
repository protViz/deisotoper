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
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.fgcz.proteomics.dto.MassSpecMeasure;
import ch.fgcz.proteomics.dto.MassSpecMeasureSerializer;
import ch.fgcz.proteomics.dto.MassSpectrum;

public class WriteMGF {
    private static final Logger LOGGER = Logger.getLogger(MassSpecMeasureSerializer.class.getName());

    private WriteMGF() {
        throw new IllegalStateException("Writer class");
    }

    public static void write(String fileName, MassSpecMeasure massSpectrometryMeasurement) {
        File f = new File(fileName);

        writeConnect(f, massSpectrometryMeasurement);
    }

    private static void writeHeader(File fileName, MassSpecMeasure massSpectrometryMeasurement) {
        PrintWriter out = null;

        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
            out.println(
                    "# deisotoped by fbdm algorithm at " + new SimpleDateFormat("yyyy-MM-dd:HH-mm").format(new Date()));
            out.println("COM=" + massSpectrometryMeasurement.getSource());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private static void writeLocal(File fileName, MassSpecMeasure massSpectrometryMeasurement) {
        PrintWriter out = null;

        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));

            for (MassSpectrum MS : massSpectrometryMeasurement.getMSlist()) {
                out.println("BEGIN IONS");

                out.println("PEPMASS=" + MS.getPeptideMass());
                out.println("CHARGE=" + MS.getChargeState() + "+");
                int i = 0;
                for (i = 0; i < MS.getMz().size(); i++) {
                    out.println(MS.getMz().get(i) + " " + MS.getIntensity().get(i));
                }
                out.println("END IONS");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private static void writeConnect(File fileName, MassSpecMeasure massSpectrometryMeasurement) {
        writeHeader(fileName, massSpectrometryMeasurement);
        writeLocal(fileName, massSpectrometryMeasurement);
    }
}
