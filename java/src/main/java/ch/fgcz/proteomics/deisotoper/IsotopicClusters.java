
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

    /**
     * Constructs the IsotopicClusters as a List of Lists of Peaks from one IsotopicSet which is a List of Peaks with a given error tolerance.
     * 
     * @param isotopicset
     * @param errortolerance
     */
    public IsotopicClusters(List<Peak> isotopicset, double errortolerance) {
        for (int i = 0; i < isotopicset.size(); i++) {
            List<Peak> isotopiccluster1 = new ArrayList<>();
            List<Peak> isotopiccluster2 = new ArrayList<>();
            double distance1 = 0;
            double distance2 = 0;

            if (i + 1 < isotopicset.size()) {
                distance1 = isotopicset.get(i + 1).getMz() - isotopicset.get(i).getMz();
            }

            if (i + 2 < isotopicset.size()) {
                distance2 = isotopicset.get(i + 2).getMz() - isotopicset.get(i + 1).getMz();
            }

            for (int charge = 1; charge <= 3; charge++) {
                if ((1.003 / charge) - errortolerance < distance1 && distance1 < (1.003 / charge) + errortolerance) {
                    isotopiccluster1.add(isotopicset.get(i));
                    isotopiccluster1.add(isotopicset.get(i + 1));
                    
                    if ((1.003 / charge) - errortolerance < distance2 && distance2 < (1.003 / charge) + errortolerance) {
                        isotopiccluster1.add(isotopicset.get(i + 2));
                    }
                    
                    break;
                }
            }

            if (2 == isotopiccluster1.size()) {
                this.isotopicclusters.add(isotopiccluster1);
            } else if (3 == isotopiccluster1.size()) {
                this.isotopicclusters.add(isotopiccluster1);
                isotopiccluster2.addAll(isotopiccluster1);
                isotopiccluster2.remove(2);
                this.isotopicclusters.add(isotopiccluster2);
            }
        }
    }
}
