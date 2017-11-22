package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-10-26
 */

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.fgcz.proteomics.dto.MassSpectrum;

public class IsotopicTest {
    private Peaklist peaklist;
    private List<Peak> A;
    private List<Peak> B;
    private List<Peak> C;
    private List<Peak> D;
    private List<Peak> E;
    private List<Peak> F;

    @Before
    public void setUp() {
	// Example
	List<Double> mz = Arrays.asList(0.2, 1.001, 2.002, 2.503, 3.003, 10.0);
	List<Double> intensity = Arrays.asList(0.5, 2.0, 1.0, 1.0, 1.0, 3.0);
	peaklist = new Peaklist(mz, intensity);

	// Possible Cluster for the example
	A = Arrays.asList(new Peak(1.001, 2.0, 0), new Peak(2.002, 1.0, 0));
	B = Arrays.asList(new Peak(1.001, 2.0, 0), new Peak(2.002, 1.0, 0), new Peak(3.003, 1.0, 0));
	C = Arrays.asList(new Peak(2.002, 1.0, 0), new Peak(2.503, 1.0, 0));
	D = Arrays.asList(new Peak(2.002, 1.0, 0), new Peak(2.503, 1.0, 0), new Peak(3.003, 1.0, 0));
	E = Arrays.asList(new Peak(2.002, 1.0, 0), new Peak(3.003, 1.0, 0));
	F = Arrays.asList(new Peak(2.503, 1.0, 0), new Peak(3.003, 1.0, 0));
    }

    @Test
    public void testIsotopeSet() {
	MassSpectrum massspectrum = new MassSpectrum();
	massspectrum.setMz(peaklist.getMz());
	massspectrum.setIntensity(peaklist.getIntensity());
	IsotopicMassSpectrum ims = new IsotopicMassSpectrum(massspectrum, peaklist, 0.01, new Configuration(),
		new Deisotoper(), "first");

	assertEquals("Created IsotopicMassSpectrum should have one IsotopicSet", ims.getIsotopicMassSpectrum().size(),
		1);
	assertEquals("Created IsotopicSet should have seven IsotopicCluster",
		ims.getIsotopicMassSpectrum().get(0).getIsotopicSet().size(), 7);

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
	MassSpectrum massspectrum = new MassSpectrum();
	massspectrum.setMz(peaklist.getMz());
	massspectrum.setIntensity(peaklist.getIntensity());
	IsotopicMassSpectrum ims = new IsotopicMassSpectrum(massspectrum, 0.01, new Configuration(), new Deisotoper(),
		"first");
	for (IsotopicSet i : ims.getIsotopicMassSpectrum()) {
	    System.out.println(i.getIsotopicSet().get(0).getIsotopicCluster().toString());
	    System.out.println(i.getIsotopicSet().get(1).getIsotopicCluster().toString());
	    System.out.println(i.getIsotopicSet().get(2).getIsotopicCluster().toString());
	    System.out.println(i.getIsotopicSet().get(3).getIsotopicCluster().toString());
	    System.out.println(i.getIsotopicSet().get(4).getIsotopicCluster().toString());
	    System.out.println(i.getIsotopicSet().get(5).getIsotopicCluster().toString());
	    assertPeaklistEquals(A, i.getIsotopicSet().get(0).getIsotopicCluster());
	    assertPeaklistEquals(B, i.getIsotopicSet().get(1).getIsotopicCluster());
	    assertPeaklistEquals(C, i.getIsotopicSet().get(2).getIsotopicCluster());
	    assertPeaklistEquals(D, i.getIsotopicSet().get(3).getIsotopicCluster());
	    assertPeaklistEquals(E, i.getIsotopicSet().get(4).getIsotopicCluster());
	    assertPeaklistEquals(F, i.getIsotopicSet().get(5).getIsotopicCluster());
	}
    }

    private void assertPeaklistEquals(List<Peak> list, List<Peak> list2) {
	assertEquals(list.size(), list2.size());

	for (int i = 0; i < list.size() && i < list2.size(); i++) {
	    assertEquals("Mz-values should be same! " + list.get(i).getMz() + " - " + list2.get(i).getMz(),
		    list.get(i).getMz(), list2.get(i).getMz(), 0.001);
	    assertEquals("Intensity-values should be same! " + list.get(i).getIntensity() + " - "
		    + list2.get(i).getIntensity(), list.get(i).getIntensity(), list2.get(i).getIntensity(), 0);
	}
    }
}
