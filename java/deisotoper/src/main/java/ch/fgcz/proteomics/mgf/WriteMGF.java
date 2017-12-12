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

    public static boolean write(String fileName, MassSpecMeasure massSpectrometryMeasurement) {
        File f = new File(fileName);

        return writeConnect(f, massSpectrometryMeasurement);
    }

    private static boolean writeHeader(File fileName, MassSpecMeasure massSpectrometryMeasurement) {
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

        return true;
    }

    private static boolean writeLocal(File fileName, MassSpecMeasure massSpectrometryMeasurement) {
        PrintWriter out = null;

        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));

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
            LOGGER.log(Level.SEVERE, e.toString(), e);
        } finally {
            if (out != null) {
                out.close();
            }
        }

        return true;
    }

    private static boolean writeConnect(File fileName, MassSpecMeasure massSpectrometryMeasurement) {
        boolean header = writeHeader(fileName, massSpectrometryMeasurement);
        boolean local = writeLocal(fileName, massSpectrometryMeasurement);

        if (local == false || header == false) {
            return false;
        } else {
            return true;
        }
    }
}
