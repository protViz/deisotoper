package ch.fgcz.proteomics.fbdm;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import ch.fgcz.proteomics.dto.MassSpectrum;;

public class IsotopicSetGraphTest {
    private static final double MIN = Double.MIN_VALUE;

    @Test
    public void connectConnectionTest() {
        List<Peak> peaks = Arrays.asList(new Peak(101.0, 50.0, 0), new Peak(102.0, 5.0, 1));
        Configuration config = new Configuration();
        PeakList peakList = new PeakList(peaks);

        IsotopicSetGraph isotopicSetGraph = new IsotopicSetGraph(
                new IsotopicSet(peakList, peakList.getPeakList(), 1, config).getIsotopicSet(), peakList, 200, 2,
                config);

        Set<Connection> connections = isotopicSetGraph.getIsotopicClusterGraph().edgeSet();
        Iterator<Connection> iterator = connections.iterator();

        assertEquals(4.8, isotopicSetGraph.getIsotopicClusterGraph().getEdgeWeight(((Connection) iterator.next())),
                MIN);

        assertEquals(0, isotopicSetGraph.getIsotopicClusterGraph().getEdgeWeight(((Connection) iterator.next())), MIN);
    }

    @Test
    public void calculateConnectionTestBlack() {
        List<Peak> peaks = Arrays.asList(new Peak(1.0, 50.0, 0), new Peak(2.0, 5.0, 1));
        Configuration config = new Configuration();
        PeakList peakList = new PeakList(peaks);

        IsotopicCluster startCluster = new IsotopicCluster("start");
        IsotopicCluster cluster = new IsotopicCluster(peaks, 1, peakList, config.getIsotopicPeakDistance(),
                config.getDelta());

        IsotopicSetGraph isotopicSetGraph = new IsotopicSetGraph(
                new IsotopicSet(peakList, peakList.getPeakList(), 1, config).getIsotopicSet(), peakList, 200, 2,
                config);

        String color = isotopicSetGraph.calculateConnection(startCluster, cluster);

        assertEquals("black", color);
    }

    @Test
    public void calculateConnectionTestRed() {
        Peak peakA = new Peak(1.0, 5.0, 0);
        Peak peakB = new Peak(2.0, 5.0, 1);
        Peak peakC = new Peak(3.0, 5.0, 2);
        Configuration config = new Configuration();
        PeakList peakList = new PeakList(Arrays.asList(peakA, peakB, peakC));

        IsotopicCluster cluster1 = new IsotopicCluster(Arrays.asList(peakA, peakB), 1, peakList,
                config.getIsotopicPeakDistance(), config.getDelta());
        IsotopicCluster cluster2 = new IsotopicCluster(Arrays.asList(peakB, peakC), 1, peakList,
                config.getIsotopicPeakDistance(), config.getDelta());

        IsotopicSetGraph isotopicSetGraph = new IsotopicSetGraph(
                new IsotopicSet(peakList, peakList.getPeakList(), 1, config).getIsotopicSet(), peakList, 200, 2,
                config);

        String color = isotopicSetGraph.calculateConnection(cluster1, cluster2);

        assertEquals("red", color);
    }

    @Test
    public void testIsotopicClusterGraphCreation() {
        Peak peakA = new Peak(101.0, 100, 0);
        Peak peakB = new Peak(102.0, 100, 1);
        Peak peakC = new Peak(103.0, 100, 2);

        PeakList peakList = new PeakList(new MassSpectrum(Arrays.asList(89.0, 101.0, 102.0, 103.0, 188.0),
                Arrays.asList(100.0, 100.0, 100.0, 100.0, 100.0)));
        List<Peak> peaksInSet = Arrays.asList(peakA, peakB, peakC);
        IsotopicSet isotopicSet = new IsotopicSet(peakList, peaksInSet, 0, new Configuration());

        IsotopicSetGraph isotopicSetGraph = new IsotopicSetGraph(isotopicSet.getIsotopicSet(), peakList, 200, 2,
                new Configuration());

        Set<IsotopicCluster> isotopicClusters = isotopicSetGraph.getIsotopicClusterGraph().vertexSet();

        assertEquals(5, isotopicClusters.size());

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

        assertEquals(1, startCount);
        assertEquals(1, endCount);
        assertEquals(3, normalCount);

        // Add tests for connections
    }
}
