package ch.fgcz.proteomics.fdbm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-06
 */

// UNDER CONSTRUCTION
public class Edge {
    private Node head;
    private Node tail;
    private String color;
    private double score;

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Node getHead() {
        return head;
    }

    public void setHead(Node head) {
        this.head = head;
    }

    public Node getTail() {
        return tail;
    }

    public void setTail(Node tail) {
        this.tail = tail;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Edge(Node cluster1, Node cluster2, String color, double score) {
        this.head = cluster1;
        this.tail = cluster2;
        this.color = color;
        this.score = score;
    }

    public Node fromNode() {
        return head;
    }

    public Node toNode() {
        return tail;
    }

    public boolean isBetween(Node cluster1, Node cluster2) {
        return (this.head == cluster1 && this.tail == cluster2);
    }
}
