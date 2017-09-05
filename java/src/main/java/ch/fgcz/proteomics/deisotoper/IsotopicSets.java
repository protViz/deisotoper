
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
        int i = 1;
        int o = 1;
        for (int n = 1; n < peaklist.getPeaklist().size(); n++) {
            List<Peak> isotopicset = new ArrayList<>();

            for (int m = i; m < peaklist.getPeaklist().size(); m++) {
                double distance = peaklist.getPeaklist().get(m).getMz() - peaklist.getPeaklist().get(m - 1).getMz();
                boolean rightdistance = false;
                for (int charge = 1; charge <= 3; charge++) {
                    if ((1.003 / charge) - errortolerance < distance && distance < (1.003 / charge) + errortolerance) {
                        if (isotopicset.size() == 0) {
                            isotopicset.add((peaklist.getPeaklist().get(m - 1)));
                        }
                        isotopicset.add((peaklist.getPeaklist().get(m)));
                        rightdistance = true;
                    }
                }

                if (rightdistance == false) {
                    o = m;
                    break;
                }
            }

            if (isotopicset.size() > 1) {
                this.isotopicsets.add(isotopicset);
            }

            if (i == o + 1) {
                break;
            }

            i = o + 1;
        }
    }
}
