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
import ch.fgcz.proteomics.dto.MassSpectrumMetaInformation;
import ch.fgcz.proteomics.utilities.MathUtils;

public class PeakList implements MassSpectrumMetaInformation {
    private static final String LINESEP = System.lineSeparator();
    private double peptideMass;
    private int chargeState;

    private List<Peak> pList = new ArrayList<>();

    public boolean isEmpty() {
        return pList.isEmpty();
    }

    public void setPeakList(List<Peak> peakList) {
        this.pList = peakList;
    }

    public List<Peak> getPeakList() {
        return pList;
    }

    public PeakList() {
        chargeState = 0;
        peptideMass = 0.;
        this.pList = new ArrayList<>();
    }

    public PeakList(MassSpectrum massSpectrum) {
        this.peptideMass = massSpectrum.getPeptideMass();
        this.chargeState = massSpectrum.getChargeState();

        List<Peak> plist = new ArrayList<>();

        for (int i = 0; i < massSpectrum.getMz().size() || i < massSpectrum.getIntensity().size(); i++) {
            plist.add(new Peak(massSpectrum.getMz().get(i), massSpectrum.getIntensity().get(i), i));
        }

        this.pList = plist;
    }

    public PeakList add(Peak peak) {
        this.pList.add(peak);
        return this;
    }

    public Peak get(int i) {
        return this.pList.get(i);
    }

    public void addAll(PeakList peakList) {
        this.pList.addAll(peakList.pList);
    }

    public void addAll(List<Peak> peakList) {
        this.pList.addAll(peakList);
    }

    public int size() {
        return this.pList.size();
    }

    public PeakList(List<Peak> peaks) {
        this.pList = peaks;
    }

    public PeakList mergePeakLists(PeakList peakList2) {
        PeakList notInIsotopicSet = new PeakList();

        for (Peak peak : pList) {
            if (!peak.isInSet()) {
                notInIsotopicSet.add(peak);
            }
        }

        notInIsotopicSet.addAll(peakList2);
        return notInIsotopicSet;
    }

    public double sumIntensities() {
        double intensitySum = 0;

        for (Peak peak : this.pList) {
            intensitySum += peak.getIntensity();
        }

        return intensitySum;
    }

    public PeakList dechargePeaks(double hMass) {
        PeakList peaklistDecharged = new PeakList();

        for (int i = 0; i < pList.size(); i++) {
            if (pList.get(i).getCharge() > 1) {
                peaklistDecharged.getPeakList()
                        .add(new Peak(
                                pList.get(i).getMz() * pList.get(i).getCharge()
                                        - (pList.get(i).getCharge() - 1) * hMass,
                                pList.get(i).getIntensity(), pList.get(i).getIsotope(), 1, pList.get(i).getPeakID(),
                                pList.get(i).getIsotopicClusterID(), pList.get(i).getIsotopicSetID()));
            } else {
                peaklistDecharged.getPeakList()
                        .add(new Peak(pList.get(i).getMz(), pList.get(i).getIntensity(), pList.get(i).getIsotope(),
                                pList.get(i).getCharge(), pList.get(i).getPeakID(), pList.get(i).getIsotopicClusterID(),
                                pList.get(i).getIsotopicSetID()));
            }
        }

        return peaklistDecharged;
    }

    public PeakList filterNoisePeaks(double noise) {
        List<Double> intensity = new ArrayList<>();
        for (Peak peak : pList) {
            intensity.add(peak.getIntensity());
        }

        double threshold = Collections.max(intensity) * noise / 100;

        PeakList peaklistNoise = new PeakList();
        for (int i = 0; i < pList.size(); i++) {
            if (threshold < pList.get(i).getIntensity()) {
                peaklistNoise.getPeakList().add(pList.get(i));
            }
        }

        return peaklistNoise;
    }

    public String saveAnnotatedSpectrum() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("IsotopicSet,IsotopicCluster,Peak,Charge,mZ,Intensity").append(LINESEP);

        for (Peak peak : this.pList) {
            stringBuilder.append(peak.getIsotopicSetID()).append(",").append(peak.getIsotopicClusterID()).append(",")
                    .append(peak.getPeakID()).append(",").append(peak.getCharge()).append(",").append(peak.getMz())
                    .append(",").append(peak.getIntensity()).append(LINESEP);

        }

        return stringBuilder.toString();
    }

    public PeakList sortByMZ() {
        Collections.sort(this.pList, new Comparator<Peak>() {
            @Override
            public int compare(Peak peakOne, Peak peakTwo) {
                return Double.compare(peakOne.getMz(), peakTwo.getMz());
            }
        });
        return this;
    }

    public PeakList removeMultiplePeaks() {
        // TODO (LS) :
        ListIterator<Peak> peakListIterator = this.pList.listIterator();

        while (peakListIterator.hasNext()) {
            int index = peakListIterator.nextIndex();
            Peak currentPeak = peakListIterator.next();
            for (int j = 0; j < index; ++j) {
                if (currentPeak.equalsPeak(this.pList.get(j))) {
                    peakListIterator.remove();
                    break; // TODO why break ?
                }
            }
        }

        return this;
    }

    // TODO (LS)
    public PeakList collectForEachCharge(Peak peakI, Peak peakJ, Configuration config) {
        // check if both peaks could be in set.
        for (int charge = 1; charge < 4; charge++) {
            double lowerThreshold = peakI.getMz() + config.getIsotopicPeakDistance() / charge - config.getDelta();
            double higherThreshold = peakI.getMz() + config.getIsotopicPeakDistance() / charge + config.getDelta();

            if (lowerThreshold < peakJ.getMz() && peakJ.getMz() < higherThreshold) {
                peakI.setInSet(true);
                peakJ.setInSet(true);
                this.add(peakI);
                this.add(peakJ);
            }
        }

        return this;
    }

    public PeakList sortByPeakID() {
        Collections.sort(this.pList, new Comparator<Peak>() {
            @Override
            public int compare(Peak peakOne, Peak peakTwo) {
                return Double.compare(peakOne.getPeakID(), peakTwo.getPeakID());
            }
        });

        return this;
    }

    public boolean isSortedByMass() {
        boolean sorted = true;
        for (int i = 1; i < pList.size(); i++) {
            if (pList.get(i - 1).getMz() > (pList.get(i).getMz()))
                sorted = false;
        }
        return sorted;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Peak peak : this.pList) {
            stringBuilder.append(peak.toString()).append(LINESEP);
        }

        return "PeakList: " + LINESEP + stringBuilder.toString();
    }

    public static void checkForIntensityCorrectness(PeakList peakList1, PeakList peakList2) {
        double sumBefore = peakList1.sumIntensities();
        double sumAfter = peakList2.sumIntensities();

        if (!MathUtils.fuzzyEqual(sumBefore, sumAfter, 0.001)) {
            // throw new IllegalStateException(
            // "Wrong intensities (Intensity before: " + sumBefore + " and after: " +
            // sumAfter + "!");
        }
    }

    @Override
    public double getPeptideMass() {
        return this.peptideMass;
    }

    @Override
    public int getChargeState() {
        return this.chargeState;
    }

}
