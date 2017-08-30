
/**
 * @author Lucas Schmidt
 * @since 2017-08-22
 */

import java.util.ArrayList;
import java.util.List;

// TODO (LS) : Add source file name to MassSpectrumMeasurement
public class MassSpectrometryMeasurement {
    public static List<MassSpectrum> MSMlist = new ArrayList<MassSpectrum>();

    public static List<MassSpectrum> getMSMlist() {
        return MSMlist;
    }

    public static void setMSMlist(List<MassSpectrum> list) {
        MSMlist = list;
    }

    /**
     * Adds a MassSpectrum to a List of MassSpectrum.
     * 
     * @param typ
     * @param searchengine
     * @param mz
     * @param intensity
     * @param peptidmass
     * @param rt
     * @param chargestate
     * @param id
     * @param scans
     * @param title
     * @return List of MassSpectrum
     */
    public static List<MassSpectrum> addMSM(String typ, String searchengine, double[] mz, double[] intensity, double peptidmass, double rt, int chargestate, int id) {
        MassSpectrum MS = new MassSpectrum();

        MS.setTyp(typ);
        MS.setSearchEngine(searchengine);
        MS.setMz(mz);
        MS.setIntensity(intensity);
        MS.setPeptidMass(peptidmass);
        MS.setRt(rt);
        MS.setChargeState(chargestate);
        MS.setId(id);

        MSMlist.add(MS);

        return MSMlist;
    }
}
