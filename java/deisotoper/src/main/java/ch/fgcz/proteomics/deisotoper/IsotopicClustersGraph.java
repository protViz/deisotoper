
/**
 * @author Lucas Schmidt
 * @since 2017-09-06
 * @see https://codereview.stackexchange.com/questions/67970/graph-implementation-in-java-8
 */

import java.util.ArrayList;
import java.util.List;

public class IsotopicClustersGraph {
    private final List<Node> adjacencylist;
    private double min;

    public List<Node> getAdjacencylist() {
        return adjacencylist;
    }

    public IsotopicClustersGraph() {
        adjacencylist = new ArrayList<>();
        addStart();
    }

    public boolean addStart() {
        List<Peak> cluster = new ArrayList<>();
        Node n = new Node(cluster);
        n.setStart(true);

        if (adjacencylist.contains(n)) {
            return false;
        }
        adjacencylist.add(n);
        return true;
    }

    public boolean addEnd() {
        for (Node m : adjacencylist) {
            if (m.getEdgeCount() == 0) {
                List<Peak> cluster = new ArrayList<>();
                Node n = new Node(cluster);
                n.setEnd(true);

                if (adjacencylist.contains(n)) {
                    return false;
                }
                adjacencylist.add(n);
                addEdge(m, n);
                return true;
            }
        }
        return false;
    }

    public boolean addNode(List<Peak> cluster) {
        if (adjacencylist.size() == 2) {
            min = adjacencylist.get(1).getCluster().get(0).getMz();
        }

        Node n = new Node(cluster);

        if (adjacencylist.contains(n)) {
            return false;
        }
        adjacencylist.add(n);
        return true;
    }

    public boolean addEdge(Node cluster1, Node cluster2) {
        return addEdge(cluster1, cluster2, 0);
    }

    public boolean addEdge(Node cluster1, Node cluster2, double score) {
        if (!containsNode(cluster1) || !containsNode(cluster2)) {
            throw new RuntimeException("Node does not exist");
        }

        String color = null;

        if (!cluster2.isStart()) {
            if (cluster1.isStart() && cluster2.getCluster().get(0).getMz() == min) {
                color = "black";
                return cluster1.addEdge(cluster2, color, score);
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

    public boolean removeNode(Node cluster) {
        if (!adjacencylist.contains(cluster)) {
            return false;
        }

        adjacencylist.forEach(node -> node.removeEdge(cluster));

        adjacencylist.remove(cluster);
        return true;
    }

    public boolean removeEdge(Node cluster1, Node cluster2) {
        if (!containsNode(cluster1) || !containsNode(cluster2)) {
            return false;
        }
        return cluster1.removeEdge(cluster2);
    }

    public boolean containsNode(Node cluster) {
        return adjacencylist.contains(cluster);
    }

    public boolean containsEdge(Node cluster1, Node cluster2) {
        if (!containsNode(cluster1) || !containsNode(cluster2)) {
            return false;
        }
        return cluster1.hasEdge(cluster2);
    }

    public int nodeCount() {
        return adjacencylist.size();
    }

    public int edgeCount() {
        return adjacencylist.stream().mapToInt(Node::getEdgeCount).sum();
    }

    public static String prettyPrint(IsotopicClustersGraph graph) {
        StringBuilder pretty = new StringBuilder();
        String linesep = System.getProperty("line.separator");
        for (Node n : graph.getAdjacencylist()) {
            pretty.append("NODE: ");
            pretty.append("[");
            for (Peak c : n.getCluster()) {
                pretty.append(" " + c.getMz() + " ");
            }
            pretty.append("]");
            pretty.append(linesep);
            pretty.append("EDGES OF THIS NODE:");
            pretty.append(linesep);
            for (Edge e : n.getEdges()) {
                pretty.append("[");
                for (Peak c : e.getHead().getCluster()) {
                    pretty.append(" " + c.getMz() + " ");
                }
                pretty.append("]");
                pretty.append(" -" + e.getColor() + "- ");
                pretty.append("[");
                for (Peak c : e.getTail().getCluster()) {
                    pretty.append(" " + c.getMz() + " ");
                }
                pretty.append("]");
                pretty.append(linesep);
            }
            pretty.append(linesep);
        }
        return pretty.toString();
    }

    public void createGraph(IsotopicClusters ic) {
        for (int i = 0; i < ic.getIsotopicclusters().size(); i++) {
            this.addNode(ic.getIsotopicclusters().get(i));
        }

        for (int i = 0; i < this.getAdjacencylist().size(); i++) {
            for (int j = 0; j < this.getAdjacencylist().size(); j++) {
                this.addEdge(this.getAdjacencylist().get(i), this.getAdjacencylist().get(j));
            }
        }

        this.addEnd();
    }
}
