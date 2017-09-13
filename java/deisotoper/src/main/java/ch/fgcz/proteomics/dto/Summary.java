package ch.fgcz.proteomics.dto;

/**
 * @author Lucas Schmidt
 * @since 2017-08-25
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Summary {
    /**
     * Creates a summary of a MassSpectrometryMeasurement Object and returns it in a CSV-formatted String.
     * 
     * @param MassSpectrometryMeasurement
     * @return String
     * @see MassSpectrometryMeasurement
     */
    public static String makeSummary(MassSpectrometryMeasurement m) {
        StringBuilder summary = new StringBuilder();
        String linesep = System.getProperty("line.separator");
        summary.append("SpectrumID, Attribute, Value").append(linesep);

        List<MassSpectrum> list = m.getMSlist();
        for (MassSpectrum spectrum : list) {
            List<Double> dist = new ArrayList<>();

            for (int i = 1; i < spectrum.getMz().size(); i++) {
                dist.add(spectrum.getMz().get(i) - spectrum.getMz().get(i - 1));
            }

            // summary.append(spectrum.getId()).append(",spectrum_type,").append(spectrum.getTyp()).append(linesep);
            summary.append(spectrum.getId()).append(",nr_of_peaks,").append(spectrum.getMz().size()).append(linesep);
            summary.append(spectrum.getId()).append(",min_intensity,").append(Collections.min(spectrum.getIntensity())).append(linesep);
            summary.append(spectrum.getId()).append(",max_intensity,").append(Collections.max(spectrum.getIntensity())).append(linesep);
            summary.append(spectrum.getId()).append(",sum_intensity,").append(spectrum.getIntensity().stream().mapToDouble(f -> f.doubleValue()).sum()).append(linesep);
            summary.append(spectrum.getId()).append(",min_mass,").append(Collections.min(spectrum.getMz())).append(linesep);
            summary.append(spectrum.getId()).append(",max_mass,").append(Collections.max(spectrum.getMz())).append(linesep);
            summary.append(spectrum.getId()).append(",min_peak_distance,").append(Collections.min(dist)).append(linesep);
            summary.append(spectrum.getId()).append(",max_peak_distance,").append(Collections.max(dist)).append(linesep);
            summary.append(spectrum.getId()).append(",precursor_charge,").append(spectrum.getChargeState()).append(linesep);
            summary.append(spectrum.getId()).append(",precursor_mass,").append(spectrum.getPeptidMass()).append(linesep);
            summary.append(spectrum.getId()).append(",rt_in_seconds,").append(spectrum.getRt()).append(linesep);
            if (!spectrum.getCharge().isEmpty() && !spectrum.getIsotope().isEmpty()) {
                double sumcharge = 0;
                double sumisotope = 0;
                int i = 0;
                for (int c : spectrum.getCharge()) {
                    if (c != -1) {
                        sumcharge += c;
                        i++;
                    }
                }
                for (double iso : spectrum.getIsotope()) {
                    if (iso != -1) {
                        sumisotope += iso;
                        i++;
                    }
                }
                summary.append(spectrum.getId()).append(",average_charge,").append(sumcharge / i).append(linesep);
                summary.append(spectrum.getId()).append(",average_isotope,").append(sumisotope / i).append(linesep);
            }
        }

        return summary.toString();
    }
}