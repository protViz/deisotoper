package ch.fgcz.proteomics.mspy;

import java.util.ArrayList;

/**
 * @author Lucas Schmidt
 * @since 2017-09-13
 */

import java.util.List;

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;

public class Main {
    public static void main(String[] args) {

        ch.fgcz.proteomics.dto.MassSpectrometryMeasurement TP_HeLa_200ng_filtered_pd21_MSM = ch.fgcz.proteomics.dto.Serialize
                .deserializeJsonToMSM("/srv/lucas1/Downloads/TP_HeLa_200ng_filtered_pd21_MSM.json");

        // Make Summary of TP_HeLa_200ng_filtered_pd21_MSM
        System.out.println("Input summary:");
        System.out.println(ch.fgcz.proteomics.dto.Summary.makeSummary(TP_HeLa_200ng_filtered_pd21_MSM));

        // Create a new MSM and deisotope the input and fill it in the new MSM
        ch.fgcz.proteomics.dto.MassSpectrometryMeasurement TP_HeLa_200ng_filtered_pd21_MSM_output = new MassSpectrometryMeasurement("TP_HeLa_200ng_filtered_pd21_MSM_output");
        for (ch.fgcz.proteomics.dto.MassSpectrum ms : TP_HeLa_200ng_filtered_pd21_MSM.getMSlist()) {
            Peaklist input = new Peaklist(ms);

            List<Peak> output = Mspy.deisotope(input.getPeaklist(), 3, 0.05, 0.5, 0.0);

            List<Integer> chargelist = new ArrayList<>();
            List<Double> isotopelist = new ArrayList<>();

            for (int i = 0; i < output.size(); i++) {
                chargelist.add(output.get(i).getCharge());
                isotopelist.add(output.get(i).getIsotope());
            }

            TP_HeLa_200ng_filtered_pd21_MSM_output.addMS(ms.getTyp(), ms.getSearchEngine(), ms.getMz(), ms.getIntensity(), ms.getPeptidMass(), ms.getRt(), ms.getChargeState(), ms.getId(), chargelist,
                    isotopelist);
        }

        // Make Summary of TP_HeLa_200ng_filtered_pd21_MSM_output
        System.out.println("Output summary:");
        System.out.println(ch.fgcz.proteomics.dto.Summary.makeSummary(TP_HeLa_200ng_filtered_pd21_MSM_output));
    }
}
