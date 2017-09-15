package ch.fgcz.proteomics.mspy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucas Schmidt
 * @since 2017-09-15
 */

public class Deconvolute {
    public static ch.fgcz.proteomics.dto.MassSpectrometryMeasurement deconvoluteMSM(ch.fgcz.proteomics.dto.MassSpectrometryMeasurement input) {
        ch.fgcz.proteomics.dto.MassSpectrometryMeasurement output = new ch.fgcz.proteomics.dto.MassSpectrometryMeasurement(input.getSource() + "_output");
        for (ch.fgcz.proteomics.dto.MassSpectrum ms : input.getMSlist()) {
            Peaklist inputpeaklist = new Peaklist(ms);

            List<Peak> outputpeaklist = Mspy.deconvolute(inputpeaklist.getPeaklist(), 0);

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

            output.addMS(ms.getTyp(), ms.getSearchEngine(), mzlist, intensitylist, ms.getPeptidMass(), ms.getRt(), ms.getChargeState(), ms.getId(), chargelist, isotopelist);
        }

        return output;
    }
}
