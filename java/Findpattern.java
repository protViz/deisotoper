import java.util.ArrayList;
import java.util.List;

//Author: Lucas Schmidt
//Date: 2017-08-15

public class Findpattern {
	private static double[] mZ;
	private static double[] pattern;
	private static double eps;

	public static void setmZ(double[] x) {
		Findpattern.mZ = x;
	}

	public static void setpattern(double[] x) {
		Findpattern.pattern = x;
	}

	public static void seteps(double x) {
		Findpattern.eps = x;
	}

	public static double[] find_pattern(double[] mZ, double[] pattern, double eps) {
		List<Double> index = new ArrayList<Double>();
		for (int i = 0; i < mZ.length; i++) {
			for (int j = 0; j < pattern.length; j++) {
				if (pattern[j] - eps <= mZ[i] && mZ[i] <= pattern[j] + eps) {
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
		return find_pattern(Findpattern.mZ, Findpattern.pattern, Findpattern.eps);
	}

	// For Testing only
	public static void setall() {
		double[] mZmain = { 1, 3, 4, 5, 7, 9, 11, 13, 14, 15, 17.5, 18, 20, 22 };
		double[] patternmain = {0.6, 1, 5, 6, 23 };
		double epsmain = 1;

		Findpattern.mZ = mZmain;
		Findpattern.pattern = patternmain;
		Findpattern.eps = epsmain;

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
