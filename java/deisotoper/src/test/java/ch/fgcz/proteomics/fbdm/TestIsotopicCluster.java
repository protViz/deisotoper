package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-11-23
 */

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestIsotopicCluster {
    @Test
    public void testIsotopicCluster() {
        List<Peak> peaks = new ArrayList<>();
        peaks.add(new Peak(1.0, 50.0, 0));
        peaks.add(new Peak(2.0, 5.0, 1));
        IsotopicCluster isotopicCluster = new IsotopicCluster(peaks, 1, new Configuration());

        assertEquals(isotopicCluster.toString(), "(0) [ 1.0 2.0 ]");

        IsotopicCluster isotopicClusterAggregated = isotopicCluster.aggregation("first");

        assertEquals(isotopicClusterAggregated.toString(), "(0) [ 1.0 ]");
    }
}
