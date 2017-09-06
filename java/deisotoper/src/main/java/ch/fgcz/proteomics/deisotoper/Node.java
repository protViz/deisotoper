
/**
 * @author Lucas Schmidt
 * @since 2017-09-06
 */

import java.util.ArrayList;
import java.util.List;

//UNDER CONSTRUCTION
public class Node {
    private List<Peak> cluster = new ArrayList<>();
    boolean start;
    boolean end;

    public List<Peak> getCluster() {
        return cluster;
    }

    public void setCluster(List<Peak> cluster) {
        this.cluster = cluster;
    }

    public Node(List<Peak> cluster) {
        this.cluster = cluster;
        if (cluster.isEmpty()) {
            this.start = true;
        } else {
            this.start = false;
        }
        if (cluster.isEmpty()) {
            this.end = true;
        } else {
            this.end = false;
        }
    }
}
