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

public class PeakList {
    private List<Peak> peaklist = new ArrayList<>();

    static PeakList dechargePeaks(PeakList peakList, double H_MASS) {
            PeakList peakListDecharged = new PeakList();

            for (int i = 0; i < peakList.getPeaklist().size(); i++) {
                if (peakList.getPeaklist().get(i).getCharge() > 1) {
                    peakListDecharged.getPeaklist().add(new Peak(
                            peakList.getPeaklist().get(i).getMz() * peakList.getPeaklist().get(i).getCharge()
                                    - (peakList.getPeaklist().get(i).getCharge() - 1) * H_MASS,
                            peakList.getPeaklist().get(i).getIntensity(), peakList.getPeaklist().get(i).getIsotope(), 1,
                            peakList.getPeaklist().get(i).getPeakID(),
                            peakList.getPeaklist().get(i).getIsotopicClusterID(),
                            peakList.getPeaklist().get(i).getIsotopicSetID()));
                } else {
                    peakListDecharged.getPeaklist().add(new Peak(peakList.getPeaklist().get(i).getMz(),
                            peakList.getPeaklist().get(i).getIntensity(), peakList.getPeaklist().get(i).getIsotope(),
                            peakList.getPeaklist().get(i).getCharge(), peakList.getPeaklist().get(i).getPeakID(),
                            peakList.getPeaklist().get(i).getIsotopicClusterID(),
                            peakList.getPeaklist().get(i).getIsotopicSetID()));
                }
            }

            return peakListDecharged;
    }

    static PeakList filterNoisePeaks(PeakList peakList, double noise) {
            List<Double> intensity = new ArrayList<>();
            for (Peak peak : peakList.getPeaklist()) {
                intensity.add(peak.getIntensity());
            }

            double threshold = Collections.max(intensity) * noise / 100;

            PeakList peakListNoise = new PeakList();
            for (int i = 0; i < peakList.getPeaklist().size(); i++) {
                if (threshold < peakList.getPeaklist().get(i).getIntensity()) {
                    peakListNoise.getPeaklist().add(peakList.getPeaklist().get(i));
                }
            }

            return peakListNoise;
    }

    static PeakList sortPeaks(PeakList peakList) {
        Collections.sort(peakList.getPeaklist(), new Comparator<Peak>() {
            @Override
            public int compare(Peak peakOne, Peak peakTwo) {
                return Double.compare(peakOne.getMz(), peakTwo.getMz());
            }
        });

        return peakList;
    }

    static String saveAnnotatedSpectrum(PeakList peakList) {
        StringBuilder stringBuilder = new StringBuilder();
        String lineSep = System.getProperty("line.separator");

        stringBuilder.append("IsotopicSet,IsotopicCluster,Peak,Charge,mZ,Intensity").append(lineSep);

        for (Peak peak : peakList.getPeaklist()) {
            stringBuilder.append(peak.getIsotopicSetID()).append(",").append(peak.getIsotopicClusterID()).append(",")
                    .append(peak.getPeakID()).append(",").append(peak.getCharge()).append(",").append(peak.getMz())
                    .append(",").append(peak.getIntensity()).append(lineSep);

        }

        return stringBuilder.toString();
    }

    static MassSpectrum makeResultSpectrum(MassSpectrum massSpectrum, PeakList peakList) {
        List<Double> mz = new ArrayList<>();
        List<Double> intensity = new ArrayList<>();
        List<Double> isotope = new ArrayList<>();
        List<Integer> charge = new ArrayList<>();
        for (int i = 0; i < peakList.getPeaklist().size(); i++) {
            mz.add(peakList.getPeaklist().get(i).getMz());
            intensity.add(peakList.getPeaklist().get(i).getIntensity());
            isotope.add(peakList.getPeaklist().get(i).getIsotope());
            charge.add(peakList.getPeaklist().get(i).getCharge());
        }

        return new MassSpectrum(massSpectrum.getTyp(), massSpectrum.getSearchEngine(), mz, intensity,
                massSpectrum.getPeptideMass(), massSpectrum.getRt(), massSpectrum.getChargeState(),
                massSpectrum.getId(), charge, isotope);
    }

    public List<Peak> getPeaklist() {
        return peaklist;
    }

    public PeakList() {
        this.peaklist = new ArrayList<>();
    }

    public PeakList(MassSpectrum massSpectrum) {
        List<Peak> plist = new ArrayList<>();

        for (int i = 0; i < massSpectrum.getMz().size() || i < massSpectrum.getIntensity().size(); i++) {
            plist.add(new Peak(massSpectrum.getMz().get(i), massSpectrum.getIntensity().get(i), i));
        }

        this.peaklist = plist;
    }

    public PeakList(List<Double> mz, List<Double> intensity) {
        List<Peak> plist = new ArrayList<>();

        for (int i = 0; i < mz.size() || i < intensity.size(); i++) {
            plist.add(new Peak(mz.get(i), intensity.get(i), i));
        }

        this.peaklist = plist;
    }

    public void add(Peak peak) {
        this.peaklist.add(peak);
    }

    public Peak get(int i) {
        return this.peaklist.get(i);
    }
}
