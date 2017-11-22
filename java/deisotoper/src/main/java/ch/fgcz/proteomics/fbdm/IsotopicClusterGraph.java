package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-20
 */

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IsotopicClusterGraph {
    private double minimum = Double.MAX_VALUE;
    private DefaultDirectedWeightedGraph<IsotopicCluster, Connection> isotopicclustergraph = new DefaultDirectedWeightedGraph<IsotopicCluster, Connection>(
            Connection.class);

    public DefaultDirectedWeightedGraph<IsotopicCluster, Connection> getIsotopicclustergraph() {
        return isotopicclustergraph;
    }

    public void setIsotopicclustergraph(
            DefaultDirectedWeightedGraph<IsotopicCluster, Connection> isotopicclustergraph) {
        this.isotopicclustergraph = isotopicclustergraph;
    }

    public GraphPath<IsotopicCluster, Connection> bestPath(IsotopicCluster startcluster, IsotopicCluster endcluster) {
        KShortestPaths<IsotopicCluster, Connection> allpaths = new KShortestPaths<>(this.isotopicclustergraph, 999999);

        List<GraphPath<IsotopicCluster, Connection>> paths = allpaths.getPaths(startcluster, endcluster);

        Set<Double> weights = new HashSet<Double>();
        for (GraphPath<IsotopicCluster, Connection> path : paths) {
            weights.add(path.getWeight());
        }
        if (weights.size() == 1 && paths.size() != 1) {
            System.err.println(
                    "WARNING: All scores are the same, therefore there is no valid best path! Please check if your input mass spectrum is correct! This could have a minimal impact on the results.");
        }

        return paths.get(paths.size() - 1);
    }

    public String toDOTGraph() {
        StringBuilder stringbuilder = new StringBuilder();
        String linesep = System.getProperty("line.separator");

        stringbuilder.append("digraph {").append(linesep);
        stringbuilder.append("rankdir=LR;").append(linesep);
        stringbuilder.append("node [shape=box];").append(linesep);

        for (Connection connection : this.isotopicclustergraph.edgeSet()) {
            if (this.isotopicclustergraph.getEdgeSource(connection).getIsotopicCluster() != null
                    && this.isotopicclustergraph.getEdgeTarget(connection).getIsotopicCluster() != null) {
                stringbuilder
                        .append("\"(" + this.isotopicclustergraph.getEdgeSource(connection).getClusterID() + ") [ ");
                for (Peak peak : this.isotopicclustergraph.getEdgeSource(connection).getIsotopicCluster()) {
                    stringbuilder.append(" (" + peak.getPeakID() + ") " + Math.round(peak.getMz() * 100d) / 100d + " ");
                }
                stringbuilder.append("] z:" + this.isotopicclustergraph.getEdgeSource(connection).getCharge()
                        + "\" -> \"(" + this.isotopicclustergraph.getEdgeTarget(connection).getClusterID() + ") [ ");
                for (Peak peak : this.isotopicclustergraph.getEdgeTarget(connection).getIsotopicCluster()) {
                    stringbuilder.append(" (" + peak.getPeakID() + ") " + Math.round(peak.getMz() * 100d) / 100d + " ");
                }
                stringbuilder.append("] z:" + this.isotopicclustergraph.getEdgeTarget(connection).getCharge() + "\"")
                        .append("[color=\"" + connection.getColor() + "\",label=\""
                                + Math.round(connection.getScore() * 10000d) / 10000d + "\",weight=\""
                                + connection.getScore() + "\"];")
                        .append(linesep);
            } else if (this.isotopicclustergraph.getEdgeSource(connection).getIsotopicCluster() == null
                    && this.isotopicclustergraph.getEdgeTarget(connection).getIsotopicCluster() != null) {
                stringbuilder.append(this.isotopicclustergraph.getEdgeSource(connection).getStatus());
                stringbuilder.append(
                        " -> \"(" + this.isotopicclustergraph.getEdgeTarget(connection).getClusterID() + ") [ ");
                for (Peak peak : this.isotopicclustergraph.getEdgeTarget(connection).getIsotopicCluster()) {
                    stringbuilder.append(" (" + peak.getPeakID() + ") " + Math.round(peak.getMz() * 100d) / 100d + " ");
                }
                stringbuilder.append("] z:" + this.isotopicclustergraph.getEdgeTarget(connection).getCharge() + "\"")
                        .append("[color=\"" + connection.getColor() + "\",label=\""
                                + Math.round(connection.getScore() * 10000d) / 10000d + "\",weight=\""
                                + connection.getScore() + "\"];")
                        .append(linesep);
            } else if (this.isotopicclustergraph.getEdgeTarget(connection).getIsotopicCluster() == null
                    && this.isotopicclustergraph.getEdgeSource(connection).getIsotopicCluster() != null) {
                stringbuilder
                        .append("\"(" + this.isotopicclustergraph.getEdgeSource(connection).getClusterID() + ") [ ");
                for (Peak peak : this.isotopicclustergraph.getEdgeSource(connection).getIsotopicCluster()) {
                    stringbuilder.append(" (" + peak.getPeakID() + ") " + Math.round(peak.getMz() * 100d) / 100d + " ");
                }
                stringbuilder
                        .append("] z:" + this.isotopicclustergraph.getEdgeSource(connection).getCharge() + "\" -> "
                                + this.isotopicclustergraph.getEdgeTarget(connection).getStatus())
                        .append("[color=\"" + connection.getColor() + "\",label=\""
                                + Math.round(connection.getScore() * 10000d) / 10000d + "\",weight=\""
                                + connection.getScore() + "\"];")
                        .append(linesep);
            }
        }

        stringbuilder.append("}");

        return stringbuilder.toString();
    }

    public IsotopicClusterGraph(List<IsotopicCluster> isotopicset) {
        this.minimum = Double.MAX_VALUE;
        isotopicset.add(new IsotopicCluster("start"));

        for (IsotopicCluster isotopiccluster1 : isotopicset) {
            for (IsotopicCluster isotopiccluster2 : isotopicset) {
                String color = calculateConnection(isotopiccluster1, isotopiccluster2);

                // Start
                if (color != null && isotopiccluster1.getIsotopicCluster() == null
                        && isotopiccluster2.getIsotopicCluster() != null) {
                    connectClusters(isotopiccluster1, isotopiccluster2, color);
                }

                // Other
                if (color != null && isotopiccluster1.getIsotopicCluster() != null
                        && isotopiccluster2.getIsotopicCluster() != null) {
                    connectClusters(isotopiccluster1, isotopiccluster2, color);
                }
            }
        }

        // End
        List<IsotopicCluster> isotopicclusters = new ArrayList<>();
        for (IsotopicCluster isotopiccluster1 : this.isotopicclustergraph.vertexSet()) {
            int edgecount = 0;
            for (IsotopicCluster isotopiccluster2 : this.isotopicclustergraph.vertexSet()) {
                edgecount += this.isotopicclustergraph.getAllEdges(isotopiccluster1, isotopiccluster2).size();
            }

            if (edgecount == 0) {
                isotopicclusters.add(isotopiccluster1);
            }
        }

        IsotopicCluster endcluster = new IsotopicCluster("end");
        for (IsotopicCluster isotopiccluster : isotopicclusters) {
            connectClusters(isotopiccluster, endcluster, "black");
        }
    }

    public void scoreIsotopicClusterGraph(double pepmass, int chargestate, double errorolerance, Peaklist peaklist,
            Configuration config) {
        Score score = new Score(errorolerance, pepmass, chargestate, this.isotopicclustergraph, config);

        for (Connection connection : this.isotopicclustergraph.edgeSet()) {
            double scoresum = 0;
            if (this.isotopicclustergraph.getEdgeTarget(connection).getIsotopicCluster() != null) {
                for (Peak peakx : this.isotopicclustergraph.getEdgeTarget(connection).getIsotopicCluster()) {
                    for (Peak peaky : peaklist.getPeaklist()) {
                        if (peakx.getMz() > peaky.getMz()) {
                            continue;
                        }

                        double sres = score.calculateScore(peakx, peaky,
                                this.isotopicclustergraph.getEdgeTarget(connection), connection);

                        scoresum += sres;
                    }
                }
                connection.setScore(scoresum);
                this.isotopicclustergraph.setEdgeWeight(connection, scoresum);
            }
        }
    }

    public IsotopicCluster getStart() {
        for (IsotopicCluster cluster : this.isotopicclustergraph.vertexSet()) {
            if (cluster.getIsotopicCluster() == null && cluster.getStatus() == "start") {
                return cluster;
            }
        }
        return null;
    }

    public IsotopicCluster getEnd() {
        for (IsotopicCluster cluster : this.isotopicclustergraph.vertexSet()) {
            if (cluster.getIsotopicCluster() == null && cluster.getStatus() == "end") {
                return cluster;
            }
        }
        return null;
    }

    private void connectClusters(IsotopicCluster isotopiccluster1, IsotopicCluster isotopiccluster2, String color) {
        this.isotopicclustergraph.addVertex(isotopiccluster1);
        this.isotopicclustergraph.addVertex(isotopiccluster2);

        Connection connection = new Connection(color);
        this.isotopicclustergraph.addEdge(isotopiccluster1, isotopiccluster2, connection);
    }

    private String calculateConnection(IsotopicCluster isotopiccluster1, IsotopicCluster isotopiccluster2) {
        if (isotopiccluster1.getIsotopicCluster() != null) {
            if (isotopiccluster1.getIsotopicCluster().get(0).getMz() < this.minimum) {
                this.minimum = isotopiccluster1.getIsotopicCluster().get(0).getMz();
            }
        }

        if (isotopiccluster1.getStatus() == "start" && isotopiccluster2.getIsotopicCluster() != null
                && isotopiccluster1.getIsotopicCluster() == null
                && isotopiccluster2.getIsotopicCluster().get(0).getMz() == this.minimum) {
            return "black";
        }

        if (isotopiccluster1.getIsotopicCluster() == null || isotopiccluster2.getIsotopicCluster() == null) {
            return null;
        }

        if (isotopiccluster1.getIsotopicCluster().get(isotopiccluster1.getIsotopicCluster().size() - 1)
                .getMz() < isotopiccluster2.getIsotopicCluster().get(0).getMz()) {
            return "black";
        }

        if (isotopiccluster1.getIsotopicCluster().get(0).getMz() < isotopiccluster2.getIsotopicCluster().get(0)
                .getMz()) {
            if (isotopiccluster1.getIsotopicCluster().size() == 2) {
                if (isotopiccluster1.getIsotopicCluster().get(1).getMz() == isotopiccluster2.getIsotopicCluster().get(0)
                        .getMz()) {
                    return "red";
                }
            } else if (isotopiccluster1.getIsotopicCluster().size() == 3) {
                if (isotopiccluster1.getIsotopicCluster().get(1).getMz() == isotopiccluster2.getIsotopicCluster().get(0)
                        .getMz()
                        || isotopiccluster1.getIsotopicCluster().get(2).getMz() == isotopiccluster2.getIsotopicCluster()
                                .get(0).getMz()) {
                    return "red";
                }
            } else if (isotopiccluster1.getIsotopicCluster().size() == 3) {
                if (isotopiccluster1.getIsotopicCluster().get(1).getMz() == isotopiccluster2.getIsotopicCluster().get(0)
                        .getMz()
                        && isotopiccluster1.getIsotopicCluster().get(2).getMz() == isotopiccluster2.getIsotopicCluster()
                                .get(1).getMz()) {
                    return "red";
                }
            }
        }

        return null;
    }
}
