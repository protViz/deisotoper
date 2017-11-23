package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-21
 */

import java.util.ArrayList;
import java.util.List;

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.dto.MassSpectrum;

// TODO: Adjust tests!
public class Deisotoper {
    private Configuration config;
    private String annotatedSpectrum = null;
    private List<IsotopicSet> isotopicSets = new ArrayList<>();
    private PeakList peakList;
    private PeakList mergedPeakList;

    public Configuration getConfiguration() {
        return config;
    }

    public void setConfiguration(Configuration config) {
        this.config = config;
    }

    public String getAnnotatedSpectrum() {
        return PeakList.saveAnnotatedSpectrum(mergedPeakList);
    }

    public List<String> getDotGraphs() {
        List<String> graph = new ArrayList<>();
        for (IsotopicSet isotopicSet : this.isotopicSets) {
            graph.add(isotopicSet.getDot());
        }
        return graph;
    }

    public List<IsotopicSet> getIsotopicSets() {
        return isotopicSets;
    }

    // Will be used to deisotope entire mgf files from Java.
    public MassSpectrometryMeasurement deisotopeMSM(MassSpectrometryMeasurement massSpectrometryMeasurementin,
            Configuration config) {
        MassSpectrometryMeasurement massSpectrometryMeasurementOut = new MassSpectrometryMeasurement(
                massSpectrometryMeasurementin.getSource());

        this.config = config;

        for (MassSpectrum massSpectrum : massSpectrometryMeasurementin.getMSlist()) {
            massSpectrometryMeasurementOut.getMSlist().add(deisotopeMS(massSpectrum));
        }

        return massSpectrometryMeasurementOut;
    }

    public MassSpectrum deisotopeMS(MassSpectrum massSpectrum) {
        this.annotatedSpectrum = null;
        this.isotopicSets = new ArrayList<>();


        generateIsotopicSets(massSpectrum);

        List<IsotopicCluster> bestClusters = getBestClusters();
        PeakList peakListAggregated = aggregate(bestClusters, config.getModus());

        if(config.isDecharge()) {
            peakListAggregated = PeakList.dechargePeaks(peakListAggregated, config.getH_MASS());
        }
        mergedPeakList = merge(peakListAggregated);

        if (this.config.getNoise() != 0) {
            mergedPeakList = PeakList.filterNoisePeaks(mergedPeakList, config.getNoise());
        }

        this.mergedPeakList = PeakList.sortPeaks(mergedPeakList);
        return PeakList.makeResultSpectrum(massSpectrum, mergedPeakList);
    }

    private PeakList merge(PeakList peakListAggregated) {
        PeakList notInIsotopicSet = new PeakList();
        for(Peak peak : peakList.getPeaklist()){
            if(!peak.isInSet()){
                notInIsotopicSet.add(peak);
            }

        }
        notInIsotopicSet.getPeaklist().addAll(peakListAggregated.getPeaklist());
        return notInIsotopicSet;
    }


    // TODO : 1, 1.1, 1.5 then 1, 1.5 in isotopicSet
    // TODO : 1, 1.1, 1.5 than all in isotopicSet.
    private void generateIsotopicSets(MassSpectrum massSpectrum) {
        peakList = new PeakList(massSpectrum);

        int id = 0;
        for (int i = 0; i < peakList.getPeaklist().size(); i++) {
            List<Peak> isotopicSet = new ArrayList<>();

            while (i < peakList.getPeaklist().size() - 1) {

                boolean trigger = false;
                double distance = peakList.get(i + 1).getMz() - peakList.get(i).getMz();

                for (int charge = 1; charge <= 3; charge++) {
                    if ((config.getDistance() / charge) - config.getDelta() < distance
                            && distance < (config.getDistance() / charge) + config.getDelta()) {
                        if (isotopicSet.size() == 0) {
                            Peak peak =  peakList.get(i);
                            peak.inSet();
                            isotopicSet.add(peak);
                        }

                        Peak peak =  peakList.get(i + 1);
                        peak.inSet();
                        isotopicSet.add(peak);
                        trigger = true;
                    }
                }

                if (trigger == false) {
                    break;
                }

                i++;
            }

            if (1 < isotopicSet.size()) {
                IsotopicSet temporaryIsotopicSet = new IsotopicSet(massSpectrum, isotopicSet, id, config);
                id++;

                this.isotopicSets.add(temporaryIsotopicSet);

                if (isotopicSet.size() == peakList.getPeaklist().size()) {
                    break;
                }
            }
        }
    }

    private List<IsotopicCluster> getBestClusters() {
        List<IsotopicCluster> bestClusters = new ArrayList<>();
        for (IsotopicSet isotopicSet : this.isotopicSets) {
            bestClusters.addAll(isotopicSet.getBestPath());
        }
        return bestClusters;
    }

    static private PeakList aggregate(List<IsotopicCluster> isotopicClusters, String modus){
       PeakList resultPeakList = new PeakList();
        for (IsotopicCluster istotopicCluster: isotopicClusters ) {
            IsotopicCluster aggragetedCluster = istotopicCluster.aggregation(modus);
            Peak peak = aggragetedCluster.getIsotopicCluster().get(0);
            resultPeakList.add( peak );
        }
        return resultPeakList;
    }

}