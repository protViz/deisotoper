package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-11-16
 */

import org.jgrapht.graph.DefaultDirectedWeightedGraph;

public class Score5 {
    // NOT FINISHED YET
    public static int fifthScoringFeature(Connection connection,
	    DefaultDirectedWeightedGraph<IsotopicCluster, Connection> isotopicclustergraph, Configuration config) {
	int F5 = 0;

	double threshold = 0.3;

	int i = 0;
	for (Peak p : isotopicclustergraph.getEdgeTarget(connection).getIsotopicCluster()) {
	    double T_MIN = (p.getMz() / config.getASP_MASS()) * p.getIntensity();
	    double T_MEAN = (p.getMz() / config.getAVE_UPDATED_MASS()) * p.getIntensity();
	    double T_MEAN_OVERLAP = 0;
	    if (isotopicclustergraph.getEdgeSource(connection).getIsotopicCluster() != null) {
		if (i < isotopicclustergraph.getEdgeSource(connection).getIsotopicCluster().size()) {
		    T_MEAN_OVERLAP = (isotopicclustergraph.getEdgeSource(connection).getIsotopicCluster().get(i).getMz()
			    / config.getAVE_UPDATED_MASS()) * p.getIntensity();
		} else {
		    T_MEAN_OVERLAP = (isotopicclustergraph.getEdgeSource(connection).getIsotopicCluster()
			    .get(isotopicclustergraph.getEdgeSource(connection).getIsotopicCluster().size() - 1).getMz()
			    / config.getAVE_UPDATED_MASS()) * p.getIntensity();
		}
	    }
	    double T_MAX = (p.getMz() / config.getPHE_MASS()) * p.getIntensity();

	    if (connection.getColor() == "black") {
		if (Math.min(Math.abs(p.getIntensity() - T_MIN), Math.abs(p.getIntensity() - T_MAX))
			/ T_MEAN <= threshold) {
		    F5++;
		}
	    }

	    if (connection.getColor() == "red") {
		if (Math.min(Math.abs((p.getIntensity() - T_MEAN_OVERLAP) - T_MIN),
			Math.abs((p.getIntensity() - T_MEAN_OVERLAP) - T_MAX)) / T_MEAN <= threshold) {
		    F5++;
		}
	    }
	    i++;
	}

	return F5;
    }
}
