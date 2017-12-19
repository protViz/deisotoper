package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-11-23
 */
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ch.fgcz.proteomics.dto.MassSpecMeasure;
import ch.fgcz.proteomics.dto.MassSpectrum;

public class DeisotoperTest {
    @Test
    public void generateIsotopicSets_two_clusters_Interleaved() {
        Configuration config = new Configuration();

        double oneSetStart = 100;
        double secondSetStart = 100.1;

        List<Double> mz = Arrays.asList(oneSetStart, secondSetStart, oneSetStart + config.getIsotopicPeakDistance(),
                secondSetStart + config.getIsotopicPeakDistance(), oneSetStart + config.getIsotopicPeakDistance() * 2,
                secondSetStart + config.getIsotopicPeakDistance() * 2);
        List<Double> intensity = Arrays.asList(4.0, 4.0, 5.0, 6.0, 6.0, 7.0);
        MassSpectrum massSpectrum = new MassSpectrum(mz, intensity);

        List<IsotopicSet> isotopicSets = Deisotoper.generateIsotopicSets(new PeakList(massSpectrum), config);
        assertEquals(2, isotopicSets.size());
        assertEquals(3, isotopicSets.get(0).getPeaksInSet().size());
        assertEquals(3, isotopicSets.get(1).getPeaksInSet().size());
    }

    @Test
    public void generateIsotopicSets_one_cluster_interrupted() {
        Configuration config = new Configuration();

        double oneSetStart = 100;
        double secondSetStart = 100.1;

        List<Double> mz2 = Arrays.asList(oneSetStart, secondSetStart, oneSetStart + config.getIsotopicPeakDistance(),
                oneSetStart + config.getIsotopicPeakDistance() * 2);
        List<Double> intensity2 = Arrays.asList(4.0, 4.0, 5.0, 6.0);
        MassSpectrum massSpectrum2 = new MassSpectrum(mz2, intensity2);

        List<IsotopicSet> isotopicSets = Deisotoper.generateIsotopicSets(new PeakList(massSpectrum2), config);
        assertEquals(1, isotopicSets.size());
        assertEquals(3, isotopicSets.get(0).getPeaksInSet().size());
    }

    @Test
    public void generateIsotopicSets_single_isotopic_set_with_two_charges_case1() {
        Configuration config = new Configuration();

        double oneSetStart = 100;

        List<Double> mz3 = Arrays.asList(oneSetStart, oneSetStart + config.getIsotopicPeakDistance() / 2.,
                oneSetStart + config.getIsotopicPeakDistance(), oneSetStart + config.getIsotopicPeakDistance() * 2.,
                oneSetStart + config.getIsotopicPeakDistance() * 3.);
        List<Double> intensity3 = Arrays.asList(4.0, 4.0, 5.0, 6.0, 6.0);
        MassSpectrum massSpectrum3 = new MassSpectrum(mz3, intensity3);

        List<IsotopicSet> isotopicSets = Deisotoper.generateIsotopicSets(new PeakList(massSpectrum3), config);
        assertEquals(1, isotopicSets.size());
        assertEquals(5, isotopicSets.get(0).getPeaksInSet().size());
    }

    @Test
    public void generateIsotopicSets_single_isotopic_set_with_two_charges_case2() {
        Configuration config = new Configuration();

        double oneSetStart = 100;

        List<Double> mz4 = Arrays.asList(oneSetStart, oneSetStart + config.getIsotopicPeakDistance(),
                oneSetStart + 2. * config.getIsotopicPeakDistance(),
                oneSetStart + config.getIsotopicPeakDistance() * 2.5,
                oneSetStart + config.getIsotopicPeakDistance() * 3); // 100.0, 101.0, 102.0, 102.5, 103.0
        List<Double> intensity4 = Arrays.asList(4.0, 4.0, 5.0, 6.0, 6.0);
        MassSpectrum massSpectrum4 = new MassSpectrum(mz4, intensity4);

        List<IsotopicSet> isotopicSets = Deisotoper.generateIsotopicSets(new PeakList(massSpectrum4), config);

        assertEquals(1, isotopicSets.size());
        assertEquals(5, isotopicSets.get(0).getPeaksInSet().size());
    }

    @Test
    public void generateIsotopicSets_two_non_overlappling_isotopicsets() {
        Configuration config = new Configuration();

        double oneSetStart = 100;
        double secondStart = 1000;

        List<Double> mz4 = Arrays.asList(oneSetStart, oneSetStart + config.getIsotopicPeakDistance(),
                oneSetStart + 2. * config.getIsotopicPeakDistance(), secondStart,
                secondStart + config.getIsotopicPeakDistance(), secondStart + config.getIsotopicPeakDistance() * 2); // 100.0,
                                                                                                                     // 101.0,
                                                                                                                     // 103.0
        List<Double> intensity4 = Arrays.asList(4.0, 4.0, 5.0, 6.0, 6.0, 7.0);
        MassSpectrum massSpectrum4 = new MassSpectrum(mz4, intensity4);

        List<IsotopicSet> isotopicSets = Deisotoper.generateIsotopicSets(new PeakList(massSpectrum4), config);
        assertEquals(2, isotopicSets.size());
        assertEquals(3, isotopicSets.get(0).getPeaksInSet().size());
        assertEquals(3, isotopicSets.get(1).getPeaksInSet().size());
    }

    @Test
    public void testDeisotopeMSM() {
        String source = "Unit Test Case";

        double[] mz = { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };
        double[] intensity = { 4.0, 4.0, 5.0, 6.0, 6.0, 7.0, 7.0, 7.0, 8.0, 8.0 };
        double peptidMass = 309.22;
        int chargeState = 2;
        int id = 123;

        double[] mz2 = { 2.0, 4.0, 6.0, 8.0, 10.0, 12.0, 14.0, 16.0, 18.0, 20.0 };
        double[] intensity2 = { 65.0, 44.0, 23.0, 88.0, 666.0, 451.0, 44.0, 22.0, 111.0, 1000.0 };
        double peptidMass2 = 203.23;
        int chargeState2 = 2;
        int id2 = 124;

        MassSpecMeasure massSpectrometryMeasurementIn = new MassSpecMeasure(source);
        massSpectrometryMeasurementIn.addMS(mz, intensity, peptidMass, chargeState, id);
        massSpectrometryMeasurementIn.addMS(mz2, intensity2, peptidMass2, chargeState2, id2);

        Configuration config = new Configuration();

        DeisotoperMassSpectrumAdapter deisotoper = new DeisotoperMassSpectrumAdapter(new Deisotoper());

        MassSpecMeasure massSpectrometryMeasurementOut = deisotoper.deisotopeMSM(massSpectrometryMeasurementIn, config);

        assertEquals("Source must be equal!", massSpectrometryMeasurementIn.getSource(),
                massSpectrometryMeasurementOut.getSource());

        assertEquals("Size of massSpectrumList must be equal!", massSpectrometryMeasurementIn.getMSlist().size(),
                massSpectrometryMeasurementOut.getMSlist().size());

        assertNotEquals("Size of mZ-Values must be not equal, becauase there should be isotopic sets!",
                massSpectrometryMeasurementIn.getMSlist().get(0).getMz().size(),
                massSpectrometryMeasurementOut.getMSlist().get(0).getMz().size());

        assertEquals("Size of mZ-Values must be equal, because there should not be isotopic sets!",
                massSpectrometryMeasurementIn.getMSlist().get(1).getMz().size(),
                massSpectrometryMeasurementOut.getMSlist().get(1).getMz().size());
    }

    @Test
    public void testDeisotopeMsOne() {
        List<Double> mz = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0);
        List<Double> intensity = Arrays.asList(4.0, 4.0, 5.0, 6.0, 6.0, 7.0, 7.0, 7.0, 8.0, 8.0);
        double peptidMass = 309.22;
        int chargeState = 2;
        int id = 123;

        MassSpectrum massSpectrum = new MassSpectrum(mz, intensity, peptidMass, chargeState, id);

        Configuration config = new Configuration();

        DeisotoperMassSpectrumAdapter deisotoper = new DeisotoperMassSpectrumAdapter(new Deisotoper(config));

        MassSpectrum massSpectrumOut = deisotoper.deisotopeMS(massSpectrum);

        assertNotEquals("Size of mZ-Values must be not equal, becuase there should be isotopic sets!",
                massSpectrum.getMz().size(), massSpectrumOut.getMz().size());

        assertEquals(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0), massSpectrumOut.getMz());

        assertEquals(Arrays.asList(4.0, 4.0, 5.0, 6.0, 6.0, 7.0, 7.0, 23.0), massSpectrumOut.getIntensity());

        assertEquals(Arrays.asList(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, -1.0), massSpectrumOut.getIsotope());

        assertEquals(Arrays.asList(1, 1, 1, 1, 1, 1, 1, -1), massSpectrumOut.getCharge());
    }

    @Test
    public void testDeisotopeMsTwo() {
        List<Double> mz = Arrays.asList(101.0, 102.0, 103.0, 104.0, 106.0, 107.0, 107.5, 108.0, 109.0, 115.0);
        List<Double> intensity = Arrays.asList(4.0, 4.0, 5.0, 6.0, 6.0, 7.0, 7.0, 7.0, 8.0, 8.0);
        double peptidMass = 309.22;
        int chargeState = 2;
        int id = 123;

        MassSpectrum massSpectrum = new MassSpectrum(mz, intensity, peptidMass, chargeState, id);

        Configuration config = new Configuration();

        DeisotoperMassSpectrumAdapter deisotoper = new DeisotoperMassSpectrumAdapter(new Deisotoper(config));

        MassSpectrum massSpectrumOut = deisotoper.deisotopeMS(massSpectrum);

        assertNotEquals("Size of mZ-Values must be not equal, becuase there should be isotopic sets!",
                massSpectrum.getMz().size(), massSpectrumOut.getMz().size());

        assertEquals(Arrays.asList(101.0, 102.0, 106.0, 107.5, 115.0), massSpectrumOut.getMz());

        assertEquals(Arrays.asList(4.0, 15.0, 20.0, 7.0, 8.0), massSpectrumOut.getIntensity());

        assertEquals(Arrays.asList(1.0, -1.0, -1.0, 1.0, -1.0), massSpectrumOut.getIsotope());

        assertEquals(Arrays.asList(1, -1, -1, 2, -1), massSpectrumOut.getCharge());
    }
}
