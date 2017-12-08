package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-11-23
 */

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class IsotopicClusterTest {
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

        assertEquals(1.0, isotopicClusterAggregated.getMz(), 0);
        assertEquals(55.0, isotopicClusterAggregated.getIntensity(), 0);

    }

    // Do we allow that peaks have the same peak ID?
    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void isotopicClusterCreation_SamePeakIDs() {
        List<Peak> peaks = new ArrayList<Peak>();
        peaks.add(new Peak(1.0, 5.0, 0));
        peaks.add(new Peak(2.0, 50.0, 0));
        peaks.add(new Peak(3.0, 5.0, 2));
        PeakList peakList = new PeakList(peaks);
        Configuration config = new Configuration();
        new IsotopicCluster(peaks, 1, peakList, config.getIsotopicPeakDistance(), config.getDelta());
    }

    // Do we allow for unsorted clusters? This should pass.

    public void aggregation_Unsorted() throws Exception {

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
    }

    @Test
    public void hasSamePeaks() throws Exception {
    }

    @Test
    public void manipulateWhenHasSamePeaks() throws Exception {
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
