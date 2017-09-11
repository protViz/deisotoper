package ch.fgcz.proteomics.mspy;

/**
 * @author Lucas Schmidt
 * @since 2017-08-29
 */

import java.util.ArrayList;
import java.util.List;

import ch.fgcz.proteomics.dto.MassSpectrum;

public class Peaklist {
    private List<Peak> peaklist = new ArrayList<Peak>();

    public List<Peak> getPeaklist() {
        return peaklist;
    }

    public void setPeaklist(List<Peak> peaklist) {
        this.peaklist = peaklist;
    }

    /**
     * Converts from a MassSpectrum to a Peaklist
     * 
     * @param ms
     */
    public Peaklist(MassSpectrum ms) {
        List<Peak> plist = new ArrayList<>();

        for (int i = 0; i < ms.getMz().size() || i < ms.getIntensity().size(); i++) {
            plist.add(new Peak(ms.getMz().get(i), ms.getIntensity().get(i)));
        }

        this.peaklist = plist;
    }

    /**
     * Converts from two lists (mZ Values and Intensity Values) to a Peaklist.
     * 
     * @param mz
     * @param intensity
     */
    public Peaklist(List<Double> mz, List<Double> intensity) {
        List<Peak> plist = new ArrayList<>();

        for (int i = 0; i < mz.size() || i < intensity.size(); i++) {
            plist.add(new Peak(mz.get(i), intensity.get(i)));
        }

        this.peaklist = plist;
    }
}
