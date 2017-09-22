package ch.fgcz.proteomics.fdbm;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class IsotopicTest {
    Peaklist peaklist;
    Peaklist peaklistr;
    List<Peak> A;
    List<Peak> B;
    List<Peak> C;
    List<Peak> D;
    List<Peak> E;
    List<Peak> F;

    @Before
    public void setUp() {
        // Example
        List<Double> mz = Arrays.asList(0.2, 1.0, 2.0, 2.5, 3.0, 10.0);
        List<Double> intensity = Arrays.asList(0.5, 2.0, 1.0, 1.0, 1.0, 3.0);
        peaklist = new Peaklist(mz, intensity);

        // Example result
        List<Double> mzr = Arrays.asList(1.0, 2.0, 2.5, 3.0);
        List<Double> intensityr = Arrays.asList(2.0, 1.0, 1.0, 1.0);
        peaklistr = new Peaklist(mzr, intensityr);

        // Possible Cluster for the example
        A = Arrays.asList(new Peak(1.0, 2.0), new Peak(2.0, 1.0), new Peak(3.0, 1.0));
        B = Arrays.asList(new Peak(1.0, 2.0), new Peak(2.0, 1.0));
        C = Arrays.asList(new Peak(2.0, 1.0), new Peak(2.5, 1.0), new Peak(3.0, 1.0));
        D = Arrays.asList(new Peak(2.0, 1.0), new Peak(2.5, 1.0));
        E = Arrays.asList(new Peak(2.0, 1.0), new Peak(3.0, 1.0));
        F = Arrays.asList(new Peak(2.5, 1.0), new Peak(3.0, 1.0));
    }

    @Test
    public void testIsotopeSet() {
        IsotopicMassSpectrum ims = new IsotopicMassSpectrum(peaklist, 0.01);

        assertEquals("Created IsotopicMassSpectrum should have one IsotopicSet", ims.getIsotopicMassSpectrum().size(), 1);
        assertEquals("Created IsotopicSet should have six IsotopicCluster", ims.getIsotopicMassSpectrum().get(0).getIsotopicSet().size(), 6);

        for (IsotopicSet i : ims.getIsotopicMassSpectrum()) {
            assertPeaklistEquals(A, i.getIsotopicSet().get(0).getIsotopicCluster());
            assertPeaklistEquals(B, i.getIsotopicSet().get(1).getIsotopicCluster());
            assertPeaklistEquals(C, i.getIsotopicSet().get(2).getIsotopicCluster());
            assertPeaklistEquals(D, i.getIsotopicSet().get(3).getIsotopicCluster());
            assertPeaklistEquals(E, i.getIsotopicSet().get(4).getIsotopicCluster());
            assertPeaklistEquals(F, i.getIsotopicSet().get(5).getIsotopicCluster());
        }
    }

    @Test
    public void testIsotopeCluster() {
        IsotopicMassSpectrum ims = new IsotopicMassSpectrum(peaklist, 0.01);
        for (IsotopicSet i : ims.getIsotopicMassSpectrum()) {
            assertPeaklistEquals(A, i.getIsotopicSet().get(0).getIsotopicCluster());
            assertPeaklistEquals(B, i.getIsotopicSet().get(1).getIsotopicCluster());
            assertPeaklistEquals(C, i.getIsotopicSet().get(2).getIsotopicCluster());
            assertPeaklistEquals(D, i.getIsotopicSet().get(3).getIsotopicCluster());
            assertPeaklistEquals(E, i.getIsotopicSet().get(4).getIsotopicCluster());
            assertPeaklistEquals(F, i.getIsotopicSet().get(5).getIsotopicCluster());
        }
    }

    private void assertPeaklistEquals(List<Peak> list, List<Peak> list2) {
        for (int i = 0; i < list.size() || i < list2.size(); i++) {
            assertEquals("Mz-values should be same! " + list.get(i).getMz() + " - " + list2.get(i).getMz(), list.get(i).getMz(), list2.get(i).getMz(), 0.001);
            assertEquals("Intensity-values should be same! " + list.get(i).getIntensity() + " - " + list2.get(i).getIntensity(), list.get(i).getIntensity(), list2.get(i).getIntensity(), 0);
        }
    }
}