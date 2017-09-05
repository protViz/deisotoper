
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

    public IsotopicClusters(List<Peak> isotopicset, double errortolerance) {
        for (int i = 0; i < isotopicset.size(); i++) {
            List<Peak> isotopiccluster = new ArrayList<>();
            List<Peak> isotopiccluster2 = new ArrayList<>();

            double distance = 0;
            double distance2 = 0;

            if (i + 1 < isotopicset.size()) {
                distance = isotopicset.get(i + 1).getMz() - isotopicset.get(i).getMz();
            }

            if (i + 2 < isotopicset.size()) {
                distance2 = isotopicset.get(i + 2).getMz() - isotopicset.get(i + 1).getMz();
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
                this.isotopicclusters.add(isotopiccluster);
            } else if (3 == isotopiccluster.size()) {
                this.isotopicclusters.add(isotopiccluster);
                isotopiccluster2.addAll(isotopiccluster);
                isotopiccluster2.remove(2);
                this.isotopicclusters.add(isotopiccluster2);
            }
        }
    }
}
