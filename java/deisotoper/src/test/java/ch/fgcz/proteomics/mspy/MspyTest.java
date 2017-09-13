package ch.fgcz.proteomics.mspy;

/**
 * @author Lucas Schmidt
 * @since 2017-09-11
 */

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class MspyTest {
    Peaklist peaklistin;
    Peaklist peaklistout;

    @Before
    public void setUp() {
        // HeLa filtered pd21 ID 10 input
        List<Double> mzi = Arrays.asList(120.08112, 127.05046, 129.10248, 130.08647, 145.06100, 147.11298, 173.05595, 183.14944, 187.10793, 191.11806, 211.14433, 219.11301, 219.14932, 226.11879,
                228.17076, 233.12860, 245.12456, 246.18129, 272.12418, 273.15988, 311.17133, 311.67285, 318.18118, 361.21945, 365.21802, 367.71344, 368.21527, 374.20749, 389.25476, 393.24963,
                417.24994, 464.28693, 465.29050, 473.27631, 474.28079, 478.30136, 488.28372, 489.28659, 491.28381, 506.27274, 524.28314, 525.28650, 526.28674, 563.29309, 563.35559, 564.35809,
                581.30481, 582.30756, 621.33667, 622.33978, 631.33752, 636.37140);
        List<Double> ii = Arrays.asList(134798.0, 250757.0, 113242.0, 96282.8, 73135.9, 209426.0, 76674.6, 90304.0, 89916.3, 61007.0, 55596.0, 128882.0, 54083.8, 33974.4, 21921.7, 59215.3, 21259.0,
                158157.0, 23047.4, 33589.2, 108160.0, 39693.8, 84191.1, 52743.0, 13328.6, 40664.2, 21989.6, 34377.6, 14409.4, 83177.9, 7503.34, 181362.0, 33635.4, 2454.38, 1675.37, 2984.05, 2407.48,
                2389.05, 1778.45, 2974.41, 40190.6, 12985.8, 1740.92, 2295.79, 60876.1, 15365.0, 31691.5, 11432.0, 16625.5, 4982.01, 1941.02, 2423.14);
        peaklistin = new Peaklist(mzi, ii);

        // HeLa filtered pd21 ID 10 mMass output #1
        List<Double> mzo1 = Arrays.asList(120.08112, 127.05046, 129.10248, 130.08647, 145.06100, 147.11298, 173.05595, 183.14944, 187.10793, 191.11806, 211.14433, 219.11301, 219.14932, 226.11879,
                228.17076, 233.12860, 245.12456, 246.18129, 272.12418, 273.15988, 311.17133, 311.67285, 318.18118, 361.21945, 365.21802, 367.71344, 368.21527, 374.20749, 389.25476, 393.24963,
                417.24994, 464.28693, 465.29050, 473.27631, 474.28079, 478.30136, 488.28372, 489.28659, 491.28381, 506.27274, 524.28314, 525.28650, 526.28674, 563.29309, 563.35559, 564.35809,
                581.30481, 582.30756, 621.33667, 622.33978, 631.33752, 636.37140);
        List<Double> io1 = Arrays.asList(134798.0, 250757.0, 113242.0, 96282.8, 73135.9, 209426.0, 76674.6, 90304.0, 89916.3, 61007.0, 55596.0, 128882.0, 54083.8, 33974.4, 21921.7, 59215.3, 21259.0,
                158157.0, 23047.4, 33589.2, 108160.0, 39693.8, 84191.1, 52743.0, 13328.6, 40664.2, 21989.6, 34377.6, 14409.4, 83177.9, 7503.34, 181362.0, 33635.4, 2454.38, 1675.37, 2984.05, 2407.48,
                2389.05, 1778.45, 2974.41, 40190.6, 12985.8, 1740.92, 2295.79, 60876.1, 15365.0, 31691.5, 11432.0, 16625.5, 4982.01, 1941.02, 2423.14);
        peaklistout = new Peaklist(mzo1, io1);

        peaklistout.getPeaklist().get(2).setCharge(1);
        peaklistout.getPeaklist().get(2).setIsotope(0);

        peaklistout.getPeaklist().get(19).setCharge(1);
        peaklistout.getPeaklist().get(19).setIsotope(0);

        peaklistout.getPeaklist().get(21).setCharge(2);
        peaklistout.getPeaklist().get(21).setIsotope(0);

        peaklistout.getPeaklist().get(22).setCharge(2);
        peaklistout.getPeaklist().get(22).setIsotope(1);

        peaklistout.getPeaklist().get(26).setCharge(2);
        peaklistout.getPeaklist().get(26).setIsotope(0);

        peaklistout.getPeaklist().get(31).setCharge(1);
        peaklistout.getPeaklist().get(31).setIsotope(0);

        peaklistout.getPeaklist().get(32).setCharge(1);
        peaklistout.getPeaklist().get(32).setIsotope(1);

        peaklistout.getPeaklist().get(33).setCharge(1);
        peaklistout.getPeaklist().get(33).setIsotope(0);

        peaklistout.getPeaklist().get(36).setCharge(1);
        peaklistout.getPeaklist().get(36).setIsotope(0);

        peaklistout.getPeaklist().get(40).setCharge(1);
        peaklistout.getPeaklist().get(40).setIsotope(0);

        peaklistout.getPeaklist().get(41).setCharge(1);
        peaklistout.getPeaklist().get(41).setIsotope(1);

        peaklistout.getPeaklist().get(42).setCharge(1);
        peaklistout.getPeaklist().get(42).setIsotope(2);

        peaklistout.getPeaklist().get(44).setCharge(1);
        peaklistout.getPeaklist().get(44).setIsotope(0);

        peaklistout.getPeaklist().get(45).setCharge(1);
        peaklistout.getPeaklist().get(45).setIsotope(1);

        peaklistout.getPeaklist().get(46).setCharge(1);
        peaklistout.getPeaklist().get(46).setIsotope(0);

        peaklistout.getPeaklist().get(47).setCharge(1);
        peaklistout.getPeaklist().get(47).setIsotope(1);

        peaklistout.getPeaklist().get(48).setCharge(1);
        peaklistout.getPeaklist().get(48).setIsotope(0);

        peaklistout.getPeaklist().get(49).setCharge(1);
        peaklistout.getPeaklist().get(49).setIsotope(1);
    }

    @Test
    public void test() {
        List<Peak> out = Deisotope.deisotope(peaklistin.getPeaklist(), 3, 0.15, 0.5, 0.0);

        System.out.println("Out output:");
        int a = 0;
        int index1 = 0;
        for (Peak p : out) {
            if (p.getCharge() != -1) {
                System.out.println("test| Index: " + index1 + ", MZ: " + p.getMz() + ", INTENSITY:" + p.getIntensity() + ", CHARGE:" + p.getCharge() + ", ISOTOPE:" + p.getIsotope());
                a++;
            }
            index1++;
        }
        System.out.println("test| Size: " + out.size() + ", charge != 0: " + a);
        System.out.println();

        System.out.println("Their output:");
        int b = 0;
        int index2 = 0;
        for (Peak p : peaklistout.getPeaklist()) {
            if (p.getCharge() != -1) {
                System.out.println("test| Index: " + index2 + ", MZ: " + p.getMz() + ", INTENSITY:" + p.getIntensity() + ", CHARGE:" + p.getCharge() + ", ISOTOPE:" + p.getIsotope());
                b++;
            }
            index2++;
        }
        System.out.println("test| Size: " + peaklistout.getPeaklist().size() + ", charge != 0: " + b);

        System.out.println();
        System.out.println("Correctness check: ");
        for (int i = 0; i < out.size() || i < peaklistout.getPeaklist().size(); i++) {
            // if (out.get(i).getCharge() != -1 && peaklistout.getPeaklist().get(i).getCharge() != -1) {
            if (out.get(i).getMz() == peaklistout.getPeaklist().get(i).getMz() && out.get(i).getIntensity() == peaklistout.getPeaklist().get(i).getIntensity()
                    && out.get(i).getCharge() == peaklistout.getPeaklist().get(i).getCharge() && out.get(i).getIsotope() == peaklistout.getPeaklist().get(i).getIsotope()) {
                System.out.println("Correct: ");
                System.out.println("test| Index: " + i + ", MZ: " + out.get(i).getMz() + ", INTENSITY:" + out.get(i).getIntensity() + ", CHARGE:" + out.get(i).getCharge() + ", ISOTOPE:"
                        + out.get(i).getIsotope());
                System.out.println("test| Index: " + i + ", MZ: " + peaklistout.getPeaklist().get(i).getMz() + ", INTENSITY:" + peaklistout.getPeaklist().get(i).getIntensity() + ", CHARGE:"
                        + peaklistout.getPeaklist().get(i).getCharge() + ", ISOTOPE:" + peaklistout.getPeaklist().get(i).getIsotope());
                System.out.println();
                // }
            } else {
                // if (out.get(i).getCharge() != -1 && peaklistout.getPeaklist().get(i).getCharge() != -1) {
                System.out.println("Incorrect: ");
                System.out.println("test| Index: " + i + ", MZ: " + out.get(i).getMz() + ", INTENSITY:" + out.get(i).getIntensity() + ", CHARGE:" + out.get(i).getCharge() + ", ISOTOPE:"
                        + out.get(i).getIsotope());
                System.out.println("test| Index: " + i + ", MZ: " + peaklistout.getPeaklist().get(i).getMz() + ", INTENSITY:" + peaklistout.getPeaklist().get(i).getIntensity() + ", CHARGE:"
                        + peaklistout.getPeaklist().get(i).getCharge() + ", ISOTOPE:" + peaklistout.getPeaklist().get(i).getIsotope());
                System.out.println();
                // }
            }
        }

        assertPeaklistEquals(out, peaklistout.getPeaklist());
    }

    private void assertPeaklistEquals(List<Peak> list, List<Peak> list2) {
        for (int i = 0; i < list.size() || i < list2.size(); i++) {
            boolean b = false;

            if (list.get(i).getMz() == list2.get(i).getMz() && list.get(i).getIntensity() == list2.get(i).getIntensity() && list.get(i).getCharge() == list2.get(i).getCharge()
                    && list.get(i).getIsotope() == list2.get(i).getIsotope()) {
                b = true;
            }

            assertTrue("Values should be same. (Index: " + i + ")", b);
        }
    }
}