package ch.fgcz.proteomics.fdbm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-19
 */

import org.jgrapht.graph.DefaultWeightedEdge;

public class Connection extends DefaultWeightedEdge {
    /**
        *
        */
    private static final long serialVersionUID = 1L;
    private String color;
    private double score;

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Connection(String color) {
        this.color = color;
        this.score = 0;
    }

    @Override
    public String toString() {
        return "" + (double) Math.round(this.score * 10000d) / 10000d;
    }
}
