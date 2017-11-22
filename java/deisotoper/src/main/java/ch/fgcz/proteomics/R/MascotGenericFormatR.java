package ch.fgcz.proteomics.R;

/**
 * @author Lucas Schmidt
 * @since 2017-11-03
 */

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.mgf.ReadMGF;
import ch.fgcz.proteomics.mgf.WriteMGF;

public class MascotGenericFormatR {
    public static MassSpectrometryMeasurement readR(String filename) {
        return ReadMGF.read(filename);
    }

    public static boolean writeR(String filename, MassSpectrometryMeasurement massspectrometrymeasurement) {
        return WriteMGF.write(filename, massspectrometrymeasurement);
    }
}
