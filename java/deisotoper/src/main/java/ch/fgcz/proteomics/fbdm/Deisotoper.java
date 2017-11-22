package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-21
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.dto.MassSpectrum;
import ch.fgcz.proteomics.utilities.Sort;

public class Deisotoper {
    private Configuration config;
    private List<IsotopicClusterGraph> isotopicclustergraphlist = new ArrayList<>();
    private List<IsotopicSet> isotopicSets = new ArrayList<>();

    static public List<IsotopicSet> generateIsotopicSets(MassSpectrum massspectrum, double delta, Configuration config,
                                                         String modus) {

        List<IsotopicSet> isotopicSets = new ArrayList<>();
        Peaklist peaklist = new Peaklist(massspectrum);
        int id = 0;
        IsotopicSet is = null;
        for (int i = 0; i < peaklist.getPeaklist().size(); i++) {
            List<Peak> isotopicset = new ArrayList<>();

            while (i < peaklist.getPeaklist().size() - 1) {
                boolean trigger = false;
                double distance = peaklist.getPeaklist().get(i + 1).getMz() - peaklist.getPeaklist().get(i).getMz();

                for (int charge = 1; charge <= 3; charge++) {
                    if ((config.getDistance() / charge) - delta < distance
                            && distance < (config.getDistance() / charge) + delta) {
                        if (isotopicset.size() == 0) {
                            isotopicset.add((peaklist.getPeaklist().get(i)));
                        }
                        isotopicset.add((peaklist.getPeaklist().get(i + 1)));
                        trigger = true;
                    }
                }

                if (trigger == false) {
                    break;
                }

                i++;
            }

            if (1 < isotopicset.size()) {
                is = new IsotopicSet(massspectrum, isotopicset, delta, id, config);
                id++;

                isotopicSets.add(is);

                if (isotopicset.size() == peaklist.getPeaklist().size()) {
                    break;
                }
            }
        }

        return isotopicSets;
        //this.aggregatedpeaklist = deisotoper.aggregation(massspectrum, modus, this);
    }

    public Configuration getConfiguration() {
        return config;
    }

    public void setConfiguration(Configuration config) {
        this.config = config;
    }

    public List<IsotopicClusterGraph> getIsotopicClusterGraphList() {
        return isotopicclustergraphlist;
    }


    public String getAnnotatedSpectrum() {

        StringBuilder stringbuilder = new StringBuilder();
        String linesep = System.getProperty("line.separator");

        stringbuilder.append("IsotopicSet,IsotopicCluster,Peak,Charge,mZ,Intensity").append(linesep);

        for (IsotopicSet isotopicset : isotopicSets) {
            for (IsotopicCluster isotopiccluster : isotopicset.getIsotopicSet()) {
                if (isotopiccluster.getIsotopicCluster() != null) {
                    for (Peak peak : isotopiccluster.getIsotopicCluster()) {
                        stringbuilder.append(isotopicset.getSetID()).append(",").append(isotopiccluster.getClusterID())
                                .append(",").append(peak.getPeakID()).append(",").append(isotopiccluster.getCharge())
                                .append(",").append(peak.getMz()).append(",").append(peak.getIntensity())
                                .append(linesep);
                    }
                }
            }
        }
        return stringbuilder.toString();
    }

    // Will be used to deisotop entire mgf files from Java.
    public MassSpectrometryMeasurement deisotopeMSM(MassSpectrometryMeasurement massspectrometrymeasurementin,
                                                    String modus, Configuration config) {
        MassSpectrometryMeasurement massspectrometrymeasurementout = new MassSpectrometryMeasurement(
                massspectrometrymeasurementin.getSource());

        this.config = config;

        for (MassSpectrum massspectrum : massspectrometrymeasurementin.getMSlist()) {
            massspectrometrymeasurementout.getMSlist().add(deisotopeMS(massspectrum, modus));
        }

        return massspectrometrymeasurementout;
    }

    //TODO
    public MassSpectrum deisotopeMS(MassSpectrum massspectrum, String modus) {


        this.isotopicSets = generateIsotopicSets(massspectrum, this.config.getDelta(),
                this.config, modus);

        // iterate over istopicSets and get all Clusters in best paths.
        // List<IstopicClusters> bestIsotopicCluster = getClustersInBestPaths();

        // aggragete the best isotopic clusters
        // PeakList aggregatedPeaks = aggregateClusters(bestIsotopicCluster);

        // Merge both peaksLists and return;
        // deisotopedPeaklist = mergePeaksLists(aggregatedPeaks, peaksNotInIsotopicSet);
        // return deisotopedPeakslist;

        /*
        Peaklist listmassspectrumaggregated = isotopicmassspectrum.getAggregatedPeaklist();

        Peaklist listmassspectrumdecharged = decharge(listmassspectrumaggregated, this.config);

        Sort.keySort(listmassspectrumdecharged.getMz(), listmassspectrumdecharged.getMz(),
                listmassspectrumdecharged.getIntensity(), listmassspectrumdecharged.getIsotope(),
                listmassspectrumdecharged.getCharge());

        MassSpectrum massspectrumdeisotoped = noiseFiltering(massspectrum, listmassspectrumdecharged, this.config);
        return massspectrumdeisotoped;
        */

        return null;
    }

    private MassSpectrum noiseFiltering(MassSpectrum massspectrum, Peaklist listmassspectrum, Configuration config) {
        MassSpectrum massspectrundeisotoped;

        if (config.getNoise() != 0) {
            double threshold = Collections.max(listmassspectrum.getIntensity()) * config.getNoise() / 100;

            List<Double> mz5 = new ArrayList<>();
            List<Double> intensity5 = new ArrayList<>();
            List<Double> isotope5 = new ArrayList<>();
            List<Integer> charge5 = new ArrayList<>();

            for (int i = 0; i < listmassspectrum.getIntensity().size(); i++) {
                if (threshold < listmassspectrum.getIntensity().get(i)) {
                    mz5.add(listmassspectrum.getMz().get(i));
                    intensity5.add(listmassspectrum.getIntensity().get(i));
                    isotope5.add(listmassspectrum.getIsotope().get(i));
                    charge5.add(listmassspectrum.getCharge().get(i));
                }
            }

            massspectrundeisotoped = new MassSpectrum(massspectrum.getTyp(), massspectrum.getSearchEngine(), mz5,
                    intensity5, massspectrum.getPeptideMass(), massspectrum.getRt(), massspectrum.getChargeState(),
                    massspectrum.getId(), charge5, isotope5);
        } else {
            massspectrundeisotoped = new MassSpectrum(massspectrum.getTyp(), massspectrum.getSearchEngine(),
                    listmassspectrum.getMz(), listmassspectrum.getIntensity(), massspectrum.getPeptideMass(),
                    massspectrum.getRt(), massspectrum.getChargeState(), massspectrum.getId(),
                    listmassspectrum.getCharge(), listmassspectrum.getIsotope());
        }

        return massspectrundeisotoped;
    }

    private Peaklist decharge(Peaklist listmassspectrum, Configuration config) {
        Peaklist listmassspectrumdecharged = new Peaklist();

        if (config.isDecharge() == true) {
            for (int i = 0; i < listmassspectrum.getMz().size(); i++) {
                if (listmassspectrum.getCharge().get(i) > 1) {
                    listmassspectrumdecharged.getMz()
                            .add(listmassspectrum.getMz().get(i) * listmassspectrum.getCharge().get(i)
                                    - (listmassspectrum.getCharge().get(i) - 1) * config.getH_MASS());
                    listmassspectrumdecharged.getIntensity().add(listmassspectrum.getIntensity().get(i));
                    listmassspectrumdecharged.getIsotope().add(listmassspectrum.getIsotope().get(i));
                    listmassspectrumdecharged.getCharge().add(1);
                } else {
                    listmassspectrumdecharged.getMz().add(listmassspectrum.getMz().get(i));
                    listmassspectrumdecharged.getIntensity().add(listmassspectrum.getIntensity().get(i));
                    listmassspectrumdecharged.getIsotope().add(listmassspectrum.getIsotope().get(i));
                    listmassspectrumdecharged.getCharge().add(listmassspectrum.getCharge().get(i));
                }
            }
        } else {
            listmassspectrumdecharged.getMz().addAll(listmassspectrum.getMz());
            listmassspectrumdecharged.getIntensity().addAll(listmassspectrum.getIntensity());
            listmassspectrumdecharged.getIsotope().addAll(listmassspectrum.getIsotope());
            listmassspectrumdecharged.getCharge().addAll(listmassspectrum.getCharge());
        }

        return listmassspectrumdecharged;
    }

    public Peaklist aggregation(MassSpectrum massspectrumin, String modus) {
        getIsotopicClusterGraphList().removeAll(getIsotopicClusterGraphList());
        Peaklist listmassspectrumaggregated = new Peaklist();
        List<Double> mz = new ArrayList<>();

        for (IsotopicSet isotopicset :isotopicSets) {

            List<IsotopicCluster> bestpath = isotopicset.getBestPath();

            getIsotopicClusterGraphList().add(isotopicset.getIsotopicClusterGraph());

            Peaklist listmassspectrumaggregated2 = new Peaklist();

            List<Double> mz2 = new ArrayList<>();

            for (IsotopicCluster cluster : bestpath) {
                if (cluster.getIsotopicCluster() != null) {
                    for (Peak peak : cluster.getIsotopicCluster()) {
                        mz2.add(peak.getMz());
                    }

                    cluster.aggregation(modus);

                    int position = 1;
                    for (Peak peak : cluster.getIsotopicCluster()) {
                        listmassspectrumaggregated2.getMz().add(peak.getMz());
                        listmassspectrumaggregated2.getIntensity().add(peak.getIntensity());
                        listmassspectrumaggregated2.getIsotope().add((double) position);
                        listmassspectrumaggregated2.getCharge().add(cluster.getCharge());
                        position++;
                    }
                }
            }

            listmassspectrumaggregated.getMz().addAll(listmassspectrumaggregated2.getMz());
            listmassspectrumaggregated.getIntensity().addAll(listmassspectrumaggregated2.getIntensity());
            listmassspectrumaggregated.getIsotope().addAll(listmassspectrumaggregated2.getIsotope());
            listmassspectrumaggregated.getCharge().addAll(listmassspectrumaggregated2.getCharge());

            mz.addAll(mz2);
        }

        for (int i = 0; i < massspectrumin.getMz().size(); i++) {
            if (!mz.contains(massspectrumin.getMz().get(i))) {
                listmassspectrumaggregated.getMz().add(massspectrumin.getMz().get(i));
                listmassspectrumaggregated.getIntensity().add(massspectrumin.getIntensity().get(i));
                listmassspectrumaggregated.getIsotope().add(-1.0);
                listmassspectrumaggregated.getCharge().add(-1);
            }
        }

        return listmassspectrumaggregated;
    }
}