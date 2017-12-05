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
import ch.fgcz.proteomics.utilities.MathUtils;

public class PeakList {
    private List<Peak> peakList = new ArrayList<Peak>();

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
        this.peakList = new ArrayList<Peak>();
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
        List<Peak> plist = new ArrayList<Peak>();

        for (int i = 0; i < massSpectrum.getMz().size() || i < massSpectrum.getIntensity().size(); i++) {
            plist.add(new Peak(massSpectrum.getMz().get(i), massSpectrum.getIntensity().get(i), i));
        }

        this.peakList = plist;
    }

    public PeakList(List<Double> mz, List<Double> intensity) {
        List<Peak> plist = new ArrayList<Peak>();

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
        try {
            checkForIntensityCorrectness(new PeakList(massSpectrum), this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Double> mz = new ArrayList<Double>();
        List<Double> intensity = new ArrayList<Double>();
        List<Double> isotope = new ArrayList<Double>();
        List<Integer> charge = new ArrayList<Integer>();
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

    public double sumIntensities() {
        double intensitySum = 0;

        for (Peak peak : this.peakList) {
            intensitySum += peak.getIntensity();
        }

        return intensitySum;
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
        List<Double> intensity = new ArrayList<Double>();
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

        for (Peak peak : this.peakList) {
            stringBuilder.append(peak.getIsotopicSetID()).append(",").append(peak.getIsotopicClusterID()).append(",")
                    .append(peak.getPeakID()).append(",").append(peak.getCharge()).append(",").append(peak.getMz())
                    .append(",").append(peak.getIntensity()).append(lineSep);

        }

        return stringBuilder.toString();
    }

    public PeakList sortByMZ() {
        Collections.sort(this.peakList, new Comparator<Peak>() {
            @Override
            public int compare(Peak peakOne, Peak peakTwo) {
                return Double.compare(peakOne.getMz(), peakTwo.getMz());
            }
        });
        return this;
    }


    public PeakList removeMultiplePeaks() {
        // TODO (LS) :
        ListIterator<Peak> peakListIterator = this.peakList.listIterator();
        while (peakListIterator.hasNext()) {
            int index = peakListIterator.nextIndex();
            Peak currentPeak = peakListIterator.next();
            for (int j = 0; j < index; ++j) {
                if (currentPeak.equals(this.peakList.get(j))) {
                    peakListIterator.remove();
                    break; //TODO why break ?
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
        Collections.sort(this.peakList, new Comparator<Peak>() {
            @Override
            public int compare(Peak peakOne, Peak peakTwo) {
                return Double.compare(peakOne.getPeakID(), peakTwo.getPeakID());
            }
        });

        return this;
    }

    public boolean isSortedByMass()
    {
        boolean sorted = true;
        for (int i = 1; i < peakList.size(); i++) {
            if (peakList.get(i-1).getMz() > (peakList.get(i).getMz())) sorted = false;
        }
        return sorted;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Peak peak : this.peakList) {
            stringBuilder.append(peak.toString()).append(System.getProperty("line.separator"));
        }

        return "PeakList: " + System.getProperty("line.separator") + stringBuilder.toString();
    }

    private void checkForIntensityCorrectness(PeakList peakList1, PeakList peakList2) throws Exception {
        double sumBefore = peakList1.sumIntensities();
        double sumAfter = peakList2.sumIntensities();
        if(! MathUtils.fuzzyEqual(sumBefore, sumAfter, 0.001)){
            throw new Exception("Wrong intensities (Intensity before: " + sumBefore + " and after: " + sumAfter + "!");
        }
    }


}
