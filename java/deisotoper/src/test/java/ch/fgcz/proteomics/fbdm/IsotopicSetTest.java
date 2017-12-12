package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-10-26
 */
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ch.fgcz.proteomics.dto.MassSpectrum;

public class IsotopicSetTest {
    @Test
    public void testClusterCreation() {
        Configuration config = new Configuration();

        double startMass = 100.;
        List<Double> mz = Arrays.asList(startMass, startMass + config.getIsotopicPeakDistance(),
                startMass + config.getIsotopicPeakDistance() * 1.5, startMass + config.getIsotopicPeakDistance() * 2);
        List<Double> intensity = Arrays.asList(1., 1., 1., 1.);
        MassSpectrum massSpectrum = new MassSpectrum(mz, intensity);
        List<Peak> isotpicSet = Arrays.asList(new Peak(mz.get(0), 1., 1), new Peak(mz.get(1), 1., 1),
                new Peak(mz.get(2), 1., 1), new Peak(mz.get(3), 1., 1));

        IsotopicSet isotopicSet = new IsotopicSet(new PeakList(massSpectrum), isotpicSet, 1, config);
        assertEquals(6, isotopicSet.getIsotopicSet().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCreation_MustFail11() {
        Configuration config = new Configuration();
        double startMass = 100.;
        List<Double> mz = Arrays.asList(startMass, startMass + config.getIsotopicPeakDistance(),
                startMass + config.getIsotopicPeakDistance() * 2, startMass + config.getIsotopicPeakDistance() * 3.5);
        List<Double> intensity = Arrays.asList(1., 1., 1., 1.);
        MassSpectrum massSpectrum = new MassSpectrum(mz, intensity);
        PeakList peakList = new PeakList(massSpectrum);

        new IsotopicSet(peakList, peakList.getPeakList(), 1, config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCreation_MustFail12() {
        // second scenario
        Configuration config = new Configuration();
        double startMass = 100.;
        List<Double> mz = Arrays.asList(startMass, startMass + config.getIsotopicPeakDistance(),
                startMass + config.getIsotopicPeakDistance() * 2, startMass + config.getIsotopicPeakDistance() * 2.25);
        List<Double> intensity = Arrays.asList(1., 1., 1., 1.);
        MassSpectrum massSpectrum = new MassSpectrum(mz, intensity);
        PeakList peakList = new PeakList(massSpectrum);

        new IsotopicSet(peakList, peakList.getPeakList(), 1, config);
    }

    @Test
    public void testRemoveDoubleClusterLeaveTripleCluster() {
        Configuration config = new Configuration();
        PeakList peakList = new PeakList();
        peakList.add(new Peak(1, 100, 0));
        peakList.add(new Peak(2, 200, 1));
        peakList.add(new Peak(3, 300, 2));

        List<IsotopicCluster> inputSet = new ArrayList<IsotopicCluster>();
        inputSet.add(new IsotopicCluster(Arrays.asList(peakList.get(0), peakList.get(1), peakList.get(2)), 1, peakList,
                config.getIsotopicPeakDistance(), config.getDelta()));
        inputSet.add(new IsotopicCluster(Arrays.asList(peakList.get(0), peakList.get(1)), 1, peakList,
                config.getIsotopicPeakDistance(), config.getDelta()));
        inputSet.add(new IsotopicCluster(Arrays.asList(peakList.get(1), peakList.get(2)), 1, peakList,
                config.getIsotopicPeakDistance(), config.getDelta()));

        List<IsotopicCluster> resultSet = IsotopicSet.removeDoubleClusterLeaveTripleCluster(inputSet);

        assertEquals(1, resultSet.size());
        assertEquals(new IsotopicCluster(Arrays.asList(peakList.get(0), peakList.get(1), peakList.get(2)), 1, peakList,
                config.getIsotopicPeakDistance(), config.getDelta()).toString(), resultSet.get(0).toString());
    }

    @Test
    public void testRemoveDoubleClusterLeaveTripleClusterTwo() {
        Configuration config = new Configuration();
        PeakList peakList = new PeakList();
        peakList.add(new Peak(1, 100, 0));
        peakList.add(new Peak(2, 200, 1));
        peakList.add(new Peak(4, 300, 2));
        peakList.add(new Peak(5, 400, 3));

        List<IsotopicCluster> inputSet = new ArrayList<IsotopicCluster>();
        inputSet.add(new IsotopicCluster(Arrays.asList(peakList.get(0), peakList.get(1)), 1, peakList,
                config.getIsotopicPeakDistance(), config.getDelta()));
        inputSet.add(new IsotopicCluster(Arrays.asList(peakList.get(2), peakList.get(3)), 1, peakList,
                config.getIsotopicPeakDistance(), config.getDelta()));

        List<IsotopicCluster> resultSet = IsotopicSet.removeDoubleClusterLeaveTripleCluster(inputSet);

        assertEquals(2, resultSet.size());
        assertEquals(new IsotopicCluster(Arrays.asList(peakList.get(0), peakList.get(1)), 1, peakList,
                config.getIsotopicPeakDistance(), config.getDelta()).toString(), resultSet.get(0).toString());
        assertEquals(new IsotopicCluster(Arrays.asList(peakList.get(2), peakList.get(3)), 1, peakList,
                config.getIsotopicPeakDistance(), config.getDelta()).toString(), resultSet.get(1).toString());
    }

    @Test
    public void testRemoveDoubleClusterLeaveTripleClusterThree() {
        Configuration config = new Configuration();
        PeakList peakList = new PeakList();
        peakList.add(new Peak(1, 100, 0));
        peakList.add(new Peak(2, 200, 1));
        peakList.add(new Peak(3, 300, 2));
        peakList.add(new Peak(5, 400, 3));
        peakList.add(new Peak(6, 500, 4));
        peakList.add(new Peak(7, 600, 5));

        List<IsotopicCluster> inputSet = new ArrayList<IsotopicCluster>();
        inputSet.add(new IsotopicCluster(Arrays.asList(peakList.get(0), peakList.get(1), peakList.get(2)), 1, peakList,
                config.getIsotopicPeakDistance(), config.getDelta()));
        inputSet.add(new IsotopicCluster(Arrays.asList(peakList.get(0), peakList.get(1)), 1, peakList,
                config.getIsotopicPeakDistance(), config.getDelta()));
        inputSet.add(new IsotopicCluster(Arrays.asList(peakList.get(1), peakList.get(2)), 1, peakList,
                config.getIsotopicPeakDistance(), config.getDelta()));

        inputSet.add(new IsotopicCluster(Arrays.asList(peakList.get(3), peakList.get(4), peakList.get(5)), 1, peakList,
                config.getIsotopicPeakDistance(), config.getDelta()));
        inputSet.add(new IsotopicCluster(Arrays.asList(peakList.get(3), peakList.get(4)), 1, peakList,
                config.getIsotopicPeakDistance(), config.getDelta()));
        inputSet.add(new IsotopicCluster(Arrays.asList(peakList.get(4), peakList.get(5)), 1, peakList,
                config.getIsotopicPeakDistance(), config.getDelta()));

        List<IsotopicCluster> resultSet = IsotopicSet.removeDoubleClusterLeaveTripleCluster(inputSet);

        assertEquals(2, resultSet.size());
        assertEquals(new IsotopicCluster(Arrays.asList(peakList.get(0), peakList.get(1), peakList.get(2)), 1, peakList,
                config.getIsotopicPeakDistance(), config.getDelta()).toString(), resultSet.get(0).toString());
        assertEquals(new IsotopicCluster(Arrays.asList(peakList.get(3), peakList.get(4), peakList.get(5)), 1, peakList,
                config.getIsotopicPeakDistance(), config.getDelta()).toString(), resultSet.get(1).toString());
    }
}