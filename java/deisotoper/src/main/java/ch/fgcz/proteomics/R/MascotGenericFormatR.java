package ch.fgcz.proteomics.R;

/**
 * @author Lucas Schmidt
 * @since 2017-11-03
 */

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.mgf.ReadMGF;
import ch.fgcz.proteomics.mgf.WriteMGF;

public class MascotGenericFormatR {
    public static MassSpectrometryMeasurement readR(String fileName) {
        return ReadMGF.read(fileName);
    }

    public static boolean writeR(String fileName, MassSpectrometryMeasurement massSpectrometryMeasurement) {
        return WriteMGF.write(fileName, massSpectrometryMeasurement);
    }
}
