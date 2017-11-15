package ch.fgcz.proteomics.fbdm;

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
import ch.fgcz.proteomics.fbdm.IsotopicClusterGraph;
import ch.fgcz.proteomics.utilities.Sort;

public class Deisotoper {
    public static MassSpectrometryMeasurement deisotopeMSM(MassSpectrometryMeasurement input, String modus, Configuration config) {
        MassSpectrometryMeasurement output = new MassSpectrometryMeasurement(input.getSource() + "_output");

        String date = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());

        for (MassSpectrum ms : input.getMSlist()) {
            output.getMSlist().add(deisotopeMS(ms, modus, config, date));
        }

        return output;
    }

    public static MassSpectrum deisotopeMS(MassSpectrum input, String modus, Configuration config) {
        String date = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());

        return deisotopeMS(input, modus, config, date);
    }

    // TODO (LS): refactor method.
    public static MassSpectrum deisotopeMS(MassSpectrum input, String modus, Configuration config, String date) {
        IsotopicMassSpectrum ims = new IsotopicMassSpectrum(input, config.getDelta(), config);

        List<Double> mz = new ArrayList<>();
        List<Double> intensity = new ArrayList<>();
        List<Double> isotope = new ArrayList<>();
        List<Integer> charge = new ArrayList<>();

        List<Double> mz3 = new ArrayList<>();

        for (IsotopicSet is : ims.getIsotopicMassSpectrum()) {
            IsotopicClusterGraph icg = new IsotopicClusterGraph(is);

            icg.scoreIsotopicClusterGraph(input.getPeptideMass(), input.getChargeState(), config.getErrortolerance(), new Peaklist(input.getMz(), input.getIntensity()), config);

            GraphPath<IsotopicCluster, Connection> bp = icg.bestPath(getStart(icg), getEnd(icg));

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

        List<Double> mz6 = new ArrayList<>();
        List<Double> intensity6 = new ArrayList<>();
        List<Double> isotope6 = new ArrayList<>();
        List<Integer> charge6 = new ArrayList<>();

        if (config.isDecharge() == true) {
            for (int i = 0; i < mz.size(); i++) {
                if (charge.get(i) > 1) {
                    mz6.add(mz.get(i) * charge.get(i) - (charge.get(i) - 1) * config.getH_MASS());
                    intensity6.add(intensity.get(i));
                    isotope6.add(isotope.get(i));
                    charge6.add(1);
                } else {
                    mz6.add(mz.get(i));
                    intensity6.add(intensity.get(i));
                    isotope6.add(isotope.get(i));
                    charge6.add(charge.get(i));
                }
            }
        } else {
            mz6.addAll(mz);
            intensity6.addAll(intensity);
            isotope6.addAll(isotope);
            charge6.addAll(charge);
        }

        Sort.keySort(mz6, mz6, intensity6, isotope6, charge6);

        if (config.getNoise() != 0) {
            double threshold = Collections.max(intensity6) * config.getNoise() / 100;

            List<Double> mz5 = new ArrayList<>();
            List<Double> intensity5 = new ArrayList<>();
            List<Double> isotope5 = new ArrayList<>();
            List<Integer> charge5 = new ArrayList<>();

            for (int i = 0; i < intensity6.size(); i++) {
                if (threshold < intensity6.get(i)) {
                    mz5.add(mz6.get(i));
                    intensity5.add(intensity6.get(i));
                    isotope5.add(isotope6.get(i));
                    charge5.add(charge6.get(i));
                }
            }

            return new MassSpectrum(input.getTyp(), input.getSearchEngine(), mz5, intensity5, input.getPeptideMass(), input.getRt(), input.getChargeState(), input.getId(), charge5, isotope5);
        } else {
            return new MassSpectrum(input.getTyp(), input.getSearchEngine(), mz6, intensity6, input.getPeptideMass(), input.getRt(), input.getChargeState(), input.getId(), charge6, isotope6);
        }
    }

    private static IsotopicCluster getStart(IsotopicClusterGraph icg) {
        for (IsotopicCluster e : icg.getIsotopicclustergraph().vertexSet()) {
            if (e.getIsotopicCluster() == null && e.getStatus() == "start") {
                return e;
            }
        }
        return null;
    }

    private static IsotopicCluster getEnd(IsotopicClusterGraph icg) {
        for (IsotopicCluster e : icg.getIsotopicclustergraph().vertexSet()) {
            if (e.getIsotopicCluster() == null && e.getStatus() == "end") {
                return e;
            }
        }
        return null;
    }

    private static IsotopicCluster aggregation(IsotopicCluster cluster, String modus) {
        if (modus.equals("first")) {
            return cluster.aggregateFirst();
        } else if (modus.equals("highest")) {
            return cluster.aggregateHighest();
        } else if (modus.equals("none")) {
            return cluster;
        } else {
            throw new IllegalArgumentException("Modus not found (" + modus + ")");
        }
    }
}