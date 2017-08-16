import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Lucas Schmidt
 * @since 2017-08-16
 */

public class TestClass {
    private static void setAll() {
        List<Double> mZmain = new ArrayList<Double>();
        List<Double> patternmain = new ArrayList<Double>();

        for (int i = 0; i < 1000; i++) {
            double random = ThreadLocalRandom.current().nextDouble(50, 500);
            mZmain.add(random);
        }

        for (int i = 0; i < 50; i++) {
            double random = ThreadLocalRandom.current().nextDouble(0, 550);
            patternmain.add(random);
        }

        Collections.sort(mZmain);
        Collections.sort(patternmain);

        double[] mZmain2 = mZmain.stream().mapToDouble(Double::doubleValue).toArray();
        double[] patternmain2 = patternmain.stream().mapToDouble(Double::doubleValue).toArray();

        System.out.print("mZ: ");
        for (int m = 0; m < mZmain2.length; m++) {
            System.out.print(mZmain2[m] + " ");
        }
        System.out.println();

        System.out.print("pattern: ");
        for (int p = 0; p < patternmain2.length; p++) {
            System.out.print(patternmain2[p] + " ");
        }
        System.out.println();

        double epsmain = 2;

        FindPatternBinarySearch.setMz(mZmain2);
        FindPatternBinarySearch.setPattern(patternmain2);
        FindPatternBinarySearch.setEps(epsmain);

        FindPatternLinear.setMz(mZmain2);
        FindPatternLinear.setPattern(patternmain2);
        FindPatternLinear.setEps(epsmain);
    }

    public static boolean testForFindPattern() {
        setAll();
        double[] linear = FindPatternLinear.getIndex();
        double[] binary = FindPatternBinarySearch.getIndex();

        if (linear.length == binary.length) {
            for (int i = 0; i < linear.length; i++) {
                if (linear[i] == binary[i]) {
                    System.out.println("linear: " + (int) linear[i] + ", binary: " + (int) binary[i] + ", true");
                } else {
                    System.out.println("linear: " + (int) linear[i] + ", binary: " + (int) binary[i] + ", false");
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println(testForFindPattern());
    }
}
