package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-11-16
 */

import org.jgrapht.graph.DefaultDirectedWeightedGraph;

// TODO: Needs to be finished and included.
public class ScoreFive {
    private DefaultDirectedWeightedGraph<IsotopicCluster, Connection> isotopicClusterGraph;
    private Configuration config;

    public ScoreFive(DefaultDirectedWeightedGraph<IsotopicCluster, Connection> isotopicClusterGraph,
            Configuration config) {
        this.isotopicClusterGraph = isotopicClusterGraph;
        this.config = config;
    }

    public double calculateFifthScore(Connection connection) {
        return config.getF(5) * calculateScoreBasedOnExperimentalIsotopicDistribution(connection,
                this.isotopicClusterGraph, this.config);
    }

    // NOT FINISHED YET
    public static int calculateScoreBasedOnExperimentalIsotopicDistribution(Connection connection,
            DefaultDirectedWeightedGraph<IsotopicCluster, Connection> isotopicClusterGraph, Configuration config) {
        int f5 = 0;
        double threshold = 0.3;

        int i = 0;
        for (Peak peak : isotopicClusterGraph.getEdgeTarget(connection).getIsotopicCluster()) {
            double tMin = (peak.getMz() / config.getAspMass()) * peak.getIntensity();
            double tMean = (peak.getMz() / config.getAveUpdatedMass()) * peak.getIntensity();
            double tMeanOverlap = calculateTMeanOverlap(isotopicClusterGraph, connection, i, peak, config);

            double tMax = (peak.getMz() / config.getPheMass()) * peak.getIntensity();
            if (connection.getColor() == "black"
                    && (Math.min(Math.abs(peak.getIntensity() - tMin), Math.abs(peak.getIntensity() - tMax))
                            / tMean <= threshold)) {
                f5++;
            }

            if (connection.getColor() == "red" && (Math.min(Math.abs((peak.getIntensity() - tMeanOverlap) - tMin),
                    Math.abs((peak.getIntensity() - tMeanOverlap) - tMax)) / tMean <= threshold)) {
                f5++;
            }
            i++;
        }

        return f5;
    }

    private static double calculateTMeanOverlap(
            DefaultDirectedWeightedGraph<IsotopicCluster, Connection> isotopicClusterGraph, Connection connection,
            int i, Peak peak, Configuration config) {
        double tMeanOverlap = 0;
        if (isotopicClusterGraph.getEdgeSource(connection).isNotNull()) {
            if (i < isotopicClusterGraph.getEdgeSource(connection).size()) {
                tMeanOverlap = (isotopicClusterGraph.getEdgeSource(connection).getPeak(i).getMz()
                        / config.getAveUpdatedMass()) * peak.getIntensity();
            } else {
                tMeanOverlap = (isotopicClusterGraph.getEdgeSource(connection)
                        .getPeak(isotopicClusterGraph.getEdgeSource(connection).size() - 1).getMz()
                        / config.getAveUpdatedMass()) * peak.getIntensity();
            }
        }

        return tMeanOverlap;
    }
}
