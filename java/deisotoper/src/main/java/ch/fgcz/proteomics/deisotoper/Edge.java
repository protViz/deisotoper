
/**
 * @author Lucas Schmidt
 * @since 2017-09-06
 */

// UNDER CONSTRUCTION
public class Edge {
    private Node head;
    private Node tail;
    private String color;

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

    public Edge(Node cluster1, Node cluster2, String color) {
        this.head = cluster1;
        this.tail = cluster2;
        this.color = color;
    }
}
