package ch.fgcz.proteomics.fbdm;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;

public class IsotopicSetGraphToDotGraph {
    static final String LINESEP = "\n"; // System.lineSeparator(); usage of API documented as 1.7
    static final String COLOR = "[color=\"";
    static final String LABEL = "\",label=\"";
    static final String WEIGHT = "\",weight=\"";

    static public String toDOTGraph(DefaultDirectedWeightedGraph<IsotopicCluster, Connection> iClusterGraph ) {
        StringBuilder stringBuilder = new StringBuilder();
        String lineSep = System.getProperty("line.separator");

        stringBuilder.append("digraph {").append(lineSep);
        stringBuilder.append("rankdir=LR;").append(lineSep);
        stringBuilder.append("node [shape=box];").append(lineSep);

        for (Connection connection : iClusterGraph.edgeSet()) {
            if (iClusterGraph.getEdgeSource(connection).isNotNull()
                    && iClusterGraph.getEdgeTarget(connection).isNotNull()) {
                firstIfStatement(stringBuilder, connection, iClusterGraph);
            } else if (iClusterGraph.getEdgeSource(connection).isNull()
                    && iClusterGraph.getEdgeTarget(connection).isNotNull()) {
                secondIfStatement(stringBuilder, connection, iClusterGraph);
            } else if (iClusterGraph.getEdgeTarget(connection).isNull()
                    && iClusterGraph.getEdgeSource(connection).isNotNull()) {
                thirdIfStatement(stringBuilder, connection, iClusterGraph);
            }
        }

        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    // TODO (LS) some better name.
    static private void firstIfStatement(StringBuilder stringBuilder,
                                         Connection connection,
                                         DefaultDirectedWeightedGraph<IsotopicCluster, Connection> iClusterGraph  ) {
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
                .append(COLOR + connection.getColor() + LABEL + Math.round(connection.getScore() * 10000d) / 10000d
                        + WEIGHT + connection.getScore() + "\"];")
                .append(LINESEP);
    }

    // TODO (LS) some better name.
    static private void secondIfStatement(StringBuilder stringBuilder,
                                   Connection connection,
                                   DefaultDirectedWeightedGraph<IsotopicCluster, Connection> iClusterGraph) {
        stringBuilder.append(iClusterGraph.getEdgeSource(connection).getStatus());
        stringBuilder.append(" -> \"(" + iClusterGraph.getEdgeTarget(connection).getClusterID() + ") [ ");
        for (Peak peak : iClusterGraph.getEdgeTarget(connection).getIsotopicCluster()) {
            stringBuilder.append(" (" + peak.getPeakID() + ") " + Math.round(peak.getMz() * 100d) / 100d + " ");
        }
        stringBuilder.append("] z:" + iClusterGraph.getEdgeTarget(connection).getCharge() + "\"")
                .append(COLOR + connection.getColor() + LABEL + Math.round(connection.getScore() * 10000d) / 10000d
                        + WEIGHT + connection.getScore() + "\"];")
                .append(LINESEP);
    }

    // TODO (LS) some better name.
    static private void thirdIfStatement(StringBuilder stringBuilder, Connection connection,
                                  DefaultDirectedWeightedGraph<IsotopicCluster, Connection> iClusterGraph) {
        stringBuilder.append("\"(" + iClusterGraph.getEdgeSource(connection).getClusterID() + ") [ ");
        for (Peak peak : iClusterGraph.getEdgeSource(connection).getIsotopicCluster()) {
            stringBuilder.append(" (" + peak.getPeakID() + ") " + Math.round(peak.getMz() * 100d) / 100d + " ");
        }
        stringBuilder
                .append("] z:" + iClusterGraph.getEdgeSource(connection).getCharge() + "\" -> "
                        + iClusterGraph.getEdgeTarget(connection).getStatus())
                .append(COLOR + connection.getColor() + LABEL + Math.round(connection.getScore() * 10000d) / 10000d
                        + WEIGHT + connection.getScore() + "\"];")
                .append(LINESEP);
    }
}
