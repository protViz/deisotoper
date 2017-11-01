package ch.fgcz.proteomics.fdbm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-04
 */

import java.util.ArrayList;
import java.util.List;

import ch.fgcz.proteomics.dto.MassSpectrum;

public class Peaklist {
    private List<Peak> peaklist = new ArrayList<>();

    public List<Peak> getPeaklist() {
        return peaklist;
    }

    public void setPeaklist(List<Peak> peaklist) {
        this.peaklist = peaklist;
    }

    public Peaklist(MassSpectrum ms) {
        List<Peak> plist = new ArrayList<>();

        for (int i = 0; i < ms.getMz().size() || i < ms.getIntensity().size(); i++) {
            plist.add(new Peak(ms.getMz().get(i), ms.getIntensity().get(i), i));
        }

        this.peaklist = plist;
    }

    public Peaklist(List<Double> mz, List<Double> intensity) {
        List<Peak> plist = new ArrayList<>();

        for (int i = 0; i < mz.size() || i < intensity.size(); i++) {
            plist.add(new Peak(mz.get(i), intensity.get(i), i));
        }

        this.peaklist = plist;
    }

    public Peaklist(List<Double> mz, List<Double> intensity, List<Double> isotope, List<Integer> charge) {
        List<Peak> plist = new ArrayList<>();

        for (int i = 0; i < mz.size() || i < intensity.size(); i++) {
            plist.add(new Peak(mz.get(i), intensity.get(i), isotope.get(i), charge.get(i), i));
        }

        this.peaklist = plist;
    }
}
