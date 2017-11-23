package ch.fgcz.proteomics.fbdm;

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

    public Peaklist() {
        this.peaklist = new ArrayList<>();
    }

    public Peaklist(MassSpectrum massSpectrum) {
        List<Peak> plist = new ArrayList<>();

        for (int i = 0; i < massSpectrum.getMz().size() || i < massSpectrum.getIntensity().size(); i++) {
            plist.add(new Peak(massSpectrum.getMz().get(i), massSpectrum.getIntensity().get(i), i));
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
}
