package ch.fgcz.proteomics.mgf;

/**
 * @author Lucas Schmidt
 * @since 2017-11-06
 */

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.dto.MassSpectrum;

public class WriteStdOut {
    public static void write(MassSpectrometryMeasurement MSM) {
        OutputStreamWriter osw = new OutputStreamWriter(System.out);

        PrintWriter out = new PrintWriter(new BufferedWriter(osw));
        out.println("# deisotoped by fbdm algorithm at " + new SimpleDateFormat("yyyy-MM-dd:HH-mm").format(new Date()));
        out.println("COM=" + MSM.getSource());

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
    }
}
