/**
 * @author Lucas Schmidt
 * @since 2017-11-03
 */

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.fbdm.Deisotoper;
import ch.fgcz.proteomics.mgf.ReadMGF;
import ch.fgcz.proteomics.mgf.WriteMGF;

public class ExecDeisotoper {
    public static void main(String[] args) {
        MassSpectrometryMeasurement msm = ReadMGF.read();

        MassSpectrometryMeasurement msmd = Deisotoper.deisotopeMSM(msm, "first", "nofile");

        WriteMGF.write(msmd);
    }
}

