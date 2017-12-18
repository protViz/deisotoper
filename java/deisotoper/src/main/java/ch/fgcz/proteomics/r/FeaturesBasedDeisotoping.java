package ch.fgcz.proteomics.r;

/**
 * @author Lucas Schmidt
 * @since 2017-11-16
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.fgcz.proteomics.dto.MassSpectrum;
import ch.fgcz.proteomics.fbdm.Configuration;
import ch.fgcz.proteomics.fbdm.Deisotoper;
import ch.fgcz.proteomics.fbdm.DeisotoperMassSpectrumAdapter;

public class FeaturesBasedDeisotoping {
    private DeisotoperMassSpectrumAdapter deisotoper = new DeisotoperMassSpectrumAdapter(new Deisotoper());
    private MassSpectrum massSpectrum = new MassSpectrum();
    private MassSpectrum resultSpectrum = new MassSpectrum();

    public void setConfiguration(String[] aaMassNames, double[] aaMassValues, double f1, double f2, double f3, // NOSONAR
            double f4, double f5, double delta, double errortolerance, double distance, double noise, boolean decharge, // NOSONAR
            String modus) { // NOSONAR
        Configuration config;
        if (aaMassValues.length > 1 && aaMassNames.length > 1 && (aaMassNames.length == aaMassValues.length)) {
            Map<String, Double> aaMass = new HashMap<String, Double>();
            for (int i = 0; i < aaMassNames.length; i++) {
                aaMass.put(aaMassNames[i], aaMassValues[i]);
            }

            config = new Configuration(aaMass, delta, errortolerance, distance, noise, decharge, modus);
            config.setF(1, f1);
            config.setF(2, f2);
            config.setF(3, f3);
            config.setF(4, f4);
            config.setF(5, f5);
        } else {
            config = new Configuration(delta, errortolerance, distance, noise, decharge, modus);
            config.setF(1, f1);
            config.setF(2, f2);
            config.setF(3, f3);
            config.setF(4, f4);
            config.setF(5, f5);
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