/**
 * @author Lucas Schmidt
 * @since 2017-11-03
 */

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.fbdm.Deisotoper;
import ch.fgcz.proteomics.mgf.ReadMGF;
import ch.fgcz.proteomics.mgf.WriteMGF;

public class ExecDeisotoper {
    // USAGE: java MGFR <INPUTFILE> <OUTPUTFILE>
    public static void main(String[] args) {
        if (args.length == 2) {
            MassSpectrometryMeasurement msm = ReadMGF.read(args[0]);

            MassSpectrometryMeasurement msmd = Deisotoper.deisotopeMSM(msm, "first", "nofile");

            WriteMGF.write(args[1], msmd);
        } else {
            System.err.println("WARNING: The arguments are missing (first argument: inputfile, second argument: outputfile)!");
        }
    }
}

