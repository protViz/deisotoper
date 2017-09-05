
/**
 * @author Lucas Schmidt
 * @since 2017-09-05
 * THIS CODE IS UNDER CONSTRUCTION
 */

import java.util.ArrayList;
import java.util.List;

public class IsotopicClusters {
    private List<List<Peak>> isotopicclusters = new ArrayList<>();

    public List<List<Peak>> getIsotopicclusters() {
        return isotopicclusters;
    }

    public void setIsotopicclusters(List<List<Peak>> isotopicclusters) {
        this.isotopicclusters = isotopicclusters;
    }

    public IsotopicClusters(List<Peak> isotopicclusters, double errortolerance) {
        // TODO: Auto-generated constructor stub
    }

    // OLD
    // TODO: Make it work with IsotopicSets class and change this function to a constructor
    public static List<List<Double>> createIsotopicClusters(List<Double> isotopicset, double errortolerance) {
        List<List<Double>> isotopicclusters = new ArrayList<>();

        for (int i = 0; i < isotopicset.size(); i++) {
            List<Double> isotopiccluster = new ArrayList<>();
            List<Double> isotopiccluster2 = new ArrayList<>();

            double distance = 0;
            double distance2 = 0;

            if (i + 1 < isotopicset.size()) {
                distance = isotopicset.get(i + 1) - isotopicset.get(i);
            }

            if (i + 2 < isotopicset.size()) {
                distance2 = isotopicset.get(i + 2) - isotopicset.get(i + 1);
            }

            for (int charge = 1; charge <= 3; charge++) {
                if ((1.003 / charge) - errortolerance < distance && distance < (1.003 / charge) + errortolerance) {
                    isotopiccluster.add(isotopicset.get(i));
                    isotopiccluster.add(isotopicset.get(i + 1));
                    if ((1.003 / charge) - errortolerance < distance2 && distance2 < (1.003 / charge) + errortolerance) {
                        isotopiccluster.add(isotopicset.get(i + 2));
                    }
                    break;
                }
            }

            if (2 == isotopiccluster.size()) {
                isotopicclusters.add(isotopiccluster);
            } else if (3 == isotopiccluster.size()) {
                isotopicclusters.add(isotopiccluster);
                isotopiccluster2.addAll(isotopiccluster);
                isotopiccluster2.remove(2);
                isotopicclusters.add(isotopiccluster2);
            }
        }

        return isotopicclusters;
    }
}
