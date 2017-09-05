
/**
 * @author Lucas Schmidt
 * @since 2017-09-05
 */

import java.util.ArrayList;
import java.util.List;

public class IsotopicSets {
    private List<List<Peak>> isotopicsets = new ArrayList<>();

    public List<List<Peak>> getIsotopicsets() {
        return isotopicsets;
    }

    public void setIsotopicsets(List<List<Peak>> isotopicsets) {
        this.isotopicsets = isotopicsets;
    }

    // TODO: Code review and refactoring
    /**
     * Constructs the IsotopicSets as a List of Lists of Peaks from one Peaklist Object with a given error tolerance.
     * 
     * @param peaklist
     * @param errortolerance
     */
    public IsotopicSets(Peaklist peaklist, double errortolerance) {
        int indexisotopicset = 1;
        int oldindexisotopicset = 1;

        for (int n = 1; n < peaklist.getPeaklist().size(); n++) {
            List<Peak> isotopicset = new ArrayList<>();

            int j = 0;
            for (int i = indexisotopicset; i < peaklist.getPeaklist().size(); i++) {
                double distance = peaklist.getPeaklist().get(i).getMz() - peaklist.getPeaklist().get(i - 1).getMz();
                boolean rightdistance = false;
                for (int charge = 1; charge <= 3; charge++) {
                    if ((1.003 / charge) - errortolerance < distance && distance < (1.003 / charge) + errortolerance) {
                        if (j == 0) {
                            isotopicset.add((peaklist.getPeaklist().get(i - 1)));
                            j++;
                        }
                        isotopicset.add((peaklist.getPeaklist().get(i)));
                        j++;
                        rightdistance = true;
                    }
                }

                if (rightdistance == false) {
                    oldindexisotopicset = i;
                    break;
                }
            }

            if (isotopicset.size() > 1) {
                this.isotopicsets.add(isotopicset);
            }

            if (indexisotopicset == oldindexisotopicset + 1) {
                break;
            }

            indexisotopicset = oldindexisotopicset + 1;
        }
    }
}
