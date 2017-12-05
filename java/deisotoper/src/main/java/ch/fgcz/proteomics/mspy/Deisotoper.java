package ch.fgcz.proteomics.mspy;

/**
 * @author Lucas Schmidt
 * @since 2017-11-03
 */

import java.util.ArrayList;
import java.util.List;

import ch.fgcz.proteomics.dto.MassSpecMeasure;
import ch.fgcz.proteomics.dto.MassSpectrum;

@Deprecated
public class Deisotoper {
    public static MassSpecMeasure deisotopeMSM(MassSpecMeasure input) {
        MassSpecMeasure output = new MassSpecMeasure(input.getSource() + "_output");

        for (MassSpectrum ms : input.getMSlist()) {
            Peaklist inputpeaklist = new Peaklist(ms);

            List<Peak> outputpeaklist = Mspy.deconvolute(Mspy.deisotope(inputpeaklist.getPeaklist(), 3, 0.05, 0.5, 0.0),
                    0);

            List<Integer> chargelist = new ArrayList<Integer>();
            List<Double> isotopelist = new ArrayList<Double>();
            List<Double> mzlist = new ArrayList<Double>();
            List<Double> intensitylist = new ArrayList<Double>();

            for (int i = 0; i < outputpeaklist.size(); i++) {
                chargelist.add(outputpeaklist.get(i).getCharge());
                isotopelist.add(outputpeaklist.get(i).getIsotope());
                mzlist.add(outputpeaklist.get(i).getMz());
                intensitylist.add(outputpeaklist.get(i).getIntensity());
            }

            output.addMS(ms.getTyp(), ms.getSearchEngine(), mzlist, intensitylist, ms.getPeptideMass(), ms.getRt(),
                    ms.getChargeState(), ms.getId(), chargelist, isotopelist);
        }

        return output;
    }
}
