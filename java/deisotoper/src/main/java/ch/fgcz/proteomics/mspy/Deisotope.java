package ch.fgcz.proteomics.mspy;

/**
 * @author Lucas Schmidt
 * @since 2017-09-13
 */

import java.util.ArrayList;
import java.util.List;

public class Deisotope {
    public static ch.fgcz.proteomics.dto.MassSpectrometryMeasurement deisotopeMSM(ch.fgcz.proteomics.dto.MassSpectrometryMeasurement input) {
        // Create a new MSM and deisotope the input and fill it in the new MSM
        ch.fgcz.proteomics.dto.MassSpectrometryMeasurement output = new ch.fgcz.proteomics.dto.MassSpectrometryMeasurement(input.getSource() + "_output");
        for (ch.fgcz.proteomics.dto.MassSpectrum ms : input.getMSlist()) {
            Peaklist inputpeaklist = new Peaklist(ms);

            List<Peak> outputpeaklist = Mspy.deisotope(inputpeaklist.getPeaklist(), 3, 0.05, 0.5, 0.0);

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

    public static void main(String[] args) {
        ch.fgcz.proteomics.dto.MassSpectrometryMeasurement TP_HeLa_200ng_filtered_pd21_MSM = ch.fgcz.proteomics.dto.Serialize
                .deserializeJsonToMSM("/srv/lucas1/Downloads/TP_HeLa_200ng_filtered_pd21_MSM.json");

        // Make Summary of TP_HeLa_200ng_filtered_pd21_MSM
        System.out.println("Input summary:");
        System.out.println(ch.fgcz.proteomics.dto.Summary.makeSummary(TP_HeLa_200ng_filtered_pd21_MSM));

        // Make Summary of TP_HeLa_200ng_filtered_pd21_MSM_output
        System.out.println("Output summary:");
        System.out.println(ch.fgcz.proteomics.dto.Summary.makeSummary(Deisotope.deisotopeMSM(TP_HeLa_200ng_filtered_pd21_MSM)));
    }
}
