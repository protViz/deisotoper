package ch.fgcz.proteomics.fdbm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-06
 * @see https://codereview.stackexchange.com/questions/67970/graph-implementation-in-java-8
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IsotopicClusterGraph {
    private final List<Node> adjacencylist;
    private final List<Double> AA_MASS = Arrays.asList(71.03711, 156.10111, 114.04293, 115.02694, 103.00919, 129.04259, 128.05858, 57.02146, 137.05891, 113.08406, 113.08406, 128.09496, 131.04049,
            147.06841, 97.05276, 87.03203, 101.04768, 186.07931, 163.06333, 99.06841);
    private final double H_MASS = 1.008;
    private final double NH3_MASS = 17.03052;
    private final double H2O_MASS = 18.01528;
    private final double NH_MASS = 15.01464;
    private final double CO_MASS = 28.0101;
    private double min = 0;

    public List<Node> getAdjacencylist() {
        return adjacencylist;
    }

    public IsotopicClusterGraph() {
        adjacencylist = new ArrayList<>();
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
        Node n = new Node(cluster);

        if (adjacencylist.contains(n)) {
            return false;
        }

        adjacencylist.add(n);

        if (adjacencylist.size() == 2) {
            min = adjacencylist.get(1).getCluster().get(0).getMz();
        }
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

    public static String prettyPrint(IsotopicClusterGraph graph) {
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

                pretty.append("] Score: " + e.getScore());
                pretty.append(linesep);
            }
            pretty.append(linesep);
        }
        return pretty.toString();
    }

    public void createGraph(IsotopicClusters IC) {
        this.addStart();

        for (int i = 0; i < IC.getIsotopicclusters().size(); i++) {
            this.addNode(IC.getIsotopicclusters().get(i));
        }

        for (int i = 0; i < this.getAdjacencylist().size(); i++) {
            for (int j = 0; j < this.getAdjacencylist().size(); j++) {
                this.addEdge(this.getAdjacencylist().get(i), this.getAdjacencylist().get(j));
            }
        }

        this.addEnd();
    }

    public void scoreGraph(IsotopicSets IS) {
        double error = 0.3;
        for (Node n : this.adjacencylist) {
            for (Edge e : n.getEdges()) {
                double score = 0;
                int i = 0;
                for (Peak y : IS.getIsotopicsets().get(i)) {
                    for (Peak x : n.getCluster()) {
                        double scorenow = 0.8 * firstNonintensityFeature(x, y, error) + 0.5 * secondNonintensityFeature(x, y, error, IS)
                                + 0.1 * thirdNonintensityFeature(x, y, error + 0.1 * fifthIntensityFeature(x, y)) + 0.1 * fourthNonintensityFeature(x, y, error);
                        score = score + scorenow;
                    }
                    i++;
                }
                if (!e.getTail().isEnd()) {
                    e.setScore(score);
                }
            }
        }

    }

    private double diff1(Peak x, Peak y) {
        return x.getMz() - y.getMz();
    }

    private double diff2(Peak x, Peak y) {
        return x.getMz() - ((y.getMz() + H_MASS) / 2);
    }

    private double diff3(Peak x, Peak y) {
        return x.getMz() - ((y.getMz() + (2 * H_MASS)) / 3);
    }

    private double diff4(Peak x, Peak y) {
        return x.getMz() - (((y.getMz() * 2) + H_MASS) / 3);
    }

    private double sum1(Peak x, Peak y) {
        return x.getMz() + y.getMz();
    }

    private double sum2(Peak x, Peak y) {
        return x.getMz() + ((y.getMz() + H_MASS) / 2);
    }

    private double sum3(Peak x, Peak y) {
        return x.getMz() + ((y.getMz() + (2 * H_MASS)) / 3);
    }

    private double sum4(Peak x, Peak y) {
        return x.getMz() + (((y.getMz() * 2) + H_MASS) / 3);
    }

    private int firstNonintensityFeature(Peak x, Peak y, double e) {
        List<Peak> F1 = new ArrayList<>();

        for (Double aa : AA_MASS) {
            if (aa - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < aa + e) {
                F1.add(y);
            } else if (aa / 2 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < aa / 2 + e) {
                F1.add(y);
            } else if (aa / 3 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < aa / 3 + e) {
                F1.add(y);
            } else if (aa / 2 - e < Math.abs(diff2(x, y)) && Math.abs(diff2(x, y)) < aa / 2 + e) {
                F1.add(y);
            } else if (aa / 2 - e < Math.abs(diff2(y, x)) && Math.abs(diff2(y, x)) < aa / 2 + e) {
                F1.add(y);
            } else if (aa / 3 - e < Math.abs(diff3(x, y)) && Math.abs(diff3(x, y)) < aa / 3 + e) {
                F1.add(y);
            } else if (aa / 3 - e < Math.abs(diff3(y, x)) && Math.abs(diff3(y, x)) < aa / 3 + e) {
                F1.add(y);
            } else if (aa / 3 - e < Math.abs(diff4(x, y)) && Math.abs(diff4(x, y)) < aa / 3 + e) {
                F1.add(y);
            } else if (aa / 3 - e < Math.abs(diff4(y, x)) && Math.abs(diff4(y, x)) < aa / 3 + e) {
                F1.add(y);
            }
        }

        return F1.size();
    }

    // NOT IMPLEMENTED YET
    private int secondNonintensityFeature(Peak x, Peak y, double e, IsotopicSets IS) {
        List<Peak> F2 = new ArrayList<>();

        // double M = 0;
        // for (int i = 0; i < IS.getIsotopicsets().size(); i++) {
        // for (int j = 1; j < IS.getIsotopicsets().get(i).size(); j++) {
        // if (IS.getIsotopicsets().get(i).get(j).getMz() == x.getMz()) {
        // M = IS.getIsotopicsets().get(i).get(j - 1).getMz();
        // }
        // }
        // }

        if (-e < sum1(x, y) && sum1(x, y) < +e) {
            F2.add(y);
        }

        return F2.size();
    }

    private int thirdNonintensityFeature(Peak x, Peak y, double e) {
        List<Peak> F3 = new ArrayList<>();

        if (H2O_MASS - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < H2O_MASS + e) {
            F3.add(y);
        } else if (NH3_MASS - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < NH3_MASS + e) {
            F3.add(y);
        } else if (H2O_MASS / 2 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < H2O_MASS / 2 + e) {
            F3.add(y);
        } else if (NH3_MASS / 2 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < NH3_MASS / 2 + e) {
            F3.add(y);
        } else if (H2O_MASS / 3 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < H2O_MASS / 3 + e) {
            F3.add(y);
        } else if (NH3_MASS / 3 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < NH3_MASS / 3 + e) {
            F3.add(y);
        } else if (H2O_MASS / 2 - e < Math.abs(diff2(x, y)) && Math.abs(diff2(x, y)) < H2O_MASS / 2 + e) {
            F3.add(y);
        } else if (NH3_MASS / 2 - e < Math.abs(diff2(x, y)) && Math.abs(diff2(x, y)) < NH3_MASS / 2 + e) {
            F3.add(y);
        } else if (H2O_MASS / 2 - e < Math.abs(diff2(y, x)) && Math.abs(diff2(y, x)) < H2O_MASS / 2 + e) {
            F3.add(y);
        } else if (NH3_MASS / 2 - e < Math.abs(diff2(y, x)) && Math.abs(diff2(y, x)) < NH3_MASS / 2 + e) {
            F3.add(y);
        } else if (H2O_MASS / 3 - e < Math.abs(diff3(x, y)) && Math.abs(diff3(x, y)) < H2O_MASS / 3 + e) {
            F3.add(y);
        } else if (NH3_MASS / 3 - e < Math.abs(diff3(x, y)) && Math.abs(diff3(x, y)) < NH3_MASS / 3 + e) {
            F3.add(y);
        } else if (H2O_MASS / 3 - e < Math.abs(diff3(y, x)) && Math.abs(diff3(y, x)) < H2O_MASS / 3 + e) {
            F3.add(y);
        } else if (NH3_MASS / 3 - e < Math.abs(diff3(y, x)) && Math.abs(diff3(y, x)) < NH3_MASS / 3 + e) {
            F3.add(y);
        } else if (H2O_MASS / 3 - e < Math.abs(diff4(x, y)) && Math.abs(diff4(x, y)) < H2O_MASS / 3 + e) {
            F3.add(y);
        } else if (NH3_MASS / 3 - e < Math.abs(diff4(x, y)) && Math.abs(diff4(x, y)) < NH3_MASS / 3 + e) {
            F3.add(y);
        } else if (H2O_MASS / 3 - e < Math.abs(diff4(y, x)) && Math.abs(diff4(y, x)) < H2O_MASS / 3 + e) {
            F3.add(y);
        } else if (NH3_MASS / 3 - e < Math.abs(diff4(y, x)) && Math.abs(diff4(y, x)) < NH3_MASS / 3 + e) {
            F3.add(y);
        }

        return F3.size();
    }

    private int fourthNonintensityFeature(Peak x, Peak y, double e) {
        List<Peak> F4 = new ArrayList<>();

        if (NH_MASS - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < NH_MASS + e) {
            F4.add(y);
        } else if (CO_MASS - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < CO_MASS + e) {
            F4.add(y);
        } else if (NH_MASS / 2 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < NH_MASS / 2 + e) {
            F4.add(y);
        } else if (CO_MASS / 2 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < CO_MASS / 2 + e) {
            F4.add(y);
        } else if (NH_MASS / 3 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < NH_MASS / 3 + e) {
            F4.add(y);
        } else if (CO_MASS / 3 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < CO_MASS / 3 + e) {
            F4.add(y);
        } else if (NH_MASS / 2 - e < Math.abs(diff2(x, y)) && Math.abs(diff2(x, y)) < NH_MASS / 2 + e) {
            F4.add(y);
        } else if (CO_MASS / 2 - e < Math.abs(diff2(x, y)) && Math.abs(diff2(x, y)) < CO_MASS / 2 + e) {
            F4.add(y);
        } else if (NH_MASS / 2 - e < Math.abs(diff2(y, x)) && Math.abs(diff2(y, x)) < NH_MASS / 2 + e) {
            F4.add(y);
        } else if (CO_MASS / 2 - e < Math.abs(diff2(y, x)) && Math.abs(diff2(y, x)) < CO_MASS / 2 + e) {
            F4.add(y);
        } else if (NH_MASS / 3 - e < Math.abs(diff3(x, y)) && Math.abs(diff3(x, y)) < NH_MASS / 3 + e) {
            F4.add(y);
        } else if (CO_MASS / 3 - e < Math.abs(diff3(x, y)) && Math.abs(diff3(x, y)) < CO_MASS / 3 + e) {
            F4.add(y);
        } else if (NH_MASS / 3 - e < Math.abs(diff3(y, x)) && Math.abs(diff3(y, x)) < NH_MASS / 3 + e) {
            F4.add(y);
        } else if (CO_MASS / 3 - e < Math.abs(diff3(y, x)) && Math.abs(diff3(y, x)) < CO_MASS / 3 + e) {
            F4.add(y);
        } else if (NH_MASS / 3 - e < Math.abs(diff4(x, y)) && Math.abs(diff4(x, y)) < NH_MASS / 3 + e) {
            F4.add(y);
        } else if (CO_MASS / 3 - e < Math.abs(diff4(x, y)) && Math.abs(diff4(x, y)) < CO_MASS / 3 + e) {
            F4.add(y);
        } else if (NH_MASS / 3 - e < Math.abs(diff4(y, x)) && Math.abs(diff4(y, x)) < NH_MASS / 3 + e) {
            F4.add(y);
        } else if (CO_MASS / 3 - e < Math.abs(diff4(y, x)) && Math.abs(diff4(y, x)) < CO_MASS / 3 + e) {
            F4.add(y);
        }

        return F4.size();
    }

    // NOT IMPLEMENTED YET
    private int fifthIntensityFeature(Peak x, Peak y) {
        List<Peak> F5 = new ArrayList<>();

        return F5.size();
    }
}
