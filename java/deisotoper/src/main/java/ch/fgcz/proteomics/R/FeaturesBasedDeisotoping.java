package ch.fgcz.proteomics.R;

/**
 * @author Lucas Schmidt
 * @since 2017-11-16
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.fgcz.proteomics.dto.MassSpectrum;
import ch.fgcz.proteomics.fbdm.Configuration;
import ch.fgcz.proteomics.fbdm.Deisotoper;
import ch.fgcz.proteomics.fbdm.IsotopicCluster;
import ch.fgcz.proteomics.fbdm.IsotopicSet;
import ch.fgcz.proteomics.fbdm.Peak;

public class FeaturesBasedDeisotoping {
    private Deisotoper deisotoper = new Deisotoper();
    private MassSpectrum massSpectrum = new MassSpectrum();
    private MassSpectrum resultSpectrum = new MassSpectrum();

    public void setConfiguration(double[] aaMass, double F1, double F2, double F3, double F4, double F5, double delta,
            double errortolerance, double distance, double noise, boolean decharge, String modus) {
        Configuration config;
        if (aaMass.length > 1) {
            List<Double> aaMassList = new ArrayList<>();
            for (int i = 0; i < aaMass.length; i++) {
                aaMassList.add(aaMass[i]);
            }

            config = new Configuration(aaMassList, F1, F2, F3, F4, F5, delta, errortolerance, distance, noise, decharge,
                    modus);
        } else {
            config = new Configuration(F1, F2, F3, F4, F5, delta, errortolerance, distance, noise, decharge, modus);
        }

        this.deisotoper.setConfiguration(config);
    }

    public String getConfiguration() {
        return this.deisotoper.getConfiguration().toString();
    }

    public void setMz(double[] mz) {
        List<Double> mzList = new ArrayList<>();
        for (int i = 0; i < mz.length; i++) {
            mzList.add(mz[i]);
        }

        this.massSpectrum.setMz(mzList);
    }

    public void setIntensity(double[] intensity) {
        List<Double> intensityList = new ArrayList<>();
        for (int i = 0; i < intensity.length; i++) {
            intensityList.add(intensity[i]);
        }

        this.massSpectrum.setIntensity(intensityList);
    }

    public void setPepMass(double peptidMass) {
        this.massSpectrum.setPeptideMass(peptidMass);
    }

    public void setCharge(int charge) {
        this.massSpectrum.setChargeState(charge);
    }

    public double[] getMz() {
        return this.resultSpectrum.getMzArray();
    }

    public double[] getIntensity() {
        return this.resultSpectrum.getIntensityArray();
    }

    public void deisotope() {
        this.resultSpectrum = this.deisotoper.deisotopeMS(this.massSpectrum);
    }

    public String[] getDot() {
        try {
            this.deisotoper.wasRunning();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] dotGraphs = new String[this.deisotoper.getDotGraphs().size()];

        for (int i = 0; i < this.deisotoper.getDotGraphs().size(); i++) {
            dotGraphs[i] = this.deisotoper.getDotGraphs().get(i);
        }

        return dotGraphs;
    }

    public String getAnnotatedSpectrum() {
        try {
            this.deisotoper.wasRunning();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.deisotoper.getAnnotatedSpectrum();
    }

    public String getSummary() {
        try {
            this.deisotoper.wasRunning();
        } catch (Exception e) {
            e.printStackTrace();
        }
        MassSpectrum massspectrum = this.massSpectrum;
        int numberOfIsotopicSets = 0;
        int numberOfIsotopicClusters = 0;
        int numberOfIsotopicPeaks = 0;
        int numberOfPeaks = 0;

        numberOfPeaks += massspectrum.getMz().size();

        numberOfIsotopicSets += this.deisotoper.getIsotopicSets().size();

        for (IsotopicSet isotopicSet : this.deisotoper.getIsotopicSets()) {
            numberOfIsotopicClusters += isotopicSet.getIsotopicSet().size();
            List<Peak> peaksOfIsotopicClusters = new ArrayList<>();

            for (IsotopicCluster isotopicCluster : isotopicSet.getIsotopicSet()) {
                if (isotopicCluster.getIsotopicCluster() != null) {
                    peaksOfIsotopicClusters.addAll(isotopicCluster.getIsotopicCluster());
                }
            }

            Set<Double> set = new HashSet<Double>();
            List<Peak> result = new ArrayList<Peak>();

            for (Peak peak : peaksOfIsotopicClusters) {
                if (set.add(peak.getMz())) {
                    result.add(peak);
                }
            }

            numberOfIsotopicPeaks += result.size();
        }

        StringBuilder stringBuilder = new StringBuilder();
        String linesep = System.getProperty("line.separator");
        stringBuilder
                .append("NumberOfIsotopicSets,NumberOfIsotopicClusters,NumberOfPeaksInIsotopicClusters,NumberOfPeaks")
                .append(linesep);
        stringBuilder.append(numberOfIsotopicSets).append(",").append(numberOfIsotopicClusters).append(",")
                .append(numberOfIsotopicPeaks).append(",").append(numberOfPeaks).append(linesep);

        return stringBuilder.toString();
    }
}