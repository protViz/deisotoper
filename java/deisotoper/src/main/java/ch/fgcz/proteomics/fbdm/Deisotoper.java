package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-21
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.dto.MassSpectrum;

// TODO: Adjust tests!
// TODO: Change names of variables.
public class Deisotoper {
    private Configuration config;
    private String annotatedspectrum = null;
    private List<String> dotgraphs = new ArrayList<>();
    private List<IsotopicSet> isotopicsets = new ArrayList<>();

    public Configuration getConfiguration() {
        return config;
    }

    public void setConfiguration(Configuration config) {
        this.config = config;
    }

    public String getAnnotatedSpectrum() {
        return annotatedspectrum;
    }

    public void setAnnotatedSpectrum(String annotatedspectrum) {
        this.annotatedspectrum = annotatedspectrum;
    }

    public List<String> getDotGraphs() {
        return dotgraphs;
    }

    public void setDotGraphs(List<String> dotgraphs) {
        this.dotgraphs = dotgraphs;
    }

    public List<IsotopicSet> getIsotopicSets() {
        return isotopicsets;
    }

    public void setIsotopicSets(List<IsotopicSet> isotopicsets) {
        this.isotopicsets = isotopicsets;
    }

    // Will be used to deisotope entire mgf files from Java.
    public MassSpectrometryMeasurement deisotopeMSM(MassSpectrometryMeasurement massspectrometrymeasurementin,
            Configuration config) {
        MassSpectrometryMeasurement massspectrometrymeasurementout = new MassSpectrometryMeasurement(
                massspectrometrymeasurementin.getSource());

        this.config = config;

        for (MassSpectrum massspectrum : massspectrometrymeasurementin.getMSlist()) {
            massspectrometrymeasurementout.getMSlist().add(deisotopeMS(massspectrum));
        }

        return massspectrometrymeasurementout;
    }

    public MassSpectrum deisotopeMS(MassSpectrum massspectrum) {
        this.annotatedspectrum = null;
        this.dotgraphs = new ArrayList<>();
        this.isotopicsets = new ArrayList<>();

        Peaklist peaklist = new Peaklist(massspectrum);

        Peaklist peaklistisotopicsets = generateIsotopicSets(massspectrum);

        this.annotatedspectrum = saveAnnotatedSpectrum(peaklistisotopicsets);

        Peaklist peaklistaggregated = aggregatePeaks(peaklist);

        Peaklist peaklistdecharged = dechargePeaks(peaklistaggregated);

        Peaklist peaklistnoise = filterNoisePeaks(peaklistdecharged);

        Peaklist peaklist2 = sortPeaks(peaklistnoise);

        return makeResultSpectrum(massspectrum, peaklist2);
    }

    private MassSpectrum makeResultSpectrum(MassSpectrum massspectrum, Peaklist peaklist) {
        List<Double> mz = new ArrayList<>();
        List<Double> intensity = new ArrayList<>();
        List<Double> isotope = new ArrayList<>();
        List<Integer> charge = new ArrayList<>();
        for (int i = 0; i < peaklist.getPeaklist().size(); i++) {
            mz.add(peaklist.getPeaklist().get(i).getMz());
            intensity.add(peaklist.getPeaklist().get(i).getIntensity());
            isotope.add(peaklist.getPeaklist().get(i).getIsotope());
            charge.add(peaklist.getPeaklist().get(i).getCharge());
        }

        return new MassSpectrum(massspectrum.getTyp(), massspectrum.getSearchEngine(), mz, intensity,
                massspectrum.getPeptideMass(), massspectrum.getRt(), massspectrum.getChargeState(),
                massspectrum.getId(), charge, isotope);
    }

    private Peaklist generateIsotopicSets(MassSpectrum massspectrum) {
        Peaklist peaklist = new Peaklist(massspectrum);

        int id = 0;
        for (int i = 0; i < peaklist.getPeaklist().size(); i++) {
            List<Peak> isotopicset = new ArrayList<>();

            while (i < peaklist.getPeaklist().size() - 1) {
                boolean trigger = false;
                double distance = peaklist.getPeaklist().get(i + 1).getMz() - peaklist.getPeaklist().get(i).getMz();

                for (int charge = 1; charge <= 3; charge++) {
                    if ((config.getDistance() / charge) - config.getDelta() < distance
                            && distance < (config.getDistance() / charge) + config.getDelta()) {
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
                IsotopicSet temporaryisotopicset = new IsotopicSet(massspectrum, isotopicset, id, config);
                id++;

                this.isotopicsets.add(temporaryisotopicset);

                if (isotopicset.size() == peaklist.getPeaklist().size()) {
                    break;
                }
            }
        }

        Peaklist peaklistisotopicsets = getPeaklistFromIsotopicSets(peaklist);

        return sortPeaks(peaklistisotopicsets);
    }

    // TODO: Duplicated
    private Peaklist getPeaklistFromIsotopicSets(Peaklist peaklist) {
        Peaklist peaklistisotopicsets = new Peaklist();
        List<Double> mz = new ArrayList<>();

        for (IsotopicSet isotopicset : this.isotopicsets) {
            Peaklist peaklistisotopicsetsinside = new Peaklist();

            List<Double> mzinside = new ArrayList<>();

            for (IsotopicCluster cluster : isotopicset.getIsotopicSet()) {
                if (cluster.getIsotopicCluster() != null) {
                    for (Peak peak : cluster.getIsotopicCluster()) {
                        mzinside.add(peak.getMz());
                    }

                    int position = 1;
                    for (Peak peak : cluster.getIsotopicCluster()) {
                        peaklistisotopicsetsinside.getPeaklist()
                                .add(new Peak(peak.getMz(), peak.getIntensity(), (double) position, cluster.getCharge(),
                                        peak.getPeakID(), cluster.getClusterID(), isotopicset.getSetID()));
                        position++;
                    }
                }
            }

            peaklistisotopicsets.getPeaklist().addAll(peaklistisotopicsetsinside.getPeaklist());

            mz.addAll(mzinside);
        }

        for (int i = 0; i < peaklist.getPeaklist().size(); i++) {
            if (!mz.contains(peaklist.getPeaklist().get(i).getMz())) {
                peaklistisotopicsets.getPeaklist().add(new Peak(peaklist.getPeaklist().get(i).getMz(),
                        peaklist.getPeaklist().get(i).getIntensity(), peaklist.getPeaklist().get(i).getPeakID()));
            }
        }

        return peaklistisotopicsets;
    }

    // TODO: Duplicated
    private Peaklist aggregatePeaks(Peaklist peaklist) {
        Peaklist peaklistaggregated = new Peaklist();
        List<Double> mz = new ArrayList<>();

        for (IsotopicSet isotopicset : this.isotopicsets) {
            this.dotgraphs.add(isotopicset.getDot());

            List<IsotopicCluster> bestpath = isotopicset.getBestPath();

            Peaklist peaklistaggregatedinside = new Peaklist();

            List<Double> mzinside = new ArrayList<>();

            for (IsotopicCluster cluster : bestpath) {
                if (cluster.getIsotopicCluster() != null) {
                    for (Peak peak : cluster.getIsotopicCluster()) {
                        mzinside.add(peak.getMz());
                    }

                    cluster.aggregation(config.getModus());

                    int position = 1;
                    for (Peak peak : cluster.getIsotopicCluster()) {
                        peaklistaggregatedinside.getPeaklist()
                                .add(new Peak(peak.getMz(), peak.getIntensity(), (double) position, cluster.getCharge(),
                                        peak.getPeakID(), cluster.getClusterID(), isotopicset.getSetID()));
                        position++;
                    }
                }
            }

            peaklistaggregated.getPeaklist().addAll(peaklistaggregatedinside.getPeaklist());

            mz.addAll(mzinside);
        }

        for (int i = 0; i < peaklist.getPeaklist().size(); i++) {
            if (!mz.contains(peaklist.getPeaklist().get(i).getMz())) {
                peaklistaggregated.getPeaklist().add(new Peak(peaklist.getPeaklist().get(i).getMz(),
                        peaklist.getPeaklist().get(i).getIntensity(), peaklist.getPeaklist().get(i).getPeakID()));
            }
        }

        return peaklistaggregated;
    }

    private Peaklist dechargePeaks(Peaklist peaklist) {
        if (config.isDecharge() == true) {
            Peaklist peaklistdecharged = new Peaklist();

            for (int i = 0; i < peaklist.getPeaklist().size(); i++) {
                if (peaklist.getPeaklist().get(i).getCharge() > 1) {
                    peaklistdecharged.getPeaklist().add(new Peak(
                            peaklist.getPeaklist().get(i).getMz() * peaklist.getPeaklist().get(i).getCharge()
                                    - (peaklist.getPeaklist().get(i).getCharge() - 1) * config.getH_MASS(),
                            peaklist.getPeaklist().get(i).getIntensity(), peaklist.getPeaklist().get(i).getIsotope(), 1,
                            peaklist.getPeaklist().get(i).getPeakID(),
                            peaklist.getPeaklist().get(i).getIsotopicClusterID(),
                            peaklist.getPeaklist().get(i).getIsotopicSetID()));
                } else {
                    peaklistdecharged.getPeaklist().add(new Peak(peaklist.getPeaklist().get(i).getMz(),
                            peaklist.getPeaklist().get(i).getIntensity(), peaklist.getPeaklist().get(i).getIsotope(),
                            peaklist.getPeaklist().get(i).getCharge(), peaklist.getPeaklist().get(i).getPeakID(),
                            peaklist.getPeaklist().get(i).getIsotopicClusterID(),
                            peaklist.getPeaklist().get(i).getIsotopicSetID()));
                }
            }

            return peaklistdecharged;
        } else {
            return peaklist;
        }
    }

    private Peaklist filterNoisePeaks(Peaklist peaklist) {
        if (this.config.getNoise() != 0) {
            List<Double> intensity = new ArrayList<>();
            for (Peak peak : peaklist.getPeaklist()) {
                intensity.add(peak.getIntensity());
            }

            double threshold = Collections.max(intensity) * config.getNoise() / 100;

            Peaklist peaklistnoise = new Peaklist();
            for (int i = 0; i < peaklist.getPeaklist().size(); i++) {
                if (threshold < peaklist.getPeaklist().get(i).getIntensity()) {
                    peaklistnoise.getPeaklist().add(peaklist.getPeaklist().get(i));
                }
            }

            return peaklistnoise;
        } else {
            return peaklist;

        }
    }

    private String saveAnnotatedSpectrum(Peaklist peaklist) {
        StringBuilder stringbuilder = new StringBuilder();
        String linesep = System.getProperty("line.separator");

        stringbuilder.append("IsotopicSet,IsotopicCluster,Peak,Charge,mZ,Intensity").append(linesep);

        for (Peak peak : peaklist.getPeaklist()) {
            stringbuilder.append(peak.getIsotopicSetID()).append(",").append(peak.getIsotopicClusterID()).append(",")
                    .append(peak.getPeakID()).append(",").append(peak.getCharge()).append(",").append(peak.getMz())
                    .append(",").append(peak.getIntensity()).append(linesep);

        }

        return stringbuilder.toString();
    }

    private Peaklist sortPeaks(Peaklist peaklist) {
        Collections.sort(peaklist.getPeaklist(), new Comparator<Peak>() {
            @Override
            public int compare(Peak peak1, Peak peak2) {
                return Double.compare(peak1.getMz(), peak2.getMz());
            }
        });

        return peaklist;
    }
}