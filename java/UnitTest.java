
/**
 * @author Lucas Schmidt
 * @since 2017-08-16
 */

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.Test;

public class UnitTest {

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

        // System.out.print("mZ: ");
        // for (int m = 0; m < mZmain2.length; m++) {
        // System.out.print(mZmain2[m] + " ");
        // }
        // System.out.println();
        //
        // System.out.print("pattern: ");
        // for (int p = 0; p < patternmain2.length; p++) {
        // System.out.print(patternmain2[p] + " ");
        // }
        // System.out.println();

        double epsmain = 2;

        FindPatternBinarySearch.setMz(mZmain2);
        FindPatternBinarySearch.setPattern(patternmain2);
        FindPatternBinarySearch.setEps(epsmain);

        FindPatternLinear.setMz(mZmain2);
        FindPatternLinear.setPattern(patternmain2);
        FindPatternLinear.setEps(epsmain);
    }

    @Test
    public void test() {
        setAll();

        double[] indexlinear = FindPatternLinear.getIndex();
        double[] indexbinary = FindPatternBinarySearch.getIndex();

        assertEquals("MUST BE: indexlinear.length == indexbinary.length", indexlinear.length, indexbinary.length);

        for (int i = 0; i < indexlinear.length; i++) {
            assertEquals("MUST BE: double[] indexlinear == double[] indexbinary", (int) indexlinear[i], (int) indexbinary[i]);
        }

    }

}
