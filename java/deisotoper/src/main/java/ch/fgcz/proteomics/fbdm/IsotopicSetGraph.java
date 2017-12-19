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

    public IsotopicSetGraph(List<IsotopicCluster> isotopicSet, PeakList peakList, double peptidMass, int chargeState,
            Configuration config) {
        List<IsotopicCluster> isotopicSet2 = new ArrayList<IsotopicCluster>(isotopicSet);

        this.minimum = Double.MAX_VALUE;
        isotopicSet2.add(new IsotopicCluster(START));

        assignStartAndOther(isotopicSet2, peakList, peptidMass, chargeState, config);

        assignEnd(peakList, peptidMass, chargeState, config);
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

    private void connectClusters(IsotopicCluster cluster1, IsotopicCluster cluster2, String color, PeakList peakList,
            double peptidMass, int chargeState, Configuration config) {
        Connection connection = new Connection(color);

        this.iClusterGraph.addVertex(cluster1);
        this.iClusterGraph.addVertex(cluster2);

        this.iClusterGraph.addEdge(cluster1, cluster2, connection);

        // TODO : Set the connection here. done
        Score score = new Score(peptidMass, chargeState, config);
        ScoreFive scoreFive = new ScoreFive(this.iClusterGraph, config);

        double scoreSum = 0;
        double additionalScore = 0;
        if (this.iClusterGraph.getEdgeTarget(connection).isNotNull()) {
            for (Peak peakX : this.iClusterGraph.getEdgeTarget(connection).getIsotopicCluster()) {
                for (Peak peakY : peakList.getPeakList()) {
                    double scoreResult = score.calculateAggregatedScore(peakX.getMz(), peakY.getMz(),
                            this.iClusterGraph.getEdgeTarget(connection).getIsotopicCluster());

                    double scoreFiveResult = scoreFive.calculateFifthScore(connection);

                    additionalScore += 0.00001;

                    scoreSum += scoreResult + scoreFiveResult;
                }
            }
        }

        if (scoreSum == 0) {
            scoreSum = additionalScore;
        }

        this.iClusterGraph.setEdgeWeight(connection, scoreSum);
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
    private void assignStartAndOther(List<IsotopicCluster> isotopicSet, PeakList peakList, double peptidMass,
            int chargeState, Configuration config) {
        for (IsotopicCluster cluster1 : isotopicSet) {
            for (IsotopicCluster cluster2 : isotopicSet) {

                String color = calculateConnection(cluster1, cluster2);

                // TODO(LS) why do you need the if statements here? LS: to check if it has a
                // color and the other has peaks in it to connect

                // Start
                if (color != null && cluster1.isNull() && cluster2.isNotNull()) {
                    connectClusters(cluster1, cluster2, color, peakList, peptidMass, chargeState, config);
                }

                // Other
                if (color != null && cluster1.isNotNull() && cluster2.isNotNull()) {
                    connectClusters(cluster1, cluster2, color, peakList, peptidMass, chargeState, config);
                }
            }
        }
    }

    private void assignEnd(PeakList peakList, double peptidMass, int chargeState, Configuration config) {
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
            connectClusters(cluster, endCluster, BLACK, peakList, peptidMass, chargeState, config); // special case
        }
    }

}
