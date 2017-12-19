package ch.fgcz.proteomics.fbdm;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;

public class IsotopicSetGraphToDotGraph {
    private static final String LINESEP = "\n"; // System.lineSeparator(); usage of API documented as 1.7
    private static final String COLOR = "[color=\"";
    private static final String LABEL = "\",label=\"";
    private static final String WEIGHT = "\",weight=\"";

    public static String toDOTGraph(DefaultDirectedWeightedGraph<IsotopicCluster, Connection> iClusterGraph) {
        StringBuilder stringBuilder = new StringBuilder();
        String lineSep = System.getProperty("line.separator");

        stringBuilder.append("digraph {").append(lineSep);
        stringBuilder.append("rankdir=LR;").append(lineSep);
        stringBuilder.append("node [shape=box];").append(lineSep);

        for (Connection connection : iClusterGraph.edgeSet()) {
            if (iClusterGraph.getEdgeSource(connection).isNotNull()
                    && iClusterGraph.getEdgeTarget(connection).isNotNull()) {
                appendOtherVertex(stringBuilder, connection, iClusterGraph);
            } else if (iClusterGraph.getEdgeSource(connection).isNull()
                    && iClusterGraph.getEdgeTarget(connection).isNotNull()) {
                appendStartVertex(stringBuilder, connection, iClusterGraph);
            } else if (iClusterGraph.getEdgeTarget(connection).isNull()
                    && iClusterGraph.getEdgeSource(connection).isNotNull()) {
                appendEndVertex(stringBuilder, connection, iClusterGraph);
            }
        }

        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    // TODO (LS) some better name. Done
    private static void appendOtherVertex(StringBuilder stringBuilder, Connection connection,
            DefaultDirectedWeightedGraph<IsotopicCluster, Connection> iClusterGraph) {
        stringBuilder.append("\"(" + iClusterGraph.getEdgeSource(connection).getClusterID() + ") [ ");
        for (Peak peak : iClusterGraph.getEdgeSource(connection).getIsotopicCluster()) {
            stringBuilder.append(" (" + peak.getPeakID() + ") " + Math.round(peak.getMz() * 100d) / 100d + " ");
        }
        stringBuilder.append("] z:" + iClusterGraph.getEdgeSource(connection).getCharge() + "\" -> \"("
                + iClusterGraph.getEdgeTarget(connection).getClusterID() + ") [ ");
        for (Peak peak : iClusterGraph.getEdgeTarget(connection).getIsotopicCluster()) {
            stringBuilder.append(" (" + peak.getPeakID() + ") " + Math.round(peak.getMz() * 100d) / 100d + " ");
        }
        stringBuilder.append("] z:" + iClusterGraph.getEdgeTarget(connection).getCharge() + "\"")
                .append(COLOR + connection.getColor() + LABEL
                        + Math.round(iClusterGraph.getEdgeWeight(connection) * 10000d) / 10000d + WEIGHT
                        + iClusterGraph.getEdgeWeight(connection) + "\"];")
                .append(LINESEP);
    }

    // TODO (LS) some better name. Done
    private static void appendStartVertex(StringBuilder stringBuilder, Connection connection,
            DefaultDirectedWeightedGraph<IsotopicCluster, Connection> iClusterGraph) {
        stringBuilder.append(iClusterGraph.getEdgeSource(connection).getStatus());
        stringBuilder.append(" -> \"(" + iClusterGraph.getEdgeTarget(connection).getClusterID() + ") [ ");
        for (Peak peak : iClusterGraph.getEdgeTarget(connection).getIsotopicCluster()) {
            stringBuilder.append(" (" + peak.getPeakID() + ") " + Math.round(peak.getMz() * 100d) / 100d + " ");
        }
        stringBuilder.append("] z:" + iClusterGraph.getEdgeTarget(connection).getCharge() + "\"")
                .append(COLOR + connection.getColor() + LABEL
                        + Math.round(iClusterGraph.getEdgeWeight(connection) * 10000d) / 10000d + WEIGHT
                        + iClusterGraph.getEdgeWeight(connection) + "\"];")
                .append(LINESEP);
    }

    // TODO (LS) some better name. Done
    private static void appendEndVertex(StringBuilder stringBuilder, Connection connection,
            DefaultDirectedWeightedGraph<IsotopicCluster, Connection> iClusterGraph) {
        stringBuilder.append("\"(" + iClusterGraph.getEdgeSource(connection).getClusterID() + ") [ ");
        for (Peak peak : iClusterGraph.getEdgeSource(connection).getIsotopicCluster()) {
            stringBuilder.append(" (" + peak.getPeakID() + ") " + Math.round(peak.getMz() * 100d) / 100d + " ");
        }
        stringBuilder
                .append("] z:" + iClusterGraph.getEdgeSource(connection).getCharge() + "\" -> "
                        + iClusterGraph.getEdgeTarget(connection).getStatus())
                .append(COLOR + connection.getColor() + LABEL
                        + Math.round(iClusterGraph.getEdgeWeight(connection) * 10000d) / 10000d + WEIGHT
                        + iClusterGraph.getEdgeWeight(connection) + "\"];")
                .append(LINESEP);
    }

    private IsotopicSetGraphToDotGraph() {
        throw new IllegalStateException("Dot Graph class");
    }
}
