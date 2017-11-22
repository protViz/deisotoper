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
import ch.fgcz.proteomics.fbdm.IsotopicClusterGraph;
import ch.fgcz.proteomics.fbdm.IsotopicMassSpectrum;
import ch.fgcz.proteomics.fbdm.IsotopicSet;
import ch.fgcz.proteomics.fbdm.Peak;

public class FeaturesBasedDeisotoping {
    private Deisotoper deisotoper = new Deisotoper();
    private MassSpectrum massspectrum = new MassSpectrum();
    private MassSpectrum resultspectrum = new MassSpectrum();

    public void setConfiguration(double[] AA_MASS, double F1, double F2, double F3, double F4, double F5, double DELTA,
	    double ERRORTOLERANCE, double DISTANCE, double NOISE, boolean DECHARGE, String MODUS) {
	Configuration config;
	if (AA_MASS.length > 1) {
	    List<Double> AA_MASS_LIST = new ArrayList<>();
	    for (int i = 0; i < AA_MASS.length; i++) {
		AA_MASS_LIST.add(AA_MASS[i]);
	    }

	    config = new Configuration(AA_MASS_LIST, F1, F2, F3, F4, F5, DELTA, ERRORTOLERANCE, DISTANCE, NOISE,
		    DECHARGE, MODUS);
	} else {
	    config = new Configuration(F1, F2, F3, F4, F5, DELTA, ERRORTOLERANCE, DISTANCE, NOISE, DECHARGE, MODUS);
	}

	this.deisotoper.setConfiguration(config);
    }

    public String getConfiguration() {
	return this.deisotoper.getConfiguration().toString(); // TODO: override toString
    }

    public void setMz(double[] mz) {
	List<Double> mzlist = new ArrayList<>();
	for (int i = 0; i < mz.length; i++) {
	    mzlist.add(mz[i]);
	}

	this.massspectrum.setMz(mzlist);
    }

    public void setIntensity(double[] intensity) {
	List<Double> intensitylist = new ArrayList<>();
	for (int i = 0; i < intensity.length; i++) {
	    intensitylist.add(intensity[i]);
	}

	this.massspectrum.setIntensity(intensitylist);
    }

    public void setPepMass(double pepmass) {
	this.massspectrum.setPeptideMass(pepmass);
    }

    public void setCharge(int charge) {
	this.massspectrum.setChargeState(charge);
    }

    public double[] getMz() {
	return this.resultspectrum.getMzArray();
    }

    public double[] getIntensity() {
	return this.resultspectrum.getIntensityArray();
    }

    public void deisotope() {
	this.resultspectrum = this.deisotoper.deisotopeMS(massspectrum, this.deisotoper.getConfiguration().getModus());
    }

    public String[] getDOT() {
	String[] dotgraphs = new String[this.deisotoper.getIsotopicClusterGraphList().size()];

	int i = 0;
	for (IsotopicClusterGraph isotopicclustergraph : this.deisotoper.getIsotopicClusterGraphList()) {
	    dotgraphs[i] = isotopicclustergraph.toDOTGraph();
	    i++;
	}

	return dotgraphs;
    }

    public String getAnnotatedSpectrum() {
	return this.deisotoper.getAnnotatedSpectrum();
    }

    public String getSummary() {
	MassSpectrum massspectrum = this.massspectrum;
	int numberis = 0;
	int numberic = 0;
	int numberipeaks = 0;
	int numberpeaks = 0;

	numberpeaks += massspectrum.getMz().size();

	IsotopicMassSpectrum isotopicmassspectrum = new IsotopicMassSpectrum(massspectrum,
		this.deisotoper.getConfiguration().getDelta(), this.deisotoper.getConfiguration(), this.deisotoper,
		this.deisotoper.getConfiguration().getModus());

	numberis += isotopicmassspectrum.getIsotopicMassSpectrum().size();

	for (IsotopicSet isotopicset : isotopicmassspectrum.getIsotopicMassSpectrum()) {
	    numberic += isotopicset.getIsotopicSet().size();
	    List<Peak> peakic = new ArrayList<>();

	    for (IsotopicCluster isotopiccluster : isotopicset.getIsotopicSet()) {
		if (isotopiccluster.getIsotopicCluster() != null) {
		    peakic.addAll(isotopiccluster.getIsotopicCluster());
		}
	    }

	    Set<Double> set = new HashSet<Double>();
	    List<Peak> result = new ArrayList<Peak>();

	    for (Peak peak : peakic) {
		if (set.add(peak.getMz())) {
		    result.add(peak);
		}
	    }

	    numberipeaks += result.size();
	}

	StringBuilder stringbuilder = new StringBuilder();
	String linesep = System.getProperty("line.separator");
	stringbuilder
		.append("NumberOfIsotopicSets,NumberOfIsotopicClusters,NumberOfPeaksInIsotopicClusters,NumberOfPeaks")
		.append(linesep);
	stringbuilder.append(numberis).append(",").append(numberic).append(",").append(numberipeaks).append(",")
		.append(numberpeaks).append(linesep);

	return stringbuilder.toString();
    }
}