package ch.fgcz.proteomics.fdbm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-05
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Why is this Class called IsotopicClusters instead of IsotopicSet?
 * 
 * The name IsotopicClusters has more reasons. The first reason is, that when you create a IsotopicClusters Object you pass a IsotopicSet (List<Peak>) as parameter to the constructor. That means you
 * give it a IsotopicSet to make the IsotopicClusters which are in this set. If the Class would be named IsotopicSet, you would give it a IsotopicSet to make a IsotopicSet and that doesn't make any
 * sense. The next reason is that when you have a Peaklist (List<Peak>) and you create some IsotopicSets from it, the IsotopicSets Object contains a List of IsotopicSet (List<Peak>) and then you pass
 * one of these IsotopicSets to the constructor to construct the IsotopicClusters for exspecially this IsotopicSet. The Class IsotopicSets and IsotopicClusters are connected by its building structure,
 * that means that when you want to build clusters from a Peaklist, then you must create firstly a IsotopicSets Object and for each IsotopicSet in this Object you would create a IsotopicClusters
 * Object and every IsotopicClusters Object has more IsotopicCluster in it. Therefore you could also rename the IsotopicClusters to IsotopicSet, because in this definition it would make sense, but
 * otherwise it doesn't. The final question is therefore, if it is better to change everything just because of a name and have problems at creation of the Objects, because with other names it would
 * look much more complicated or change the names to have more logic behind it, when you look at the created Objects? Fact is that the name IsotpicSet and IsotopicClusters are definitely the same, but
 * only when the clusters are only from one set, which would be correct in this implementation, because you create only isotopicClusters for one passed IsotopicSet. The advantage of this
 * implementation is simply, that when you have a predefined and not calculated IsotopicSet, you also can pass it to this function and it would make more sense because you get the IsotopicClusters for
 * exact this IsotopicSet.
 */
public class IsotopicClusters {
    private List<List<Peak>> isotopicclusters = new ArrayList<>();

    public List<List<Peak>> getIsotopicclusters() {
        return isotopicclusters;
    }

    public void setIsotopicclusters(List<List<Peak>> isotopicclusters) {
        this.isotopicclusters = isotopicclusters;
    }

    /**
     * Constructs the IsotopicClusters as a List of Lists of Peaks from one IsotopicSet which is a List of Peaks with a given error tolerance.
     * 
     * @param isotopicset
     * @param errortolerance
     */
    // BUGFIX NEEDED, DOESN'T WORK PROPERLY
    public IsotopicClusters(List<Peak> isotopicset, double errortolerance) {
        Map<List<Peak>, Double> distance = new HashMap<>();

        for (int i = 0; i < isotopicset.size(); i++) {
            // List<Peak> isotopiccluster1 = new ArrayList<>();
            // List<Peak> isotopiccluster2 = new ArrayList<>();
            double dist = 0;
            // double distance2 = 0;

            for (int j = 0; j < isotopicset.size(); j++) {
                if (i == j) {
                    continue;
                }
                System.out.println(isotopicset.get(i).getMz() + " " + isotopicset.get(j).getMz());

                dist = isotopicset.get(j).getMz() - isotopicset.get(i).getMz();
                List<Peak> plist = new ArrayList<>();
                plist.add(isotopicset.get(i));
                plist.add(isotopicset.get(j));
                distance.put(plist, dist);
            }

            // if (i + 2 < isotopicset.size()) {
            // distance2 = isotopicset.get(i + 2).getMz() - isotopicset.get(i + 1).getMz();
            // }
            //
            // for (int charge = 1; charge <= 3; charge++) {
            // if ((1.003 / charge) - errortolerance < distance1 && distance1 < (1.003 / charge) + errortolerance) {
            // isotopiccluster1.add(isotopicset.get(i));
            // isotopiccluster1.add(isotopicset.get(i + 1));
            //
            // if ((1.003 / charge) - errortolerance < distance2 && distance2 < (1.003 / charge) + errortolerance) {
            // isotopiccluster1.add(isotopicset.get(i + 2));
            // }
            //
            // break;
            // }
            // }
            //
            // if (2 == isotopiccluster1.size()) {
            // this.isotopicclusters.add(isotopiccluster1);
            // } else if (3 == isotopiccluster1.size()) {
            // this.isotopicclusters.add(isotopiccluster1);
            // isotopiccluster2.addAll(isotopiccluster1);
            // isotopiccluster2.remove(2);
            // this.isotopicclusters.add(isotopiccluster2);
            // }
        }

        for (Map.Entry<List<Peak>, Double> e : distance.entrySet()) {
            // System.out.println(e.getKey().get(0).getMz() + " " + e.getValue().doubleValue() + " " + e.getKey().get(1).getMz());

            // if ((1.003 / 1) - errortolerance < e.getValue().doubleValue() && e.getValue().doubleValue() < (1.003 / 1) + errortolerance) {
            // System.out.println(e.getKey().get(0).getMz() + " " + e.getValue().doubleValue() + " " + e.getKey().get(1).getMz());
            // } else if ((1.003 / 2) - errortolerance < e.getValue().doubleValue() && e.getValue().doubleValue() < (1.003 / 2) + errortolerance) {
            // System.out.println(e.getKey().get(0).getMz() + " " + e.getValue().doubleValue() + " " + e.getKey().get(1).getMz());
            // } else if ((1.003 / 3) - errortolerance < e.getValue().doubleValue() && e.getValue().doubleValue() < (1.003 / 3) + errortolerance) {
            // System.out.println(e.getKey().get(0).getMz() + " " + e.getValue().doubleValue() + " " + e.getKey().get(1).getMz());
            // }
        }
    }

    public static void main(String[] args) {
        List<Double> mz3 = Arrays.asList(1.0, 2.0, 2.5, 3.0);
        List<Double> intensity3 = Arrays.asList(2.0, 1.0, 1.0, 1.0);
        Peaklist peaklist3 = new Peaklist(mz3, intensity3);
        IsotopicSets IS3 = new IsotopicSets(peaklist3, 0.01);

        for (List<Peak> set : IS3.getIsotopicsets()) {
            IsotopicClusters IC = new IsotopicClusters(set, 0.01);

            for (List<Peak> c : IC.getIsotopicclusters()) {
                for (Peak cc : c) {
                    System.out.println(cc.getMz());
                }
                System.out.println();
            }
        }
    }
}
