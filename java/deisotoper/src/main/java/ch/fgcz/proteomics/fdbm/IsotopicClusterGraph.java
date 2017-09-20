package ch.fgcz.proteomics.fdbm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-20
 */

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.dto.MassSpectrum;

import java.util.ArrayList;
import java.util.List;

public class IsotopicClusterGraph {
    private double min = Double.MAX_VALUE;
    private DirectedGraph<IsotopicCluster, Connection> isotopicclustergraph = new DefaultDirectedWeightedGraph<IsotopicCluster, Connection>(Connection.class);

    public static void main(String[] args) {
        String s = "TesterinoData.RData";

        String typ = "MS2 Spectrum";
        String searchengine = "mascot";
        double[] mz = { 0.2, 1.0, 2.0, 2.5, 3.0, 10.0 };
        double[] intensity = { 0.5, 2.0, 1.0, 1.0, 1.0, 3.0 };
        double peptidmass = 309.22;
        double rt = 38383.34;
        int chargestate = 2;
        int id = 123;

        String typ2 = "MS2 Spectrum";
        String searchengine2 = "mascot";
        double[] mz2 = { 0.2, 2.0, 3.0, 4.0, 9.0, 10.0, 10.5, 11.0 };
        double[] intensity2 = { 0.5, 2.0, 1.0, 1.0, 1.0, 3.0, 2.0, 3.0 };
        double peptidmass2 = 423.22;
        double rt2 = 12431.12;
        int chargestate2 = 2;
        int id2 = 124;

        MassSpectrometryMeasurement MSM = new MassSpectrometryMeasurement(s);
        MSM.addMS(typ, searchengine, mz, intensity, peptidmass, rt, chargestate, id);
        MSM.addMS(typ2, searchengine2, mz2, intensity2, peptidmass2, rt2, chargestate2, id2);

        createIsotopicClusterGraphFromMSM(MSM);
    }

    public static void createIsotopicClusterGraphFromMSM(MassSpectrometryMeasurement MSM) {
        for (MassSpectrum MS : MSM.getMSlist()) {
            createIsotopicClusterGraphFromMS(MS);
        }
    }

    public static void createIsotopicClusterGraphFromMS(MassSpectrum MS) {
        IsotopicMassSpectrum ims = new IsotopicMassSpectrum(MS, 0.01);
        for (IsotopicSet IS : ims.getIsotopicMassSpectrum()) {
            IsotopicClusterGraph test = new IsotopicClusterGraph(IS);
            prettyPrint(test.isotopicclustergraph, IS);
        }
    }

    public IsotopicClusterGraph(IsotopicSet IS) {
        this.min = Double.MAX_VALUE;
        IS.getIsotopicSet().add(new IsotopicCluster("start"));
        for (IsotopicCluster n : IS.getIsotopicSet()) {
            for (IsotopicCluster m : IS.getIsotopicSet()) {
                String color = calculateConnection(n, m);

                // Start
                if (color != null && n.getIsotopicCluster() == null && m.getIsotopicCluster() != null) {
                    connectClusters(n, m, this.isotopicclustergraph, color);
                }

                // Other
                if (color != null && n.getIsotopicCluster() != null && m.getIsotopicCluster() != null) {
                    connectClusters(n, m, this.isotopicclustergraph, color);
                }
            }
        }

        // End
        List<IsotopicCluster> list = new ArrayList<>();
        for (IsotopicCluster e : this.isotopicclustergraph.vertexSet()) {
            int edgecount = 0;
            for (IsotopicCluster f : this.isotopicclustergraph.vertexSet()) {
                edgecount += this.isotopicclustergraph.getAllEdges(e, f).size();
            }

            if (edgecount == 0) {
                list.add(e);
            }
        }

        for (IsotopicCluster e : list) {
            connectClusters(e, new IsotopicCluster("end"), this.isotopicclustergraph, "black");
        }

    }

    private static void prettyPrint(DirectedGraph<IsotopicCluster, Connection> clustergraph, IsotopicSet IS) {
        System.out.println("For this IsotopicClusters: ");
        for (IsotopicCluster cluster : IS.getIsotopicSet()) {
            if (cluster.getIsotopicCluster() != null) {
                System.out.print("[ ");
                for (Peak p : cluster.getIsotopicCluster()) {
                    System.out.print(p.getMz() + " ");
                }
                System.out.print("] ");
            }
        }
        System.out.println();
        System.out.println();
        System.out.println("Exists this IsotopicClusterGraph:");
        for (Connection e : clustergraph.edgeSet()) {
            if (clustergraph.getEdgeSource(e).getIsotopicCluster() != null && clustergraph.getEdgeTarget(e).getIsotopicCluster() != null) {
                for (Peak x : clustergraph.getEdgeSource(e).getIsotopicCluster()) {
                    System.out.print(x.getMz() + " ");
                }
                System.out.print("--" + e.getColor() + "-> ");
                for (Peak x : clustergraph.getEdgeTarget(e).getIsotopicCluster()) {
                    System.out.print(x.getMz() + " ");
                }
                System.out.println();
            } else if (clustergraph.getEdgeSource(e).getIsotopicCluster() == null && clustergraph.getEdgeTarget(e).getIsotopicCluster() != null) {
                System.out.print("start ");
                System.out.print("--" + e.getColor() + "-> ");
                for (Peak x : clustergraph.getEdgeTarget(e).getIsotopicCluster()) {
                    System.out.print(x.getMz() + " ");
                }
                System.out.println();
            } else if (clustergraph.getEdgeTarget(e).getIsotopicCluster() == null && clustergraph.getEdgeSource(e).getIsotopicCluster() != null) {
                for (Peak x : clustergraph.getEdgeSource(e).getIsotopicCluster()) {
                    System.out.print(x.getMz() + " ");
                }
                System.out.print("--" + e.getColor() + "-> ");
                System.out.println("end");
            }
        }
        System.out.println();
        System.out.println();
    }

    private static DirectedGraph<IsotopicCluster, Connection> connectClusters(IsotopicCluster cluster1, IsotopicCluster cluster2, DirectedGraph<IsotopicCluster, Connection> graph, String color) {

        graph.addVertex(cluster1);
        graph.addVertex(cluster2);

        Connection connection = new Connection(color);
        graph.addEdge(cluster1, cluster2, connection);

        return graph;
    }

    /**
     * Returns the color of the connection between cluster1 and cluster2. If the clusters are not connected it returns null;
     * 
     * @param cluster1
     * @param cluster2
     * @return color
     */
    public String calculateConnection(IsotopicCluster cluster1, IsotopicCluster cluster2) {
        if (cluster1.getIsotopicCluster() != null) {
            if (cluster1.getIsotopicCluster().get(0).getMz() < this.min) {
                this.min = cluster1.getIsotopicCluster().get(0).getMz();
            }
        }

        if (cluster1.getStatus() == "start" && cluster2.getIsotopicCluster() != null && cluster1.getIsotopicCluster() == null && cluster2.getIsotopicCluster().get(0).getMz() == this.min) {
            return "black";
        }

        if (cluster1.getIsotopicCluster() == null || cluster2.getIsotopicCluster() == null) {
            return null;
        }

        if (cluster1.getIsotopicCluster().get(cluster1.getIsotopicCluster().size() - 1).getMz() < cluster2.getIsotopicCluster().get(0).getMz()) {
            return "black";
        }

        if (cluster1.getIsotopicCluster().get(0).getMz() < cluster2.getIsotopicCluster().get(0).getMz()) {
            if (cluster1.getIsotopicCluster().size() == 2) {
                if (cluster1.getIsotopicCluster().get(1).getMz() == cluster2.getIsotopicCluster().get(0).getMz()) {
                    return "red";
                }
            } else if (cluster1.getIsotopicCluster().size() == 3) {
                if (cluster1.getIsotopicCluster().get(1).getMz() == cluster2.getIsotopicCluster().get(0).getMz()
                        || cluster1.getIsotopicCluster().get(2).getMz() == cluster2.getIsotopicCluster().get(0).getMz()) {
                    return "red";
                }
            } else if (cluster1.getIsotopicCluster().size() == 3) {
                if (cluster1.getIsotopicCluster().get(1).getMz() == cluster2.getIsotopicCluster().get(0).getMz()
                        && cluster1.getIsotopicCluster().get(2).getMz() == cluster2.getIsotopicCluster().get(1).getMz()) {
                    return "red";
                }
            }
        }

        return null;
    }
}
