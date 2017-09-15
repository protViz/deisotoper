package ch.fgcz.proteomics.mspy;

/**
 * @author Lucas Schmidt
 * @since 2017-08-29
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.dto.Summary;

public class Mspy {
    public static final double ISOTOPE_DISTANCE = 1.00287;
    public static final double ELECTRON_MASS = 0.00054857990924;
    public static final double H_MASS = 1.008;

    /**
     * @param peaklist
     * @param maxcharge
     * @param mztolerance
     * @param inttolerance
     * @param isotopeshift
     * @return deisotoped peaklist
     */
    public static List<Peak> deisotope(List<Peak> peaklist, int maxcharge, double mztolerance, double inttolerance, double isotopeshift) {
        List<Integer> charges = new ArrayList<>();

        for (Peak p : peaklist) {
            p.setCharge(-1); // -1 = None
            p.setIsotope(-1); // -1 = None
        }

        if (maxcharge < 0) {
            for (int i = 1; i <= Math.abs(maxcharge) + 1; i++) {
                charges.add(-i);
            }
        } else {
            for (int i = 1; i <= maxcharge + 1; i++) {
                charges.add(i);
            }
        }
        Collections.reverse(charges);

        int maxindex = peaklist.size();

        int x = 0;
        for (Peak parent : peaklist) {
            if (parent.getIsotope() != -1) {
                continue;
            }

            for (int z : charges) {
                List<Peak> cluster = new ArrayList<>();
                cluster.add(parent);

                double difference = (ISOTOPE_DISTANCE + isotopeshift) / Math.abs(z);
                int y = 1;
                while (x + y < maxindex) {
                    double mzerror = (peaklist.get(x + y).getMz() - cluster.get(cluster.size() - 1).getMz() - difference);
                    if (Math.abs(mzerror) <= mztolerance) {
                        cluster.add(peaklist.get(x + y));
                    } else if (mzerror > mztolerance) {
                        break;
                    }
                    y++;
                }

                if (cluster.size() == 1) {
                    continue;
                }

                int mass = Math.min(15000, (int) calculateMass(parent.getMz(), 0, z, 0)) / 200; // NOT CLEAR

                List<Double> pattern = initPattern(mass);

                int lim = 0;

                for (double p : pattern) {
                    if (p >= 0.33) {
                        lim++;
                    }
                }

                if (cluster.size() < lim && Math.abs(z) > 1) {
                    continue;
                }

                boolean valid = true;
                int isotope = 1;
                int limit = Math.min(pattern.size(), cluster.size());

                while (isotope < limit) {
                    double inttheoretical = (cluster.get(isotope - 1).getIntensity() / pattern.get(isotope - 1)) * pattern.get(isotope);
                    double interror = cluster.get(isotope).getIntensity() - inttheoretical;

                    if (Math.abs(interror) <= (inttheoretical * inttolerance)) {
                        cluster.get(isotope).setIsotope(isotope);
                        cluster.get(isotope).setCharge(z);
                    } else if (interror < 0 && isotope == 1) {
                        valid = false;
                        break;
                    } else if (interror > 0) {
                    }

                    isotope++;
                }

                if (valid) {
                    if (z < 4) { // BUGFIX BUT NOT REALLY A FIX (ONLY KILLS THE PROBLEM AND DOES NOT FIX IT REALLY)
                        parent.setIsotope(0);
                        parent.setCharge(z);
                        break;
                    }
                }
            }
            x++;
        }

        // REMOVE EMTPY PEAKS
        List<Peak> list = removeEmptyPeaks(peaklist);

        return list;
    }

    /**
     * @param mass
     * @param charge
     * @param currentcharge
     * @param masstype
     * @return calculated mass
     */
    private static double calculateMass(double mass, int charge, int currentcharge, int masstype) {
        int agentcharge = 1;
        double agentmass = H_MASS;
        double agentcount = currentcharge / agentcharge;
        agentmass = agentmass - agentcharge * ELECTRON_MASS;

        if (currentcharge != 0) {
            mass = mass * Math.abs(currentcharge) - agentmass * agentcount;
        }

        return mass;
    }

    /**
     * @param peaklist
     * @return formatted peaklist
     */
    private static List<Peak> removeEmptyPeaks(List<Peak> peaklist) {
        List<Peak> peaklistout = new ArrayList<>();

        for (int i = 0; i < peaklist.size(); i++) {
            if (peaklist.get(i).getIsotope() != -1.0 && peaklist.get(i).getCharge() != -1) {
                peaklistout.add(peaklist.get(i));
            }
        }

        return peaklistout;
    }

    /**
     * @param mass
     * @return pattern
     */
    private static List<Double> initPattern(int mass) {
        List<List<Double>> patternLookupTable = new ArrayList<>();

        patternLookupTable.add(Arrays.asList(1.000, 0.059, 0.003));
        patternLookupTable.add(Arrays.asList(1.000, 0.122, 0.013));
        patternLookupTable.add(Arrays.asList(1.000, 0.241, 0.040, 0.005));
        patternLookupTable.add(Arrays.asList(1.000, 0.303, 0.059, 0.008));
        patternLookupTable.add(Arrays.asList(1.000, 0.426, 0.109, 0.020, 0.003));
        patternLookupTable.add(Arrays.asList(1.000, 0.533, 0.166, 0.038, 0.006));
        patternLookupTable.add(Arrays.asList(1.000, 0.655, 0.244, 0.066, 0.014, 0.002));
        patternLookupTable.add(Arrays.asList(1.000, 0.786, 0.388, 0.143, 0.042, 0.009, 0.001));
        patternLookupTable.add(Arrays.asList(1.000, 0.845, 0.441, 0.171, 0.053, 0.013, 0.002));
        patternLookupTable.add(Arrays.asList(1.000, 0.967, 0.557, 0.236, 0.080, 0.021, 0.005));
        patternLookupTable.add(Arrays.asList(0.921, 1.000, 0.630, 0.291, 0.107, 0.032, 0.007, 0.001));
        patternLookupTable.add(Arrays.asList(0.828, 1.000, 0.687, 0.343, 0.136, 0.044, 0.011, 0.002));
        patternLookupTable.add(Arrays.asList(0.752, 1.000, 0.744, 0.400, 0.171, 0.060, 0.017, 0.004));
        patternLookupTable.add(Arrays.asList(0.720, 1.000, 0.772, 0.428, 0.188, 0.068, 0.020, 0.005));
        patternLookupTable.add(Arrays.asList(0.667, 1.000, 0.825, 0.487, 0.228, 0.088, 0.028, 0.007));
        patternLookupTable.add(Arrays.asList(0.616, 1.000, 0.884, 0.556, 0.276, 0.113, 0.039, 0.010, 0.002));
        patternLookupTable.add(Arrays.asList(0.574, 1.000, 0.941, 0.628, 0.330, 0.143, 0.052, 0.015, 0.003));
        patternLookupTable.add(Arrays.asList(0.536, 0.999, 1.000, 0.706, 0.392, 0.179, 0.069, 0.022, 0.005));
        patternLookupTable.add(Arrays.asList(0.506, 0.972, 1.000, 0.725, 0.412, 0.193, 0.077, 0.025, 0.006));
        patternLookupTable.add(Arrays.asList(0.449, 0.919, 1.000, 0.764, 0.457, 0.226, 0.094, 0.033, 0.009, 0.001));
        patternLookupTable.add(Arrays.asList(0.392, 0.853, 1.000, 0.831, 0.543, 0.295, 0.136, 0.053, 0.017, 0.004));
        patternLookupTable.add(Arrays.asList(0.353, 0.812, 1.000, 0.869, 0.593, 0.336, 0.162, 0.067, 0.023, 0.006));
        patternLookupTable.add(Arrays.asList(0.321, 0.776, 1.000, 0.907, 0.644, 0.379, 0.190, 0.082, 0.030, 0.009));
        patternLookupTable.add(Arrays.asList(0.308, 0.760, 1.000, 0.924, 0.669, 0.401, 0.205, 0.090, 0.033, 0.011, 0.001));
        patternLookupTable.add(Arrays.asList(0.282, 0.729, 1.000, 0.962, 0.723, 0.451, 0.239, 0.110, 0.042, 0.014, 0.003));
        patternLookupTable.add(Arrays.asList(0.258, 0.699, 1.000, 1.000, 0.780, 0.504, 0.277, 0.132, 0.053, 0.018, 0.004));
        patternLookupTable.add(Arrays.asList(0.228, 0.645, 0.962, 1.000, 0.809, 0.542, 0.308, 0.153, 0.065, 0.023, 0.007));
        patternLookupTable.add(Arrays.asList(0.203, 0.598, 0.927, 1.000, 0.839, 0.581, 0.343, 0.176, 0.078, 0.029, 0.010));
        patternLookupTable.add(Arrays.asList(0.192, 0.577, 0.911, 1.000, 0.854, 0.602, 0.361, 0.189, 0.086, 0.033, 0.011));
        patternLookupTable.add(Arrays.asList(0.171, 0.536, 0.880, 1.000, 0.884, 0.644, 0.399, 0.216, 0.102, 0.040, 0.014, 0.003));
        patternLookupTable.add(Arrays.asList(0.154, 0.501, 0.851, 1.000, 0.912, 0.686, 0.439, 0.244, 0.120, 0.050, 0.018, 0.004));
        patternLookupTable.add(Arrays.asList(0.139, 0.468, 0.823, 1.000, 0.942, 0.730, 0.482, 0.278, 0.141, 0.062, 0.023, 0.007));
        patternLookupTable.add(Arrays.asList(0.126, 0.441, 0.799, 1.000, 0.969, 0.772, 0.524, 0.310, 0.162, 0.073, 0.028, 0.009));
        patternLookupTable.add(Arrays.asList(0.121, 0.427, 0.787, 1.000, 0.983, 0.794, 0.547, 0.328, 0.174, 0.080, 0.031, 0.011));
        patternLookupTable.add(Arrays.asList(0.104, 0.381, 0.732, 0.971, 1.000, 0.848, 0.614, 0.390, 0.219, 0.109, 0.045, 0.016, 0.004));
        patternLookupTable.add(Arrays.asList(0.092, 0.349, 0.691, 0.944, 1.000, 0.872, 0.648, 0.422, 0.244, 0.125, 0.054, 0.020, 0.006));
        patternLookupTable.add(Arrays.asList(0.082, 0.321, 0.654, 0.919, 1.000, 0.894, 0.682, 0.456, 0.270, 0.143, 0.063, 0.024, 0.008));
        patternLookupTable.add(Arrays.asList(0.073, 0.296, 0.620, 0.895, 1.000, 0.917, 0.718, 0.492, 0.299, 0.162, 0.077, 0.030, 0.011));
        patternLookupTable.add(Arrays.asList(0.069, 0.284, 0.604, 0.884, 1.000, 0.929, 0.735, 0.509, 0.313, 0.172, 0.084, 0.033, 0.012));
        patternLookupTable.add(Arrays.asList(0.062, 0.262, 0.573, 0.861, 1.000, 0.952, 0.772, 0.548, 0.345, 0.195, 0.098, 0.040, 0.015, 0.003));
        patternLookupTable.add(Arrays.asList(0.056, 0.243, 0.544, 0.839, 1.000, 0.976, 0.811, 0.589, 0.380, 0.220, 0.114, 0.049, 0.019, 0.005));
        patternLookupTable.add(Arrays.asList(0.051, 0.227, 0.521, 0.821, 1.000, 0.997, 0.846, 0.628, 0.413, 0.244, 0.130, 0.058, 0.022, 0.007));
        patternLookupTable.add(Arrays.asList(0.045, 0.206, 0.486, 0.786, 0.980, 1.000, 0.869, 0.660, 0.444, 0.268, 0.147, 0.070, 0.027, 0.010));
        patternLookupTable.add(Arrays.asList(0.042, 0.196, 0.468, 0.767, 0.968, 1.000, 0.879, 0.676, 0.460, 0.281, 0.156, 0.075, 0.030, 0.011));
        patternLookupTable.add(Arrays.asList(0.038, 0.179, 0.437, 0.733, 0.947, 1.000, 0.899, 0.705, 0.491, 0.307, 0.173, 0.086, 0.036, 0.013, 0.002));
        patternLookupTable.add(Arrays.asList(0.033, 0.163, 0.408, 0.701, 0.926, 1.000, 0.919, 0.736, 0.524, 0.335, 0.193, 0.099, 0.043, 0.016, 0.004));
        patternLookupTable.add(Arrays.asList(0.030, 0.149, 0.382, 0.670, 0.906, 1.000, 0.938, 0.768, 0.558, 0.364, 0.215, 0.113, 0.051, 0.020, 0.006));
        patternLookupTable.add(Arrays.asList(0.026, 0.132, 0.348, 0.629, 0.877, 1.000, 0.971, 0.823, 0.620, 0.420, 0.258, 0.143, 0.069, 0.028, 0.010));
        patternLookupTable.add(Arrays.asList(0.024, 0.126, 0.337, 0.616, 0.868, 1.000, 0.981, 0.839, 0.638, 0.437, 0.271, 0.153, 0.074, 0.031, 0.011));
        patternLookupTable.add(Arrays.asList(0.022, 0.116, 0.317, 0.592, 0.851, 1.000, 1.000, 0.872, 0.676, 0.472, 0.298, 0.172, 0.087, 0.037, 0.014, 0.002));
        patternLookupTable.add(Arrays.asList(0.020, 0.106, 0.294, 0.561, 0.822, 0.983, 1.000, 0.888, 0.700, 0.498, 0.320, 0.188, 0.099, 0.043, 0.017, 0.004));
        patternLookupTable.add(Arrays.asList(0.017, 0.096, 0.272, 0.529, 0.790, 0.965, 1.000, 0.905, 0.727, 0.526, 0.346, 0.207, 0.113, 0.050, 0.020, 0.006));
        patternLookupTable.add(Arrays.asList(0.015, 0.087, 0.251, 0.499, 0.761, 0.946, 1.000, 0.922, 0.755, 0.556, 0.373, 0.227, 0.126, 0.061, 0.024, 0.008));
        patternLookupTable.add(Arrays.asList(0.014, 0.083, 0.242, 0.486, 0.747, 0.937, 1.000, 0.930, 0.768, 0.570, 0.385, 0.237, 0.134, 0.065, 0.026, 0.009));
        patternLookupTable.add(Arrays.asList(0.013, 0.075, 0.225, 0.459, 0.720, 0.920, 1.000, 0.947, 0.796, 0.602, 0.415, 0.260, 0.149, 0.075, 0.032, 0.012, 0.001));
        patternLookupTable.add(Arrays.asList(0.012, 0.069, 0.208, 0.435, 0.695, 0.904, 1.000, 0.963, 0.824, 0.633, 0.443, 0.284, 0.165, 0.085, 0.037, 0.015, 0.002));
        patternLookupTable.add(Arrays.asList(0.010, 0.063, 0.194, 0.412, 0.669, 0.888, 1.000, 0.980, 0.852, 0.667, 0.475, 0.309, 0.184, 0.098, 0.044, 0.018, 0.005));
        patternLookupTable.add(Arrays.asList(0.009, 0.057, 0.180, 0.391, 0.646, 0.872, 1.000, 0.997, 0.882, 0.702, 0.509, 0.336, 0.204, 0.113, 0.052, 0.021, 0.006));
        patternLookupTable.add(Arrays.asList(0.009, 0.054, 0.173, 0.379, 0.631, 0.861, 0.995, 1.000, 0.892, 0.717, 0.523, 0.350, 0.214, 0.119, 0.057, 0.023, 0.008));
        patternLookupTable.add(Arrays.asList(0.008, 0.049, 0.160, 0.355, 0.602, 0.834, 0.980, 1.000, 0.906, 0.739, 0.548, 0.373, 0.231, 0.132, 0.066, 0.026, 0.010));
        patternLookupTable.add(Arrays.asList(0.007, 0.042, 0.141, 0.321, 0.557, 0.791, 0.953, 1.000, 0.931, 0.781, 0.596, 0.417, 0.268, 0.158, 0.082, 0.037, 0.014, 0.002));
        patternLookupTable.add(Arrays.asList(0.006, 0.038, 0.130, 0.301, 0.531, 0.767, 0.939, 1.000, 0.945, 0.805, 0.624, 0.443, 0.289, 0.174, 0.093, 0.043, 0.017, 0.004));
        patternLookupTable.add(Arrays.asList(0.005, 0.035, 0.120, 0.283, 0.507, 0.744, 0.925, 1.000, 0.960, 0.830, 0.653, 0.470, 0.312, 0.191, 0.106, 0.051, 0.020, 0.006));
        patternLookupTable.add(Arrays.asList(0.005, 0.033, 0.115, 0.274, 0.495, 0.732, 0.918, 1.000, 0.967, 0.842, 0.668, 0.485, 0.324, 0.200, 0.112, 0.054, 0.023, 0.007));
        patternLookupTable.add(Arrays.asList(0.004, 0.030, 0.107, 0.257, 0.472, 0.710, 0.904, 1.000, 0.982, 0.868, 0.699, 0.515, 0.351, 0.219, 0.126, 0.063, 0.027, 0.010));
        patternLookupTable.add(Arrays.asList(0.004, 0.027, 0.098, 0.242, 0.450, 0.689, 0.890, 1.000, 0.997, 0.894, 0.731, 0.547, 0.378, 0.241, 0.141, 0.072, 0.032, 0.012, 0.002));
        patternLookupTable.add(Arrays.asList(0.003, 0.025, 0.090, 0.224, 0.426, 0.661, 0.867, 0.989, 1.000, 0.911, 0.756, 0.574, 0.402, 0.260, 0.155, 0.082, 0.037, 0.014, 0.003));
        patternLookupTable.add(Arrays.asList(0.003, 0.022, 0.082, 0.208, 0.402, 0.633, 0.843, 0.975, 1.000, 0.925, 0.777, 0.598, 0.425, 0.279, 0.169, 0.092, 0.043, 0.017, 0.005));
        patternLookupTable.add(Arrays.asList(0.003, 0.021, 0.079, 0.202, 0.392, 0.621, 0.833, 0.969, 1.000, 0.930, 0.786, 0.609, 0.435, 0.288, 0.176, 0.097, 0.046, 0.018, 0.006));
        patternLookupTable.add(Arrays.asList(0.003, 0.019, 0.073, 0.188, 0.370, 0.595, 0.810, 0.955, 1.000, 0.943, 0.808, 0.634, 0.460, 0.309, 0.191, 0.108, 0.053, 0.022, 0.007));
        patternLookupTable.add(Arrays.asList(0.002, 0.017, 0.067, 0.175, 0.350, 0.570, 0.787, 0.942, 1.000, 0.956, 0.831, 0.662, 0.487, 0.331, 0.209, 0.121, 0.062, 0.026, 0.010));
        patternLookupTable.add(Arrays.asList(0.002, 0.016, 0.061, 0.163, 0.330, 0.547, 0.765, 0.929, 1.000, 0.968, 0.855, 0.690, 0.515, 0.356, 0.227, 0.135, 0.070, 0.031, 0.012, 0.002));
        patternLookupTable.add(Arrays.asList(0.002, 0.014, 0.056, 0.151, 0.312, 0.524, 0.743, 0.916, 1.000, 0.982, 0.878, 0.718, 0.544, 0.382, 0.247, 0.149, 0.079, 0.037, 0.014, 0.003));
        patternLookupTable.add(Arrays.asList(0.002, 0.013, 0.054, 0.146, 0.304, 0.514, 0.733, 0.909, 1.000, 0.989, 0.890, 0.733, 0.559, 0.395, 0.257, 0.156, 0.084, 0.039, 0.016, 0.004));
        patternLookupTable.add(Arrays.asList(0.001, 0.012, 0.047, 0.131, 0.276, 0.478, 0.697, 0.881, 0.989, 1.000, 0.920, 0.777, 0.605, 0.437, 0.292, 0.182, 0.102, 0.051, 0.022, 0.007));
        patternLookupTable.add(Arrays.asList(0.001, 0.010, 0.043, 0.121, 0.259, 0.454, 0.671, 0.859, 0.977, 1.000, 0.932, 0.797, 0.629, 0.460, 0.312, 0.197, 0.114, 0.058, 0.025, 0.008, 0.001));

        return patternLookupTable.get(mass);
    }

    /**
     * TODO: Debug this function
     * 
     * @param peaklist
     * @return deconvoluted peaklist
     */
    public static List<Peak> deconvolute(List<Peak> peaklist, int masstype) {
        List<Peak> buff = new ArrayList<>();
        List<Peak> peaklistcopy = peaklist;

        for (Peak peak : peaklistcopy) {
            System.out.println("MZ: " + peak.getMz() + ", INTENSITY:" + peak.getIntensity() + ", CHARGE:" + peak.getCharge() + ", ISOTOPE:" + peak.getIsotope());

            if (peak.getCharge() == -1) {
                System.out.println("for|if| continue");
                continue;
            } else if (peak.getCharge() == 1) {
                buff.add(peak);
                System.out.println("for|else if| add peak to buff");
            } else {
                if (peak.getFwhm() != -1) {
                    peak.setFwhm(Math.abs(peak.getFwhm() * peak.getCharge()));
                    System.out.println("for|else|if| set fwhm");
                }

                if (peak.getCharge() < 0) {
                    peak.setMz(calculateMass(peak.getMz(), -1, peak.getCharge(), masstype)); // Debug this
                    peak.setCharge(-1);
                    System.out.println("for|else|if| set mz to calcMass");
                    System.out.println("for|else|if| set charge to -1");
                } else {
                    peak.setMz(calculateMass(peak.getMz(), 1, peak.getCharge(), masstype)); // Debug this
                    peak.setCharge(1);
                    System.out.println("for|else|else| set mz to calcMass");
                    System.out.println("for|else|else| set charge to 1");
                }

                buff.add(peak);
                System.out.println("for|else| add peak to buff");
            }
        }

        peaklist = buff;
        System.out.println("peaklist = buff");

        return peaklist;
    }

    public static void main(String[] args) {
        String s = "TesterinoData.RData";

        String typ = "MS2 Spectrum";
        String searchengine = "mascot";
        double[] mz = { 110.0715485, 110.1648788, 112.0395432, 113.0712204, 114.0746384, 115.05056, 115.9784622, 120.080986, 120.0833893, 121.0843735, 125.7498245, 129.0659332, 129.1023102,
                130.0863342, 130.1057281, 131.0815735, 132.0849609, 132.5098419, 137.0346222, 141.0658417, 143.0816193, 145.0608368, 147.1128235, 149.0233917, 150.0266876, 153.0657654, 157.0970917,
                159.076416, 160.0798035, 162.0875397, 162.0912323, 167.0816345, 170.092392, 171.0762177, 172.8989868, 175.118927, 180.0767212, 185.0920105, 189.1023407, 198.0873413, 199.0907135,
                204.0980682, 205.0570374, 207.1127777, 208.0717316, 210.0866547, 210.7322388, 211.5193329, 212.10289, 214.0822296, 216.0977783, 223.0636444, 224.1031799, 225.0603638, 226.0820923,
                227.0399933, 227.1023407, 228.098175, 230.1135101, 231.1125488, 231.1492462, 232.0926361, 232.2126312, 233.1646729, 235.1072845, 240.09758, 241.129364, 242.1128387, 246.1234741,
                249.098175, 253.0928345, 254.1314087, 254.6329193, 255.1085358, 258.1084595, 259.1398315, 263.1368713, 267.1086426, 268.1119995, 269.1243591, 271.1038513, 273.1199646, 274.1184998,
                276.1701965, 277.0926819, 277.1541138, 278.4438171, 279.108429, 285.1191101, 287.1348877, 289.650177, 292.1772461, 294.1808777, 295.1038513, 295.184082, 296.1346436, 297.1190491,
                299.0617065, 300.133606, 301.0583801, 303.1295166, 310.4591064, 314.1459961, 315.129303, 318.1446228, 318.1605225, 324.1297607, 328.1278992, 331.8494568, 332.1560364, 333.1555786,
                334.114502, 338.1456299, 342.1403198, 343.1411133, 346.176239, 351.1661377, 356.1558228, 357.1579285, 357.6632385, 358.1647949, 360.1513672, 360.6679077, 361.1502075, 363.2023926,
                366.6683655, 368.6846924, 375.1660156, 375.2023926, 379.1607361, 381.2128296, 382.2158813, 387.1657104, 388.1895447, 390.9455261, 395.1658325, 397.1503906, 397.1950684, 397.6967163,
                400.1613159, 403.135437, 403.1973877, 411.1614075, 418.1715698, 420.2235107, 421.1462402, 421.2265015, 426.1759949, 438.2343445, 439.2384644, 443.2040405, 444.1874084, 454.1725464,
                461.2140198, 462.1977844, 471.1980896, 472.2180786, 479.2248535, 482.1990967, 489.2085571, 490.2118835, 490.2296448, 495.2563477, 507.2199402, 507.2555847, 508.2598572, 525.2663574,
                526.2697754, 577.2515259, 578.2937012, 584.8656006, 596.3037109, 597.3069458, 617.3034668, 618.2872925, 635.3146362, 636.3175049, 653.3252563, 654.3284302 };
        double[] intensity = { 6250.6640625, 563.5891113281, 787.7743530273, 42913.109375, 927.4467773438, 2023.1707763672, 529.0408325195, 25228.6796875, 1165.1545410156, 603.5643310547,
                528.4467773438, 7070.5766601563, 31543.533203125, 19388.078125, 833.7221679688, 81864.546875, 3091.2973632813, 582.6003417969, 6660.9750976563, 34947.4921875, 684.2407836914,
                3036.4206542969, 36999.45703125, 2973.7102050781, 829.9125366211, 6804.5180664063, 1259.5620117188, 46302.8125, 2067.9748535156, 1154.1105957031, 765.7245483398, 794.5812988281,
                4291.3037109375, 888.9245605469, 606.1282958984, 766.3591918945, 14302.1279296875, 3288.0661621094, 688.5368652344, 35533.54296875, 2225.658203125, 670.2319335938, 677.4953613281,
                6984.8188476563, 2347.3420410156, 594.0815429688, 2258.9313964844, 568.3290405273, 802.1146850586, 3599.0229492188, 7246.6596679688, 2139.9233398438, 828.2638549805, 884.8049316406,
                1193.3479003906, 566.4050292969, 786.0283813477, 615.2380981445, 1079.8623046875, 1339.6888427734, 806.2881469727, 9631.443359375, 569.907409668, 1065.3564453125, 991.0939331055,
                690.4099731445, 3580.0187988281, 672.0654296875, 845.2703857422, 1281.3441162109, 559.6304931641, 4143.1162109375, 1155.7521972656, 2598.6123046875, 953.2352905273, 3944.8764648438,
                7301.6743164063, 4961.2807617188, 569.8205566406, 2566.8024902344, 795.173828125, 757.6403808594, 3254.8908691406, 7315.53515625, 1220.8388671875, 1115.0716552734, 524.3715820313,
                676.455871582, 2324.4851074219, 808.6432495117, 1269.125, 581.4161376953, 31443.404296875, 2546.380859375, 2911.2058105469, 780.770324707, 738.4011230469, 1135.4598388672,
                1176.7568359375, 3533.0815429688, 2457.2565917969, 562.0629882813, 928.1300048828, 1034.6174316406, 1035.1872558594, 2155.4260253906, 3214.2192382813, 586.0258178711, 545.24609375,
                2240.283203125, 3230.6267089844, 1323.4107666016, 924.3392944336, 8073.1616210938, 767.8354492188, 789.9375610352, 2135.9985351563, 999.5626831055, 642.1485595703, 7692.0913085938,
                1246.6643066406, 4829.984375, 799.643371582, 3193.9008789063, 1350.830078125, 901.1105957031, 687.5123901367, 996.9341430664, 1064.1864013672, 911.1711425781, 16409.24609375,
                1171.5484619141, 960.3439331055, 935.4268188477, 677.4844970703, 657.7783813477, 2241.4836425781, 8700.474609375, 782.1491699219, 686.3291015625, 785.7287597656, 753.0210571289,
                714.055847168, 2145.2751464844, 5911.2006835938, 1220.7615966797, 623.0693969727, 634.6696777344, 33137.39453125, 5135.7211914063, 813.663269043, 1231.4427490234, 933.5162963867,
                3532.599609375, 1009.3364868164, 3219.3342285156, 800.7995605469, 3228.8891601563, 1368.7080078125, 6327.9125976563, 810.8509521484, 1239.9750976563, 791.6758422852, 1281.634765625,
                6519.0903320313, 575.096496582, 65968.4140625, 11777.5908203125, 2284.7736816406, 2809.2331542969, 622.4571533203, 22998.53515625, 3657.8000488281, 1279.3221435547, 1236.6485595703,
                9816.9814453125, 1331.3529052734, 87058.9140625, 21567.640625 };
        double peptidmass = 309.22;
        double rt = 38383.34;
        int chargestate = 2;
        int id = 123;

        MassSpectrometryMeasurement test = new MassSpectrometryMeasurement(s);
        test.addMS(typ, searchengine, mz, intensity, peptidmass, rt, chargestate, id);

        MassSpectrometryMeasurement test2 = Deisotope.deisotopeMSM(test);

        System.out.println(test.getMSlist().get(0).getMz().size());
        System.out.println(Summary.makeSummary(test));

        System.out.println(test2.getMSlist().get(0).getMz().size());
        System.out.println(Summary.makeSummary(test2));

        MassSpectrometryMeasurement test3 = Deconvolute.deconvoluteMSM(test2);

        System.out.println("Size: " + test3.getMSlist().get(0).getMz().size());
        System.out.println(Summary.makeSummary(test3));
    }
}
