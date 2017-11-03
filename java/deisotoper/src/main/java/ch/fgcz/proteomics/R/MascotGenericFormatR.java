package ch.fgcz.proteomics.R;

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.fdbm.Deisotoper;
import ch.fgcz.proteomics.mgf.ReadMGF;
import ch.fgcz.proteomics.mgf.WriteMGF;

/**
 * @author Lucas Schmidt
 * @since 2017-11-03
 */

public class MascotGenericFormatR {
    public static MassSpectrometryMeasurement readR(String file) {
        return ReadMGF.read(file);
    }

    public static boolean writeR(String file, MassSpectrometryMeasurement msm) {
        return WriteMGF.write(file, msm);
    }

    // USAGE: java MGFR <INPUTFILE> <OUTPUTFILE>
    public static void main(String[] args) {
        if (args.length == 2) {
            MassSpectrometryMeasurement msm = readR(args[0]);

            MassSpectrometryMeasurement msmd = Deisotoper.deisotopeMSM(msm, "first", "nofile");

            writeR(args[1], msmd);
        } else {
            System.err.println("WARNING: The arguments are missing (first argument: inputfile, second argument: outputfile)!");
        }
    }
}
