
/**
 * @author Lucas Schmidt
 * @since 2017-08-25
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Summary {
    public static String makeSummary(List<MassSpectrometryMeasurement.MassSpectrum> list) {
        StringBuilder summary = new StringBuilder();
        summary.append("SpectrumID, Attribute, Value").append(System.getProperty("line.separator"));
        int spectrumID = 1;

        for (MassSpectrometryMeasurement.MassSpectrum spectrum : list) {
            List<Double> dist = new ArrayList<>();
            for (int i = 1; i < spectrum.getMz().length; i++) {
                dist.add(spectrum.getMz()[i] - spectrum.getMz()[i - 1]);
            }

            summary.append(spectrumID).append(",nr_of_peaks,").append(spectrum.getMz().length).append(System.getProperty("line.separator"));
            summary.append(spectrumID).append(",min_intensity,").append(Arrays.stream(spectrum.getIntensity()).min().getAsDouble()).append(System.getProperty("line.separator"));
            summary.append(spectrumID).append(",max_intensity,").append(Arrays.stream(spectrum.getIntensity()).max().getAsDouble()).append(System.getProperty("line.separator"));
            summary.append(spectrumID).append(",min_mass,").append(Arrays.stream(spectrum.getMz()).min().getAsDouble()).append(System.getProperty("line.separator"));
            summary.append(spectrumID).append(",max_mass,").append(Arrays.stream(spectrum.getMz()).max().getAsDouble()).append(System.getProperty("line.separator"));
            summary.append(spectrumID).append(",min_peak_distance,").append(Collections.min(dist)).append(System.getProperty("line.separator"));
            summary.append(spectrumID).append(",max_peak_distance,").append(Collections.max(dist)).append(System.getProperty("line.separator"));
            summary.append(spectrumID).append(",precursor_charge,").append(spectrum.getChargeState()).append(System.getProperty("line.separator"));
            summary.append(spectrumID).append(",precursor_mass,").append(spectrum.getPeptidMass()).append(System.getProperty("line.separator"));

            spectrumID++;
        }

        return summary.toString();
    }
}
