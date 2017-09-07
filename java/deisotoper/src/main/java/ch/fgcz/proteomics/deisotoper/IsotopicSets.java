package ch.fgcz.proteomics.deisotoper;

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
        int index = 1;
        int oldindex = 1;
        for (int outterindex = 1; outterindex < peaklist.getPeaklist().size(); outterindex++) {
            List<Peak> isotopicset = new ArrayList<>();

            for (int innerindex = index; innerindex < peaklist.getPeaklist().size(); innerindex++) {
                double distance = peaklist.getPeaklist().get(innerindex).getMz() - peaklist.getPeaklist().get(innerindex - 1).getMz();
                boolean trigger = false;

                for (int charge = 1; charge <= 3; charge++) {
                    if ((1.003 / charge) - errortolerance < distance && distance < (1.003 / charge) + errortolerance) {
                        if (isotopicset.size() == 0) {
                            isotopicset.add((peaklist.getPeaklist().get(innerindex - 1)));
                        }

                        isotopicset.add((peaklist.getPeaklist().get(innerindex)));
                        trigger = true;
                    }
                }

                if (trigger == false) {
                    oldindex = innerindex;
                    break;
                }
            }

            if (1 < isotopicset.size()) {
                this.isotopicsets.add(isotopicset);
            }

            if (index == oldindex + 1) {
                break;
            }

            index = oldindex + 1;
        }
    }
}
