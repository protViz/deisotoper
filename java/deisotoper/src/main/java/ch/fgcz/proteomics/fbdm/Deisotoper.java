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
public class Deisotoper {
    private Configuration config;
    private String annotatedSpectrum = null;
    private List<String> dotGraphs = new ArrayList<>();
    private List<IsotopicSet> isotopicSets = new ArrayList<>();

    public Configuration getConfiguration() {
        return config;
    }

    public void setConfiguration(Configuration config) {
        this.config = config;
    }

    public String getAnnotatedSpectrum() {
        return annotatedSpectrum;
    }

    public void setAnnotatedSpectrum(String annotatedSpectrum) {
        this.annotatedSpectrum = annotatedSpectrum;
    }

    public List<String> getDotGraphs() {
        return dotGraphs;
    }

    public void setDotGraphs(List<String> dotGraphs) {
        this.dotGraphs = dotGraphs;
    }

    public List<IsotopicSet> getIsotopicSets() {
        return isotopicSets;
    }

    public void setIsotopicSets(List<IsotopicSet> isotopicSets) {
        this.isotopicSets = isotopicSets;
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
        this.dotGraphs = new ArrayList<>();
        this.isotopicSets = new ArrayList<>();

        Peaklist peaklistIn = new Peaklist(massSpectrum);

        generateIsotopicSets(massSpectrum);

        Peaklist peaklistAggregated = aggregatePeaks(peaklistIn);

        Peaklist peaklistDecharged = dechargePeaks(peaklistAggregated);

        Peaklist peaklistNoise = filterNoisePeaks(peaklistDecharged);

        Peaklist peaklistOut = sortPeaks(peaklistNoise);

        return makeResultSpectrum(massSpectrum, peaklistOut);
    }

    private MassSpectrum makeResultSpectrum(MassSpectrum massSpectrum, Peaklist peaklist) {
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

        return new MassSpectrum(massSpectrum.getTyp(), massSpectrum.getSearchEngine(), mz, intensity,
                massSpectrum.getPeptideMass(), massSpectrum.getRt(), massSpectrum.getChargeState(),
                massSpectrum.getId(), charge, isotope);
    }

    private void generateIsotopicSets(MassSpectrum massSpectrum) {
        Peaklist peaklist = new Peaklist(massSpectrum);

        int id = 0;
        for (int i = 0; i < peaklist.getPeaklist().size(); i++) {
            List<Peak> isotopicSet = new ArrayList<>();

            while (i < peaklist.getPeaklist().size() - 1) {
                boolean trigger = false;
                double distance = peaklist.getPeaklist().get(i + 1).getMz() - peaklist.getPeaklist().get(i).getMz();

                for (int charge = 1; charge <= 3; charge++) {
                    if ((config.getDistance() / charge) - config.getDelta() < distance
                            && distance < (config.getDistance() / charge) + config.getDelta()) {
                        if (isotopicSet.size() == 0) {
                            isotopicSet.add((peaklist.getPeaklist().get(i)));
                        }
                        isotopicSet.add((peaklist.getPeaklist().get(i + 1)));
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

                if (isotopicSet.size() == peaklist.getPeaklist().size()) {
                    break;
                }
            }
        }

        Peaklist peaklistIsotopicSets = getPeaklistFromIsotopicSets(peaklist);

        this.annotatedSpectrum = saveAnnotatedSpectrum(peaklistIsotopicSets);
    }

    private Peaklist getPeaklistFromIsotopicSets(Peaklist peaklist) {
        return getPeaklistFromIsotopicSets(peaklist, false);
    }

    private Peaklist aggregatePeaks(Peaklist peaklist) {
        return getPeaklistFromIsotopicSets(peaklist, true);
    }

    private Peaklist getPeaklistFromIsotopicSets(Peaklist peaklistIn, boolean aggregation) {
        Peaklist peaklistOut = new Peaklist();
        List<Double> mz = new ArrayList<>();

        for (IsotopicSet isotopicSet : this.isotopicSets) {
            this.dotGraphs.add(isotopicSet.getDot());

            List<IsotopicCluster> bestpath = isotopicSet.getBestPath();

            Peaklist peaklistAggregatedInside = new Peaklist();

            List<Double> mzInside = new ArrayList<>();

            for (IsotopicCluster cluster : bestpath) {
                if (cluster.getIsotopicCluster() != null) {
                    for (Peak peak : cluster.getIsotopicCluster()) {
                        mzInside.add(peak.getMz());
                    }

                    if (aggregation) {
                        cluster.aggregation(config.getModus());
                    }

                    int position = 1;
                    for (Peak peak : cluster.getIsotopicCluster()) {
                        peaklistAggregatedInside.getPeaklist()
                                .add(new Peak(peak.getMz(), peak.getIntensity(), (double) position, cluster.getCharge(),
                                        peak.getPeakID(), cluster.getClusterID(), isotopicSet.getSetID()));
                        position++;
                    }
                }
            }

            peaklistOut.getPeaklist().addAll(peaklistAggregatedInside.getPeaklist());

            mz.addAll(mzInside);
        }

        for (int i = 0; i < peaklistIn.getPeaklist().size(); i++) {
            if (!mz.contains(peaklistIn.getPeaklist().get(i).getMz())) {
                peaklistOut.getPeaklist().add(new Peak(peaklistIn.getPeaklist().get(i).getMz(),
                        peaklistIn.getPeaklist().get(i).getIntensity(), peaklistIn.getPeaklist().get(i).getPeakID()));
            }
        }

        return sortPeaks(peaklistOut);
    }

    private Peaklist dechargePeaks(Peaklist peaklist) {
        if (config.isDecharge() == true) {
            Peaklist peaklistDecharged = new Peaklist();

            for (int i = 0; i < peaklist.getPeaklist().size(); i++) {
                if (peaklist.getPeaklist().get(i).getCharge() > 1) {
                    peaklistDecharged.getPeaklist().add(new Peak(
                            peaklist.getPeaklist().get(i).getMz() * peaklist.getPeaklist().get(i).getCharge()
                                    - (peaklist.getPeaklist().get(i).getCharge() - 1) * config.getH_MASS(),
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

            Peaklist peaklistNoise = new Peaklist();
            for (int i = 0; i < peaklist.getPeaklist().size(); i++) {
                if (threshold < peaklist.getPeaklist().get(i).getIntensity()) {
                    peaklistNoise.getPeaklist().add(peaklist.getPeaklist().get(i));
                }
            }

            return peaklistNoise;
        } else {
            return peaklist;
        }
    }

    private String saveAnnotatedSpectrum(Peaklist peaklist) {
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

    private Peaklist sortPeaks(Peaklist peaklist) {
        Collections.sort(peaklist.getPeaklist(), new Comparator<Peak>() {
            @Override
            public int compare(Peak peakOne, Peak peakTwo) {
                return Double.compare(peakOne.getMz(), peakTwo.getMz());
            }
        });

        return peaklist;
    }
}