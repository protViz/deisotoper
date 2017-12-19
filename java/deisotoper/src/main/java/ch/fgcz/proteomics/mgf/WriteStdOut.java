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

    public static void write(MassSpecMeasure massSpectrometryMeasurement) {
        System.out.print("# deisotoped by fbdm algorithm at "); // NOSONAR
        System.out.println(new SimpleDateFormat("yyyy-MM-dd:HH-mm").format(new Date())); // NOSONAR

        for (MassSpectrum MS : massSpectrometryMeasurement.getMSlist()) {
            System.out.println("BEGIN IONS"); // NOSONAR

            System.out.println("PEPMASS=" + MS.getPeptideMass()); // NOSONAR
            System.out.println("CHARGE=" + MS.getChargeState() + "+"); // NOSONAR
            int i = 0;
            for (i = 0; i < MS.getMz().size(); i++) {
                System.out.println(MS.getMz().get(i) + " " + MS.getIntensity().get(i)); // NOSONAR
            }
            System.out.println("END IONS"); // NOSONAR
        }
    }
}
