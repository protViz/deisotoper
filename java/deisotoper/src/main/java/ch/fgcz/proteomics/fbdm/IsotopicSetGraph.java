package ch.fgcz.proteomics.fbdm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucas Schmidt
 * @since 2017-09-20
 */

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

public class IsotopicSetGraph {
    private static final String BLACK = "black";
    private static final String RED = "red";
    private static final String START = "start";
    private static final String END = "end";
    private double minimum = Double.MAX_VALUE;
    private DefaultDirectedWeightedGraph<IsotopicCluster, Connection> iClusterGraph = new DefaultDirectedWeightedGraph<IsotopicCluster, Connection>(
            Connection.class);

    public IsotopicSetGraph(List<IsotopicCluster> isotopicSet) {
        List<IsotopicCluster> isotopicSet2 = new ArrayList<IsotopicCluster>(isotopicSet);

        this.minimum = Double.MAX_VALUE;
        isotopicSet2.add(new IsotopicCluster(START));

        assignStartAndOther(isotopicSet2);

        assignEnd();
    }

    public DefaultDirectedWeightedGraph<IsotopicCluster, Connection> getIsotopicClusterGraph() {
        return iClusterGraph;
    }

    public GraphPath<IsotopicCluster, Connection> bestPath(IsotopicCluster startCluster, IsotopicCluster endCluster) {
        KShortestPaths<IsotopicCluster, Connection> kPaths = new KShortestPaths<IsotopicCluster, Connection>(
                this.iClusterGraph, 999999);

        List<GraphPath<IsotopicCluster, Connection>> paths = kPaths.getPaths(startCluster, endCluster);

        return paths.get(paths.size() - 1);
    }

    public IsotopicCluster getStart() {
        for (IsotopicCluster cluster : this.iClusterGraph.vertexSet()) {
            if (cluster.isNull() && cluster.getStatus() == START) {
                return cluster;
            }
        }
        return null;
    }

    public IsotopicCluster getEnd() {
        for (IsotopicCluster cluster : this.iClusterGraph.vertexSet()) {
            if (cluster.isNull() && cluster.getStatus() == END) {
                return cluster;
            }
        }
        return null;
    }

    // TODO (LS) Unit test. See IsotopicSetGraphTest
    public String calculateConnection(IsotopicCluster cluster1, IsotopicCluster cluster2) {
        if (cluster1.isNotNull() && cluster1.getPeak(0).getMz() < this.minimum) {
            this.minimum = cluster1.getPeak(0).getMz();
        }

        // is start or is is null? LS: Both!
        // TODO (LS)
        if (cluster1.getStatus() == START && cluster2.isNotNull() && cluster2.getPeak(0).getMz() == this.minimum) {
            return BLACK;
        }

        if (cluster1.isNull() || cluster2.isNull()) {
            return null;
        }

        if (cluster1.getPeak(cluster1.size() - 1).getMz() < cluster2.getPeak(0).getMz()) {
            return BLACK;
        }

        return calculateConnectionCompare(cluster1, cluster2);
    }

    private void connectClusters(IsotopicCluster cluster1, IsotopicCluster cluster2, String color) {
        Connection connection = new Connection(color);

        this.iClusterGraph.addVertex(cluster1);
        this.iClusterGraph.addVertex(cluster2);

        this.iClusterGraph.addEdge(cluster1, cluster2, connection);
        this.iClusterGraph.setEdgeWeight(connection, cluster2.getScore());
    }

    // TODO unit test. See IsotopicSetGraphTest
    private String calculateConnectionCompare(IsotopicCluster cluster1, IsotopicCluster cluster2) {
        if (cluster1.getPeak(0).getMz() < cluster2.getPeak(0).getMz()) {
            if (cluster1.size() == 2) {
                if (cluster1.getPeak(1).getMz() == cluster2.getPeak(0).getMz()) {
                    return RED;
                } else {
                    return null;
                }
            } else if (cluster1.size() == 3) {
                if (cluster1.getPeak(1).getMz() == cluster2.getPeak(0).getMz()
                        || cluster1.getPeak(2).getMz() == cluster2.getPeak(0).getMz()) {
                    return RED;
                } else if (cluster1.getPeak(1).getMz() == cluster2.getPeak(0).getMz()
                        && cluster1.getPeak(2).getMz() == cluster2.getPeak(1).getMz()) {
                    return RED;
                } else {
                    return null;
                }
            }
        }

        return null;
    }

    // TODO (LS) what does Other mean? LS: Other = all the other clusters (therefore
    // not start and end cluster)
    private void assignStartAndOther(List<IsotopicCluster> isotopicSet) {
        for (IsotopicCluster cluster1 : isotopicSet) {
            for (IsotopicCluster cluster2 : isotopicSet) {

                String color = calculateConnection(cluster1, cluster2);

                // TODO(LS) why do you need the if statements here? LS: to check if it has a
                // color and the other has peaks in it to connect

                // Start
                if (color != null && cluster1.isNull() && cluster2.isNotNull()) {
                    connectClusters(cluster1, cluster2, color);
                }

                // Other
                if (color != null && cluster1.isNotNull() && cluster2.isNotNull()) {
                    connectClusters(cluster1, cluster2, color);
                }
            }
        }
    }

    private void assignEnd() {
        // End
        List<IsotopicCluster> isotopicClusters = new ArrayList<IsotopicCluster>();
        for (IsotopicCluster cluster1 : this.iClusterGraph.vertexSet()) {
            int edgeCount = 0;
            for (IsotopicCluster cluster2 : this.iClusterGraph.vertexSet()) {
                edgeCount += this.iClusterGraph.getAllEdges(cluster1, cluster2).size();
            }

            if (edgeCount == 0) {
                isotopicClusters.add(cluster1);
            }
        }

        IsotopicCluster endCluster = new IsotopicCluster(END);
        for (IsotopicCluster cluster : isotopicClusters) {
            connectClusters(cluster, endCluster, BLACK); // special case
        }
    }

}