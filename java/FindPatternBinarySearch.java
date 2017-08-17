/**
 * @author Lucas Schmidt
 * @since 2017-08-16
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FindPatternBinarySearch {
    private static double[] mZ;
    private static double[] pattern;
    private static double eps;

    public static void setMz(double[] x) {
        FindPatternBinarySearch.mZ = x;
    }

    public static void setPattern(double[] x) {
        FindPatternBinarySearch.pattern = x;
    }

    public static void setEps(double x) {
        FindPatternBinarySearch.eps = x;
    }

    private static double[] findPattern(double[] mZ, double[] pattern, double eps) {
        List<Double> index = new ArrayList<Double>();

        for (int i = 0; i < pattern.length; i++) {
            List<Double> found = searchNearest(mZ, pattern[i]);

            index.addAll(found);
        }

        Collections.sort(index);

        double[] idx = index.stream().mapToDouble(Double::doubleValue).toArray();

        if (idx.length == 0) {
            return null;
        } else {
            return idx;
        }
    }

    public static double[] getIndex() {
        return findPattern(FindPatternBinarySearch.mZ, FindPatternBinarySearch.pattern, FindPatternBinarySearch.eps);
    }

    private static final List<Double> searchNearest(final double[] values, final double key) {
        List<Double> neighbor1 = new ArrayList<Double>();
        List<Double> neighbor2 = new ArrayList<Double>();
        int n = binarySearch(values, key);

        if (n < 0) {
            n = (-n) - 1; // Convert n to an index if not correctly
        }

        if (n > values.length - 1) {
            n = values.length - 1; // If key is larger than any value in values
            // System.out.println((n - 1) + ": " + values[n - 1] + ", " + key);
            for (int i = 0; i < values.length; i++) {
                // System.out.println("m " + i);
                if (0 <= n - i) {
                    if (values[n - i] >= key - FindPatternBinarySearch.eps) {
                        if (values[n - i] <= key) {
                            neighbor2.add((double) n - i);
                            // System.out.println("MINUS: " + values[n - i] + " > " + (key -
                            // Findpatternbinary.eps));
                        }
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        } else if (n > 0) {
            // System.out.println((n - 1) + ": " + values[n - 1] + ", " + key + ", " + n +
            // ": " + values[n]);

            for (int i = 0; i < values.length; i++) {
                // System.out.println("m " + i);
                if (0 <= n - i) {
                    if (values[n - i] >= key - FindPatternBinarySearch.eps) {
                        if (values[n - i] < key) {
                            neighbor2.add((double) n - i);
                            // System.out.println("MINUS: " + values[n - i] + " > " + (key -
                            // Findpatternbinary.eps)
                            // + " INDEX: " + (n - i));
                        }
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }

            for (int j = 0; j < values.length; j++) {
                // System.out.println("p " + j);
                if (n + j < values.length) {
                    if (values[n + j] <= key + FindPatternBinarySearch.eps) {
                        if (values[n + j] >= key) {
                            neighbor2.add((double) n + j);
                            // System.out.println("PLUS: " + values[n + j] + " < " + (key +
                            // Findpatternbinary.eps)
                            // + " INDEX: " + (n + j));
                        }
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        } else if (n == 0) {
            for (int j = 0; j < values.length; j++) {
                // System.out.println("p " + j);
                if (n + j < values.length) {
                    if (values[n + j] <= key + FindPatternBinarySearch.eps) {
                        if (values[n + j] >= key) {
                            neighbor2.add((double) n + j);
                            // System.out.println("PLUS: " + values[n + j] + " < " + (key +
                            // Findpatternbinary.eps));
                        }
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        }

        neighbor2.addAll(neighbor1);

        return neighbor2;

    }

    private static int binarySearch(double[] a, double key) {
        int index = -1;
        if (a[0] < a[1]) {
            index = Arrays.binarySearch(a, key);
            // System.out.println(key + " a[0] < a[1], index: " + index);
        } else {
            index = binarySearch(a, key, 0, a.length - 1);
            // System.out.println("else, index: " + index);
        }
        return index;
    }

    private static int binarySearch(double[] a, double key, int low, int high) {
        while (low <= high) {
            int i = 0;
            System.out.println("while " + i);
            i++;
            int mid = (low + high) / 2;
            double midVal = a[mid];

            int cmp;
            if (midVal > key) {
                cmp = -1; // Neither val is NaN, thisVal is smaller
            } else if (midVal < key) {
                cmp = 1; // Neither val is NaN, thisVal is larger
            } else {
                long midBits = Double.doubleToLongBits(midVal);
                long keyBits = Double.doubleToLongBits(key);
                cmp = (midBits == keyBits ? 0 : (midBits < keyBits ? -1 : 1)); // (0.0, -0.0) or (NaN, !NaN)
            }

            if (cmp < 0) {
                low = mid + 1;
                // System.out.println("cmp < 0, low: " + low);
            } else if (cmp > 0) {
                high = mid - 1;
                // System.out.println("cmp > 0, high: " + high);
            } else {
                return mid; // key found
            }
        }
        return -(low + 1); // key not found.
    }

    // For Testing only
    public static void setAll() {
        double[] mZmain = { 1, 3, 4, 5, 7, 9, 11, 13, 14, 15, 17.5, 18, 20, 22 };
        double[] patternmain = { 0.6, 1, 5, 6, 23 };
        double epsmain = 1;

        FindPatternBinarySearch.mZ = mZmain;
        FindPatternBinarySearch.pattern = patternmain;
        FindPatternBinarySearch.eps = epsmain;

    }

    // For Testing only
    public static void main(String[] args) {
        setAll();

        double[] indexmain = getIndex();

        for (int i = 0; i < indexmain.length; i++) {
            System.out.println("Pattern found at Index: " + (int) indexmain[i]);
        }
    }
}
