
/**
 * @author Lucas Schmidt
 * @since 2017-09-05
 * THIS CODE IS UNDER CONSTRUCTION
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

    public IsotopicSets(Peaklist peaklist, double errortolerance) {
        // TODO: Auto-generated constructor stub
    }

    // OLD
    private static int indexisotopicset = 1;
    private static int oldindexisotopicset = 1;

    // TODO: Change createIsotopicSets and createIsotopicSet to one function (constructor) and use class IsotopicClusters
    public List<List<Double>> createIsotopicSets(MassSpectrum massspectrum, double errortolerance) {
        for (int n = 1; n < massspectrum.getMz().size(); n++) {
            if (createIsotopicSet(massspectrum, errortolerance).size() > 1) {
                this.isotopicsets.add(createIsotopicSet(massspectrum, errortolerance));
            }
            if (indexisotopicset == oldindexisotopicset + 1) {
                break;
            }
            indexisotopicset = oldindexisotopicset + 1;
        }

        return isotopicsets;
    }

    private List<Double> createIsotopicSet(MassSpectrum massspectrum, double errortolerance) {
        List<Double> isotopicset = new ArrayList<>();

        int j = 0;
        for (int i = indexisotopicset; i < massspectrum.getMz().size(); i++) {
            double distance = massspectrum.getMz().get(i) - massspectrum.getMz().get(i - 1);
            boolean rightdistance = false;
            for (int charge = 1; charge <= 3; charge++) {
                if ((1.003 / charge) - errortolerance < distance && distance < (1.003 / charge) + errortolerance) {
                    if (j == 0) {
                        isotopicset.add(massspectrum.getMz().get(i - 1));
                        j++;
                    }
                    isotopicset.add(massspectrum.getMz().get(i));
                    j++;
                    rightdistance = true;
                }
            }

            if (rightdistance == false) {
                oldindexisotopicset = i;
                break;
            }
        }

        return isotopicset;
    }
}
