package ch.fgcz.proteomics.fdbm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-19
 */

public class Connection {
    public static void connect(cluster1, cluster2) {
        if (!containsNode(cluster1) || !containsNode(cluster2)) {
            throw new RuntimeException("Node does not exist");
        }

        String color = null;

        if (!cluster2.isStart()) {
            if (cluster2.getCluster().size() != 0) {
                if (cluster1.isStart() && cluster2.getCluster().get(0).getMz() == min) {
                    color = "black";
                    return cluster1.addEdge(cluster2, color, score);
                }
            }
        }

        if (cluster1.isStart() || cluster2.isStart()) {
            return false;
        }

        if (!cluster1.isEnd()) {
            if (cluster2.isEnd() && cluster1.getEdgeCount() == 0) {
                color = "black";
                return cluster1.addEdge(cluster2, color, score);
            }
        }

        if (cluster1.isEnd() || cluster2.isEnd()) {
            return false;
        }

        if (cluster1.getCluster().get(cluster1.getCluster().size() - 1).getMz() < cluster2.getCluster().get(0).getMz()) {
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
            return cluster1.addEdge(cluster2, color, score);
        }

        return false;
    }
}
