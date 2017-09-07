package ch.fgcz.proteomics.deisotoper;

/**
 * @author Lucas Schmidt
 * @since 2017-09-06
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//UNDER CONSTRUCTION
public class Node {
    private List<Peak> cluster = new ArrayList<>();
    private List<Edge> edges;
    private boolean start;
    private boolean end;

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public List<Peak> getCluster() {
        return cluster;
    }

    public void setCluster(List<Peak> cluster) {
        this.cluster = cluster;
    }

    public Node(List<Peak> cluster) {
        this.cluster = cluster;
        this.edges = new ArrayList<>();
    }

    public boolean addEdge(Node cluster, String color, double score) {
        if (hasEdge(cluster)) {
            return false;
        }
        Edge newedge = new Edge(this, cluster, color, score);
        return edges.add(newedge);
    }

    public boolean removeEdge(Node cluster) {
        Optional<Edge> optional = findEdge(cluster);
        if (optional.isPresent()) {
            return edges.remove(optional.get());
        }
        return false;
    }

    public boolean hasEdge(Node cluster) {
        return findEdge(cluster).isPresent();
    }

    private Optional<Edge> findEdge(Node cluster) {
        return edges.stream().filter(edge -> edge.isBetween(this, cluster)).findFirst();
    }

    public int getEdgeCount() {
        return edges.size();
    }
}
