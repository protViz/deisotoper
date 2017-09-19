package ch.fgcz.proteomics.fdbm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-19
 */

public class Connection {
    /**
     * Returns the color of the connection between cluster1 and cluster2. If the clusters are not connected it returns null;
     * 
     * @param cluster1
     * @param cluster2
     * @return color
     */
    public static String calculateConnection(IsotopicCluster cluster1, IsotopicCluster cluster2) {

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
