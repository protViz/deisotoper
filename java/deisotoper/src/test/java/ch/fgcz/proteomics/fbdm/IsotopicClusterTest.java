package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-11-23
 */
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class IsotopicClusterTest {
    private static final double MIN = Double.MIN_VALUE;

    @Test
    public void aggregation() throws Exception {
        List<Peak> peaks = new ArrayList<Peak>();
        peaks.add(new Peak(1.0, 50.0, 0));
        peaks.add(new Peak(2.0, 5.0, 1));
        Configuration config = new Configuration();
        PeakList peakList = new PeakList(peaks);

        IsotopicCluster isotopicCluster = new IsotopicCluster(peaks, 1, peakList, config.getIsotopicPeakDistance(),
                config.getDelta());

        Peak isotopicClusterAggregated = isotopicCluster.aggregation("first");

        assertEquals(1.0, isotopicClusterAggregated.getMz(), MIN);
        assertEquals(55.0, isotopicClusterAggregated.getIntensity(), MIN);

    }

    // Do we allow that peaks have the same peak ID?
    public void isotopicClusterCreationSamePeakIDs() {
        List<Peak> peaks = new ArrayList<Peak>();
        peaks.add(new Peak(1.0, 5.0, 0));
        peaks.add(new Peak(2.0, 50.0, 0));
        peaks.add(new Peak(3.0, 5.0, 2));
        PeakList peakList = new PeakList(peaks);
        Configuration config = new Configuration();
        new IsotopicCluster(peaks, 1, peakList, config.getIsotopicPeakDistance(), config.getDelta());
    }

    // Do we allow for unsorted clusters? This should pass.
    public void aggregationUnsorted() throws Exception {
        List<Peak> peaks = new ArrayList<Peak>();
        peaks.add(new Peak(3.0, 5.0, 1));
        peaks.add(new Peak(1.0, 50.0, 0));
        peaks.add(new Peak(2.0, 5.0, 2));
        PeakList peaklist = new PeakList(peaks);
        Configuration config = new Configuration();
        new IsotopicCluster(peaks, 1, peaklist, config.getIsotopicPeakDistance(), config.getDelta());
    }

    @Test
    public void sumIntensity() throws Exception {
        Configuration config = new Configuration();
        Peak peak1 = new Peak(1.0, 5.0, 0);
        Peak peak2 = new Peak(2.0, 5.0, 1);
        Peak peak3 = new Peak(3.0, 5.0, 2);

        List<Peak> peaks1 = new ArrayList<Peak>();
        peaks1.add(peak1);
        peaks1.add(peak2);
        peaks1.add(peak3);
        PeakList peaklist1 = new PeakList(peaks1);

        IsotopicCluster isotopicCluster1 = new IsotopicCluster(peaks1, 1, peaklist1, config.getIsotopicPeakDistance(),
                config.getDelta());

        assertEquals(15.0, isotopicCluster1.sumIntensity(), MIN);
    }

    @Test
    public void hasSamePeaks() throws Exception {
        Configuration config = new Configuration();
        Peak peak1 = new Peak(1.0, 5.0, 0);
        Peak peak2 = new Peak(2.0, 5.0, 1);
        Peak peak3 = new Peak(3.0, 5.0, 2);
        Peak peak4 = new Peak(4.0, 5.0, 4);
        Peak peak5 = new Peak(5.0, 5.0, 5);

        List<Peak> peaks1 = new ArrayList<Peak>();
        peaks1.add(peak1);
        peaks1.add(peak2);
        peaks1.add(peak3);
        PeakList peaklist1 = new PeakList(peaks1);

        List<Peak> peaks2 = new ArrayList<Peak>();
        peaks2.add(peak3);
        peaks2.add(peak4);
        peaks2.add(peak5);
        PeakList peaklist2 = new PeakList(peaks2);

        IsotopicCluster isotopicCluster1 = new IsotopicCluster(peaks1, 1, peaklist1, config.getIsotopicPeakDistance(),
                config.getDelta());

        IsotopicCluster isotopicCluster2 = new IsotopicCluster(peaks2, 1, peaklist2, config.getIsotopicPeakDistance(),
                config.getDelta());

        assertEquals(true, isotopicCluster1.hasSamePeaks(isotopicCluster1));
        assertEquals(true, isotopicCluster2.hasSamePeaks(isotopicCluster2));
        assertEquals(true, isotopicCluster1.hasSamePeaks(isotopicCluster2));
    }

    @Test
    public void manipulateWhenHasSamePeaks() throws Exception {
        Configuration config = new Configuration();
        Peak peak1 = new Peak(1.0, 5.0, 0);
        Peak peak2 = new Peak(2.0, 5.0, 1);
        Peak peak3 = new Peak(3.0, 5.0, 2);
        Peak peak4 = new Peak(4.0, 5.0, 4);
        Peak peak5 = new Peak(5.0, 5.0, 5);

        List<Peak> peaks1 = new ArrayList<Peak>();
        peaks1.add(peak1);
        peaks1.add(peak2);
        peaks1.add(peak3);
        PeakList peaklist1 = new PeakList(peaks1);

        List<Peak> peaks2 = new ArrayList<Peak>();
        peaks2.add(peak3);
        peaks2.add(peak4);
        peaks2.add(peak5);
        PeakList peaklist2 = new PeakList(peaks2);

        IsotopicCluster isotopicCluster1 = new IsotopicCluster(peaks1, 1, peaklist1, config.getIsotopicPeakDistance(),
                config.getDelta());

        IsotopicCluster isotopicCluster2 = new IsotopicCluster(peaks2, 1, peaklist2, config.getIsotopicPeakDistance(),
                config.getDelta());

        assertEquals("(0) [ 1.0 2.0 3.0 ]", isotopicCluster1.toString());
        assertEquals("(0) [ 3.0 4.0 5.0 ]", isotopicCluster2.toString());

        isotopicCluster1.manipulateWhenHasSamePeaks(isotopicCluster2);

        assertEquals("(0) [ 1.0 2.0 ]", isotopicCluster1.toString());
        assertEquals("(0) [ 3.0 4.0 5.0 ]", isotopicCluster2.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsotopicClusterCreation_MustFail1() {
        List<Peak> peaks = new ArrayList<Peak>();
        peaks.add(new Peak(1.0, 1, 0));
        peaks.add(new Peak(2.0, 1, 1));
        PeakList peaklist = new PeakList(peaks);
        Configuration config = new Configuration();

        new IsotopicCluster(peaks, 2, peaklist, config.getIsotopicPeakDistance(), config.getDelta());
        new IsotopicCluster(peaks, 3, peaklist, config.getIsotopicPeakDistance(), config.getDelta());
    }

    @Test
    public void testIsotopicCluster() {

    }
}
