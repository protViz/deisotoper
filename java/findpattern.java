import java.util.ArrayList;
import java.util.List;

//Author: Lucas Schmidt
//Date: 2017-08-15

public class findpattern {
	private static double[] mZ;
	private static double[] pattern;
	private static double eps;

	public static void setmZ(double[] x) {
		findpattern.mZ = x;
	}

	public static void setpattern(double[] x) {
		findpattern.pattern = x;
	}

	public static void seteps(double x) {
		findpattern.eps = x;
	}

	public static double[] find_pattern(double[] mZ, double[] pattern, double eps) {
		// double[] index = null;
		List<Double> index = new ArrayList<Double>();
		for (int i = 0; i < mZ.length; i++) {
			// System.out.println("i " + i + " mZ " + mZ[i]);
			for (int j = 0; j < pattern.length; j++) {
				if (pattern[j] - eps <= mZ[i] && mZ[i] <= pattern[j] + eps) {
					// System.out.println("j " + j + " mZ " + mZ[i] + " pattern " + pattern[j]);
					index.add((double) i);
				}
			}

		}

		double[] idx = index.stream().mapToDouble(Double::doubleValue).toArray();

		if (idx.length == 0) {
			return null;
		} else {
			return idx;
		}
	}

	public static double[] getIndex() {
		return find_pattern(findpattern.mZ, findpattern.pattern, findpattern.eps);
	}

	// For Testing only
	public static void setall() {
		double[] mZmain = { 3.3, 3.4, 4.1, 4.3, 4.34, 5.3, 5.3456, 7.664 };
		double[] patternmain = { 4.3 };
		double epsmain = 0.5;

		findpattern.mZ = mZmain;
		findpattern.pattern = patternmain;
		findpattern.eps = epsmain;

	}

	// For Testing only
	public static void main(String[] args) {
		// double[] mZmain = { 3.3, 4.3, 4.34 };
		// double[] patternmain = { 4.3 };
		// double epsmain = 0.01;

		setall();

		// double[] indexmain = find_pattern(mZmain, patternmain, epsmain);
		double[] indexmain = getIndex();

		for (int i = 0; i < indexmain.length; i++) {
			System.out.println("Pattern found at Index: " + (int) indexmain[i]);
		}
	}
}
