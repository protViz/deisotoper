import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindNN {
	private static double[] q;
	private static double[] vec;

	public static void setQ(double[] x) {
		FindNN.q = x;
	}

	public static void setVec(double[] x) {
		FindNN.vec = x;
	}

	public static double[] findNN(double[] q, double[] vec) {
		List<Double> idx = new ArrayList<Double>();

		for (double d : q)
			idx.add((double) Arrays.binarySearch(vec, d));

		return (idx.stream().mapToDouble(Double::doubleValue).toArray());
	}

	public static double[] getIndex() {
		return FindNN.findNN(FindNN.q, FindNN.vec);
	}

	public static void main(String[] args) {
		double[] vectest2 = { -1, -0.98, -0.96, -0.94, -0.92, -0.9, -0.88, -0.86, -0.84, -0.82, -0.8, -0.78, -0.76,
				-0.74, -0.72, -0.7, -0.68, -0.66, -0.64, -0.62, -0.6, -0.58, -0.56, -0.54, -0.52, -0.5, -0.48, -0.46,
				-0.44, -0.42, -0.4, -0.38, -0.36, -0.34, -0.32, -0.3, -0.28, -0.26, -0.24, -0.22, -0.2, -0.18, -0.16,
				-0.14, -0.12, -0.1, -0.08, -0.0599999999999999, -0.04, -0.02, 0, 0.02, 0.04, 0.0600000000000001,
				0.0800000000000001, 0.1, 0.12, 0.14, 0.16, 0.18, 0.2, 0.22, 0.24, 0.26, 0.28, 0.3, 0.32, 0.34, 0.36,
				0.38, 0.4, 0.42, 0.44, 0.46, 0.48, 0.5, 0.52, 0.54, 0.56, 0.58, 0.6, 0.62, 0.64, 0.66, 0.68, 0.7, 0.72,
				0.74, 0.76, 0.78, 0.8, 0.82, 0.84, 0.86, 0.88, 0.9, 0.92, 0.94, 0.96, 0.98, 1 };
		
		double[] vectest = {2,3};

		double[] qtest2 = { -1000, 0, 0.001, 10, 10000 };

		double[] qtest = {2.3,2.5,2.8};
		
		for (double i : findNN(qtest, vectest)) {
			if (i < 0) {
				i = -i - 1; // Convert n to an index if not correctly
			}
			System.out.println("Pattern found at Index: " + (int) i);
		}

	}
}
