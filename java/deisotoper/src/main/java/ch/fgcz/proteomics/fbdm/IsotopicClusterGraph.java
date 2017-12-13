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

public class IsotopicClusterGraph {
    private static final String LINESEP = System.lineSeparator();
    private static final String COLOR = "[color=\"";
    private static final String LABEL = "\",label=\"";
    private static final String WEIGHT = "\",weight=\"";
    private static final String BLACK = "black";
    private static final String RED = "red";
    private static final String START = "start";
    private static final String END = "end";
    private double minimum = Double.MAX_VALUE;
    private DefaultDirectedWeightedGraph<IsotopicCluster, Connection> iClusterGraph = new DefaultDirectedWeightedGraph<IsotopicCluster, Connection>(
            Connection.class);

    public IsotopicClusterGraph(List<IsotopicCluster> isotopicSet) {
        List<IsotopicCluster> isotopicSet2 = new ArrayList<IsotopicCluster>(isotopicSet);

        this.minimum = Double.MAX_VALUE;
        isotopicSet2.add(new IsotopicCluster(START));

        assignStartAndOther(isotopicSet2);

        assignEnd();
    }

    private void assignStartAndOther(List<IsotopicCluster> isotopicSet) {
        for (IsotopicCluster cluster1 : isotopicSet) {
            for (IsotopicCluster cluster2 : isotopicSet) {
                String color = calculateConnection(cluster1, cluster2);

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
            connectClusters(cluster, endCluster, BLACK);
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

    public String toDOTGraph() {
        StringBuilder stringBuilder = new StringBuilder();
        String lineSep = System.getProperty("line.separator");

        stringBuilder.append("digraph {").append(lineSep);
        stringBuilder.append("rankdir=LR;").append(lineSep);
        stringBuilder.append("node [shape=box];").append(lineSep);

        for (Connection connection : this.iClusterGraph.edgeSet()) {
            if (this.iClusterGraph.getEdgeSource(connection).isNotNull()
                    && this.iClusterGraph.getEdgeTarget(connection).isNotNull()) {
                firstIfStatement(stringBuilder, connection);
            } else if (this.iClusterGraph.getEdgeSource(connection).isNull()
                    && this.iClusterGraph.getEdgeTarget(connection).isNotNull()) {
                secondIfStatement(stringBuilder, connection);
            } else if (this.iClusterGraph.getEdgeTarget(connection).isNull()
                    && this.iClusterGraph.getEdgeSource(connection).isNotNull()) {
                thirdIfStatement(stringBuilder, connection);
            }
        }

        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    private void firstIfStatement(StringBuilder stringBuilder, Connection connection) {
        stringBuilder.append("\"(" + this.iClusterGraph.getEdgeSource(connection).getClusterID() + ") [ ");
        for (Peak peak : this.iClusterGraph.getEdgeSource(connection).getIsotopicCluster()) {
            stringBuilder.append(" (" + peak.getPeakID() + ") " + Math.round(peak.getMz() * 100d) / 100d + " ");
        }
        stringBuilder.append("] z:" + this.iClusterGraph.getEdgeSource(connection).getCharge() + "\" -> \"("
                + this.iClusterGraph.getEdgeTarget(connection).getClusterID() + ") [ ");
        for (Peak peak : this.iClusterGraph.getEdgeTarget(connection).getIsotopicCluster()) {
            stringBuilder.append(" (" + peak.getPeakID() + ") " + Math.round(peak.getMz() * 100d) / 100d + " ");
        }
        stringBuilder.append("] z:" + this.iClusterGraph.getEdgeTarget(connection).getCharge() + "\"")
                .append(COLOR + connection.getColor() + LABEL + Math.round(connection.getScore() * 10000d) / 10000d
                        + WEIGHT + connection.getScore() + "\"];")
                .append(LINESEP);
    }

    private void secondIfStatement(StringBuilder stringBuilder, Connection connection) {
        stringBuilder.append(this.iClusterGraph.getEdgeSource(connection).getStatus());
        stringBuilder.append(" -> \"(" + this.iClusterGraph.getEdgeTarget(connection).getClusterID() + ") [ ");
        for (Peak peak : this.iClusterGraph.getEdgeTarget(connection).getIsotopicCluster()) {
            stringBuilder.append(" (" + peak.getPeakID() + ") " + Math.round(peak.getMz() * 100d) / 100d + " ");
        }
        stringBuilder.append("] z:" + this.iClusterGraph.getEdgeTarget(connection).getCharge() + "\"")
                .append(COLOR + connection.getColor() + LABEL + Math.round(connection.getScore() * 10000d) / 10000d
                        + WEIGHT + connection.getScore() + "\"];")
                .append(LINESEP);
    }

    private void thirdIfStatement(StringBuilder stringBuilder, Connection connection) {
        stringBuilder.append("\"(" + this.iClusterGraph.getEdgeSource(connection).getClusterID() + ") [ ");
        for (Peak peak : this.iClusterGraph.getEdgeSource(connection).getIsotopicCluster()) {
            stringBuilder.append(" (" + peak.getPeakID() + ") " + Math.round(peak.getMz() * 100d) / 100d + " ");
        }
        stringBuilder
                .append("] z:" + this.iClusterGraph.getEdgeSource(connection).getCharge() + "\" -> "
                        + this.iClusterGraph.getEdgeTarget(connection).getStatus())
                .append(COLOR + connection.getColor() + LABEL + Math.round(connection.getScore() * 10000d) / 10000d
                        + WEIGHT + connection.getScore() + "\"];")
                .append(LINESEP);
    }

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

    private void connectClusters(IsotopicCluster cluster1, IsotopicCluster cluster2, String color) {
        this.iClusterGraph.addVertex(cluster1);
        this.iClusterGraph.addVertex(cluster2);

        Connection connection = new Connection(color);
        this.iClusterGraph.addEdge(cluster1, cluster2, connection);
    }

    private String calculateConnection(IsotopicCluster cluster1, IsotopicCluster cluster2) {
        if (cluster1.isNotNull() && cluster1.getPeak(0).getMz() < this.minimum) {
            this.minimum = cluster1.getPeak(0).getMz();
        }

        if (cluster1.getStatus() == START && cluster2.isNotNull() && cluster1.isNull()
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
