package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-04
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ch.fgcz.proteomics.dto.MassSpectrum;

public class Peaklist {
    private List<Peak> peaklist = new ArrayList<>();

    static Peaklist dechargePeaks(Peaklist peaklist, double H_MASS) {
            Peaklist peaklistDecharged = new Peaklist();

            for (int i = 0; i < peaklist.getPeaklist().size(); i++) {
                if (peaklist.getPeaklist().get(i).getCharge() > 1) {
                    peaklistDecharged.getPeaklist().add(new Peak(
                            peaklist.getPeaklist().get(i).getMz() * peaklist.getPeaklist().get(i).getCharge()
                                    - (peaklist.getPeaklist().get(i).getCharge() - 1) * H_MASS,
                            peaklist.getPeaklist().get(i).getIntensity(), peaklist.getPeaklist().get(i).getIsotope(), 1,
                            peaklist.getPeaklist().get(i).getPeakID(),
                            peaklist.getPeaklist().get(i).getIsotopicClusterID(),
                            peaklist.getPeaklist().get(i).getIsotopicSetID()));
                } else {
                    peaklistDecharged.getPeaklist().add(new Peak(peaklist.getPeaklist().get(i).getMz(),
                            peaklist.getPeaklist().get(i).getIntensity(), peaklist.getPeaklist().get(i).getIsotope(),
                            peaklist.getPeaklist().get(i).getCharge(), peaklist.getPeaklist().get(i).getPeakID(),
                            peaklist.getPeaklist().get(i).getIsotopicClusterID(),
                            peaklist.getPeaklist().get(i).getIsotopicSetID()));
                }
            }

            return peaklistDecharged;
    }

    static Peaklist filterNoisePeaks(Peaklist peaklist, double noise) {
            List<Double> intensity = new ArrayList<>();
            for (Peak peak : peaklist.getPeaklist()) {
                intensity.add(peak.getIntensity());
            }

            double threshold = Collections.max(intensity) * noise / 100;

            Peaklist peaklistNoise = new Peaklist();
            for (int i = 0; i < peaklist.getPeaklist().size(); i++) {
                if (threshold < peaklist.getPeaklist().get(i).getIntensity()) {
                    peaklistNoise.getPeaklist().add(peaklist.getPeaklist().get(i));
                }
            }

            return peaklistNoise;
    }

    static Peaklist sortPeaks(Peaklist peaklist) {
        Collections.sort(peaklist.getPeaklist(), new Comparator<Peak>() {
            @Override
            public int compare(Peak peakOne, Peak peakTwo) {
                return Double.compare(peakOne.getMz(), peakTwo.getMz());
            }
        });

        return peaklist;
    }

    static String saveAnnotatedSpectrum(Peaklist peaklist) {
        StringBuilder stringBuilder = new StringBuilder();
        String lineSep = System.getProperty("line.separator");

        stringBuilder.append("IsotopicSet,IsotopicCluster,Peak,Charge,mZ,Intensity").append(lineSep);

        for (Peak peak : peaklist.getPeaklist()) {
            stringBuilder.append(peak.getIsotopicSetID()).append(",").append(peak.getIsotopicClusterID()).append(",")
                    .append(peak.getPeakID()).append(",").append(peak.getCharge()).append(",").append(peak.getMz())
                    .append(",").append(peak.getIntensity()).append(lineSep);

        }

        return stringBuilder.toString();
    }

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
