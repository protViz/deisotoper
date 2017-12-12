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
    private final String BLACK = "black";
    private final String RED = "red";
    private final String START = "start";
    private final String END = "end";
    private double minimum = Double.MAX_VALUE;
    private DefaultDirectedWeightedGraph<IsotopicCluster, Connection> isotopicClusterGraph = new DefaultDirectedWeightedGraph<IsotopicCluster, Connection>(
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
        for (IsotopicCluster cluster1 : this.isotopicClusterGraph.vertexSet()) {
            int edgeCount = 0;
            for (IsotopicCluster cluster2 : this.isotopicClusterGraph.vertexSet()) {
                edgeCount += this.isotopicClusterGraph.getAllEdges(cluster1, cluster2).size();
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
        return isotopicClusterGraph;
    }

    public GraphPath<IsotopicCluster, Connection> bestPath(IsotopicCluster startCluster, IsotopicCluster endCluster) {
        KShortestPaths<IsotopicCluster, Connection> kPaths = new KShortestPaths<IsotopicCluster, Connection>(
                this.isotopicClusterGraph, 999999);

        List<GraphPath<IsotopicCluster, Connection>> paths = kPaths.getPaths(startCluster, endCluster);

        // Set<Double> weights = new HashSet<Double>();
        // for (GraphPath<IsotopicCluster, Connection> path : paths) {
        // weights.add(path.getWeight());
        // }
        // if (weights.size() == 1 && paths.size() != 1) {
        // }

        return paths.get(paths.size() - 1);
    }

    public String toDOTGraph() {
        StringBuilder stringBuilder = new StringBuilder();
        String lineSep = System.getProperty("line.separator");

        stringBuilder.append("digraph {").append(lineSep);
        stringBuilder.append("rankdir=LR;").append(lineSep);
        stringBuilder.append("node [shape=box];").append(lineSep);

        for (Connection connection : this.isotopicClusterGraph.edgeSet()) {
            if (this.isotopicClusterGraph.getEdgeSource(connection).isNotNull()
                    && this.isotopicClusterGraph.getEdgeTarget(connection).isNotNull()) {
                stringBuilder
                        .append("\"(" + this.isotopicClusterGraph.getEdgeSource(connection).getClusterID() + ") [ ");
                for (Peak peak : this.isotopicClusterGraph.getEdgeSource(connection).getIsotopicCluster()) {
                    stringBuilder.append(" (" + peak.getPeakID() + ") " + Math.round(peak.getMz() * 100d) / 100d + " ");
                }
                stringBuilder.append("] z:" + this.isotopicClusterGraph.getEdgeSource(connection).getCharge()
                        + "\" -> \"(" + this.isotopicClusterGraph.getEdgeTarget(connection).getClusterID() + ") [ ");
                for (Peak peak : this.isotopicClusterGraph.getEdgeTarget(connection).getIsotopicCluster()) {
                    stringBuilder.append(" (" + peak.getPeakID() + ") " + Math.round(peak.getMz() * 100d) / 100d + " ");
                }
                stringBuilder.append("] z:" + this.isotopicClusterGraph.getEdgeTarget(connection).getCharge() + "\"")
                        .append("[color=\"" + connection.getColor() + "\",label=\""
                                + Math.round(connection.getScore() * 10000d) / 10000d + "\",weight=\""
                                + connection.getScore() + "\"];")
                        .append(lineSep);
            } else if (this.isotopicClusterGraph.getEdgeSource(connection).isNull()
                    && this.isotopicClusterGraph.getEdgeTarget(connection).isNotNull()) {
                stringBuilder.append(this.isotopicClusterGraph.getEdgeSource(connection).getStatus());
                stringBuilder.append(
                        " -> \"(" + this.isotopicClusterGraph.getEdgeTarget(connection).getClusterID() + ") [ ");
                for (Peak peak : this.isotopicClusterGraph.getEdgeTarget(connection).getIsotopicCluster()) {
                    stringBuilder.append(" (" + peak.getPeakID() + ") " + Math.round(peak.getMz() * 100d) / 100d + " ");
                }
                stringBuilder.append("] z:" + this.isotopicClusterGraph.getEdgeTarget(connection).getCharge() + "\"")
                        .append("[color=\"" + connection.getColor() + "\",label=\""
                                + Math.round(connection.getScore() * 10000d) / 10000d + "\",weight=\""
                                + connection.getScore() + "\"];")
                        .append(lineSep);
            } else if (this.isotopicClusterGraph.getEdgeTarget(connection).isNull()
                    && this.isotopicClusterGraph.getEdgeSource(connection).isNotNull()) {
                stringBuilder
                        .append("\"(" + this.isotopicClusterGraph.getEdgeSource(connection).getClusterID() + ") [ ");
                for (Peak peak : this.isotopicClusterGraph.getEdgeSource(connection).getIsotopicCluster()) {
                    stringBuilder.append(" (" + peak.getPeakID() + ") " + Math.round(peak.getMz() * 100d) / 100d + " ");
                }
                stringBuilder
                        .append("] z:" + this.isotopicClusterGraph.getEdgeSource(connection).getCharge() + "\" -> "
                                + this.isotopicClusterGraph.getEdgeTarget(connection).getStatus())
                        .append("[color=\"" + connection.getColor() + "\",label=\""
                                + Math.round(connection.getScore() * 10000d) / 10000d + "\",weight=\""
                                + connection.getScore() + "\"];")
                        .append(lineSep);
            }
        }

        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    public void scoreIsotopicClusterGraph(double peptidMass, int chargeState, PeakList peakList, Configuration config) {
        Score score = new Score(peptidMass, chargeState, config);
        ScoreFive scoreFive = new ScoreFive(this.isotopicClusterGraph, config);

        for (Connection connection : this.isotopicClusterGraph.edgeSet()) {
            double scoreSum = 0;
            double additionalScore = 0;
            if (this.isotopicClusterGraph.getEdgeTarget(connection).isNotNull()) {
                for (Peak peakX : this.isotopicClusterGraph.getEdgeTarget(connection).getIsotopicCluster()) {
                    for (Peak peakY : peakList.getPeakList()) {
                        // if (peakX.getMz() > peakY.getMz()) {
                        // continue;
                        // }

                        double scoreResult = score.calculateAggregatedScore(peakX.getMz(), peakY.getMz(),
                                this.isotopicClusterGraph.getEdgeTarget(connection).getIsotopicCluster());

                        double scoreFiveResult = scoreFive.calculateFifthScore(connection);

                        additionalScore += 0.00001;

                        scoreSum += scoreResult + scoreFiveResult;
                    }
                }

                if (scoreSum == 0) {
                    scoreSum = additionalScore;
                }
                connection.setScore(scoreSum);
                this.isotopicClusterGraph.setEdgeWeight(connection, scoreSum);
            }
        }
    }

    public IsotopicCluster getStart() {
        for (IsotopicCluster cluster : this.isotopicClusterGraph.vertexSet()) {
            if (cluster.isNull() && cluster.getStatus() == START) {
                return cluster;
            }
        }
        return null;
    }

    public IsotopicCluster getEnd() {
        for (IsotopicCluster cluster : this.isotopicClusterGraph.vertexSet()) {
            if (cluster.isNull() && cluster.getStatus() == END) {
                return cluster;
            }
        }
        return null;
    }

    private void connectClusters(IsotopicCluster cluster1, IsotopicCluster cluster2, String color) {
        this.isotopicClusterGraph.addVertex(cluster1);
        this.isotopicClusterGraph.addVertex(cluster2);

        Connection connection = new Connection(color);
        this.isotopicClusterGraph.addEdge(cluster1, cluster2, connection);
    }

    private String calculateConnection(IsotopicCluster cluster1, IsotopicCluster cluster2) {
        if (cluster1.isNotNull()) {
            if (cluster1.getPeak(0).getMz() < this.minimum) {
                this.minimum = cluster1.getPeak(0).getMz();
            }
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
