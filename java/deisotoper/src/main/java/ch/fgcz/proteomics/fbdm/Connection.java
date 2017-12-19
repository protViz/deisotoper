package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-19
 */

import org.jgrapht.graph.DefaultWeightedEdge;

@SuppressWarnings("serial")
public class Connection extends DefaultWeightedEdge {
    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Connection(String color) {
        this.color = color;
    }
}
