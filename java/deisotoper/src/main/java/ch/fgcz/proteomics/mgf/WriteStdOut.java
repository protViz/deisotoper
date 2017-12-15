package ch.fgcz.proteomics.mgf;

/**
 * @author Lucas Schmidt
 * @since 2017-11-06
 */

import java.text.SimpleDateFormat;
import java.util.Date;

import ch.fgcz.proteomics.dto.MassSpecMeasure;
import ch.fgcz.proteomics.dto.MassSpectrum;

public class WriteStdOut {
    private WriteStdOut() {
        throw new IllegalStateException("Writer class");
    }

    // TODO (LS) @SuppressWarnings("squid:S1197")
    public static void write(MassSpecMeasure massSpectrometryMeasurement) {
        System.out.println(
                "# deisotoped by fbdm algorithm at " + new SimpleDateFormat("yyyy-MM-dd:HH-mm").format(new Date()));

        for (MassSpectrum MS : massSpectrometryMeasurement.getMSlist()) {
            System.out.println("BEGIN IONS");

            System.out.println("PEPMASS=" + MS.getPeptideMass());
            System.out.println("CHARGE=" + MS.getChargeState() + "+");
            int i = 0;
            for (i = 0; i < MS.getMz().size(); i++) {
                System.out.println(MS.getMz().get(i) + " " + MS.getIntensity().get(i));
            }
            System.out.println("END IONS");
        }
    }
}
