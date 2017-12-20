package ch.fgcz.proteomics.mspy;

import java.util.ArrayList;
import java.util.List;

import ch.fgcz.proteomics.dto.MassSpecMeasure;
import ch.fgcz.proteomics.dto.MassSpectrum;

/**
 * @deprecated isn't up to date anymore.
 */
@Deprecated
public class Deisotoper {
    private Deisotoper() {
        throw new IllegalStateException("Algorithm");
    }

    public static MassSpecMeasure deisotopeMSM(MassSpecMeasure input) {
        MassSpecMeasure output = new MassSpecMeasure(input.getSource() + "_output");

        for (MassSpectrum ms : input.getMSlist()) {
            Peaklist inputpeaklist = new Peaklist(ms);

            List<Peak> outputpeaklist = Mspy
                    .deconvolute(Mspy.deisotope(inputpeaklist.getPeaklist(), 3, 0.05, 0.5, 0.0));

            List<Integer> chargelist = new ArrayList<>();
            List<Double> isotopelist = new ArrayList<>();
            List<Double> mzlist = new ArrayList<>();
            List<Double> intensitylist = new ArrayList<>();

            for (int i = 0; i < outputpeaklist.size(); i++) {
                chargelist.add(outputpeaklist.get(i).getCharge());
                isotopelist.add(outputpeaklist.get(i).getIsotope());
                mzlist.add(outputpeaklist.get(i).getMz());
                intensitylist.add(outputpeaklist.get(i).getIntensity());
            }

            output.addMS(mzlist, intensitylist, ms.getPeptideMass(), ms.getChargeState(), ms.getId(), chargelist,
                    isotopelist);
        }

        return output;
    }
}
