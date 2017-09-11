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

        peaklistout.getPeaklist().get(17).setCharge(1);
        peaklistout.getPeaklist().get(17).setIsotope(0);

        peaklistout.getPeaklist().get(19).setCharge(2);
        peaklistout.getPeaklist().get(19).setIsotope(0);

        peaklistout.getPeaklist().get(20).setCharge(2);
        peaklistout.getPeaklist().get(20).setIsotope(1);

        peaklistout.getPeaklist().get(24).setCharge(2);
        peaklistout.getPeaklist().get(24).setIsotope(0);

        peaklistout.getPeaklist().get(30).setCharge(1);
        peaklistout.getPeaklist().get(30).setIsotope(0);

        peaklistout.getPeaklist().get(31).setCharge(1);
        peaklistout.getPeaklist().get(31).setIsotope(1);

        peaklistout.getPeaklist().get(32).setCharge(1);
        peaklistout.getPeaklist().get(32).setIsotope(0);

        peaklistout.getPeaklist().get(35).setCharge(1);
        peaklistout.getPeaklist().get(35).setIsotope(0);

        peaklistout.getPeaklist().get(39).setCharge(1);
        peaklistout.getPeaklist().get(39).setIsotope(0);

        peaklistout.getPeaklist().get(40).setCharge(1);
        peaklistout.getPeaklist().get(40).setIsotope(1);

        peaklistout.getPeaklist().get(41).setCharge(1);
        peaklistout.getPeaklist().get(41).setIsotope(2);

        peaklistout.getPeaklist().get(43).setCharge(1);
        peaklistout.getPeaklist().get(43).setIsotope(0);

        peaklistout.getPeaklist().get(44).setCharge(1);
        peaklistout.getPeaklist().get(44).setIsotope(1);

        peaklistout.getPeaklist().get(45).setCharge(1);
        peaklistout.getPeaklist().get(45).setIsotope(0);

        peaklistout.getPeaklist().get(46).setCharge(1);
        peaklistout.getPeaklist().get(46).setIsotope(1);

        peaklistout.getPeaklist().get(47).setCharge(1);
        peaklistout.getPeaklist().get(47).setIsotope(0);

        peaklistout.getPeaklist().get(48).setCharge(1);
        peaklistout.getPeaklist().get(48).setIsotope(1);

    }

    @Test
    public void test() {
        List<Peak> out = Deisotope.deisotope(peaklistin.getPeaklist(), 3, 0.15, 0.5, 0.0);

        for (Peak p : out) {
            System.out.println("MZ: " + p.getMz() + ", INTENSITY:" + p.getIntensity() + ", CHARGE:" + p.getCharge() + ", ISOTOPE:" + p.getIsotope());
        }
        System.out.println(out.size());
        for (Peak p : peaklistout.getPeaklist()) {
            System.out.println("MZ: " + p.getMz() + ", INTENSITY:" + p.getIntensity() + ", CHARGE:" + p.getCharge() + ", ISOTOPE:" + p.getIsotope());
        }
        System.out.println(peaklistout.getPeaklist().size());

        assertPeaklistEquals(out, peaklistout.getPeaklist());
    }

    private void assertPeaklistEquals(List<Peak> list, List<Peak> list2) {
        for (int i = 0; i < list.size() || i < list2.size(); i++) {
            boolean b = false;

            if (list.get(i).getMz() == list2.get(i).getMz() && list.get(i).getIntensity() == list2.get(i).getIntensity() && list.get(i).getCharge() == list2.get(i).getCharge()
                    && list.get(i).getIsotope() == list2.get(i).getIsotope()) {
                b = true;
            }
            if (b == false) {
                System.out.println("Error at:");
                System.out.println("MZ: " + list.get(i).getMz() + ", INTENSITY:" + list.get(i).getIntensity() + ", CHARGE:" + list.get(i).getCharge() + ", ISOTOPE:" + list.get(i).getIsotope());
                System.out.println("MZ: " + list2.get(i).getMz() + ", INTENSITY:" + list2.get(i).getIntensity() + ", CHARGE:" + list2.get(i).getCharge() + ", ISOTOPE:" + list2.get(i).getIsotope());
            }

            assertTrue(b);
        }
    }
}