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
    private double min = Double.MAX_VALUE;
    private DefaultDirectedWeightedGraph<IsotopicCluster, Connection> isotopicclustergraph = new DefaultDirectedWeightedGraph<IsotopicCluster, Connection>(
	    Connection.class);

    public DefaultDirectedWeightedGraph<IsotopicCluster, Connection> getIsotopicclustergraph() {
	return isotopicclustergraph;
    }

    public void setIsotopicclustergraph(
	    DefaultDirectedWeightedGraph<IsotopicCluster, Connection> isotopicclustergraph) {
	this.isotopicclustergraph = isotopicclustergraph;
    }

    public GraphPath<IsotopicCluster, Connection> bestPath(IsotopicCluster source, IsotopicCluster sink) {
	KShortestPaths<IsotopicCluster, Connection> paths = new KShortestPaths<>(this.isotopicclustergraph, 999999);

	List<GraphPath<IsotopicCluster, Connection>> path = paths.getPaths(source, sink);

	Set<Double> weights = new HashSet<Double>();
	for (GraphPath<IsotopicCluster, Connection> p : path) {
	    weights.add(p.getWeight());
	}
	if (weights.size() == 1 && path.size() != 1) {
	    System.err.println(
		    "WARNING: All scores are the same, therefore there is no valid best path! Please check if your input mass spectrum is correct! This could have a minimal impact on the results.");
	}

	return path.get(path.size() - 1);
    }

    public String toDOTGraph() {
	StringBuilder sb = new StringBuilder();
	String linesep = System.getProperty("line.separator");

	sb.append("digraph {").append(linesep);
	sb.append("rankdir=LR;").append(linesep);
	sb.append("node [shape=box];").append(linesep);

	for (Connection e : this.isotopicclustergraph.edgeSet()) {
	    if (this.isotopicclustergraph.getEdgeSource(e).getIsotopicCluster() != null
		    && this.isotopicclustergraph.getEdgeTarget(e).getIsotopicCluster() != null) {
		sb.append("\"(" + this.isotopicclustergraph.getEdgeSource(e).getClusterID() + ") [ ");
		for (Peak x : this.isotopicclustergraph.getEdgeSource(e).getIsotopicCluster()) {
		    sb.append(" (" + x.getPeakID() + ") " + Math.round(x.getMz() * 100d) / 100d + " ");
		}
		sb.append("] z:" + this.isotopicclustergraph.getEdgeSource(e).getCharge() + "\" -> \"("
			+ this.isotopicclustergraph.getEdgeTarget(e).getClusterID() + ") [ ");
		for (Peak x : this.isotopicclustergraph.getEdgeTarget(e).getIsotopicCluster()) {
		    sb.append(" (" + x.getPeakID() + ") " + Math.round(x.getMz() * 100d) / 100d + " ");
		}
		sb.append("] z:" + this.isotopicclustergraph.getEdgeTarget(e).getCharge() + "\"")
			.append("[color=\"" + e.getColor() + "\",label=\"" + Math.round(e.getScore() * 10000d) / 10000d
				+ "\",weight=\"" + e.getScore() + "\"];")
			.append(linesep);
	    } else if (this.isotopicclustergraph.getEdgeSource(e).getIsotopicCluster() == null
		    && this.isotopicclustergraph.getEdgeTarget(e).getIsotopicCluster() != null) {
		sb.append(this.isotopicclustergraph.getEdgeSource(e).getStatus());
		sb.append(" -> \"(" + this.isotopicclustergraph.getEdgeTarget(e).getClusterID() + ") [ ");
		for (Peak x : this.isotopicclustergraph.getEdgeTarget(e).getIsotopicCluster()) {
		    sb.append(" (" + x.getPeakID() + ") " + Math.round(x.getMz() * 100d) / 100d + " ");
		}
		sb.append("] z:" + this.isotopicclustergraph.getEdgeTarget(e).getCharge() + "\"")
			.append("[color=\"" + e.getColor() + "\",label=\"" + Math.round(e.getScore() * 10000d) / 10000d
				+ "\",weight=\"" + e.getScore() + "\"];")
			.append(linesep);
	    } else if (this.isotopicclustergraph.getEdgeTarget(e).getIsotopicCluster() == null
		    && this.isotopicclustergraph.getEdgeSource(e).getIsotopicCluster() != null) {
		sb.append("\"(" + this.isotopicclustergraph.getEdgeSource(e).getClusterID() + ") [ ");
		for (Peak x : this.isotopicclustergraph.getEdgeSource(e).getIsotopicCluster()) {
		    sb.append(" (" + x.getPeakID() + ") " + Math.round(x.getMz() * 100d) / 100d + " ");
		}
		sb.append("] z:" + this.isotopicclustergraph.getEdgeSource(e).getCharge() + "\" -> "
			+ this.isotopicclustergraph.getEdgeTarget(e).getStatus())
			.append("[color=\"" + e.getColor() + "\",label=\"" + Math.round(e.getScore() * 10000d) / 10000d
				+ "\",weight=\"" + e.getScore() + "\"];")
			.append(linesep);
	    }
	}

	sb.append("}");

	return sb.toString();
    }

    public IsotopicClusterGraph(IsotopicSet is) {
	this.min = Double.MAX_VALUE;
	is.getIsotopicSet().add(new IsotopicCluster("start"));

	for (IsotopicCluster ic1 : is.getIsotopicSet()) {
	    for (IsotopicCluster ic2 : is.getIsotopicSet()) {
		String color = calculateConnection(ic1, ic2);

		// Start
		if (color != null && ic1.getIsotopicCluster() == null && ic2.getIsotopicCluster() != null) {
		    connectClusters(ic1, ic2, color);
		}

		// Other
		if (color != null && ic1.getIsotopicCluster() != null && ic2.getIsotopicCluster() != null) {
		    connectClusters(ic1, ic2, color);
		}
	    }
	}

	// End
	List<IsotopicCluster> list = new ArrayList<>();
	for (IsotopicCluster ic3 : this.isotopicclustergraph.vertexSet()) {
	    int edgecount = 0;
	    for (IsotopicCluster ic4 : this.isotopicclustergraph.vertexSet()) {
		edgecount += this.isotopicclustergraph.getAllEdges(ic3, ic4).size();
	    }

	    if (edgecount == 0) {
		list.add(ic3);
	    }
	}

	IsotopicCluster end = new IsotopicCluster("end");
	for (IsotopicCluster ic5 : list) {
	    connectClusters(ic5, end, "black");
	}
    }

    public void scoreIsotopicClusterGraph(double pepmass, int chargestate, double errorolerance, Peaklist peaklist,
	    Configuration config) {
	Score s = new Score(errorolerance, pepmass, chargestate, this.isotopicclustergraph, config);

	for (Connection con : this.isotopicclustergraph.edgeSet()) {
	    double sumscore = 0;
	    if (this.isotopicclustergraph.getEdgeTarget(con).getIsotopicCluster() != null) {
		for (Peak x : this.isotopicclustergraph.getEdgeTarget(con).getIsotopicCluster()) {
		    for (Peak y : peaklist.getPeaklist()) {
			if (x.getMz() > y.getMz()) {
			    continue;
			}

			double sres = s.calculateScore(x, y, this.isotopicclustergraph.getEdgeTarget(con), con);

			sumscore += sres;
		    }
		}
		con.setScore(sumscore);
		this.isotopicclustergraph.setEdgeWeight(con, sumscore);
	    }
	}
    }

    private void connectClusters(IsotopicCluster ic1, IsotopicCluster ic2, String color) {
	this.isotopicclustergraph.addVertex(ic1);
	this.isotopicclustergraph.addVertex(ic2);

	Connection connection = new Connection(color);
	this.isotopicclustergraph.addEdge(ic1, ic2, connection);
    }

    private String calculateConnection(IsotopicCluster ic1, IsotopicCluster ic2) {
	if (ic1.getIsotopicCluster() != null) {
	    if (ic1.getIsotopicCluster().get(0).getMz() < this.min) {
		this.min = ic1.getIsotopicCluster().get(0).getMz();
	    }
	}

	if (ic1.getStatus() == "start" && ic2.getIsotopicCluster() != null && ic1.getIsotopicCluster() == null
		&& ic2.getIsotopicCluster().get(0).getMz() == this.min) {
	    return "black";
	}

	if (ic1.getIsotopicCluster() == null || ic2.getIsotopicCluster() == null) {
	    return null;
	}

	if (ic1.getIsotopicCluster().get(ic1.getIsotopicCluster().size() - 1).getMz() < ic2.getIsotopicCluster().get(0)
		.getMz()) {
	    return "black";
	}

	if (ic1.getIsotopicCluster().get(0).getMz() < ic2.getIsotopicCluster().get(0).getMz()) {
	    if (ic1.getIsotopicCluster().size() == 2) {
		if (ic1.getIsotopicCluster().get(1).getMz() == ic2.getIsotopicCluster().get(0).getMz()) {
		    return "red";
		}
	    } else if (ic1.getIsotopicCluster().size() == 3) {
		if (ic1.getIsotopicCluster().get(1).getMz() == ic2.getIsotopicCluster().get(0).getMz()
			|| ic1.getIsotopicCluster().get(2).getMz() == ic2.getIsotopicCluster().get(0).getMz()) {
		    return "red";
		}
	    } else if (ic1.getIsotopicCluster().size() == 3) {
		if (ic1.getIsotopicCluster().get(1).getMz() == ic2.getIsotopicCluster().get(0).getMz()
			&& ic1.getIsotopicCluster().get(2).getMz() == ic2.getIsotopicCluster().get(1).getMz()) {
		    return "red";
		}
	    }
	}

	return null;
    }
}
