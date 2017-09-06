
/**
 * @author Lucas Schmidt
 * @since 2017-09-06
 */

import java.util.List;

public class IsotopicClustersGraph {

    public static Node createNode(List<Peak> cluster) {
        return new Node(cluster);
    }

    public static Edge createEdge(Node cluster1, Node cluster2) {
        String color = null;

        if (cluster1.start == true || cluster2.start == true) { // Start and End Cluster
            color = "black";
        } else if (cluster1.getCluster().get(cluster1.getCluster().size() - 1).getMz() < cluster2.getCluster().get(0).getMz()) {
            color = "black";
        }

        if (cluster1.getCluster().get(0).getMz() < cluster2.getCluster().get(0).getMz()) {
            if (cluster1.getCluster().size() == 2) {
                if (cluster1.getCluster().get(1).getMz() == cluster2.getCluster().get(0).getMz()) {
                    color = "red";
                }
            } else if (cluster1.getCluster().size() == 3) {
                if (cluster1.getCluster().get(1).getMz() == cluster2.getCluster().get(0).getMz() || cluster1.getCluster().get(2).getMz() == cluster2.getCluster().get(0).getMz()) {
                    color = "red";
                }
            } else if (cluster1.getCluster().size() == 3) {
                if (cluster1.getCluster().get(1).getMz() == cluster2.getCluster().get(0).getMz() && cluster1.getCluster().get(2).getMz() == cluster2.getCluster().get(1).getMz()) {
                    color = "red";
                }
            }
        }

        if (color == "red" || color == "black") {
            return new Edge(cluster1, cluster2, color);
        }

        return null;
    }
}
