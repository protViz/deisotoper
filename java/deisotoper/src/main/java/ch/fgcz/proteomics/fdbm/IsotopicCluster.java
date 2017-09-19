package ch.fgcz.proteomics.fdbm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-18
 */

import java.util.ArrayList;
import java.util.List;

public class IsotopicCluster {
    private List<Peak> isotopiccluster = new ArrayList<>();
    private int charge;
    private List<Connection> connection = new ArrayList<>();
    private boolean start;
    private boolean end;

    public List<Connection> getConnection() {
        return connection;
    }

    public void setConnection(List<Connection> connection) {
        this.connection = connection;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public List<Peak> getIsotopicCluster() {
        return isotopiccluster;
    }

    public void setIsotopicCluster(List<Peak> isotopicCluster) {
        this.isotopiccluster = isotopicCluster;
    }

    public void addConnection(Connection c) {
        this.connection.add(c);
    }

    public IsotopicCluster(List<Peak> isotopiccluster) {
        this.isotopiccluster = isotopiccluster;
        this.charge = 0;
        this.start = false;
        this.end = false;
    }

    public IsotopicCluster(List<Peak> isotopiccluster, int charge) {
        this.isotopiccluster = isotopiccluster;
        this.charge = charge;
    }
}
