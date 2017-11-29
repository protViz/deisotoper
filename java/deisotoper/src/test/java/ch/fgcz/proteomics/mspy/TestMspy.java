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

@Deprecated
public class TestMspy {
    private Peaklist peaklistin;
    private Peaklist peaklistout;
    private Peaklist peaklistin2;
    private Peaklist peaklistout2;
    private Peaklist peaklistout2d;

    @Before
    public void setUp() {
        // HeLa filtered pd21 ID 10 input
        List<Double> mzi = Arrays.asList(120.08112, 127.05046, 129.10248, 130.08647, 145.06100, 147.11298, 173.05595,
                183.14944, 187.10793, 191.11806, 211.14433, 219.11301, 219.14932, 226.11879, 228.17076, 233.12860,
                245.12456, 246.18129, 272.12418, 273.15988, 311.17133, 311.67285, 318.18118, 361.21945, 365.21802,
                367.71344, 368.21527, 374.20749, 389.25476, 393.24963, 417.24994, 464.28693, 465.29050, 473.27631,
                474.28079, 478.30136, 488.28372, 489.28659, 491.28381, 506.27274, 524.28314, 525.28650, 526.28674,
                563.29309, 563.35559, 564.35809, 581.30481, 582.30756, 621.33667, 622.33978, 631.33752, 636.37140);
        List<Double> ii = Arrays.asList(134798.0, 250757.0, 113242.0, 96282.8, 73135.9, 209426.0, 76674.6, 90304.0,
                89916.3, 61007.0, 55596.0, 128882.0, 54083.8, 33974.4, 21921.7, 59215.3, 21259.0, 158157.0, 23047.4,
                33589.2, 108160.0, 39693.8, 84191.1, 52743.0, 13328.6, 40664.2, 21989.6, 34377.6, 14409.4, 83177.9,
                7503.34, 181362.0, 33635.4, 2454.38, 1675.37, 2984.05, 2407.48, 2389.05, 1778.45, 2974.41, 40190.6,
                12985.8, 1740.92, 2295.79, 60876.1, 15365.0, 31691.5, 11432.0, 16625.5, 4982.01, 1941.02, 2423.14);
        peaklistin = new Peaklist(mzi, ii);

        // HeLa filtered pd21 ID 10 mMass output
        List<Double> mzo1 = Arrays.asList(120.08112, 127.05046, 129.10248, 130.08647, 145.06100, 147.11298, 173.05595,
                183.14944, 187.10793, 191.11806, 211.14433, 219.11301, 219.14932, 226.11879, 228.17076, 233.12860,
                245.12456, 246.18129, 272.12418, 273.15988, 311.17133, 311.67285, 318.18118, 361.21945, 365.21802,
                367.71344, 368.21527, 374.20749, 389.25476, 393.24963, 417.24994, 464.28693, 465.29050, 473.27631,
                474.28079, 478.30136, 488.28372, 489.28659, 491.28381, 506.27274, 524.28314, 525.28650, 526.28674,
                563.29309, 563.35559, 564.35809, 581.30481, 582.30756, 621.33667, 622.33978, 631.33752, 636.37140);
        List<Double> io1 = Arrays.asList(134798.0, 250757.0, 113242.0, 96282.8, 73135.9, 209426.0, 76674.6, 90304.0,
                89916.3, 61007.0, 55596.0, 128882.0, 54083.8, 33974.4, 21921.7, 59215.3, 21259.0, 158157.0, 23047.4,
                33589.2, 108160.0, 39693.8, 84191.1, 52743.0, 13328.6, 40664.2, 21989.6, 34377.6, 14409.4, 83177.9,
                7503.34, 181362.0, 33635.4, 2454.38, 1675.37, 2984.05, 2407.48, 2389.05, 1778.45, 2974.41, 40190.6,
                12985.8, 1740.92, 2295.79, 60876.1, 15365.0, 31691.5, 11432.0, 16625.5, 4982.01, 1941.02, 2423.14);
        peaklistout = new Peaklist(mzo1, io1);

        peaklistout.getPeaklist().get(2).setCharge(1);
        peaklistout.getPeaklist().get(2).setIsotope(0);

        peaklistout.getPeaklist().get(18).setCharge(1);
        peaklistout.getPeaklist().get(18).setIsotope(0);

        peaklistout.getPeaklist().get(20).setCharge(2);
        peaklistout.getPeaklist().get(20).setIsotope(0);

        peaklistout.getPeaklist().get(21).setCharge(2);
        peaklistout.getPeaklist().get(21).setIsotope(1);

        peaklistout.getPeaklist().get(25).setCharge(2);
        peaklistout.getPeaklist().get(25).setIsotope(0);

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

        peaklistout.setPeaklist(Mspy.removeEmptyPeaks(peaklistout.getPeaklist()));

        // HeLa filtered ID 6 input
        // peaklistin2 =
        // Convert.mgfToPeaklist("/srv/lucas1/Downloads/mgffromhelafiltered");
        // // HeLa filtered ID 6 mMass output
        // peaklistout2 = Convert.msdToPeaklist(
        // "/srv/lucas1/Downloads/20161010_04_TP_HeLa_200ng.filtered.10005.10005.3 [6]
        // deisotoped.msd");
        // // HeLa filtered ID 6 mMass deconvolute output
        // peaklistout2d = Convert.msdToPeaklist(
        // "/srv/lucas1/Downloads/20161010_04_TP_HeLa_200ng.filtered.10005.10005.3 [6] -
        // Deconvoluted.msd");

        // Convert.mgfToPeaklist("/srv/lucas1/Downloads/mgffromhelafiltered2");
        // Convert.msdToPeaklist("/srv/lucas1/Downloads/20161010_04_TP_HeLa_200ng.filtered.10013.10013.2
        // [13].msd");
    }

    @Test
    public void testDeisotope() {
        List<Peak> out = Mspy.deisotope(peaklistin.getPeaklist(), 3, 0.05, 0.5, 0.0);
        // List<Peak> out2 = Mspy.deisotope(peaklistin2.getPeaklist(), 3, 0.05, 0.5,
        // 0.0);
        // List<Peak> out3 = Mspy.deisotope(peaklistin3.getPeaklist(), 3, 0.05, 0.5,
        // 0.0);
        //
        // System.out.println("Out output:");
        // int a = 0;
        // int index1 = 0;
        // for (Peak p : out) {
        // if (p.getCharge() != -1) {
        // System.out.println("test| Index: " + index1 + ", MZ: " + p.getMz() + ",
        // INTENSITY:" + p.getIntensity() + ", CHARGE:" + p.getCharge() + ", ISOTOPE:" +
        // p.getIsotope());
        // a++;
        // }
        // index1++;
        // }
        // System.out.println("test| Size: " + out.size() + ", charge != 0: " + a);
        // System.out.println();
        //
        // System.out.println("Their output:");
        // int b = 0;
        // int index2 = 0;
        // for (Peak p : peaklistout.getPeaklist()) {
        // if (p.getCharge() != -1) {
        // System.out.println("test| Index: " + index2 + ", MZ: " + p.getMz() + ",
        // INTENSITY:" + p.getIntensity() + ", CHARGE:" + p.getCharge() + ", ISOTOPE:" +
        // p.getIsotope());
        // b++;
        // }
        // index2++;
        // }
        // System.out.println("test| Size: " + peaklistout.getPeaklist().size() + ",
        // charge != 0: " + b);
        //
        // System.out.println();
        // System.out.println("Correctness check: ");
        // for (int i = 0; i < out.size() || i < peaklistout.getPeaklist().size(); i++)
        // {
        // // if (out.get(i).getCharge() != -1 &&
        // peaklistout.getPeaklist().get(i).getCharge() != -1) {
        // if (out.get(i).getMz() == peaklistout.getPeaklist().get(i).getMz() &&
        // out.get(i).getIntensity() == peaklistout.getPeaklist().get(i).getIntensity()
        // && out.get(i).getCharge() == peaklistout.getPeaklist().get(i).getCharge() &&
        // out.get(i).getIsotope() == peaklistout.getPeaklist().get(i).getIsotope()) {
        // System.out.println("Correct: ");
        // System.out.println("test| Index: " + i + ", MZ: " + out.get(i).getMz() + ",
        // INTENSITY:" + out.get(i).getIntensity() + ", CHARGE:" +
        // out.get(i).getCharge() + ", ISOTOPE:"
        // + out.get(i).getIsotope());
        // System.out.println("test| Index: " + i + ", MZ: " +
        // peaklistout.getPeaklist().get(i).getMz() + ", INTENSITY:" +
        // peaklistout.getPeaklist().get(i).getIntensity() + ", CHARGE:"
        // + peaklistout.getPeaklist().get(i).getCharge() + ", ISOTOPE:" +
        // peaklistout.getPeaklist().get(i).getIsotope());
        // System.out.println();
        // // }
        // } else {
        // // if (out.get(i).getCharge() != -1 &&
        // peaklistout.getPeaklist().get(i).getCharge() != -1) {
        // System.out.println("Incorrect: ");
        // System.out.println("test| Index: " + i + ", MZ: " + out.get(i).getMz() + ",
        // INTENSITY:" + out.get(i).getIntensity() + ", CHARGE:" +
        // out.get(i).getCharge() + ", ISOTOPE:"
        // + out.get(i).getIsotope());
        // System.out.println("test| Index: " + i + ", MZ: " +
        // peaklistout.getPeaklist().get(i).getMz() + ", INTENSITY:" +
        // peaklistout.getPeaklist().get(i).getIntensity() + ", CHARGE:"
        // + peaklistout.getPeaklist().get(i).getCharge() + ", ISOTOPE:" +
        // peaklistout.getPeaklist().get(i).getIsotope());
        // System.out.println();
        // // }
        // }
        // }

        assertPeaklistEquals(out, peaklistout.getPeaklist()); // Test 1

        // System.out.println("Out output:");
        // int a = 0;
        // int index1 = 0;
        // for (Peak p : out2) {
        // if (p.getCharge() != -1) {
        // System.out.println("test| Index: " + index1 + ", MZ: " + p.getMz() + ",
        // INTENSITY:" + p.getIntensity() + ", CHARGE:" + p.getCharge() + ", ISOTOPE:" +
        // p.getIsotope());
        // a++;
        // }
        // index1++;
        // }
        // System.out.println("test| Size: " + out2.size() + ", charge != 0: " + a);
        // System.out.println();
        //
        // System.out.println("Their output:");
        // int b = 0;
        // int index2 = 0;
        // for (Peak p : peaklistout2.getPeaklist()) {
        // if (p.getCharge() != -1) {
        // System.out.println("test| Index: " + index2 + ", MZ: " + p.getMz() + ",
        // INTENSITY:" + p.getIntensity() + ", CHARGE:" + p.getCharge() + ", ISOTOPE:" +
        // p.getIsotope());
        // b++;
        // }
        // index2++;
        // }
        // System.out.println("test| Size: " + peaklistout2.getPeaklist().size() + ",
        // charge != 0: " + b);
        //
        // System.out.println();
        // System.out.println("Correctness check: ");
        // for (int i = 0; i < out2.size() || i < peaklistout2.getPeaklist().size();
        // i++) {
        // if (out2.get(i).getMz() == peaklistout2.getPeaklist().get(i).getMz() &&
        // out2.get(i).getIntensity() ==
        // peaklistout2.getPeaklist().get(i).getIntensity()
        // && out2.get(i).getCharge() == peaklistout2.getPeaklist().get(i).getCharge()
        // && out2.get(i).getIsotope() ==
        // peaklistout2.getPeaklist().get(i).getIsotope()) {
        // System.out.println("Correct: ");
        // System.out.println("test| Index: " + i + ", MZ: " + out2.get(i).getMz() + ",
        // INTENSITY:" + out2.get(i).getIntensity() + ", CHARGE:" +
        // out2.get(i).getCharge() + ", ISOTOPE:"
        // + out2.get(i).getIsotope());
        // System.out.println("test| Index: " + i + ", MZ: " +
        // peaklistout2.getPeaklist().get(i).getMz() + ", INTENSITY:" +
        // peaklistout2.getPeaklist().get(i).getIntensity() + ", CHARGE:"
        // + peaklistout2.getPeaklist().get(i).getCharge() + ", ISOTOPE:" +
        // peaklistout2.getPeaklist().get(i).getIsotope());
        // System.out.println();
        // } else {
        // System.out.println("Incorrect: ");
        // System.out.println("test| Index: " + i + ", MZ: " + out2.get(i).getMz() + ",
        // INTENSITY:" + out2.get(i).getIntensity() + ", CHARGE:" +
        // out2.get(i).getCharge() + ", ISOTOPE:"
        // + out2.get(i).getIsotope());
        // System.out.println("test| Index: " + i + ", MZ: " +
        // peaklistout2.getPeaklist().get(i).getMz() + ", INTENSITY:" +
        // peaklistout2.getPeaklist().get(i).getIntensity() + ", CHARGE:"
        // + peaklistout2.getPeaklist().get(i).getCharge() + ", ISOTOPE:" +
        // peaklistout2.getPeaklist().get(i).getIsotope());
        // System.out.println();
        // }
        // }
        //
        // assertPeaklistEquals(out2, peaklistout2.getPeaklist()); // Test 2
        //
        // assertPeaklistEquals(out3, peaklistout3.getPeaklist()); // Test 3
    }

    @Test
    @org.junit.Ignore
    public void testDeconvolute() {
        List<Peak> deisotoped = Mspy.deisotope(peaklistin2.getPeaklist(), 3, 0.05, 0.5, 0.0);

        assertPeaklistEquals(deisotoped, peaklistout2.getPeaklist());

        List<Peak> deconvoluted = Mspy.deconvolute(deisotoped, 0);

        // System.out.println("Size of deconvoluted: " + deconvoluted.size() + ", Size
        // of peaklistout2d: " + peaklistout2d.getPeaklist().size());
        // System.out.println();
        // System.out.println("Correctness check: ");
        // for (int i = 0; i < deconvoluted.size() || i <
        // peaklistout2d.getPeaklist().size(); i++) {
        // if (deconvoluted.get(i).getMz() == peaklistout2d.getPeaklist().get(i).getMz()
        // && deconvoluted.get(i).getIntensity() ==
        // peaklistout2d.getPeaklist().get(i).getIntensity()
        // && deconvoluted.get(i).getCharge() ==
        // peaklistout2d.getPeaklist().get(i).getCharge() &&
        // deconvoluted.get(i).getIsotope() ==
        // peaklistout2d.getPeaklist().get(i).getIsotope()) {
        // System.out.println("Correct: ");
        // System.out.println("test| Index: " + i + ", MZ: " +
        // deconvoluted.get(i).getMz() + ", INTENSITY:" +
        // deconvoluted.get(i).getIntensity() + ", CHARGE:" +
        // deconvoluted.get(i).getCharge()
        // + ", ISOTOPE:" + deconvoluted.get(i).getIsotope());
        // System.out.println("test| Index: " + i + ", MZ: " +
        // peaklistout2d.getPeaklist().get(i).getMz() + ", INTENSITY:" +
        // peaklistout2d.getPeaklist().get(i).getIntensity() + ", CHARGE:"
        // + peaklistout2d.getPeaklist().get(i).getCharge() + ", ISOTOPE:" +
        // peaklistout2d.getPeaklist().get(i).getIsotope());
        // System.out.println();
        // } else {
        // System.out.println("Incorrect: ");
        // System.out.println("test| Index: " + i + ", MZ: " +
        // deconvoluted.get(i).getMz() + ", INTENSITY:" +
        // deconvoluted.get(i).getIntensity() + ", CHARGE:" +
        // deconvoluted.get(i).getCharge()
        // + ", ISOTOPE:" + deconvoluted.get(i).getIsotope());
        // System.out.println("test| Index: " + i + ", MZ: " +
        // peaklistout2d.getPeaklist().get(i).getMz() + ", INTENSITY:" +
        // peaklistout2d.getPeaklist().get(i).getIntensity() + ", CHARGE:"
        // + peaklistout2d.getPeaklist().get(i).getCharge() + ", ISOTOPE:" +
        // peaklistout2d.getPeaklist().get(i).getIsotope());
        // System.out.println();
        // }
        // }

        assertPeaklistEquals(deconvoluted, peaklistout2d.getPeaklist()); // ROUNDING PROBOLEMS!
    }

    private void assertPeaklistEquals(List<Peak> list, List<Peak> list2) {
        for (int i = 0; i < list.size() || i < list2.size(); i++) {
            assertEquals("Mz-values should be same! " + list.get(i).getMz() + " - " + list2.get(i).getMz(),
                    list.get(i).getMz(), list2.get(i).getMz(), 0.001);
            assertEquals("Intensity-values should be same! " + list.get(i).getIntensity() + " - "
                    + list2.get(i).getIntensity(), list.get(i).getIntensity(), list2.get(i).getIntensity(), 0);
            assertEquals(
                    "Isotope-values should be same! " + list.get(i).getIsotope() + " - " + list2.get(i).getIsotope(),
                    list.get(i).getIsotope(), list2.get(i).getIsotope(), 0);
            assertEquals("Charge-values should be same! " + list.get(i).getCharge() + " - " + list2.get(i).getCharge(),
                    list.get(i).getCharge(), list2.get(i).getCharge());
        }
    }
}