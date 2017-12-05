package ch.fgcz.proteomics.dto;

/**
 * @author Lucas Schmidt
 * @since 2017-08-25
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.fgcz.proteomics.dto.MassSpecMeasure;
import ch.fgcz.proteomics.dto.MassSpectrum;

public class MassSpecMeasureSummary {
    public static String summarize(MassSpecMeasure massSpectrometryMeasurement) {
        StringBuilder stringBuilder = new StringBuilder();
        String lineSep = System.getProperty("line.separator");
        stringBuilder.append("SpectrumID,Attribute,Value").append(lineSep);

        List<MassSpectrum> massSpectrumList = massSpectrometryMeasurement.getMSlist();
        for (MassSpectrum massSpectrum : massSpectrumList) {
            List<Double> distance = new ArrayList<Double>();

            for (int i = 1; i < massSpectrum.getMz().size(); i++) {
                distance.add(massSpectrum.getMz().get(i) - massSpectrum.getMz().get(i - 1));
            }

            double intensitySum = 0;
            for (double intensity : massSpectrum.getIntensity()) {
                intensitySum += intensity;
            }

            // summary.append(spectrum.getId()).append(",spectrum_type,").append(spectrum.getTyp()).append(linesep);
            stringBuilder.append(massSpectrum.getId()).append(",nr_of_peaks,").append(massSpectrum.getMz().size())
                    .append(lineSep);
            if (massSpectrum.getIntensity().size() != 0) {
                stringBuilder.append(massSpectrum.getId()).append(",min_intensity,")
                        .append(Collections.min(massSpectrum.getIntensity())).append(lineSep);
                stringBuilder.append(massSpectrum.getId()).append(",max_intensity,")
                        .append(Collections.max(massSpectrum.getIntensity())).append(lineSep);
            } else {
                stringBuilder.append(massSpectrum.getId()).append(",min_intensity,").append("0").append(lineSep);
                stringBuilder.append(massSpectrum.getId()).append(",max_intensity,").append("0").append(lineSep);
            }
            stringBuilder.append(massSpectrum.getId()).append(",sum_intensity,").append(intensitySum).append(lineSep);
            if (massSpectrum.getMz().size() != 0) {
                stringBuilder.append(massSpectrum.getId()).append(",min_mass,")
                        .append(Collections.min(massSpectrum.getMz())).append(lineSep);
                stringBuilder.append(massSpectrum.getId()).append(",max_mass,")
                        .append(Collections.max(massSpectrum.getMz())).append(lineSep);
            } else {
                stringBuilder.append(massSpectrum.getId()).append(",min_mass,").append("0").append(lineSep);
                stringBuilder.append(massSpectrum.getId()).append(",max_mass,").append("0").append(lineSep);
            }
            if (distance.size() != 0) {
                stringBuilder.append(massSpectrum.getId()).append(",min_peak_distance,")
                        .append(Collections.min(distance)).append(lineSep);
                stringBuilder.append(massSpectrum.getId()).append(",max_peak_distance,")
                        .append(Collections.max(distance)).append(lineSep);
            } else {
                stringBuilder.append(massSpectrum.getId()).append(",min_peak_distance,").append("0").append(lineSep);
                stringBuilder.append(massSpectrum.getId()).append(",max_peak_distance,").append("0").append(lineSep);
            }
            stringBuilder.append(massSpectrum.getId()).append(",precursor_charge,")
                    .append(massSpectrum.getChargeState()).append(lineSep);
            stringBuilder.append(massSpectrum.getId()).append(",precursor_mass,").append(massSpectrum.getPeptideMass())
                    .append(lineSep);
            stringBuilder.append(massSpectrum.getId()).append(",rt_in_seconds,").append(massSpectrum.getRt())
                    .append(lineSep);
            if (!massSpectrum.getCharge().isEmpty() && !massSpectrum.getIsotope().isEmpty()) {
                double chargeSum = 0;
                double isotopeSum = 0;
                int z1Sum = 0;
                int z2Sum = 0;
                int z3Sum = 0;
                int deisotopeSum = 0;
                int i = 0;
                for (int c : massSpectrum.getCharge()) {
                    if (c != -1) {
                        if (c == 1) {
                            z1Sum++;
                        } else if (c == 2) {
                            z2Sum++;
                        } else if (c == 3) {
                            z3Sum++;
                        }
                        chargeSum += c;
                        i++;
                    }
                }
                for (double iso : massSpectrum.getIsotope()) {
                    if (iso != -1) {
                        if (iso == 1) {
                            deisotopeSum++;
                        }
                        isotopeSum += iso;
                    }
                }
                stringBuilder.append(massSpectrum.getId()).append(",average_charge,").append(chargeSum / i)
                        .append(lineSep);
                stringBuilder.append(massSpectrum.getId()).append(",average_isotope,").append(isotopeSum / i)
                        .append(lineSep);
                stringBuilder.append(massSpectrum.getId()).append(",sum_isotope,").append(deisotopeSum).append(lineSep);
                stringBuilder.append(massSpectrum.getId()).append(",max_charge,")
                        .append(Collections.max(massSpectrum.getCharge())).append(lineSep);
                stringBuilder.append(massSpectrum.getId()).append(",max_isotope,")
                        .append(Collections.max(massSpectrum.getIsotope())).append(lineSep);
                stringBuilder.append(massSpectrum.getId()).append(",nr_z1,").append(z1Sum).append(lineSep);
                stringBuilder.append(massSpectrum.getId()).append(",nr_z2,").append(z2Sum).append(lineSep);
                stringBuilder.append(massSpectrum.getId()).append(",nr_z3,").append(z3Sum).append(lineSep);
            }
        }

        return stringBuilder.toString();
    }
}