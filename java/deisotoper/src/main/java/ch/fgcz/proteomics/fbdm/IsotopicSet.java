package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-18
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IsotopicSet {
    private List<IsotopicCluster> isotopicset = new ArrayList<>();
    private int setID;

    public int getSetID() {
        return setID;
    }

    public List<IsotopicCluster> getIsotopicSet() {
        return isotopicset;
    }

    public IsotopicSet(List<Peak> isotopicset, double delta, int setid, ScoreConfig config) {
        List<IsotopicCluster> is = new ArrayList<>();

        is = loop(is, isotopicset, 3, delta, config);

        is = loop(is, isotopicset, 2, delta, config);

        is = loop(is, isotopicset, 1, delta, config);

        is = removeMultipleIsotopicCluster(is);

        is = sortIsotopicSet(is);

        int clusterid = 0;
        for (IsotopicCluster cluster : is) {
            cluster.setClusterID(clusterid);
            clusterid++;
        }

        this.isotopicset = is;
        this.setID = setid;
    }

    public List<IsotopicCluster> loop(List<IsotopicCluster> is, List<Peak> isotopicset, int charge, double delta, ScoreConfig config) {
        for (Peak a : isotopicset) {
            for (Peak b : isotopicset) {
                double distanceab = b.getMz() - a.getMz();
                for (Peak c : isotopicset) {
                    List<Peak> ic = new ArrayList<>();
                    double distanceac = c.getMz() - a.getMz();
                    double distancebc = c.getMz() - b.getMz();

                    if ((config.getDISTANCE_BETWEEN_ISOTOPIC_PEAKS() / charge) - delta < distanceab && distanceab < (config.getDISTANCE_BETWEEN_ISOTOPIC_PEAKS() / charge) + delta) {
                        ic.add(a);
                        ic.add(b);
                    }

                    if ((config.getDISTANCE_BETWEEN_ISOTOPIC_PEAKS() / charge) - delta < distancebc && distancebc < (config.getDISTANCE_BETWEEN_ISOTOPIC_PEAKS() / charge) + delta
                            && ((config.getDISTANCE_BETWEEN_ISOTOPIC_PEAKS() / charge) - delta) * 2 < distanceac && distanceac < ((config.getDISTANCE_BETWEEN_ISOTOPIC_PEAKS() / charge) + delta) * 2) {
                        ic.add(c);
                    }

                    if (ic.size() == 2 || ic.size() == 3) {
                        IsotopicCluster cluster = new IsotopicCluster(ic, charge);
                        is.add(cluster);
                    }
                }
            }
        }

        return is;
    }

    private static List<IsotopicCluster> sortIsotopicSet(List<IsotopicCluster> list) {
        Collections.sort(list, new Comparator<IsotopicCluster>() {

            @Override
            public int compare(IsotopicCluster o1, IsotopicCluster o2) {
                // if (o1.getIsotopicCluster().size() == 2 && o2.getIsotopicCluster().size() == 3) {
                // return 1;
                // } else if (o1.getIsotopicCluster().size() == 3 && o2.getIsotopicCluster().size() == 2) {
                // return -1;
                // }

                int result = Double.compare(o1.getIsotopicCluster().get(0).getMz(), o2.getIsotopicCluster().get(0).getMz());

                if (result == 0) {
                    result = Double.compare(o1.getIsotopicCluster().get(1).getMz(), o2.getIsotopicCluster().get(1).getMz());
                    if (result == 0) {
                        if (o1.getIsotopicCluster().size() == 3 && o2.getIsotopicCluster().size() == 3) {
                            result = Double.compare(o1.getIsotopicCluster().get(2).getMz(), o2.getIsotopicCluster().get(2).getMz());
                            return result;
                        }
                    }
                }

                return result;
            }
        });

        return list;

    }

    private static List<IsotopicCluster> removeMultipleIsotopicCluster(List<IsotopicCluster> list) {
        List<IsotopicCluster> result = new ArrayList<>();
        Set<List<Peak>> titles = new HashSet<>();

        for (IsotopicCluster item : list) {
            if (titles.add(item.getIsotopicCluster())) {
                result.add(item);
            }
        }

        return result;
    }
}