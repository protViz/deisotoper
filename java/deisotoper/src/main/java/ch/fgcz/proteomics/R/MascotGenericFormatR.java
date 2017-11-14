package ch.fgcz.proteomics.R;

/**
 * @author Lucas Schmidt
 * @since 2017-11-03
 */

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.mgf.ReadMGF;
import ch.fgcz.proteomics.mgf.WriteMGF;

public class MascotGenericFormatR {
    public static MassSpectrometryMeasurement readR(String file) {
        return ReadMGF.read(file);
    }

    public static boolean writeR(String file, MassSpectrometryMeasurement msm) {
        return WriteMGF.write(file, msm);
    }
}
