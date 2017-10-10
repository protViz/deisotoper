package ch.fgcz.proteomics.fdbm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-21
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.GraphPath;

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.dto.MassSpectrum;
import ch.fgcz.proteomics.dto.Summary;
import ch.fgcz.proteomics.fdbm.IsotopicClusterGraph;
import ch.fgcz.proteomics.utilities.Sort;

public class Deisotope {
    public MassSpectrometryMeasurement deisotopeMSM(MassSpectrometryMeasurement input, boolean save, String modus, String file) {
        MassSpectrometryMeasurement output = new MassSpectrometryMeasurement(input.getSource() + "_output");

        output = addToOutput(input, output, save, modus, new ScoreConfig(file));

        return output;
    }

    private MassSpectrometryMeasurement addToOutput(MassSpectrometryMeasurement input, MassSpectrometryMeasurement output, boolean save, String modus, ScoreConfig config) {
        for (MassSpectrum ms : input.getMSlist()) { // input.getMSlist().parallelStream().forEach((ms) -> {
            IsotopicMassSpectrum ims = new IsotopicMassSpectrum(ms, 0.01);

            List<Double> isotopelist = new ArrayList<>();
            List<Double> mzlist = new ArrayList<>();
            List<Double> intensitylist = new ArrayList<>();
            List<Integer> chargelist = new ArrayList<>();

            List<Double> mzlistnew = new ArrayList<>();

            for (IsotopicSet is : ims.getIsotopicMassSpectrum()) {
                IsotopicClusterGraph icg = new IsotopicClusterGraph(is);

                icg.scoreIsotopicClusterGraph(ms.getPeptideMass(), ms.getChargeState(), 0.3, new Peaklist(ms.getMz(), ms.getIntensity()), config);

                GraphPath<IsotopicCluster, Connection> bp = icg.bestPath(getStart(icg), getEnd(icg));

                if (save == true) {
                    icg.drawDOTIsotopicClusterGraph(is.getSetID(), ms.getId());
                }

                List<Double> clustermz = new ArrayList<>();
                List<Double> clusteri = new ArrayList<>();
                List<Double> clusteriso = new ArrayList<>();
                List<Integer> clustercharge = new ArrayList<>();

                List<Double> clustermznew = new ArrayList<>();

                for (IsotopicCluster cluster : bp.getVertexList()) {
                    if (cluster.getIsotopicCluster() != null) {

                        for (Peak p : cluster.getIsotopicCluster()) {
                            clustermznew.add(p.getMz());
                        }

                        aggregation(cluster, modus);

                        int position = 1;
                        for (Peak p : cluster.getIsotopicCluster()) {
                            clustermz.add(p.getMz());
                            clusteri.add(p.getIntensity());
                            clusteriso.add((double) position);
                            clustercharge.add(cluster.getCharge());
                            position++;
                        }
                    }
                }

                mzlist.addAll(clustermz);
                intensitylist.addAll(clusteri);
                isotopelist.addAll(clusteriso);
                chargelist.addAll(clustercharge);

                mzlistnew.addAll(clustermznew);
            }

            for (int i = 0; i < ms.getMz().size(); i++) {
                if (!mzlistnew.contains(ms.getMz().get(i))) {
                    mzlist.add(ms.getMz().get(i));
                    intensitylist.add(ms.getIntensity().get(i));
                    isotopelist.add(-1.0);
                    chargelist.add(-1);
                }
            }

            Sort.keySort(mzlist, mzlist, intensitylist, isotopelist, chargelist);

            output.addMS(ms.getTyp(), ms.getSearchEngine(), mzlist, intensitylist, ms.getPeptideMass(), ms.getRt(), ms.getChargeState(), ms.getId(), chargelist, isotopelist);
        }

        return output;
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
        if (modus.contains("first")) {
            return cluster.aggregateFirst();
        } else if (modus.contains("last")) {
            return cluster.aggregateLast();
        } else if (modus.contains("mean")) {
            return cluster.aggregateMean();
        } else if (modus.contains("none")) {
            return cluster;
        } else {
            throw new IllegalArgumentException("Modus not found (" + modus + ")");
        }
    }

    public static void main(String[] args) {
        // String typ = "MS2 Spectrum";
        // String searchengine = "mascot";
        // double[] mz = { 0.2, 1.0, 2.0, 2.5, 3.0, 10.0 };
        // double[] intensity = { 0.5, 2.0, 1.0, 1.0, 1.0, 3.0 };
        // double peptidmass = 309.22;
        // double rt = 38383.34;
        // int chargestate = 2;
        // int id = 0;
        //
        // String typ2 = "MS2 Spectrum";
        // String searchengine2 = "mascot";
        // double[] mz2 = { 0.2, 2.0, 3.0, 4.0, 9.0, 10.0, 10.5, 11.0 };
        // double[] intensity2 = { 0.5, 2.0, 1.0, 1.0, 1.0, 3.0, 2.0, 3.0 };
        // double peptidmass2 = 376.170684814453;
        // double rt2 = 36.6818232;
        // int chargestate2 = 2;
        // int id2 = 1;
        //
        // String typ3 = "MS2 Spectrum";
        // String searchengine3 = "mascot";
        // double[] mz3 = { 123, 124, 125 };
        // double[] intensity3 = { 2254.873046875, 3130.0, 586.6217041016 };
        // double peptidmass3 = 357.697749043842;
        // double rt3 = 36.3270726;
        // int chargestate3 = 2;
        // int id3 = 2;

        String s = "TesterinoData.RData";
        MassSpectrometryMeasurement msm = new MassSpectrometryMeasurement(s);

        for (int i = 0; i < 100; i++) {
            String typ = "MS2 Spectrum";
            String searchengine = "mascot";

            List<Double> mzlist = new ArrayList<>();
            List<Double> intensitylist = new ArrayList<>();

            int x = 25;
            for (double j = 125; j < 300; j++) {
                if (j / 5 == x) {
                    mzlist.add(j + 2);
                    x++;
                } else {
                    mzlist.add(j);
                }
                intensitylist.add(j * 1.24);
            }

            Collections.sort(mzlist);

            double[] mz = mzlist.stream().mapToDouble(Double::doubleValue).toArray();
            double[] intensity = intensitylist.stream().mapToDouble(Double::doubleValue).toArray();

            double peptidmass = i * 20.343;
            double rt = i * 30.2434;
            int chargestate = 2;
            int id = i;
            msm.addMS(typ, searchengine, mz, intensity, peptidmass, rt, chargestate, id);
        }

        // msm.addMS(typ2, searchengine2, mz2, intensity2, peptidmass2, rt2, chargestate2, id2);
        // msm.addMS(typ3, searchengine3, mz3, intensity3, peptidmass3, rt3, chargestate3, id3);

        // System.out.println("Input:");
        // for (MassSpectrum x : msm.getMSlist()) {
        // for (int y = 0; y < x.getMz().size(); y++) {
        // System.out.println("M: " + x.getMz().get(y) + " ");
        // }
        // }
        // System.out.println();

        long startTime = System.currentTimeMillis();

        Deisotope deiso = new Deisotope();
        System.out.println(Summary.makeSummary(deiso.deisotopeMSM(msm, false, "first", "AminoAcidMasses.ini")));
        // System.out.println("Output:");
        // for (MassSpectrum x : deiso.deisotopeMSM(msm, true, "mean", "AminoAcidMasses.ini").getMSlist()) {
        // for (int y = 0; y < x.getMz().size(); y++) {
        // System.out.print("M: " + x.getMz().get(y) + " ");
        // System.out.print("Z: " + x.getCharge().get(y) + " ");
        // System.out.println("I: " + x.getIsotope().get(y));
        // }
        // }

        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime) + "ms");
    }
}