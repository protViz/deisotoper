package ch.fgcz.proteomics.mspy;

/**
 * @author Lucas Schmidt
 * @since 2017-08-29
 */

import java.util.ArrayList;
import java.util.List;

import ch.fgcz.proteomics.dto.MassSpectrum;

/**
 * @deprecated isn't up to date anymore.
 */
@Deprecated
public class Peaklist {
    private List<Peak> pList = new ArrayList<>();

    public List<Peak> getPeaklist() {
        return pList;
    }

    public void setPeaklist(List<Peak> peakList) {
        this.pList = peakList;
    }

    public Peaklist(MassSpectrum ms) {
        List<Peak> plist = new ArrayList<>();

        if (!ms.getCharge().isEmpty() && !ms.getIsotope().isEmpty()) {
            for (int i = 0; i < ms.getMz().size() || i < ms.getIntensity().size(); i++) {
                plist.add(new Peak(ms.getMz().get(i), ms.getIntensity().get(i), ms.getIsotope().get(i),
                        ms.getCharge().get(i)));
            }
        } else {
            for (int i = 0; i < ms.getMz().size() || i < ms.getIntensity().size(); i++) {
                plist.add(new Peak(ms.getMz().get(i), ms.getIntensity().get(i)));
            }
        }

        this.pList = plist;
    }

    public Peaklist(List<Double> mz, List<Double> intensity) {
        List<Peak> plist = new ArrayList<>();

        for (int i = 0; i < mz.size() || i < intensity.size(); i++) {
            plist.add(new Peak(mz.get(i), intensity.get(i)));
        }

        this.pList = plist;
    }

    public Peaklist(List<Double> mz, List<Double> intensity, List<Integer> charge, List<Double> isotope) {
        List<Peak> plist = new ArrayList<>();

        for (int i = 0; i < mz.size() || i < intensity.size(); i++) {
            plist.add(new Peak(mz.get(i), intensity.get(i), isotope.get(i), charge.get(i)));
        }

        this.pList = plist;
    }
}
