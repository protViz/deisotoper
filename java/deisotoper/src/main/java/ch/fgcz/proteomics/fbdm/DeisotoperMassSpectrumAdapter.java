package ch.fgcz.proteomics.fbdm;

import ch.fgcz.proteomics.dto.MassSpecMeasure;
import ch.fgcz.proteomics.dto.MassSpectrum;

import java.util.ArrayList;
import java.util.List;

public class DeisotoperMassSpectrumAdapter {
    Deisotoper deisotoper;

    public DeisotoperMassSpectrumAdapter(Deisotoper deisotoper) {
        this.deisotoper = deisotoper;
    }

    public MassSpecMeasure deisotopeMSM(MassSpecMeasure massSpectrometryMeasurementin, Configuration config) {
        MassSpecMeasure massSpectrometryMeasurementOut = new MassSpecMeasure(massSpectrometryMeasurementin.getSource());

        deisotoper.setConfiguration(config);

        for (MassSpectrum massSpectrum : massSpectrometryMeasurementin.getMSlist()) {
            massSpectrometryMeasurementOut.getMSlist().add(this.deisotopeMS(massSpectrum));
        }
        return massSpectrometryMeasurementOut;
    }

    public MassSpectrum makeResultSpectrum(MassSpectrum massSpectrum, PeakList peakList) {

        List<Double> mz = new ArrayList<Double>();
        List<Double> intensity = new ArrayList<Double>();
        List<Double> isotope = new ArrayList<Double>();
        List<Integer> charge = new ArrayList<Integer>();
        for (int i = 0; i < peakList.size(); i++) {
            mz.add(peakList.get(i).getMz());
            intensity.add(peakList.get(i).getIntensity());
            isotope.add(peakList.get(i).getIsotope());
            charge.add(peakList.get(i).getCharge());
        }

        return new MassSpectrum(massSpectrum.getTyp(), massSpectrum.getSearchEngine(), mz, intensity,
                massSpectrum.getPeptideMass(), massSpectrum.getRt(), massSpectrum.getChargeState(),
                massSpectrum.getId(), charge, isotope);
    }

    public MassSpectrum deisotopeMS(MassSpectrum massSpectrum) {
        PeakList peakList = this.deisotoper.deisotopeMS(new PeakList(massSpectrum));
        return makeResultSpectrum(massSpectrum, peakList);
    }

    public void setConfiguration(Configuration config) {
        this.deisotoper.setConfiguration(config);
    }

    public String getConfiguration() {
        return this.deisotoper.getConfiguration().toString();
    }

    public List<String> getDotGraphs() {
        return this.deisotoper.getDotGraphs();
    }

    public String getAnnotatedSpectrum() {
        return this.deisotoper.getAnnotatedSpectrum();
    }

    public int getNrOfIsotopicSets() {
        return this.deisotoper.getIsotopicSets().size();
    }

    public int getNrOfIsotopePatterns() {
        int numberOfIsotopicClusters = 0;
        for (IsotopicSet isotopicSet : this.deisotoper.getIsotopicSets()) {
            numberOfIsotopicClusters += isotopicSet.getBestPath().size();
        }
        return numberOfIsotopicClusters;
    }

    public int getNrOfPeaksInIsotopePatterns() {
        int numberOfIsotopicPeaks = 0;
        for (IsotopicSet isotopicSet : this.deisotoper.getIsotopicSets()) {
            for (IsotopicCluster isotopicCluster : isotopicSet.getBestPath()) {
                numberOfIsotopicPeaks += isotopicCluster.getIsotopicCluster().size();
            }
        }
        return numberOfIsotopicPeaks;
    }
}
