package ch.fgcz.proteomics.deisotoper;

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
    Peaklist resultpeaklist3;
    Peaklist peaklist1;
    Peaklist peaklist2;
    Peaklist peaklist3;
    IsotopicSets IS1;
    IsotopicSets IS2;
    IsotopicSets IS3;
    List<List<Peak>> clusters1 = new ArrayList<>();
    List<List<Peak>> clusters2 = new ArrayList<>();
    List<List<Peak>> clusters3 = new ArrayList<>();
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

        // Example 3
        List<Double> mz3 = Arrays.asList(0.2, 1.0, 2.0, 2.5, 3.0, 10.0);
        List<Double> intensity3 = Arrays.asList(0.5, 2.0, 1.0, 1.0, 1.0, 3.0);
        peaklist3 = new Peaklist(mz3, intensity3);

        // Example 1 Result
        List<Double> resultmz1 = Arrays.asList(1.33, 1.66, 2.0, 2.5, 3.5, 4.0, 5.0, 6.0);
        List<Double> resultintensity1 = Arrays.asList(5.0, 6.0, 6.0, 7.0, 7.0, 7.0, 8.0, 8.0);
        resultpeaklist1 = new Peaklist(resultmz1, resultintensity1);

        // Example 2 Result
        List<Double> resultmz2 = Arrays.asList(1.0, 2.0, 3.0, 3.5, 4.0, 4.5, 5.0, 6.0, 6.5, 7.0);
        List<Double> resultintensity2 = Arrays.asList(8.0, 3.0, 5.0, 6.0, 1.0, 4.0, 2.0, 1.0, 4.0, 1.0);
        resultpeaklist2 = new Peaklist(resultmz2, resultintensity2);

        // Example 3 Result
        List<Double> resultmz3 = Arrays.asList(1.0, 2.0, 2.5, 3.0);
        List<Double> resultintensity3 = Arrays.asList(2.0, 1.0, 1.0, 1.0);
        resultpeaklist3 = new Peaklist(resultmz3, resultintensity3);

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

        // Clusters 3 Result List<Double> resultintensity3 = Arrays.asList(2.0, 1.0, 1.0, 1.0);
        List<Double> clustermz31 = Arrays.asList(1.0, 2.0, 3.0);
        List<Double> clusterintensity31 = Arrays.asList(2.0, 1.0, 1.0);
        Peaklist cluster31 = new Peaklist(clustermz31, clusterintensity31);
        clusters3.add(cluster31.getPeaklist());
        List<Double> clustermz32 = Arrays.asList(1.0, 2.0);
        List<Double> clusterintensity32 = Arrays.asList(2.0, 1.0);
        Peaklist cluster32 = new Peaklist(clustermz32, clusterintensity32);
        clusters3.add(cluster32.getPeaklist());
        List<Double> clustermz33 = Arrays.asList(2.0, 3.0);
        List<Double> clusterintensity33 = Arrays.asList(1.0, 1.0);
        Peaklist cluster33 = new Peaklist(clustermz33, clusterintensity33);
        clusters3.add(cluster33.getPeaklist());
        List<Double> clustermz34 = Arrays.asList(2.0, 2.5, 3.0);
        List<Double> clusterintensity34 = Arrays.asList(1.0, 1.0, 1.0);
        Peaklist cluster34 = new Peaklist(clustermz34, clusterintensity34);
        clusters3.add(cluster34.getPeaklist());
        List<Double> clustermz35 = Arrays.asList(2.0, 2.5);
        List<Double> clusterintensity35 = Arrays.asList(1.0, 1.0);
        Peaklist cluster35 = new Peaklist(clustermz35, clusterintensity35);
        clusters3.add(cluster35.getPeaklist());
        List<Double> clustermz36 = Arrays.asList(2.5, 3.0);
        List<Double> clusterintensity36 = Arrays.asList(1.0, 1.0);
        Peaklist cluster36 = new Peaklist(clustermz36, clusterintensity36);
        clusters3.add(cluster36.getPeaklist());

        // Edge Color List 1
        edgecolorlist1 = Arrays.asList("black", "black", "red", "red", "black", "black", "black", "black", "black", "red", "black", "black", "black", "black", "black", "black", "red", "black",
                "black", "black", "black", "black", "red", "black", "black", "black", "black", "red", "black", "black", "black", "red", "red", "black", "red", "red", "black");

        // Isotopic Sets
        double errortolerance = 0.01;
        IS1 = new IsotopicSets(peaklist1, errortolerance);
        IS2 = new IsotopicSets(peaklist2, errortolerance);
        IS3 = new IsotopicSets(peaklist3, errortolerance);
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

        assertNotNull(IS3);
        assertEquals("Calculated IS3 must be same size as defined result (resultpreaklist3)!", IS3.getIsotopicsets().get(0).size(), resultpeaklist3.getPeaklist().size());
        assertPeaklistEquals(IS3.getIsotopicsets().get(0), resultpeaklist3.getPeaklist());
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

        // TODO: Fix bug in IsotopicClusters at cluster creation
        for (List<Peak> set : IS3.getIsotopicsets()) {
            IsotopicClusters IC = new IsotopicClusters(set, 0.01);
            assertNotNull(IC);

            for (List<Peak> c : IC.getIsotopicclusters()) {
                for (Peak cc : c) {
                    System.out.println(cc.getMz());
                }
                System.out.println();
            }

            int i = 0;
            for (List<Peak> c : IC.getIsotopicclusters()) {
                assertPeaklistEquals(c, clusters3.get(i)); // Compare each calculated cluster with predefined cluster
                i++;
            }
            assertEquals("Calculated IC must be size 6!", IC.getIsotopicclusters().size(), 6);
        }
    }

    /**
     * Test the construction of a whole graph with marked edges.
     */
    @Test
    // UNDER CONSTRUCTION
    public void testGraphConstruction() {
        IsotopicClusters IC = new IsotopicClusters(IS1.getIsotopicsets().get(0), 0.01);

        IsotopicClusterGraph graph = new IsotopicClusterGraph();

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

    @Test
    // UNDER CONSTRUCTION
    public void testScoreConstruction() {
        List<Double> mz = Arrays.asList(110.0715485, 110.1648788, 112.0395432, 113.0712204, 114.0746384, 115.05056, 115.9784622, 120.080986, 120.0833893, 121.0843735, 125.7498245, 129.0659332,
                129.1023102, 130.0863342, 130.1057281, 131.0815735, 132.0849609, 132.5098419, 137.0346222, 141.0658417, 143.0816193, 145.0608368, 147.1128235, 149.0233917, 150.0266876, 153.0657654,
                157.0970917, 159.076416, 160.0798035, 162.0875397, 162.0912323, 167.0816345, 170.092392, 171.0762177, 172.8989868, 175.118927, 180.0767212, 185.0920105, 189.1023407, 198.0873413,
                199.0907135, 204.0980682, 205.0570374, 207.1127777, 208.0717316, 210.0866547, 210.7322388, 211.5193329, 212.10289, 214.0822296, 216.0977783, 223.0636444, 224.1031799, 225.0603638,
                226.0820923, 227.0399933, 227.1023407, 228.098175, 230.1135101, 231.1125488, 231.1492462, 232.0926361, 232.2126312, 233.1646729, 235.1072845, 240.09758, 241.129364, 242.1128387,
                246.1234741, 249.098175, 253.0928345, 254.1314087, 254.6329193, 255.1085358, 258.1084595, 259.1398315, 263.1368713, 267.1086426, 268.1119995, 269.1243591, 271.1038513, 273.1199646,
                274.1184998, 276.1701965, 277.0926819, 277.1541138, 278.4438171, 279.108429, 285.1191101, 287.1348877, 289.650177, 292.1772461, 294.1808777, 295.1038513, 295.184082, 296.1346436,
                297.1190491, 299.0617065, 300.133606, 301.0583801, 303.1295166, 310.4591064, 314.1459961, 315.129303, 318.1446228, 318.1605225, 324.1297607, 328.1278992, 331.8494568, 332.1560364,
                333.1555786, 334.114502, 338.1456299, 342.1403198, 343.1411133, 346.176239, 351.1661377, 356.1558228, 357.1579285, 357.6632385, 358.1647949, 360.1513672, 360.6679077, 361.1502075,
                363.2023926, 366.6683655, 368.6846924, 375.1660156, 375.2023926, 379.1607361, 381.2128296, 382.2158813, 387.1657104, 388.1895447, 390.9455261, 395.1658325, 397.1503906, 397.1950684,
                397.6967163, 400.1613159, 403.135437, 403.1973877, 411.1614075, 418.1715698, 420.2235107, 421.1462402, 421.2265015, 426.1759949, 438.2343445, 439.2384644, 443.2040405, 444.1874084,
                454.1725464, 461.2140198, 462.1977844, 471.1980896, 472.2180786, 479.2248535, 482.1990967, 489.2085571, 490.2118835, 490.2296448, 495.2563477, 507.2199402, 507.2555847, 508.2598572,
                525.2663574, 526.2697754, 577.2515259, 578.2937012, 584.8656006, 596.3037109, 597.3069458, 617.3034668, 618.2872925, 635.3146362, 636.3175049, 653.3252563, 654.3284302);
        List<Double> intensity = Arrays.asList(6250.6640625, 563.5891113281, 787.7743530273, 42913.109375, 927.4467773438, 2023.1707763672, 529.0408325195, 25228.6796875, 1165.1545410156,
                603.5643310547, 528.4467773438, 7070.5766601563, 31543.533203125, 19388.078125, 833.7221679688, 81864.546875, 3091.2973632813, 582.6003417969, 6660.9750976563, 34947.4921875,
                684.2407836914, 3036.4206542969, 36999.45703125, 2973.7102050781, 829.9125366211, 6804.5180664063, 1259.5620117188, 46302.8125, 2067.9748535156, 1154.1105957031, 765.7245483398,
                794.5812988281, 4291.3037109375, 888.9245605469, 606.1282958984, 766.3591918945, 14302.1279296875, 3288.0661621094, 688.5368652344, 35533.54296875, 2225.658203125, 670.2319335938,
                677.4953613281, 6984.8188476563, 2347.3420410156, 594.0815429688, 2258.9313964844, 568.3290405273, 802.1146850586, 3599.0229492188, 7246.6596679688, 2139.9233398438, 828.2638549805,
                884.8049316406, 1193.3479003906, 566.4050292969, 786.0283813477, 615.2380981445, 1079.8623046875, 1339.6888427734, 806.2881469727, 9631.443359375, 569.907409668, 1065.3564453125,
                991.0939331055, 690.4099731445, 3580.0187988281, 672.0654296875, 845.2703857422, 1281.3441162109, 559.6304931641, 4143.1162109375, 1155.7521972656, 2598.6123046875, 953.2352905273,
                3944.8764648438, 7301.6743164063, 4961.2807617188, 569.8205566406, 2566.8024902344, 795.173828125, 757.6403808594, 3254.8908691406, 7315.53515625, 1220.8388671875, 1115.0716552734,
                524.3715820313, 676.455871582, 2324.4851074219, 808.6432495117, 1269.125, 581.4161376953, 31443.404296875, 2546.380859375, 2911.2058105469, 780.770324707, 738.4011230469,
                1135.4598388672, 1176.7568359375, 3533.0815429688, 2457.2565917969, 562.0629882813, 928.1300048828, 1034.6174316406, 1035.1872558594, 2155.4260253906, 3214.2192382813, 586.0258178711,
                545.24609375, 2240.283203125, 3230.6267089844, 1323.4107666016, 924.3392944336, 8073.1616210938, 767.8354492188, 789.9375610352, 2135.9985351563, 999.5626831055, 642.1485595703,
                7692.0913085938, 1246.6643066406, 4829.984375, 799.643371582, 3193.9008789063, 1350.830078125, 901.1105957031, 687.5123901367, 996.9341430664, 1064.1864013672, 911.1711425781,
                16409.24609375, 1171.5484619141, 960.3439331055, 935.4268188477, 677.4844970703, 657.7783813477, 2241.4836425781, 8700.474609375, 782.1491699219, 686.3291015625, 785.7287597656,
                753.0210571289, 714.055847168, 2145.2751464844, 5911.2006835938, 1220.7615966797, 623.0693969727, 634.6696777344, 33137.39453125, 5135.7211914063, 813.663269043, 1231.4427490234,
                933.5162963867, 3532.599609375, 1009.3364868164, 3219.3342285156, 800.7995605469, 3228.8891601563, 1368.7080078125, 6327.9125976563, 810.8509521484, 1239.9750976563, 791.6758422852,
                1281.634765625, 6519.0903320313, 575.096496582, 65968.4140625, 11777.5908203125, 2284.7736816406, 2809.2331542969, 622.4571533203, 22998.53515625, 3657.8000488281, 1279.3221435547,
                1236.6485595703, 9816.9814453125, 1331.3529052734, 87058.9140625, 21567.640625);

        Peaklist peaklist = new Peaklist(mz, intensity);

        IsotopicSets IS = new IsotopicSets(peaklist, 0.01);

        for (List<Peak> s : IS.getIsotopicsets()) {
            IsotopicClusters IC = new IsotopicClusters(s, 0.01);

            IsotopicClusterGraph graph = new IsotopicClusterGraph();

            graph.createGraph(IC);

            graph.scoreGraph(IS);

            // System.out.println(IsotopicClustersGraph.prettyPrint(graph));
        }
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
