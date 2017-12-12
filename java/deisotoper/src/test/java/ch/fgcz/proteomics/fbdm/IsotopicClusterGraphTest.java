package ch.fgcz.proteomics.fbdm;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import ch.fgcz.proteomics.dto.MassSpectrum;

public class IsotopicClusterGraphTest {
    @Test
    public void testIsotopicClusterGraphCreation() {
        Peak peakA = new Peak(101.0, 100, 0);
        Peak peakB = new Peak(102.0, 100, 1);
        Peak peakC = new Peak(103.0, 100, 2);

        PeakList peakList = new PeakList(new MassSpectrum(Arrays.asList(89.0, 101.0, 102.0, 103.0, 188.0),
                Arrays.asList(100.0, 100.0, 100.0, 100.0, 100.0)));
        List<Peak> peaksInSet = Arrays.asList(peakA, peakB, peakC);
        IsotopicSet isotopicSet = new IsotopicSet(peakList, peaksInSet, 0, new Configuration());

        IsotopicClusterGraph isotopicClusterGraph = new IsotopicClusterGraph(isotopicSet.getIsotopicSet());

        Set<IsotopicCluster> isotopicClusters = isotopicClusterGraph.getIsotopicClusterGraph().vertexSet();

        assertEquals(isotopicClusters.size(), 5);

        int startCount = 0;
        int endCount = 0;
        int normalCount = 0;
        for (IsotopicCluster isotopicCluster : isotopicClusters) {
            if (isotopicCluster.getStatus() != null) {
                if (isotopicCluster.getStatus().equals("start")) {
                    startCount++;
                } else if (isotopicCluster.getStatus().equals("end")) {
                    endCount++;
                }
            } else {
                normalCount++;
            }
        }

        assertEquals(startCount, 1);
        assertEquals(endCount, 1);
        assertEquals(normalCount, 3);

        // Add tests for connections
    }
}
