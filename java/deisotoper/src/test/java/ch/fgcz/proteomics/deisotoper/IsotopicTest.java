
/**
 * @author Lucas Schmidt
 * @since 2017-09-05
 */

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class IsotopicTest {
    Peaklist resultpeaklist1;
    Peaklist resultpeaklist2;
    Peaklist peaklist1;
    Peaklist peaklist2;
    IsotopicSets IS1;
    IsotopicSets IS2;
    List<List<Peak>> clusters1 = new ArrayList<>();
    List<List<Peak>> clusters2 = new ArrayList<>();
    List<String> edgecolorlist1 = new ArrayList<>();

    /**
     * Sets up the starting parameters.
     * 
     * The examples are constructed as Peaklist Objects. From those Peaklist Objects you can construct the IsotopicSets with a given error tolerance.
     * 
     * Also there is a result-pattern for each example, to compare the output (IsotopicSets) with the result-pattern.
     */
    @Before
    public void setUp() {
        // Example 1
        List<Double> mz1 = Arrays.asList(0.1, 0.2, 1.33, 1.66, 2.0, 2.5, 3.5, 4.0, 5.0, 6.0, 12.0, 15.0);
        List<Double> intensity1 = Arrays.asList(7.0, 4.0, 5.0, 6.0, 6.0, 7.0, 7.0, 7.0, 8.0, 8.0, 2.0, 3.0);
        peaklist1 = new Peaklist(mz1, intensity1);

        // Example 2
        List<Double> mz2 = Arrays.asList(0.2, 1.0, 2.0, 3.0, 3.5, 4.0, 4.5, 5.0, 6.0, 6.5, 7.0, 11.0, 12.3);
        List<Double> intensity2 = Arrays.asList(10.0, 8.0, 3.0, 5.0, 6.0, 1.0, 4.0, 2.0, 1.0, 4.0, 1.0, 2.0, 4.0);
        peaklist2 = new Peaklist(mz2, intensity2);

        // Example 1 Result
        List<Double> resultmz1 = Arrays.asList(1.33, 1.66, 2.0, 2.5, 3.5, 4.0, 5.0, 6.0);
        List<Double> resultintensity1 = Arrays.asList(5.0, 6.0, 6.0, 7.0, 7.0, 7.0, 8.0, 8.0);
        resultpeaklist1 = new Peaklist(resultmz1, resultintensity1);

        // Example 2 Result
        List<Double> resultmz2 = Arrays.asList(1.0, 2.0, 3.0, 3.5, 4.0, 4.5, 5.0, 6.0, 6.5, 7.0);
        List<Double> resultintensity2 = Arrays.asList(8.0, 3.0, 5.0, 6.0, 1.0, 4.0, 2.0, 1.0, 4.0, 1.0);
        resultpeaklist2 = new Peaklist(resultmz2, resultintensity2);

        // Clusters 1 Result
        List<Double> clustermz1 = Arrays.asList(1.33, 1.66, 2.0);
        List<Double> clusterintensity1 = Arrays.asList(5.0, 6.0, 6.0);
        Peaklist cluster1 = new Peaklist(clustermz1, clusterintensity1);
        clusters1.add(cluster1.getPeaklist());
        List<Double> clustermz2 = Arrays.asList(1.33, 1.66);
        List<Double> clusterintensity2 = Arrays.asList(5.0, 6.0);
        Peaklist cluster2 = new Peaklist(clustermz2, clusterintensity2);
        clusters1.add(cluster2.getPeaklist());
        List<Double> clustermz3 = Arrays.asList(1.66, 2.0);
        List<Double> clusterintensity3 = Arrays.asList(6.0, 6.0);
        Peaklist cluster3 = new Peaklist(clustermz3, clusterintensity3);
        clusters1.add(cluster3.getPeaklist());
        List<Double> clustermz4 = Arrays.asList(2.0, 2.5);
        List<Double> clusterintensity4 = Arrays.asList(6.0, 7.0);
        Peaklist cluster4 = new Peaklist(clustermz4, clusterintensity4);
        clusters1.add(cluster4.getPeaklist());
        List<Double> clustermz5 = Arrays.asList(2.5, 3.5);
        List<Double> clusterintensity5 = Arrays.asList(7.0, 7.0);
        Peaklist cluster5 = new Peaklist(clustermz5, clusterintensity5);
        clusters1.add(cluster5.getPeaklist());
        List<Double> clustermz6 = Arrays.asList(3.5, 4.0);
        List<Double> clusterintensity6 = Arrays.asList(7.0, 7.0);
        Peaklist cluster6 = new Peaklist(clustermz6, clusterintensity6);
        clusters1.add(cluster6.getPeaklist());
        List<Double> clustermz7 = Arrays.asList(4.0, 5.0, 6.0);
        List<Double> clusterintensity7 = Arrays.asList(7.0, 8.0, 8.0);
        Peaklist cluster7 = new Peaklist(clustermz7, clusterintensity7);
        clusters1.add(cluster7.getPeaklist());
        List<Double> clustermz8 = Arrays.asList(4.0, 5.0);
        List<Double> clusterintensity8 = Arrays.asList(7.0, 8.0);
        Peaklist cluster8 = new Peaklist(clustermz8, clusterintensity8);
        clusters1.add(cluster8.getPeaklist());
        List<Double> clustermz9 = Arrays.asList(5.0, 6.0);
        List<Double> clusterintensity9 = Arrays.asList(8.0, 8.0);
        Peaklist cluster9 = new Peaklist(clustermz9, clusterintensity9);
        clusters1.add(cluster9.getPeaklist());

        // Clusters 2 Result
        List<Double> clustermz21 = Arrays.asList(1.0, 2.0, 3.0);
        List<Double> clusterintensity21 = Arrays.asList(8.0, 3.0, 5.0);
        Peaklist cluster21 = new Peaklist(clustermz21, clusterintensity21);
        clusters2.add(cluster21.getPeaklist());
        List<Double> clustermz22 = Arrays.asList(1.0, 2.0);
        List<Double> clusterintensity22 = Arrays.asList(8.0, 3.0);
        Peaklist cluster22 = new Peaklist(clustermz22, clusterintensity22);
        clusters2.add(cluster22.getPeaklist());
        List<Double> clustermz23 = Arrays.asList(2.0, 3.0);
        List<Double> clusterintensity23 = Arrays.asList(3.0, 5.0);
        Peaklist cluster23 = new Peaklist(clustermz23, clusterintensity23);
        clusters2.add(cluster23.getPeaklist());
        List<Double> clustermz24 = Arrays.asList(3.0, 3.5, 4.0);
        List<Double> clusterintensity24 = Arrays.asList(5.0, 6.0, 1.0);
        Peaklist cluster24 = new Peaklist(clustermz24, clusterintensity24);
        clusters2.add(cluster24.getPeaklist());
        List<Double> clustermz25 = Arrays.asList(3.0, 3.5);
        List<Double> clusterintensity25 = Arrays.asList(5.0, 6.0);
        Peaklist cluster25 = new Peaklist(clustermz25, clusterintensity25);
        clusters2.add(cluster25.getPeaklist());
        List<Double> clustermz26 = Arrays.asList(3.5, 4.0, 4.5);
        List<Double> clusterintensity26 = Arrays.asList(6.0, 1.0, 4.0);
        Peaklist cluster26 = new Peaklist(clustermz26, clusterintensity26);
        clusters2.add(cluster26.getPeaklist());
        List<Double> clustermz27 = Arrays.asList(3.5, 4.0);
        List<Double> clusterintensity27 = Arrays.asList(6.0, 1.0);
        Peaklist cluster27 = new Peaklist(clustermz27, clusterintensity27);
        clusters2.add(cluster27.getPeaklist());
        List<Double> clustermz28 = Arrays.asList(4.0, 4.5, 5.0);
        List<Double> clusterintensity28 = Arrays.asList(1.0, 4.0, 2.0);
        Peaklist cluster28 = new Peaklist(clustermz28, clusterintensity28);
        clusters2.add(cluster28.getPeaklist());
        List<Double> clustermz29 = Arrays.asList(4.0, 4.5);
        List<Double> clusterintensity29 = Arrays.asList(1.0, 4.0);
        Peaklist cluster29 = new Peaklist(clustermz29, clusterintensity29);
        clusters2.add(cluster29.getPeaklist());
        List<Double> clustermz210 = Arrays.asList(4.5, 5.0);
        List<Double> clusterintensity210 = Arrays.asList(4.0, 2.0);
        Peaklist cluster210 = new Peaklist(clustermz210, clusterintensity210);
        clusters2.add(cluster210.getPeaklist());
        List<Double> clustermz211 = Arrays.asList(5.0, 6.0);
        List<Double> clusterintensity211 = Arrays.asList(2.0, 1.0);
        Peaklist cluster211 = new Peaklist(clustermz211, clusterintensity211);
        clusters2.add(cluster211.getPeaklist());
        List<Double> clustermz212 = Arrays.asList(6.0, 6.5, 7.0);
        List<Double> clusterintensity212 = Arrays.asList(1.0, 4.0, 1.0);
        Peaklist cluster212 = new Peaklist(clustermz212, clusterintensity212);
        clusters2.add(cluster212.getPeaklist());
        List<Double> clustermz213 = Arrays.asList(6.0, 6.5);
        List<Double> clusterintensity213 = Arrays.asList(1.0, 4.0);
        Peaklist cluster213 = new Peaklist(clustermz213, clusterintensity213);
        clusters2.add(cluster213.getPeaklist());
        List<Double> clustermz214 = Arrays.asList(6.5, 7.0);
        List<Double> clusterintensity214 = Arrays.asList(4.0, 1.0);
        Peaklist cluster214 = new Peaklist(clustermz214, clusterintensity214);
        clusters2.add(cluster214.getPeaklist());

        // Edge Color List 1
        edgecolorlist1 = Arrays.asList("black", "black", "red", "red", "black", "black", "black", "black", "black", "red", "black", "black", "black", "black", "black", "black", "red", "black",
                "black", "black", "black", "black", "red", "black", "black", "black", "black", "red", "black", "black", "black", "red", "red", "black", "red", "red", "black");

        // Isotopic Sets
        double errortolerance = 0.01;
        IS1 = new IsotopicSets(peaklist1, errortolerance);
        IS2 = new IsotopicSets(peaklist2, errortolerance);
    }

    /**
     * Checks if the calculated IsotopicSets are not empty and if they match the result-pattern.
     */
    @Test
    public void testIsotopeSetConstruction() {
        // Assert
        assertNotNull(IS1);
        assertEquals("Calculated IS1 must be same size as defined result (resultpreaklist1)!", IS1.getIsotopicsets().get(0).size(), resultpeaklist1.getPeaklist().size());
        assertPeaklistEquals(IS1.getIsotopicsets().get(0), resultpeaklist1.getPeaklist());

        assertNotNull(IS2);
        assertEquals("Calculated IS2 must be same size as defined result (resultpreaklist2)!", IS2.getIsotopicsets().get(0).size(), resultpeaklist2.getPeaklist().size());
        assertPeaklistEquals(IS2.getIsotopicsets().get(0), resultpeaklist2.getPeaklist());
    }

    /**
     * Checks if the calculated IsotopicClusters not empty and if there is the correct number of IsotopicCluster.
     */
    @Test
    public void testIsotopeClustersConstruction() {
        for (List<Peak> set : IS1.getIsotopicsets()) {
            IsotopicClusters IC = new IsotopicClusters(set, 0.01);
            assertNotNull(IC);
            int i = 0;
            for (List<Peak> c : IC.getIsotopicclusters()) {
                assertPeaklistEquals(c, clusters1.get(i)); // Compare each calculated cluster with predefined cluster
                i++;
            }
            assertEquals("Calculated IC must be size 9!", IC.getIsotopicclusters().size(), 9);
        }

        for (List<Peak> set : IS2.getIsotopicsets()) {
            IsotopicClusters IC = new IsotopicClusters(set, 0.01);
            assertNotNull(IC);
            int i = 0;
            for (List<Peak> c : IC.getIsotopicclusters()) {
                assertPeaklistEquals(c, clusters2.get(i)); // Compare each calculated cluster with predefined cluster
                i++;
            }
            assertEquals("Calculated IC must be size 14!", IC.getIsotopicclusters().size(), 14);
        }
    }

    /**
     * Test the construction of a Edge between two clusters
     */
    @Test
    // UNDER CONSTRUCTION
    public void testGraphConstruction() {
        IsotopicClusters IC = new IsotopicClusters(IS1.getIsotopicsets().get(0), 0.01);

        IsotopicClustersGraph graph = new IsotopicClustersGraph();

        graph.createGraph(IC);

        int j = 0;
        for (Node n : graph.getAdjacencylist()) {
            for (Edge e : n.getEdges()) {
                assertEquals(e.getColor(), edgecolorlist1.get(j));
                j++;
            }
        }

        // System.out.println(IsotopicClustersGraph.prettyPrint(graph));
    }

    // TODO: UML diagram of IS, IC, etc...

    /**
     * Compares two lists of Peaks with each other.
     * 
     * The function is a additional function to the existing assert functions from the JUnit package.
     * 
     * @param list
     * @param list2
     */
    private void assertPeaklistEquals(List<Peak> list, List<Peak> list2) {
        for (int i = 0; i < list.size() || i < list2.size(); i++) {
            boolean b = false;

            if (list.get(i).getMz() == list2.get(i).getMz() && list.get(i).getIntensity() == list2.get(i).getIntensity()) {
                b = true;
            }

            assertTrue(b);
        }
    }
}
