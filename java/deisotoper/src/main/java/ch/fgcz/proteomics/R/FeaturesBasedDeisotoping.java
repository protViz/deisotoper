package ch.fgcz.proteomics.R;

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

/**
 * @author Lucas Schmidt
 * @since 2017-11-16
 */

public class FeaturesBasedDeisotoping {
    private Deisotoper deisotoper = new Deisotoper();
    private MassSpectrum massSpectrum = new MassSpectrum();
    private MassSpectrum resultSpectrum = new MassSpectrum();

    public void setConfiguration(double[] AA_MASS, double F1, double F2, double F3, double F4, double F5, double DELTA,
	    double ERRORTOLERANCE, double DISTANCE, double NOISE, boolean DECHARGE) {
	Configuration config;
	if (AA_MASS.length > 1) {
	    List<Double> AA_MASS_LIST = new ArrayList<>();
	    for (int i = 0; i < AA_MASS.length; i++) {
		AA_MASS_LIST.add(AA_MASS[i]);
	    }

	    config = new Configuration(AA_MASS_LIST, F1, F2, F3, F4, F5, DELTA, ERRORTOLERANCE, DISTANCE, NOISE,
		    DECHARGE);
	} else {
	    config = new Configuration(F1, F2, F3, F4, F5, DELTA, ERRORTOLERANCE, DISTANCE, NOISE, DECHARGE);
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

	this.massSpectrum.setMz(mzlist);
    }

    public void setIntensity(double[] intensity) {
	List<Double> intensitylist = new ArrayList<>();
	for (int i = 0; i < intensity.length; i++) {
	    intensitylist.add(intensity[i]);
	}

	this.massSpectrum.setIntensity(intensitylist);
    }

    public void setPepMass(double pepmass) {
	this.massSpectrum.setPeptideMass(pepmass);
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

    public void deisotope(String modus) {
	this.resultSpectrum = this.deisotoper.deisotopeMS(massSpectrum, modus);
    }

    public String[] getDOT() {
	String[] dotgraphs = new String[this.deisotoper.getIcgList().size()];

	int i = 0;
	for (IsotopicClusterGraph icg : this.deisotoper.getIcgList()) {
	    dotgraphs[i] = icg.toDOTGraph();
	    i++;
	}

	return dotgraphs;
    }

    public String getSummary() {
	MassSpectrum ms = this.massSpectrum;
	int numberis = 0;
	int numberic = 0;
	int numberipeaks = 0;
	int numberpeaks = 0;

	numberpeaks += ms.getMz().size();

	IsotopicMassSpectrum ims = new IsotopicMassSpectrum(ms, this.deisotoper.getConfiguration().getDelta(),
		this.deisotoper.getConfiguration());

	numberis += ims.getIsotopicMassSpectrum().size();

	for (IsotopicSet is : ims.getIsotopicMassSpectrum()) {
	    numberic += is.getIsotopicSet().size();
	    List<Peak> peakic = new ArrayList<>();

	    for (IsotopicCluster ic : is.getIsotopicSet()) {
		peakic.addAll(ic.getIsotopicCluster());
	    }

	    Set<Double> titles = new HashSet<Double>();
	    List<Peak> result = new ArrayList<Peak>();

	    for (Peak p : peakic) {
		if (titles.add(p.getMz())) {
		    result.add(p);
		}
	    }

	    numberipeaks += result.size();
	}

	StringBuilder sb = new StringBuilder();
	String linesep = System.getProperty("line.separator");
	sb.append("NumberOfIsotopicSets,NumberOfIsotopicClusters,NumberOfPeaksInIsotopicClusters,NumberOfPeaks")
		.append(linesep);
	sb.append(numberis).append(",").append(numberic).append(",").append(numberipeaks).append(",")
		.append(numberpeaks).append(linesep);

	return sb.toString();
    }

    public static void main(String[] args) {
	FeaturesBasedDeisotoping dtoper = new FeaturesBasedDeisotoping();

	double[] aa = { 123.2, 1234.4 };

	dtoper.setConfiguration(aa, 0.8, 0.5, 0.1, 0.1, 0.1, 0.003, 0.3, 1.0, 0, true);

	double[] mz = { 1.0001, 2.0002, 4.0000054, 5, 82, 82.5 };
	double[] intensity = { 1, 2, 4, 5, 33, 32 };

	dtoper.setMz(mz);
	dtoper.setIntensity(intensity);
	dtoper.setPepMass(1.2345);
	dtoper.setCharge(2);

	dtoper.deisotope("first");

	double[] mzout = dtoper.getMz();
	double[] intensityout = dtoper.getIntensity();

	System.out.println("Output Peaklist:");
	for (int i = 0; i < mzout.length || i < intensityout.length; i++) {
	    System.out.print(mzout[i] + " ");
	    System.out.println(intensityout[i]);
	}

	System.out.println();

	String[] dot = dtoper.getDOT();

	String summary = dtoper.getSummary();

	for (int i = 0; i < dot.length; i++) {
	    System.out.println(dot[i]);
	    System.out.println();
	}
	System.out.println(summary);

	System.out.println();
	System.out.println(dtoper.getConfiguration());
    }
}
