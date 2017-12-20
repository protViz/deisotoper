package ch.fgcz.proteomics.fbdm;

// TODO: Needs to be finished and included.
public class ScoreFive {
    private Configuration config;

    public ScoreFive(Configuration config) {
        this.config = config;
    }

    public double calculateFifthScore(Connection connection, IsotopicCluster sourceCluster,
            IsotopicCluster targetCluster) {
        return config.getF(5) * calculateScoreBasedOnExperimentalIsotopicDistribution(connection, sourceCluster,
                targetCluster, this.config);
    }

    // NOT FINISHED YET
    // TODO call it with sourceCluster and targetCluster, connection and config.
    public static int calculateScoreBasedOnExperimentalIsotopicDistribution(Connection connection,
            IsotopicCluster sourceCluster, IsotopicCluster targetCluster, Configuration config) {
        int f5 = 0;
        double threshold = 0.3;

        int i = 0;
        if (targetCluster.isNotNull()) {
            for (Peak peak : targetCluster.getIsotopicCluster()) {
                double tMin = (peak.getMz() / config.getAspMass()) * peak.getIntensity();
                double tMean = (peak.getMz() / config.getAveUpdatedMass()) * peak.getIntensity();
                double tMeanOverlap = calculateTMeanOverlap(sourceCluster, i, peak, config);

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
        }

        return f5;
    }

    private static double calculateTMeanOverlap(IsotopicCluster sourceCluster, int i, Peak peak, Configuration config) {
        double tMeanOverlap = 0;
        if (sourceCluster.isNotNull()) {
            if (i < sourceCluster.size()) {
                tMeanOverlap = (sourceCluster.getPeak(i).getMz() / config.getAveUpdatedMass()) * peak.getIntensity();
            } else {
                tMeanOverlap = (sourceCluster.getPeak(sourceCluster.size() - 1).getMz() / config.getAveUpdatedMass())
                        * peak.getIntensity();
            }
        }

        return tMeanOverlap;
    }
}
