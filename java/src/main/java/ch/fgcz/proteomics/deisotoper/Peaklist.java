
/**
 * @author Lucas Schmidt
 * @since 2017-09-04
 */

import java.util.ArrayList;
import java.util.List;

public class Peaklist {
    private List<Peak> peaklist = new ArrayList<>();

    public List<Peak> getPeaklist() {
        return peaklist;
    }

    public void setPeaklist(List<Peak> peaklist) {
        this.peaklist = peaklist;
    }

    // Convert from MassSpectrum
    public Peaklist(MassSpectrum ms) {
        List<Peak> plist = new ArrayList<>();

        for (int i = 0; i < ms.getMz().size() || i < ms.getIntensity().size(); i++) {
            plist.add(new Peak(ms.getMz().get(i), ms.getIntensity().get(i)));
        }

        this.peaklist = plist;
    }

    // Convert from two Lists (mZ Values and Intensity Values)
    public Peaklist(List<Double> mz, List<Double> intensity) {
        List<Peak> plist = new ArrayList<>();

        for (int i = 0; i < mz.size() || i < intensity.size(); i++) {
            plist.add(new Peak(mz.get(i), intensity.get(i)));
        }

        this.peaklist = plist;
    }
}
