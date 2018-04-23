package ch.fgcz.proteomics.mspy;

/**
 * @author Lucas Schmidt
 * @since 2017-08-29
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @deprecated isn't up to date anymore.
 */
@Deprecated
public class Mspy {
    public static final double ISOTOPE_DISTANCE = 1.00287;
    public static final double ELECTRON_MASS = 0.00054857990924;
    public static final double H_MASS = 1.008;

    private Mspy() {
        throw new IllegalStateException("This is an algorithm");
    }

    public static List<Peak> deisotope(List<Peak> peakList, int maxcharge, double mztolerance, double inttolerance,
            double isotopeshift) {

        for (Peak p : peakList) {
            p.setCharge(-1); // -1 = None
            p.setIsotope(-1); // -1 = None
        }

        List<Integer> charges = initializeCharges(maxcharge);

        int maxindex = peakList.size();

        int x = 0;
        for (Peak parent : peakList) {
            if (parent.getIsotope() != -1) {
                continue;
            }

            x = goThroughPossibleCharges(charges, peakList, parent, x, maxindex, isotopeshift, mztolerance,
                    inttolerance);
        }

        // REMOVE EMTPY PEAKS
        List<Peak> list = removeEmptyPeaks(peakList);

        sortPeaklist(list);

        return list;
    }

    private static int goThroughPossibleCharges(List<Integer> charges, List<Peak> peakList, Peak parent, int x, // NOSONAR
            int maxindex, double isotopeshift, double mztolerance, double inttolerance) { // NOSONAR
        for (int z : charges) { // NOSONAR
            List<Peak> cluster = collectPeaksForCluster(peakList, parent, x, z, maxindex, isotopeshift, mztolerance);

            if (cluster.size() == 1) {
                continue; // NOSONAR
            }

            int mass = Math.min(15000, (int) calculateMass(parent.getMz(), 0, z)) / 200; // NOT CLEAR

            List<Double> pattern = initPattern(mass);

            int lim = calculateLim(pattern);

            if (cluster.size() < lim && Math.abs(z) > 1) {
                continue; // NOSONAR
            }

            boolean valid = true;
            int isotope = 1;
            int limit = Math.min(pattern.size(), cluster.size());

            valid = manipulateCluster(cluster, pattern, isotope, limit, z, inttolerance, valid);

            if (valid && z < 4) {
                parent.setIsotope(0);
                parent.setCharge(z);
                break; // NOSONAR
            }
        }
        x++;

        return x;
    }

    private static int calculateLim(List<Double> pattern) {
        int lim = 0;
        for (double p : pattern) {
            if (p >= 0.33) {
                lim++;
            }
        }
        return lim;
    }

    private static boolean manipulateCluster(List<Peak> cluster, List<Double> pattern, int isotope, int limit, int z,
            double inttolerance, boolean valid) {
        boolean valid2 = valid;

        while (isotope < limit) {
            double inttheoretical = (cluster.get(isotope - 1).getIntensity() / pattern.get(isotope - 1))
                    * pattern.get(isotope);
            double interror = cluster.get(isotope).getIntensity() - inttheoretical;

            if (Math.abs(interror) <= (inttheoretical * inttolerance)) {
                cluster.get(isotope).setIsotope(isotope);
                cluster.get(isotope).setCharge(z);
            } else if (interror < 0 && isotope == 1) {
                valid2 = false;
                break;
            } else if (interror > 0) {
                // do nothing
            }

            isotope++;
        }

        return valid2;
    }

    private static List<Peak> collectPeaksForCluster(List<Peak> peakList, Peak parent, int x, int z, int maxindex,
            double isotopeshift, double mztolerance) {
        List<Peak> cluster = new ArrayList<>();
        cluster.add(parent);

        double difference = (ISOTOPE_DISTANCE + isotopeshift) / Math.abs(z);
        int y = 1;
        while (x + y < maxindex) {
            double mzerror = (peakList.get(x + y).getMz() - cluster.get(cluster.size() - 1).getMz() - difference);
            if (Math.abs(mzerror) <= mztolerance) {
                cluster.add(peakList.get(x + y));
            } else if (mzerror > mztolerance) {
                break;
            }
            y++;
        }

        return cluster;
    }

    private static List<Integer> initializeCharges(int maxcharge) {
        List<Integer> charges = new ArrayList<>();

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

        return charges;
    }

    private static double calculateMass(double mass, int charge, int currentcharge) {
        int agentcharge = 1;
        double agentmass = H_MASS;
        double agentcount = currentcharge / (double) agentcharge;
        agentmass = agentmass - agentcharge * ELECTRON_MASS;

        if (currentcharge != 0) {
            mass = mass * Math.abs(currentcharge) - agentmass * agentcount;
        }

        if (charge == 0) {
            return mass;
        }

        double agentcount2 = (double) charge / agentcharge;
        return (mass + agentmass * agentcount2) / Math.abs(charge);
    }

    public static List<Peak> removeEmptyPeaks(List<Peak> peakList) {
        List<Peak> peaklistout = new ArrayList<>();

        for (int i = 0; i < peakList.size(); i++) {
            if (peakList.get(i).getIsotope() != -1.0 && peakList.get(i).getCharge() != -1) {
                peaklistout.add(peakList.get(i));
            }
        }

        return peaklistout;
    }

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
        patternLookupTable
                .add(Arrays.asList(0.308, 0.760, 1.000, 0.924, 0.669, 0.401, 0.205, 0.090, 0.033, 0.011, 0.001));
        patternLookupTable
                .add(Arrays.asList(0.282, 0.729, 1.000, 0.962, 0.723, 0.451, 0.239, 0.110, 0.042, 0.014, 0.003));
        patternLookupTable
                .add(Arrays.asList(0.258, 0.699, 1.000, 1.000, 0.780, 0.504, 0.277, 0.132, 0.053, 0.018, 0.004));
        patternLookupTable
                .add(Arrays.asList(0.228, 0.645, 0.962, 1.000, 0.809, 0.542, 0.308, 0.153, 0.065, 0.023, 0.007));
        patternLookupTable
                .add(Arrays.asList(0.203, 0.598, 0.927, 1.000, 0.839, 0.581, 0.343, 0.176, 0.078, 0.029, 0.010));
        patternLookupTable
                .add(Arrays.asList(0.192, 0.577, 0.911, 1.000, 0.854, 0.602, 0.361, 0.189, 0.086, 0.033, 0.011));
        patternLookupTable
                .add(Arrays.asList(0.171, 0.536, 0.880, 1.000, 0.884, 0.644, 0.399, 0.216, 0.102, 0.040, 0.014, 0.003));
        patternLookupTable
                .add(Arrays.asList(0.154, 0.501, 0.851, 1.000, 0.912, 0.686, 0.439, 0.244, 0.120, 0.050, 0.018, 0.004));
        patternLookupTable
                .add(Arrays.asList(0.139, 0.468, 0.823, 1.000, 0.942, 0.730, 0.482, 0.278, 0.141, 0.062, 0.023, 0.007));
        patternLookupTable
                .add(Arrays.asList(0.126, 0.441, 0.799, 1.000, 0.969, 0.772, 0.524, 0.310, 0.162, 0.073, 0.028, 0.009));
        patternLookupTable
                .add(Arrays.asList(0.121, 0.427, 0.787, 1.000, 0.983, 0.794, 0.547, 0.328, 0.174, 0.080, 0.031, 0.011));
        patternLookupTable.add(Arrays.asList(0.104, 0.381, 0.732, 0.971, 1.000, 0.848, 0.614, 0.390, 0.219, 0.109,
                0.045, 0.016, 0.004));
        patternLookupTable.add(Arrays.asList(0.092, 0.349, 0.691, 0.944, 1.000, 0.872, 0.648, 0.422, 0.244, 0.125,
                0.054, 0.020, 0.006));
        patternLookupTable.add(Arrays.asList(0.082, 0.321, 0.654, 0.919, 1.000, 0.894, 0.682, 0.456, 0.270, 0.143,
                0.063, 0.024, 0.008));
        patternLookupTable.add(Arrays.asList(0.073, 0.296, 0.620, 0.895, 1.000, 0.917, 0.718, 0.492, 0.299, 0.162,
                0.077, 0.030, 0.011));
        patternLookupTable.add(Arrays.asList(0.069, 0.284, 0.604, 0.884, 1.000, 0.929, 0.735, 0.509, 0.313, 0.172,
                0.084, 0.033, 0.012));
        patternLookupTable.add(Arrays.asList(0.062, 0.262, 0.573, 0.861, 1.000, 0.952, 0.772, 0.548, 0.345, 0.195,
                0.098, 0.040, 0.015, 0.003));
        patternLookupTable.add(Arrays.asList(0.056, 0.243, 0.544, 0.839, 1.000, 0.976, 0.811, 0.589, 0.380, 0.220,
                0.114, 0.049, 0.019, 0.005));
        patternLookupTable.add(Arrays.asList(0.051, 0.227, 0.521, 0.821, 1.000, 0.997, 0.846, 0.628, 0.413, 0.244,
                0.130, 0.058, 0.022, 0.007));
        patternLookupTable.add(Arrays.asList(0.045, 0.206, 0.486, 0.786, 0.980, 1.000, 0.869, 0.660, 0.444, 0.268,
                0.147, 0.070, 0.027, 0.010));
        patternLookupTable.add(Arrays.asList(0.042, 0.196, 0.468, 0.767, 0.968, 1.000, 0.879, 0.676, 0.460, 0.281,
                0.156, 0.075, 0.030, 0.011));
        patternLookupTable.add(Arrays.asList(0.038, 0.179, 0.437, 0.733, 0.947, 1.000, 0.899, 0.705, 0.491, 0.307,
                0.173, 0.086, 0.036, 0.013, 0.002));
        patternLookupTable.add(Arrays.asList(0.033, 0.163, 0.408, 0.701, 0.926, 1.000, 0.919, 0.736, 0.524, 0.335,
                0.193, 0.099, 0.043, 0.016, 0.004));
        patternLookupTable.add(Arrays.asList(0.030, 0.149, 0.382, 0.670, 0.906, 1.000, 0.938, 0.768, 0.558, 0.364,
                0.215, 0.113, 0.051, 0.020, 0.006));
        patternLookupTable.add(Arrays.asList(0.026, 0.132, 0.348, 0.629, 0.877, 1.000, 0.971, 0.823, 0.620, 0.420,
                0.258, 0.143, 0.069, 0.028, 0.010));
        patternLookupTable.add(Arrays.asList(0.024, 0.126, 0.337, 0.616, 0.868, 1.000, 0.981, 0.839, 0.638, 0.437,
                0.271, 0.153, 0.074, 0.031, 0.011));
        patternLookupTable.add(Arrays.asList(0.022, 0.116, 0.317, 0.592, 0.851, 1.000, 1.000, 0.872, 0.676, 0.472,
                0.298, 0.172, 0.087, 0.037, 0.014, 0.002));
        patternLookupTable.add(Arrays.asList(0.020, 0.106, 0.294, 0.561, 0.822, 0.983, 1.000, 0.888, 0.700, 0.498,
                0.320, 0.188, 0.099, 0.043, 0.017, 0.004));
        patternLookupTable.add(Arrays.asList(0.017, 0.096, 0.272, 0.529, 0.790, 0.965, 1.000, 0.905, 0.727, 0.526,
                0.346, 0.207, 0.113, 0.050, 0.020, 0.006));
        patternLookupTable.add(Arrays.asList(0.015, 0.087, 0.251, 0.499, 0.761, 0.946, 1.000, 0.922, 0.755, 0.556,
                0.373, 0.227, 0.126, 0.061, 0.024, 0.008));
        patternLookupTable.add(Arrays.asList(0.014, 0.083, 0.242, 0.486, 0.747, 0.937, 1.000, 0.930, 0.768, 0.570,
                0.385, 0.237, 0.134, 0.065, 0.026, 0.009));
        patternLookupTable.add(Arrays.asList(0.013, 0.075, 0.225, 0.459, 0.720, 0.920, 1.000, 0.947, 0.796, 0.602,
                0.415, 0.260, 0.149, 0.075, 0.032, 0.012, 0.001));
        patternLookupTable.add(Arrays.asList(0.012, 0.069, 0.208, 0.435, 0.695, 0.904, 1.000, 0.963, 0.824, 0.633,
                0.443, 0.284, 0.165, 0.085, 0.037, 0.015, 0.002));
        patternLookupTable.add(Arrays.asList(0.010, 0.063, 0.194, 0.412, 0.669, 0.888, 1.000, 0.980, 0.852, 0.667,
                0.475, 0.309, 0.184, 0.098, 0.044, 0.018, 0.005));
        patternLookupTable.add(Arrays.asList(0.009, 0.057, 0.180, 0.391, 0.646, 0.872, 1.000, 0.997, 0.882, 0.702,
                0.509, 0.336, 0.204, 0.113, 0.052, 0.021, 0.006));
        patternLookupTable.add(Arrays.asList(0.009, 0.054, 0.173, 0.379, 0.631, 0.861, 0.995, 1.000, 0.892, 0.717,
                0.523, 0.350, 0.214, 0.119, 0.057, 0.023, 0.008));
        patternLookupTable.add(Arrays.asList(0.008, 0.049, 0.160, 0.355, 0.602, 0.834, 0.980, 1.000, 0.906, 0.739,
                0.548, 0.373, 0.231, 0.132, 0.066, 0.026, 0.010));
        patternLookupTable.add(Arrays.asList(0.007, 0.042, 0.141, 0.321, 0.557, 0.791, 0.953, 1.000, 0.931, 0.781,
                0.596, 0.417, 0.268, 0.158, 0.082, 0.037, 0.014, 0.002));
        patternLookupTable.add(Arrays.asList(0.006, 0.038, 0.130, 0.301, 0.531, 0.767, 0.939, 1.000, 0.945, 0.805,
                0.624, 0.443, 0.289, 0.174, 0.093, 0.043, 0.017, 0.004));
        patternLookupTable.add(Arrays.asList(0.005, 0.035, 0.120, 0.283, 0.507, 0.744, 0.925, 1.000, 0.960, 0.830,
                0.653, 0.470, 0.312, 0.191, 0.106, 0.051, 0.020, 0.006));
        patternLookupTable.add(Arrays.asList(0.005, 0.033, 0.115, 0.274, 0.495, 0.732, 0.918, 1.000, 0.967, 0.842,
                0.668, 0.485, 0.324, 0.200, 0.112, 0.054, 0.023, 0.007));
        patternLookupTable.add(Arrays.asList(0.004, 0.030, 0.107, 0.257, 0.472, 0.710, 0.904, 1.000, 0.982, 0.868,
                0.699, 0.515, 0.351, 0.219, 0.126, 0.063, 0.027, 0.010));
        patternLookupTable.add(Arrays.asList(0.004, 0.027, 0.098, 0.242, 0.450, 0.689, 0.890, 1.000, 0.997, 0.894,
                0.731, 0.547, 0.378, 0.241, 0.141, 0.072, 0.032, 0.012, 0.002));
        patternLookupTable.add(Arrays.asList(0.003, 0.025, 0.090, 0.224, 0.426, 0.661, 0.867, 0.989, 1.000, 0.911,
                0.756, 0.574, 0.402, 0.260, 0.155, 0.082, 0.037, 0.014, 0.003));
        patternLookupTable.add(Arrays.asList(0.003, 0.022, 0.082, 0.208, 0.402, 0.633, 0.843, 0.975, 1.000, 0.925,
                0.777, 0.598, 0.425, 0.279, 0.169, 0.092, 0.043, 0.017, 0.005));
        patternLookupTable.add(Arrays.asList(0.003, 0.021, 0.079, 0.202, 0.392, 0.621, 0.833, 0.969, 1.000, 0.930,
                0.786, 0.609, 0.435, 0.288, 0.176, 0.097, 0.046, 0.018, 0.006));
        patternLookupTable.add(Arrays.asList(0.003, 0.019, 0.073, 0.188, 0.370, 0.595, 0.810, 0.955, 1.000, 0.943,
                0.808, 0.634, 0.460, 0.309, 0.191, 0.108, 0.053, 0.022, 0.007));
        patternLookupTable.add(Arrays.asList(0.002, 0.017, 0.067, 0.175, 0.350, 0.570, 0.787, 0.942, 1.000, 0.956,
                0.831, 0.662, 0.487, 0.331, 0.209, 0.121, 0.062, 0.026, 0.010));
        patternLookupTable.add(Arrays.asList(0.002, 0.016, 0.061, 0.163, 0.330, 0.547, 0.765, 0.929, 1.000, 0.968,
                0.855, 0.690, 0.515, 0.356, 0.227, 0.135, 0.070, 0.031, 0.012, 0.002));
        patternLookupTable.add(Arrays.asList(0.002, 0.014, 0.056, 0.151, 0.312, 0.524, 0.743, 0.916, 1.000, 0.982,
                0.878, 0.718, 0.544, 0.382, 0.247, 0.149, 0.079, 0.037, 0.014, 0.003));
        patternLookupTable.add(Arrays.asList(0.002, 0.013, 0.054, 0.146, 0.304, 0.514, 0.733, 0.909, 1.000, 0.989,
                0.890, 0.733, 0.559, 0.395, 0.257, 0.156, 0.084, 0.039, 0.016, 0.004));
        patternLookupTable.add(Arrays.asList(0.001, 0.012, 0.047, 0.131, 0.276, 0.478, 0.697, 0.881, 0.989, 1.000,
                0.920, 0.777, 0.605, 0.437, 0.292, 0.182, 0.102, 0.051, 0.022, 0.007));
        patternLookupTable.add(Arrays.asList(0.001, 0.010, 0.043, 0.121, 0.259, 0.454, 0.671, 0.859, 0.977, 1.000,
                0.932, 0.797, 0.629, 0.460, 0.312, 0.197, 0.114, 0.058, 0.025, 0.008, 0.001));

        return patternLookupTable.get(mass);
    }

    public static List<Peak> deconvolute(List<Peak> peakList) {
        List<Peak> buff = new ArrayList<>();
        List<Peak> peaklistcopy = new ArrayList<>();

        for (Peak e : peakList) {
            peaklistcopy.add(e);
        }

        for (Peak peak : peaklistcopy) {

            if (peak.getCharge() == -1) {
                continue;
            } else if (peak.getCharge() == 1) {
                buff.add(peak);
            } else {
                if (peak.getFwhm() != -1) {
                    peak.setFwhm(Math.abs(peak.getFwhm() * peak.getCharge()));
                }

                if (peak.getCharge() < 0) {
                    peak.setMz(calculateMass(peak.getMz(), -1, peak.getCharge()));
                    peak.setCharge(-1);
                } else {
                    peak.setMz(calculateMass(peak.getMz(), 1, peak.getCharge()));
                    peak.setCharge(1);
                }

                buff.add(peak);
            }
        }

        peakList = buff;

        peakList = sortPeaklist(peakList);

        return peakList;
    }

    private static List<Peak> sortPeaklist(List<Peak> list) {
        Collections.sort(list, new Comparator<Peak>() {
            @Override
            public int compare(Peak o1, Peak o2) {
                Double mz1 = o1.getMz();
                Double mz2 = o2.getMz();
                return mz1.compareTo(mz2);
            }
        });

        return list;
    }
}
