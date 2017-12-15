package ch.fgcz.proteomics.dto;

/**
 * @author Lucas Schmidt
 * @since 2017-08-25
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MassSpecMeasureSummary {
    private MassSpecMeasureSummary() {
        throw new IllegalStateException("Summary class");
    }

    public static String summarize(MassSpecMeasure massSpectrometryMeasurement) {
        StringBuilder stringBuilder = new StringBuilder();
        String lineSep = System.getProperty("line.separator");
        stringBuilder.append("SpectrumID,Attribute,Value").append(lineSep);

        List<MassSpectrum> massSpectrumList = massSpectrometryMeasurement.getMSlist();
        for (MassSpectrum massSpectrum : massSpectrumList) {

            buildFirstPart(massSpectrum, stringBuilder, lineSep);

            buildSecondPart(massSpectrum, stringBuilder, lineSep);
        }

        return stringBuilder.toString();
    }

    private static StringBuilder buildFirstPart(MassSpectrum massSpectrum, StringBuilder stringBuilder,
            String lineSep) {
        List<Double> distance = new ArrayList<Double>();

        for (int i = 1; i < massSpectrum.getMz().size(); i++) {
            distance.add(massSpectrum.getMz().get(i) - massSpectrum.getMz().get(i - 1));
        }

        double intensitySum = 0;
        for (double intensity : massSpectrum.getIntensity()) {
            intensitySum += intensity;
        }

        stringBuilder.append(massSpectrum.getId()).append(",nr_of_peaks,").append(massSpectrum.getMz().size())
                .append(lineSep);
        if (!massSpectrum.getIntensity().isEmpty()) {
            stringBuilder.append(massSpectrum.getId()).append(",min_intensity,")
                    .append(Collections.min(massSpectrum.getIntensity())).append(lineSep);
            stringBuilder.append(massSpectrum.getId()).append(",max_intensity,")
                    .append(Collections.max(massSpectrum.getIntensity())).append(lineSep);
        } else {
            stringBuilder.append(massSpectrum.getId()).append(",min_intensity,").append("0").append(lineSep);
            stringBuilder.append(massSpectrum.getId()).append(",max_intensity,").append("0").append(lineSep);
        }
        stringBuilder.append(massSpectrum.getId()).append(",sum_intensity,").append(intensitySum).append(lineSep);
        if (!massSpectrum.getMz().isEmpty()) {
            stringBuilder.append(massSpectrum.getId()).append(",min_mass,")
                    .append(Collections.min(massSpectrum.getMz())).append(lineSep);
            stringBuilder.append(massSpectrum.getId()).append(",max_mass,")
                    .append(Collections.max(massSpectrum.getMz())).append(lineSep);
        } else {
            stringBuilder.append(massSpectrum.getId()).append(",min_mass,").append("0").append(lineSep);
            stringBuilder.append(massSpectrum.getId()).append(",max_mass,").append("0").append(lineSep);
        }
        if (!distance.isEmpty()) {
            stringBuilder.append(massSpectrum.getId()).append(",min_peak_distance,").append(Collections.min(distance))
                    .append(lineSep);
            stringBuilder.append(massSpectrum.getId()).append(",max_peak_distance,").append(Collections.max(distance))
                    .append(lineSep);
        } else {
            stringBuilder.append(massSpectrum.getId()).append(",min_peak_distance,").append("0").append(lineSep);
            stringBuilder.append(massSpectrum.getId()).append(",max_peak_distance,").append("0").append(lineSep);
        }
        stringBuilder.append(massSpectrum.getId()).append(",precursor_charge,").append(massSpectrum.getChargeState())
                .append(lineSep);
        stringBuilder.append(massSpectrum.getId()).append(",precursor_mass,").append(massSpectrum.getPeptideMass())
                .append(lineSep);

        return stringBuilder;
    }

    private static StringBuilder buildSecondPart(MassSpectrum massSpectrum, StringBuilder stringBuilder,
            String lineSep) {
        if (!massSpectrum.getCharge().isEmpty() && !massSpectrum.getIsotope().isEmpty()) {
            double chargeSum = calculateChargeSum(massSpectrum);
            double isotopeSum = calculateIsotopeSum(massSpectrum);
            int z1Sum = calculatez1Sum(massSpectrum);
            int z2Sum = calculatez2Sum(massSpectrum);
            int z3Sum = calculatez3Sum(massSpectrum);
            int deisotopeSum = calculateDeisotopeSum(massSpectrum);
            int i = calculateI(massSpectrum);
            if (i != 0) {
                stringBuilder.append(massSpectrum.getId()).append(",average_charge,").append(chargeSum / i)
                        .append(lineSep);
                stringBuilder.append(massSpectrum.getId()).append(",average_isotope,").append(isotopeSum / i)
                        .append(lineSep);
            }
            stringBuilder.append(massSpectrum.getId()).append(",sum_isotope,").append(deisotopeSum).append(lineSep);
            stringBuilder.append(massSpectrum.getId()).append(",max_charge,")
                    .append(Collections.max(massSpectrum.getCharge())).append(lineSep);
            stringBuilder.append(massSpectrum.getId()).append(",max_isotope,")
                    .append(Collections.max(massSpectrum.getIsotope())).append(lineSep);
            stringBuilder.append(massSpectrum.getId()).append(",nr_z1,").append(z1Sum).append(lineSep);
            stringBuilder.append(massSpectrum.getId()).append(",nr_z2,").append(z2Sum).append(lineSep);
            stringBuilder.append(massSpectrum.getId()).append(",nr_z3,").append(z3Sum).append(lineSep);
        }

        return stringBuilder;
    }

    private static int calculateDeisotopeSum(MassSpectrum massSpectrum) {
        int deisotopeSum = 0;
        for (double iso : massSpectrum.getIsotope()) {
            if (iso != -1 && iso == 1) {
                deisotopeSum++;
            }
        }
        return deisotopeSum;
    }

    private static double calculateChargeSum(MassSpectrum massSpectrum) {
        int chargeSum = 0;
        for (int c : massSpectrum.getCharge()) {
            if (c != -1) {
                chargeSum += c;
            }
        }
        return chargeSum;
    }

    private static int calculateI(MassSpectrum massSpectrum) {
        int i = 0;
        for (int c : massSpectrum.getCharge()) {
            if (c != -1) {
                i++;
            }
        }
        return i;
    }

    private static int calculatez1Sum(MassSpectrum massSpectrum) {
        int z1Sum = 0;
        for (int c : massSpectrum.getCharge()) {
            if (c != -1 && c == 1) {
                z1Sum++;
            }
        }
        return z1Sum;
    }

    private static int calculatez2Sum(MassSpectrum massSpectrum) {
        int z2Sum = 0;
        for (int c : massSpectrum.getCharge()) {
            if (c != -1 && c == 2) {
                z2Sum++;
            }
        }
        return z2Sum;
    }

    private static int calculatez3Sum(MassSpectrum massSpectrum) {
        int z2Sum = 0;
        for (int c : massSpectrum.getCharge()) {
            if (c != -1 && c == 3) {
                z2Sum++;
            }
        }
        return z2Sum;
    }

    private static double calculateIsotopeSum(MassSpectrum massSpectrum) {
        double isotopeSum = 0;
        for (double iso : massSpectrum.getIsotope()) {
            if (iso != -1) {
                isotopeSum += iso;
            }
        }
        return isotopeSum;
    }
}