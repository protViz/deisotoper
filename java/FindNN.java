
/**
 * @author Lucas Schmidt
 * @since 2017-08-21
 */

import java.util.Arrays;

public class FindNN {
    private static double[] q;
    private static double[] vec;

    public static void setQ(double[] x) {
        FindNN.q = x;
    }

    public static void setVec(double[] x) {
        FindNN.vec = x;
    }

    /**
     * Finds the nearest neighbors. Applies the pattern on the data to find the nearest neighbors of the pattern.
     * 
     * @param pattern
     * @param data
     * @return index array
     * @see MassSpectrometryMeasurement
     */
    public static double[] findNN(double[] pattern, double[] data) {
        double[] index = new double[pattern.length];

        for (int i = 0; i < pattern.length; i++) {
            index[i] = ((double) Arrays.binarySearch(data, pattern[i]));
            if (index[i] < 0)
                index[i] = -(index[i]) - 1;

            if (index[i] == 0) {
            } else if (index[i] >= data.length) {
                index[i] = index[i] - 1;
            } else {
                index[i] = (((Math.abs(data[(int) (index[i] - 1)] - pattern[i])) < (Math.abs(data[(int) index[i]] - pattern[i]))) ? index[i] - 1 : index[i]);
            }
        }

        return index;
    }

    /**
     * @return index array
     */
    public static double[] getIndex() {
        return FindNN.findNN(FindNN.q, FindNN.vec);
    }

    public static void main(String[] args) {
        double[] vectest1 = { 2, 3 };
        double[] vectest2 = { 85.9475706629195, 86.7750793375486, 86.9967229560885, 87.6731262158718, 88.6829821533834, 89.3396244108142, 89.8757880719337, 89.9584765519992, 90.2825646251909,
                90.431521530632, 91.1389143263587, 91.921342144088, 92.2280356720829, 92.2415691164642, 92.4377087687997, 92.5932143517964, 92.6500683333295, 92.8603607201231, 93.0491255652764,
                93.0623082543646, 93.3548338320611, 94.8106060770257, 94.9067505814658, 95.0189208409334, 95.0890198758729, 95.3262380215399, 95.6130846412633, 95.7925786610902, 96.0254409596587,
                96.35566170251, 96.3820762532736, 96.6643606292586, 96.9811909782282, 97.4360136701071, 97.746473210741, 97.9059534858245, 97.9252710745944, 97.9932950413078, 98.0576941325114,
                98.2027928423104, 98.3950806568394, 98.5216607448496, 98.6125035807534, 99.7475028100491, 100.328571560527, 100.637917766864, 100.718101434643, 100.833207927132, 100.961423954928,
                100.982024504662, 100.996346397962, 101.422700667736, 102.288507766316, 102.362217478546, 102.453825772869, 102.467483144163, 102.754701074105, 103.20327914459, 103.22183290181,
                103.314750747746, 103.333977285928, 103.363483484325, 103.422411325728, 104.011688682475, 104.197147724307, 104.426539295269, 105.078168257753, 105.26040540922, 105.400901272821,
                105.461610038605, 105.860977822103, 105.92488873846, 105.975075090741, 106.055844074642, 106.562673640634, 106.907740376078, 107.286944340326, 107.913015923358, 108.154319483362,
                108.373006215209, 109.962207556301, 109.975693121142, 110.042692353348, 110.183057433971, 111.089702456451, 111.402628677836, 111.619554942092, 111.928468788626, 112.210900515379,
                113.197816821342, 113.426947990167, 115.660867000129, 115.738388967384, 116.366726086019, 116.673517752972, 116.76284457042, 117.270241719093, 117.669586987771, 120.214589175879,
                127.695481003 };
        double[] vectest3 = { -1, -0.98, -0.96, -0.94, -0.92, -0.9, -0.88, -0.86, -0.84, -0.82, -0.8, -0.78, -0.76, -0.74, -0.72, -0.7, -0.68, -0.66, -0.64, -0.62, -0.6, -0.58, -0.56, -0.54, -0.52,
                -0.5, -0.48, -0.46, -0.44, -0.42, -0.4, -0.38, -0.36, -0.34, -0.32, -0.3, -0.28, -0.26, -0.24, -0.22, -0.2, -0.18, -0.16, -0.14, -0.12, -0.1, -0.08, -0.0599999999999999, -0.04, -0.02,
                0, 0.02, 0.04, 0.0600000000000001, 0.0800000000000001, 0.1, 0.12, 0.14, 0.16, 0.18, 0.2, 0.22, 0.24, 0.26, 0.28, 0.3, 0.32, 0.34, 0.36, 0.38, 0.4, 0.42, 0.44, 0.46, 0.48, 0.5, 0.52,
                0.54, 0.56, 0.58, 0.6, 0.62, 0.64, 0.66, 0.68, 0.7, 0.72, 0.74, 0.76, 0.78, 0.8, 0.82, 0.84, 0.86, 0.88, 0.9, 0.92, 0.94, 0.96, 0.98, 1 };

        double[] qtest1 = { 2.3, 2.49, 2.5, 2.8, 2.9 };
        double[] qtest3 = { -1000, 0, 0.001, 10, 10000 };

        for (double i : findNN(qtest1, vectest1)) {
            System.out.println("Test1: " + (int) i);
        }

        System.out.println("______________");

        int j = 0;
        int sum = 0;
        int sumj = 0;
        for (double i : findNN(vectest2, vectest2)) {
            sum = (int) i + sum;
            sumj = j + sumj;
            System.out.println((int) i == j);
            j++;
        }
        System.out.println("SUM: " + (sum == sumj));

        System.out.println("______________");

        for (double i : findNN(qtest3, vectest3)) {
            System.out.println("Test3: " + (int) i);
        }
    }
}
