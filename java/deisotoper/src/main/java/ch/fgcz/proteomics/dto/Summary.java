package ch.fgcz.proteomics.dto;

/**
 * @author Lucas Schmidt
 * @since 2017-08-25
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// TODO : It is not a DTO or data model.
// So maybe dto is not the right name for the package.
//
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
        summary.append("SpectrumID,Attribute,Value").append(linesep);

        List<MassSpectrum> list = m.getMSlist();
        for (MassSpectrum spectrum : list) {
            List<Double> dist = new ArrayList<>();

            for (int i = 1; i < spectrum.getMz().size(); i++) {
                dist.add(spectrum.getMz().get(i) - spectrum.getMz().get(i - 1));
            }

            // summary.append(spectrum.getId()).append(",spectrum_type,").append(spectrum.getTyp()).append(linesep);
            summary.append(spectrum.getId()).append(",nr_of_peaks,").append(spectrum.getMz().size()).append(linesep);
            if (spectrum.getIntensity().size() != 0) {
                summary.append(spectrum.getId()).append(",min_intensity,").append(Collections.min(spectrum.getIntensity())).append(linesep);
                summary.append(spectrum.getId()).append(",max_intensity,").append(Collections.max(spectrum.getIntensity())).append(linesep);
            } else {
                summary.append(spectrum.getId()).append(",min_intensity,").append("0").append(linesep);
                summary.append(spectrum.getId()).append(",max_intensity,").append("0").append(linesep);
            }
            summary.append(spectrum.getId()).append(",sum_intensity,").append(spectrum.getIntensity().stream().mapToDouble(f -> f.doubleValue()).sum()).append(linesep);
            if (spectrum.getMz().size() != 0) {
                summary.append(spectrum.getId()).append(",min_mass,").append(Collections.min(spectrum.getMz())).append(linesep);
                summary.append(spectrum.getId()).append(",max_mass,").append(Collections.max(spectrum.getMz())).append(linesep);
            } else {
                summary.append(spectrum.getId()).append(",min_mass,").append("0").append(linesep);
                summary.append(spectrum.getId()).append(",max_mass,").append("0").append(linesep);
            }
            if (dist.size() != 0) {
                summary.append(spectrum.getId()).append(",min_peak_distance,").append(Collections.min(dist)).append(linesep);
                summary.append(spectrum.getId()).append(",max_peak_distance,").append(Collections.max(dist)).append(linesep);
            } else {
                summary.append(spectrum.getId()).append(",min_peak_distance,").append("0").append(linesep);
                summary.append(spectrum.getId()).append(",max_peak_distance,").append("0").append(linesep);
            }
            summary.append(spectrum.getId()).append(",precursor_charge,").append(spectrum.getChargeState()).append(linesep);
            summary.append(spectrum.getId()).append(",precursor_mass,").append(spectrum.getPeptideMass()).append(linesep);
            summary.append(spectrum.getId()).append(",rt_in_seconds,").append(spectrum.getRt()).append(linesep);
            if (!spectrum.getCharge().isEmpty() && !spectrum.getIsotope().isEmpty()) {
                double chargesum = 0;
                double isotopesum = 0;
                int z1sum = 0;
                int z2sum = 0;
                int z3sum = 0;
                int deisotopesum = 0;
                int i = 0;
                for (int c : spectrum.getCharge()) {
                    if (c != -1) {
                        if (c == 1) {
                            z1sum++;
                        } else if (c == 2) {
                            z2sum++;
                        } else if (c == 3) {
                            z3sum++;
                        }
                        chargesum += c;
                        i++;
                    }
                }
                for (double iso : spectrum.getIsotope()) {
                    if (iso != -1) {
                        if (iso == 1) {
                            deisotopesum++;
                        }
                        isotopesum += iso;
                    }
                }
                summary.append(spectrum.getId()).append(",average_charge,").append(chargesum / i).append(linesep);
                summary.append(spectrum.getId()).append(",average_isotope,").append(isotopesum / i).append(linesep);
                summary.append(spectrum.getId()).append(",sum_isotope,").append(deisotopesum).append(linesep);
                summary.append(spectrum.getId()).append(",max_charge,").append(Collections.max(spectrum.getCharge())).append(linesep);
                summary.append(spectrum.getId()).append(",max_isotope,").append(Collections.max(spectrum.getIsotope())).append(linesep);
                summary.append(spectrum.getId()).append(",nr_z1,").append(z1sum).append(linesep);
                summary.append(spectrum.getId()).append(",nr_z2,").append(z2sum).append(linesep);
                summary.append(spectrum.getId()).append(",nr_z3,").append(z3sum).append(linesep);
            }
        }

        return summary.toString();
    }
}