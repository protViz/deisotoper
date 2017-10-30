package ch.fgcz.proteomics.fdbm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-21
 */

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.jgrapht.GraphPath;

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.dto.MassSpectrum;
import ch.fgcz.proteomics.fdbm.IsotopicClusterGraph;
import ch.fgcz.proteomics.utilities.Sort;

public class Deisotope {
    public MassSpectrometryMeasurement deisotopeMSM(MassSpectrometryMeasurement input, boolean save, String modus, String file, double percent, double error, double delta) {
        MassSpectrometryMeasurement output = new MassSpectrometryMeasurement(input.getSource() + "_output");

        ScoreConfig config = new ScoreConfig(file);

        String date = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());

        for (MassSpectrum ms : input.getMSlist()) { // input.getMSlist().parallelStream().forEach((ms) -> {$
            output.getMSlist().add(deisotopeMS(ms, save, modus, config, date, percent, error, delta));
        }

        return output;
    }

    public MassSpectrum deisotopeMS(MassSpectrum input, boolean save, String modus, ScoreConfig config, double percent, double error, double delta) {
        String date = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());

        return deisotopeMS(input, save, modus, config, date, percent, error, delta);
    }

    // TODO (LS): To many parameters, - e.g. move delta, error, percent, modus, config to Deisotoper constructor.
    public MassSpectrum deisotopeMS(MassSpectrum input, boolean save, String modus, ScoreConfig config, String date, double percent, double error, double delta) {
        IsotopicMassSpectrum ims = new IsotopicMassSpectrum(input, delta);

        if (save == true) {
            ims.makeStatistics(date, input.getMz().size());
        }

        List<Double> mz = new ArrayList<>();
        List<Double> intensity = new ArrayList<>();
        List<Double> isotope = new ArrayList<>();
        List<Integer> charge = new ArrayList<>();

        List<Double> mz3 = new ArrayList<>();

        for (IsotopicSet is : ims.getIsotopicMassSpectrum()) {
            IsotopicClusterGraph icg = new IsotopicClusterGraph(is);

            icg.scoreIsotopicClusterGraph(input.getPeptideMass(), input.getChargeState(), error, new Peaklist(input.getMz(), input.getIntensity()), config);

            GraphPath<IsotopicCluster, Connection> bp = icg.bestPath(getStart(icg), getEnd(icg));

            if (save == true) {
                icg.drawDOTIsotopicClusterGraph(is.getSetID(), input.getId(), date);
            }

            List<Double> mz2 = new ArrayList<>();
            List<Double> intensity2 = new ArrayList<>();
            List<Double> isotope2 = new ArrayList<>();
            List<Integer> charge2 = new ArrayList<>();

            List<Double> mz4 = new ArrayList<>();

            for (IsotopicCluster cluster : bp.getVertexList()) {
                if (cluster.getIsotopicCluster() != null) {

                    for (Peak p : cluster.getIsotopicCluster()) {
                        mz4.add(p.getMz());
                    }

                    aggregation(cluster, modus);

                    int position = 1;
                    for (Peak p : cluster.getIsotopicCluster()) {
                        mz2.add(p.getMz());
                        intensity2.add(p.getIntensity());
                        isotope2.add((double) position);
                        charge2.add(cluster.getCharge());
                        position++;
                    }
                }
            }

            mz.addAll(mz2);
            intensity.addAll(intensity2);
            isotope.addAll(isotope2);
            charge.addAll(charge2);

            mz3.addAll(mz4);
        }

        for (int i = 0; i < input.getMz().size(); i++) {
            if (!mz3.contains(input.getMz().get(i))) {
                mz.add(input.getMz().get(i));
                intensity.add(input.getIntensity().get(i));
                isotope.add(-1.0);
                charge.add(-1);
            }
        }

        Sort.keySort(mz, mz, intensity, isotope, charge);

        if (percent != 0) {
            double threshold = Collections.max(mz) * percent / 100;

            List<Double> mz5 = new ArrayList<>();
            List<Double> intensity5 = new ArrayList<>();
            List<Double> isotope5 = new ArrayList<>();
            List<Integer> charge5 = new ArrayList<>();

            for (int i = 0; i < mz.size(); i++) {
                if (threshold < mz.get(i)) {
                    mz5.add(mz.get(i));
                    intensity5.add(intensity.get(i));
                    isotope5.add(isotope.get(i));
                    charge5.add(charge.get(i));
                }
            }
            return new MassSpectrum(input.getTyp(), input.getSearchEngine(), mz5, intensity5, input.getPeptideMass(), input.getRt(), input.getChargeState(), input.getId(), charge5, isotope5);
        } else {
            return new MassSpectrum(input.getTyp(), input.getSearchEngine(), mz, intensity, input.getPeptideMass(), input.getRt(), input.getChargeState(), input.getId(), charge, isotope);
        }
    }

    private IsotopicCluster getStart(IsotopicClusterGraph icg) {
        for (IsotopicCluster e : icg.getIsotopicclustergraph().vertexSet()) {
            if (e.getIsotopicCluster() == null && e.getStatus() == "start") {
                return e;
            }
        }
        return null;
    }

    private IsotopicCluster getEnd(IsotopicClusterGraph icg) {
        for (IsotopicCluster e : icg.getIsotopicclustergraph().vertexSet()) {
            if (e.getIsotopicCluster() == null && e.getStatus() == "end") {
                return e;
            }
        }
        return null;
    }

    private IsotopicCluster aggregation(IsotopicCluster cluster, String modus) {
        if (modus.equals("first")) {
            return cluster.simpleAggregateFirst();
        } else if (modus.equals("last")) {
            return cluster.simpleAggregateLast();
        } else if (modus.equals("mean")) {
            return cluster.simpleAggregateMean();
        } else if (modus.equals("highest")) {
            return cluster.advancedAggregateHighest();
        } else if (modus.equals("none")) {
            return cluster;
        } else {
            throw new IllegalArgumentException("Modus not found (" + modus + ")");
        }
    }
}