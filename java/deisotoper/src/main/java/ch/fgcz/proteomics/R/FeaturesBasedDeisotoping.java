package ch.fgcz.proteomics.R;

/**
 * @author Lucas Schmidt
 * @since 2017-11-16
 */

import java.util.ArrayList;
import java.util.List;

import ch.fgcz.proteomics.dto.MassSpectrum;
import ch.fgcz.proteomics.fbdm.*;

public class FeaturesBasedDeisotoping {
    // private Deisotoper deisotoper = new Deisotoper();
    private DeisotoperMassSpectrumAdapter deisotoper = new DeisotoperMassSpectrumAdapter(new Deisotoper());
    private MassSpectrum massSpectrum = new MassSpectrum();
    private MassSpectrum resultSpectrum = new MassSpectrum();

    public void setConfiguration(double[] aaMass, double F1, double F2, double F3, double F4, double F5, double delta,
            double errortolerance, double distance, double noise, boolean decharge, String modus) {
        Configuration config;
        if (aaMass.length > 1) {
            List<Double> aaMassList = new ArrayList<Double>();
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
        return this.deisotoper.getConfiguration();
    }

    public void setMz(double[] mz) {
        List<Double> mzList = new ArrayList<Double>();
        for (int i = 0; i < mz.length; i++) {
            mzList.add(mz[i]);
        }

        this.massSpectrum.setMz(mzList);
    }

    public void setIntensity(double[] intensity) {
        List<Double> intensityList = new ArrayList<Double>();
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

        String[] dotGraphs = new String[this.deisotoper.getDotGraphs().size()];

        for (int i = 0; i < this.deisotoper.getDotGraphs().size(); i++) {
            dotGraphs[i] = this.deisotoper.getDotGraphs().get(i);
        }

        return dotGraphs;
    }

    public String getAnnotatedSpectrum() {
        return this.deisotoper.getAnnotatedSpectrum();
    }

    public String getSummary() {

        MassSpectrum massspectrum = this.massSpectrum;

        int numberOfIsotopicSets = this.deisotoper.getNrOfIsotopicSets();
        int numberOfIsotopicClusters = this.deisotoper.getNrOfIsotopePatterns();
        int numberOfIsotopicPeaks = this.deisotoper.getNrOfPeaksInIsotopePatterns();
        int numberOfPeaks = massspectrum.getMz().size();

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