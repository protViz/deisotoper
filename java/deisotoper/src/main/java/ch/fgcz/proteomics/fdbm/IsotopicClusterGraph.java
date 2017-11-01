package ch.fgcz.proteomics.fdbm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-20
 */

import org.jgrapht.GraphPath;
import org.jgrapht.alg.KShortestPaths;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class IsotopicClusterGraph {
    private double min = Double.MAX_VALUE;
    private DefaultDirectedWeightedGraph<IsotopicCluster, Connection> isotopicclustergraph = new DefaultDirectedWeightedGraph<IsotopicCluster, Connection>(Connection.class);

    public DefaultDirectedWeightedGraph<IsotopicCluster, Connection> getIsotopicclustergraph() {
        return isotopicclustergraph;
    }

    public void setIsotopicclustergraph(DefaultDirectedWeightedGraph<IsotopicCluster, Connection> isotopicclustergraph) {
        this.isotopicclustergraph = isotopicclustergraph;
    }

    /**
     * @param source
     * @param sink
     * @param icg
     * @return best path of the IsotopicClusterGraph
     */
    public GraphPath<IsotopicCluster, Connection> bestPath(IsotopicCluster source, IsotopicCluster sink) {
        KShortestPaths<IsotopicCluster, Connection> paths = new KShortestPaths<IsotopicCluster, Connection>(this.isotopicclustergraph, source, 1000000);

        List<GraphPath<IsotopicCluster, Connection>> p = paths.getPaths(sink);

        return p.get(p.size() - 1);
    }

    public String createDOTIsotopicClusterGraph() {
        StringBuilder sb = new StringBuilder();
        String linesep = System.getProperty("line.separator");

        sb.append("digraph {").append(linesep);
        sb.append("rankdir=LR;").append(linesep);
        sb.append("node [shape=box];").append(linesep);

        for (Connection e : this.isotopicclustergraph.edgeSet()) {
            if (this.isotopicclustergraph.getEdgeSource(e).getIsotopicCluster() != null && this.isotopicclustergraph.getEdgeTarget(e).getIsotopicCluster() != null) {
                sb.append("\"(" + this.isotopicclustergraph.getEdgeSource(e).getClusterID() + ") [ ");
                for (Peak x : this.isotopicclustergraph.getEdgeSource(e).getIsotopicCluster()) {
                    sb.append(" (" + x.getPeakID() + ") " + Math.round(x.getMz() * 100d) / 100d + " ");
                }
                sb.append("] z:" + this.isotopicclustergraph.getEdgeSource(e).getCharge() + "\" -> \"(" + this.isotopicclustergraph.getEdgeTarget(e).getClusterID() + ") [ ");
                for (Peak x : this.isotopicclustergraph.getEdgeTarget(e).getIsotopicCluster()) {
                    sb.append(" (" + x.getPeakID() + ") " + Math.round(x.getMz() * 100d) / 100d + " ");
                }
                sb.append("] z:" + this.isotopicclustergraph.getEdgeTarget(e).getCharge() + "\"")
                        .append("[color=\"" + e.getColor() + "\",label=\"" + Math.round(e.getScore() * 10000d) / 10000d + "\",weight=\"" + e.getScore() + "\"];").append(linesep);
            } else if (this.isotopicclustergraph.getEdgeSource(e).getIsotopicCluster() == null && this.isotopicclustergraph.getEdgeTarget(e).getIsotopicCluster() != null) {
                sb.append(this.isotopicclustergraph.getEdgeSource(e).getStatus());
                sb.append(" -> \"(" + this.isotopicclustergraph.getEdgeTarget(e).getClusterID() + ") [ ");
                for (Peak x : this.isotopicclustergraph.getEdgeTarget(e).getIsotopicCluster()) {
                    sb.append(" (" + x.getPeakID() + ") " + Math.round(x.getMz() * 100d) / 100d + " ");
                }
                sb.append("] z:" + this.isotopicclustergraph.getEdgeTarget(e).getCharge() + "\"")
                        .append("[color=\"" + e.getColor() + "\",label=\"" + Math.round(e.getScore() * 10000d) / 10000d + "\",weight=\"" + e.getScore() + "\"];").append(linesep);
            } else if (this.isotopicclustergraph.getEdgeTarget(e).getIsotopicCluster() == null && this.isotopicclustergraph.getEdgeSource(e).getIsotopicCluster() != null) {
                sb.append("\"(" + this.isotopicclustergraph.getEdgeSource(e).getClusterID() + ") [ ");
                for (Peak x : this.isotopicclustergraph.getEdgeSource(e).getIsotopicCluster()) {
                    sb.append(" (" + x.getPeakID() + ") " + Math.round(x.getMz() * 100d) / 100d + " ");
                }
                sb.append("] z:" + this.isotopicclustergraph.getEdgeSource(e).getCharge() + "\" -> " + this.isotopicclustergraph.getEdgeTarget(e).getStatus())
                        .append("[color=\"" + e.getColor() + "\",label=\"" + Math.round(e.getScore() * 10000d) / 10000d + "\",weight=\"" + e.getScore() + "\"];").append(linesep);
            }
        }

        sb.append("}");

        return sb.toString();
    }

    /**
     * @param is
     */
    public IsotopicClusterGraph(IsotopicSet is) {
        this.min = Double.MAX_VALUE;
        is.getIsotopicSet().add(new IsotopicCluster("start"));

        for (IsotopicCluster n : is.getIsotopicSet()) {
            for (IsotopicCluster m : is.getIsotopicSet()) {
                String color = calculateConnection(n, m);

                // Start
                if (color != null && n.getIsotopicCluster() == null && m.getIsotopicCluster() != null) {
                    connectClusters(n, m, color);
                }

                // Other
                if (color != null && n.getIsotopicCluster() != null && m.getIsotopicCluster() != null) {
                    connectClusters(n, m, color);
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
            connectClusters(e, new IsotopicCluster("end"), "black");
        }
    }

    /**
     * @param icg
     * @param pepmass
     * @param chargestate
     * @param error
     * @param peaklist
     */
    public void scoreIsotopicClusterGraph(double pepmass, int chargestate, double error, Peaklist peaklist, ScoreConfig config) {
        Score s = new Score(error, pepmass, chargestate, this.isotopicclustergraph, config);

        for (Connection e : this.isotopicclustergraph.edgeSet()) {
            double sumscore = 0;
            if (this.isotopicclustergraph.getEdgeTarget(e).getIsotopicCluster() != null) {
                for (Peak x : this.isotopicclustergraph.getEdgeTarget(e).getIsotopicCluster()) {
                    for (Peak y : peaklist.getPeaklist()) {
                        if (x.getMz() > y.getMz()) {
                            continue;
                        }

                        double sres = s.calculateScore(x, y, this.isotopicclustergraph.getEdgeTarget(e), e);

                        sumscore += sres;
                    }
                }
                e.setScore(sumscore);
                this.isotopicclustergraph.setEdgeWeight(e, sumscore);
            }
        }
    }

    /**
     * @param cluster1
     * @param cluster2
     * @param graph
     * @param color
     * @return
     */
    private void connectClusters(IsotopicCluster cluster1, IsotopicCluster cluster2, String color) {
        this.isotopicclustergraph.addVertex(cluster1);
        this.isotopicclustergraph.addVertex(cluster2);

        Connection connection = new Connection(color);
        this.isotopicclustergraph.addEdge(cluster1, cluster2, connection);
    }

    /**
     * Returns the color of the connection between cluster1 and cluster2. If the clusters are not connected it returns null;
     * 
     * @param cluster1
     * @param cluster2
     * @return color
     */
    private String calculateConnection(IsotopicCluster cluster1, IsotopicCluster cluster2) {
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
