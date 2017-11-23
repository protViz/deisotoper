package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-11-16
 */

import org.jgrapht.graph.DefaultDirectedWeightedGraph;

public class Score5 {
    // NOT FINISHED YET
    protected static int calculateFifthScoringFeature(Connection connection,
            DefaultDirectedWeightedGraph<IsotopicCluster, Connection> isotopicClusterGraph, Configuration config) {
        int F5 = 0;

        double threshold = 0.3;

        int i = 0;
        for (Peak p : isotopicClusterGraph.getEdgeTarget(connection).getIsotopicCluster()) {
            double tMin = (p.getMz() / config.getASP_MASS()) * p.getIntensity();
            double tMean = (p.getMz() / config.getAVE_UPDATED_MASS()) * p.getIntensity();
            double tMeanOverlap = 0;
            if (isotopicClusterGraph.getEdgeSource(connection).getIsotopicCluster() != null) {
                if (i < isotopicClusterGraph.getEdgeSource(connection).getIsotopicCluster().size()) {
                    tMeanOverlap = (isotopicClusterGraph.getEdgeSource(connection).getIsotopicCluster().get(i).getMz()
                            / config.getAVE_UPDATED_MASS()) * p.getIntensity();
                } else {
                    tMeanOverlap = (isotopicClusterGraph.getEdgeSource(connection).getIsotopicCluster()
                            .get(isotopicClusterGraph.getEdgeSource(connection).getIsotopicCluster().size() - 1).getMz()
                            / config.getAVE_UPDATED_MASS()) * p.getIntensity();
                }
            }

            double tMax = (p.getMz() / config.getPHE_MASS()) * p.getIntensity();
            if (connection.getColor() == "black") {
                if (Math.min(Math.abs(p.getIntensity() - tMin), Math.abs(p.getIntensity() - tMax))
                        / tMean <= threshold) {
                    F5++;
                }
            }

            if (connection.getColor() == "red") {
                if (Math.min(Math.abs((p.getIntensity() - tMeanOverlap) - tMin),
                        Math.abs((p.getIntensity() - tMeanOverlap) - tMax)) / tMean <= threshold) {
                    F5++;
                }
            }
            i++;
        }

        return F5;
    }
}
