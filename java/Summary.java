package main.java;

/**
 * @author Lucas Schmidt
 * @since 2017-08-25
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Summary {
	/**
	 * Creates a summary of a List of MassSpectrum and returns it in a CSV-formatted
	 * string.
	 * 
	 * @param list
	 * @return CSV-formatted string
	 * @see MassSpectrometryMeasurement
	 */
	public static String makeSummary(List<MassSpectrometryMeasurement.MassSpectrum> list) {
		StringBuilder summary = new StringBuilder();
		String linesep = System.getProperty("line.separator");
		summary.append("SpectrumID, Attribute, Value").append(linesep);
		int spectrumID = 1;

		for (MassSpectrometryMeasurement.MassSpectrum spectrum : list) {
			List<Double> dist = new ArrayList<>();
			double intensity = 0;
			for (int i = 1; i < spectrum.getMz().length; i++) {
				dist.add(spectrum.getMz()[i] - spectrum.getMz()[i - 1]);
			}

			for (double i : spectrum.getIntensity()) {
				intensity += i;
			}

			summary.append(spectrumID).append(",nr_of_peaks,").append(spectrum.getMz().length).append(linesep);
			summary.append(spectrumID).append(",min_intensity,")
					.append(Arrays.stream(spectrum.getIntensity()).min().getAsDouble()).append(linesep);
			summary.append(spectrumID).append(",max_intensity,")
					.append(Arrays.stream(spectrum.getIntensity()).max().getAsDouble()).append(linesep);
			summary.append(spectrumID).append(",sum_intensity,").append(intensity).append(linesep);
			summary.append(spectrumID).append(",min_mass,").append(Arrays.stream(spectrum.getMz()).min().getAsDouble())
					.append(linesep);
			summary.append(spectrumID).append(",max_mass,").append(Arrays.stream(spectrum.getMz()).max().getAsDouble())
					.append(linesep);
			summary.append(spectrumID).append(",min_peak_distance,").append(Collections.min(dist)).append(linesep);
			summary.append(spectrumID).append(",max_peak_distance,").append(Collections.max(dist)).append(linesep);
			summary.append(spectrumID).append(",precursor_charge,").append(spectrum.getChargeState()).append(linesep);
			summary.append(spectrumID).append(",precursor_mass,").append(spectrum.getPeptidMass()).append(linesep);
			summary.append(spectrumID).append(",rt_in_seconds,").append(spectrum.getRt()).append(linesep);
			spectrumID++;
		}

		return summary.toString();
	}
}
