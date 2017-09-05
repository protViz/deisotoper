
/**
 * @author Lucas Schmidt
 * @since 2017-09-05
 */

import java.util.ArrayList;
import java.util.Arrays;
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

    public static void main(String[] args) {
        // Example 1
        List<Double> mz1 = Arrays.asList(0.1, 0.2, 1.33, 1.66, 2.0, 2.5, 3.5, 4.0, 5.0, 6.0, 12.0, 15.0);
        List<Double> intensity1 = Arrays.asList(7.0, 4.0, 5.0, 6.0, 6.0, 7.0, 7.0, 7.0, 8.0, 8.0, 2.0, 3.0);
        Peaklist peaklist1 = new Peaklist(mz1, intensity1);

        IsotopicSets IS = new IsotopicSets(peaklist1, 0.01);

        // for (Peak j : i) {
        // System.out.print("IS: " + j.getMz() + ", ");
        // }
        // System.out.println(";");

        for (List<Peak> i : IS.getIsotopicsets()) {
            IsotopicClusters IC = new IsotopicClusters(i, 0.01);
            for (List<Peak> c : IC.getIsotopicclusters()) {
                for (Peak j : c) {
                    System.out.print(" " + j.getMz());
                }
                System.out.println(";");
            }
        }
    }

    // // OLD
    // // TODO: Change createIsotopicSets and createIsotopicSet to one function (constructor) and use class IsotopicClusters
    // public List<List<Double>> createIsotopicSets(MassSpectrum massspectrum, double errortolerance) {
    // for (int n = 1; n < massspectrum.getMz().size(); n++) {
    // if (createIsotopicSet(massspectrum, errortolerance).size() > 1) {
    // this.isotopicsets.add(createIsotopicSet(massspectrum, errortolerance));
    // }
    // if (indexisotopicset == oldindexisotopicset + 1) {
    // break;
    // }
    // indexisotopicset = oldindexisotopicset + 1;
    // }
    //
    // return isotopicsets;
    // }
    //
    // private List<Double> createIsotopicSet(MassSpectrum massspectrum, double errortolerance) {
    // List<Double> isotopicset = new ArrayList<>();
    //
    // int j = 0;
    // for (int i = indexisotopicset; i < massspectrum.getMz().size(); i++) {
    // double distance = massspectrum.getMz().get(i) - massspectrum.getMz().get(i - 1);
    // boolean rightdistance = false;
    // for (int charge = 1; charge <= 3; charge++) {
    // if ((1.003 / charge) - errortolerance < distance && distance < (1.003 / charge) + errortolerance) {
    // if (j == 0) {
    // isotopicset.add(massspectrum.getMz().get(i - 1));
    // j++;
    // }
    // isotopicset.add(massspectrum.getMz().get(i));
    // j++;
    // rightdistance = true;
    // }
    // }
    //
    // if (rightdistance == false) {
    // oldindexisotopicset = i;
    // break;
    // }
    // }
    //
    // return isotopicset;
    // }
}
