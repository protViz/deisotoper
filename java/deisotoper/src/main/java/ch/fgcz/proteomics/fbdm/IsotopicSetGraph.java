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

    // TODO (LS) what does Other mean?
    private void assignStartAndOther(List<IsotopicCluster> isotopicSet) {
        for (IsotopicCluster cluster1 : isotopicSet) {
            for (IsotopicCluster cluster2 : isotopicSet) {

                String color = calculateConnection(cluster1, cluster2);

                // TODO(LS) why do you need the if statements here?
                // Start
                if ( color != null && cluster1.isNull() && cluster2.isNotNull()) {
                    connectClusters(cluster1, cluster2, color, 1.0);
                }

                // Other
                if (color != null && cluster1.isNotNull() && cluster2.isNotNull()) {
                    connectClusters(cluster1, cluster2, color,1.0);
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
            connectClusters(cluster, endCluster, BLACK, 1.0);
        }
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

    // TODO (LS) (remove)
    public void scoreIsotopicClusterGraph(double peptidMass, int chargeState, PeakList peakList, Configuration config) {
        Score score = new Score(peptidMass, chargeState, config);
        ScoreFive scoreFive = new ScoreFive(this.iClusterGraph, config);

        for (Connection connection : this.iClusterGraph.edgeSet()) {
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

                if (scoreSum == 0) {
                    scoreSum = additionalScore;
                }
                connection.setScore(scoreSum);
                this.iClusterGraph.setEdgeWeight(connection, scoreSum);
            }
        }
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

    //
    private void connectClusters(IsotopicCluster cluster1, IsotopicCluster cluster2, String color, double score ) {

        Connection connection = new Connection(color);
        // TODO : Set the connection here.
        //double score =  cluster2.getScore() + Score5.score(cluster1, cluster2);
        //Connection connection = new Connection(cluster1, cluster 2, color);

        connection.setScore(score);

        this.iClusterGraph.addVertex(cluster1);
        this.iClusterGraph.addVertex(cluster2);

        this.iClusterGraph.addEdge(cluster1, cluster2, connection);
        this.iClusterGraph.setEdgeWeight(connection,score );
    }

    // TODO (LS) Unit test
    protected String calculateConnection(IsotopicCluster cluster1, IsotopicCluster cluster2) {
        if (cluster1.isNotNull() && cluster1.getPeak(0).getMz() < this.minimum) {
            this.minimum = cluster1.getPeak(0).getMz();
        }

        // is start or is is null?
        // TODO (LS)
        if ( cluster1.getStatus() == START && cluster1.isNull() && cluster2.isNotNull()
                && cluster2.getPeak(0).getMz() == this.minimum) {
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

    // TODO unit test.
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
}
