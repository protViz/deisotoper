package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-04
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import ch.fgcz.proteomics.dto.MassSpectrum;

public class PeakList {
    private List<Peak> peakList = new ArrayList<>();

    public boolean isEmpty() {
        return peakList.isEmpty();
    }

    public void setPeakList(List<Peak> peakList) {
        this.peakList = peakList;
    }

    public List<Peak> getPeakList() {
        return peakList;
    }

    public PeakList() {
        this.peakList = new ArrayList<>();
    }

    public PeakList add(Peak peak) {
        this.peakList.add(peak);
        return this;
    }

    public Peak get(int i) {
        return this.peakList.get(i);
    }

    public void addAll(PeakList peakList) {
        this.peakList.addAll(peakList.peakList);
    }

    public void addAll(List<Peak> peakList) {
        this.peakList.addAll(peakList);
    }

    public int size() {
        return this.peakList.size();
    }

    public PeakList(List<Peak> peaks) {
        this.peakList = peaks;
    }

    public PeakList(MassSpectrum massSpectrum) {
        List<Peak> plist = new ArrayList<>();

        for (int i = 0; i < massSpectrum.getMz().size() || i < massSpectrum.getIntensity().size(); i++) {
            plist.add(new Peak(massSpectrum.getMz().get(i), massSpectrum.getIntensity().get(i), i));
        }

        this.peakList = plist;
    }

    public PeakList(List<Double> mz, List<Double> intensity) {
        List<Peak> plist = new ArrayList<>();

        for (int i = 0; i < mz.size() || i < intensity.size(); i++) {
            plist.add(new Peak(mz.get(i), intensity.get(i), i));
        }

        this.peakList = plist;
    }

    public PeakList mergePeakLists(PeakList peakList2) {
        PeakList notInIsotopicSet = new PeakList();

        for (Peak peak : peakList) {
            if (!peak.isInSet()) {
                notInIsotopicSet.add(peak);
            }
        }

        notInIsotopicSet.addAll(peakList2);
        return notInIsotopicSet;
    }

    public MassSpectrum makeResultSpectrum(MassSpectrum massSpectrum) {
        List<Double> mz = new ArrayList<>();
        List<Double> intensity = new ArrayList<>();
        List<Double> isotope = new ArrayList<>();
        List<Integer> charge = new ArrayList<>();
        for (int i = 0; i < this.peakList.size(); i++) {
            mz.add(this.peakList.get(i).getMz());
            intensity.add(this.peakList.get(i).getIntensity());
            isotope.add(this.peakList.get(i).getIsotope());
            charge.add(this.peakList.get(i).getCharge());
        }

        return new MassSpectrum(massSpectrum.getTyp(), massSpectrum.getSearchEngine(), mz, intensity,
                massSpectrum.getPeptideMass(), massSpectrum.getRt(), massSpectrum.getChargeState(),
                massSpectrum.getId(), charge, isotope);
    }

    public PeakList dechargePeaks(double H_MASS) {
        PeakList peaklistDecharged = new PeakList();

        for (int i = 0; i < peakList.size(); i++) {
            if (peakList.get(i).getCharge() > 1) {
                peaklistDecharged.getPeakList()
                        .add(new Peak(
                                peakList.get(i).getMz() * peakList.get(i).getCharge()
                                        - (peakList.get(i).getCharge() - 1) * H_MASS,
                                peakList.get(i).getIntensity(), peakList.get(i).getIsotope(), 1,
                                peakList.get(i).getPeakID(), peakList.get(i).getIsotopicClusterID(),
                                peakList.get(i).getIsotopicSetID()));
            } else {
                peaklistDecharged.getPeakList()
                        .add(new Peak(peakList.get(i).getMz(), peakList.get(i).getIntensity(),
                                peakList.get(i).getIsotope(), peakList.get(i).getCharge(), peakList.get(i).getPeakID(),
                                peakList.get(i).getIsotopicClusterID(), peakList.get(i).getIsotopicSetID()));
            }
        }

        return peaklistDecharged;
    }

    public PeakList filterNoisePeaks(double noise) {
        List<Double> intensity = new ArrayList<>();
        for (Peak peak : peakList) {
            intensity.add(peak.getIntensity());
        }

        double threshold = Collections.max(intensity) * noise / 100;

        PeakList peaklistNoise = new PeakList();
        for (int i = 0; i < peakList.size(); i++) {
            if (threshold < peakList.get(i).getIntensity()) {
                peaklistNoise.getPeakList().add(peakList.get(i));
            }
        }

        return peaklistNoise;
    }

    public String saveAnnotatedSpectrum() {
        StringBuilder stringBuilder = new StringBuilder();
        String lineSep = System.getProperty("line.separator");

        stringBuilder.append("IsotopicSet,IsotopicCluster,Peak,Charge,mZ,Intensity").append(lineSep);

        for (Peak peak : peakList) {
            stringBuilder.append(peak.getIsotopicSetID()).append(",").append(peak.getIsotopicClusterID()).append(",")
                    .append(peak.getPeakID()).append(",").append(peak.getCharge()).append(",").append(peak.getMz())
                    .append(",").append(peak.getIntensity()).append(lineSep);

        }

        return stringBuilder.toString();
    }

    public PeakList sortPeakList() {
        Collections.sort(this.peakList, new Comparator<Peak>() {
            @Override
            public int compare(Peak peakOne, Peak peakTwo) {
                return Double.compare(peakOne.getMz(), peakTwo.getMz());
            }
        });
        return this;
    }

    public PeakList removeMultiplePeaks() {
        ListIterator<Peak> peakListIterator = this.peakList.listIterator();
        while (peakListIterator.hasNext()) {
            int index = peakListIterator.nextIndex();
            Peak currentPeak = peakListIterator.next();
            for (int j = 0; j < index; ++j) {
                if (currentPeak.equals(this.peakList.get(j))) {
                    peakListIterator.remove();
                    break;
                }
            }
        }

        return this;

        // Old implementation
        // PeakList result = new PeakList();
        // Set<Peak> set = new HashSet<>();
        //
        // for (Peak peak : this.getPeakList()) {
        // if (set.add(peak)) {
        // result.add(peak);
        // }
        // }
        //
        // return result;
    }

    public PeakList sortForAnnotating() {
        Collections.sort(this.peakList, new Comparator<Peak>() {
            @Override
            public int compare(Peak peakOne, Peak peakTwo) {
                return Double.compare(peakOne.getPeakID(), peakTwo.getPeakID());
            }
        });

        return this;
    }
}
