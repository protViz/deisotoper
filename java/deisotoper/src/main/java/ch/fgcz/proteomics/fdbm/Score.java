package ch.fgcz.proteomics.fdbm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Lucas Schmidt
 * @since 2017-09-19
 */

import java.util.ArrayList;
import java.util.List;

public class Score {
    private final static List<Double> AA_MASS = new ArrayList<Double>() {
        {
            try (BufferedReader br = new BufferedReader(new FileReader("AminoAcidMasses.ini"))) {
                String line = br.readLine();

                while (line != null) {
                    String[] parts = line.split("=");
                    add(Double.parseDouble(parts[1]));
                    line = br.readLine();
                }
            } catch (FileNotFoundException e) {
                add(71.03711);
                add(156.10111);
                add(114.04293);
                add(115.02694);
                add(103.00919);
                add(129.04259);
                add(128.05858);
                add(57.02146);
                add(137.05891);
                add(113.08406);
                add(113.08406);
                add(128.09496);
                add(131.04049);
                add(147.06841);
                add(97.05276);
                add(87.03203);
                add(101.04768);
                add(186.07931);
                add(163.06333);
                add(99.06841);
                System.err.println("WARNING: File not found, unsing standart amino acid masses instead!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    private final static double H_MASS = 1.008;
    private final static double NH3_MASS = 17.03052;
    private final static double H2O_MASS = 18.01528;
    private final static double NH_MASS = 15.01464;
    private final static double CO_MASS = 28.0101;
    private final static double PHE_MASS = 165.192;
    private final static double ASP_MASS = 133.104;
    private final static double AVE_UPDATED_MASS = 111.125;
    // private final static List<Peak> PHE_PATTERN = Arrays.asList(new Peak(147.06842, 100), new Peak(148.07178, 10.2), new Peak(149.07513, 0.6));
    // private final static List<Peak> ASP_PATTERN = Arrays.asList(new Peak(115.02696, 100), new Peak(116.03032, 4.9), new Peak(117.0312, 0.7));
    // private final static List<Peak> AVE_UPDATED_PATTERN = Arrays.asList(new Peak(0, 0));

    /**
     * To thoroughly assess each possible isotopic cluster, those five features above are combined in a score function.
     * 
     * @param x Peak
     * @param y Peak
     * @param error Errortolerance
     * @param mspepmass PeptidMass of MS
     * @param mscharge ChargeState of MS
     * @param icofx IsotopicCluster which contains x
     * @param con Connection
     * @param icg IsotopicClusterGraph
     * @return score
     */
    public static double score(Peak x, Peak y, double error, double mspepmass, double mscharge, IsotopicCluster icofx, Connection con, IsotopicClusterGraph icg) {
        return 0.8 * firstNonintensityFeature(x, y, error) + 0.5 * secondNonintensityFeature(x, y, error, mspepmass, mscharge, icofx) + 0.1 * thirdNonintensityFeature(x, y, error)
                + 0.1 * fourthNonintensityFeature(x, y, error) + 0.1 * fifthIntensityFeature(con, icg);
    }

    /**
     * diff1 considers that two fragment ions represented by x and y have the same charge state.
     * 
     * @param Peak of the IsotopicCluster x
     * @param Peak of the MassSpectrum y
     * @return
     */
    protected static double diff1(Peak x, Peak y) {
        return x.getMz() - y.getMz();
    }

    /**
     * diff2 considers that the fragment ion represented by x is doubly charged and that represented by y is singly charged.
     * 
     * @param Peak of the IsotopicCluster x
     * @param Peak of the MassSpectrum y
     * @return
     */
    protected static double diff2(Peak x, Peak y) {
        return x.getMz() - ((y.getMz() + H_MASS) / 2);
    }

    /**
     * diff3 considers that the fragment ion represented by x is triply charged and that represented by y is singly charged.
     * 
     * @param Peak of the IsotopicCluster x
     * @param Peak of the MassSpectrum y
     * @return
     */
    protected static double diff3(Peak x, Peak y) {
        return x.getMz() - ((y.getMz() + (2 * H_MASS)) / 3);
    }

    /**
     * diff4 considers that fragment ion represented by x is triply charged and that represented by y is doubly charged.
     * 
     * @param Peak of the IsotopicCluster x
     * @param Peak of the MassSpectrum y
     * @return
     */
    protected static double diff4(Peak x, Peak y) {
        return x.getMz() - (((y.getMz() * 2) + H_MASS) / 3);
    }

    /**
     * sum1 considers that two fragment ions represented by x and y have the same charge state.
     * 
     * @param x
     * @param y
     * @return
     */
    protected static double sum1(Peak x, Peak y) {
        return x.getMz() + y.getMz();
    }

    /**
     * sum2 considers that the fragment ion represented by x is doubly charged and that represented by y is singly charged.
     * 
     * @param Peak of the IsotopicCluster x
     * @param Peak of the MassSpectrum y
     * @return
     */
    protected static double sum2(Peak x, Peak y) {
        return x.getMz() + ((y.getMz() + H_MASS) / 2);
    }

    /**
     * sum3 considers that the fragment ion represented by x is triply charged and that represented by y is singly charged.
     * 
     * @param Peak of the IsotopicCluster x
     * @param Peak of the MassSpectrum y
     * @return
     */
    protected static double sum3(Peak x, Peak y) {
        return x.getMz() + ((y.getMz() + (2 * H_MASS)) / 3);
    }

    /**
     * sum4 considers that fragment ion represented by x is triply charged and that represented by y is doubly charged.
     * 
     * @param Peak of the IsotopicCluster x
     * @param Peak of the MassSpectrum y
     * @return
     */
    protected static double sum4(Peak x, Peak y) {
        return x.getMz() + (((y.getMz() * 2) + H_MASS) / 3);
    }

    /**
     * The first nonintensity feature is based on the number collection of peaks y whose mass differences with x approximate the residue mass of one of the twenty amino acids.
     * 
     * @param x Peak
     * @param y Peak
     * @param e Errortolerance
     * @return Cardinality of List F1
     */
    protected static int firstNonintensityFeature(Peak x, Peak y, double e) {
        List<Peak> F1 = new ArrayList<>();

        for (Double aa : AA_MASS) {
            if (aa - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < aa + e) {
                F1.add(y);
            } else if (aa / 2 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < aa / 2 + e) {
                F1.add(y);
            } else if (aa / 3 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < aa / 3 + e) {
                F1.add(y);
            } else if (aa / 2 - e < Math.abs(diff2(x, y)) && Math.abs(diff2(x, y)) < aa / 2 + e) {
                F1.add(y);
            } else if (aa / 2 - e < Math.abs(diff2(y, x)) && Math.abs(diff2(y, x)) < aa / 2 + e) {
                F1.add(y);
            } else if (aa / 3 - e < Math.abs(diff3(x, y)) && Math.abs(diff3(x, y)) < aa / 3 + e) {
                F1.add(y);
            } else if (aa / 3 - e < Math.abs(diff3(y, x)) && Math.abs(diff3(y, x)) < aa / 3 + e) {
                F1.add(y);
            } else if (aa / 3 - e < Math.abs(diff4(x, y)) && Math.abs(diff4(x, y)) < aa / 3 + e) {
                F1.add(y);
            } else if (aa / 3 - e < Math.abs(diff4(y, x)) && Math.abs(diff4(y, x)) < aa / 3 + e) {
                F1.add(y);
            }
        }

        return F1.size();
    }

    /**
     * The second nonintensity feature is based on the number collection of peaks y representing fragment ions that complement with fragment ion represented by x.
     * 
     * @param x Peak
     * @param y Peak
     * @param e Errortolerance
     * @param pepmass PeptidMass of MS
     * @param charge ChargeState of MS
     * @param ic IsotopicCluster
     * @return Cardinality of List F2
     */
    protected static int secondNonintensityFeature(Peak x, Peak y, double e, double pepmass, double charge, IsotopicCluster ic) {
        List<Peak> F2 = new ArrayList<>();
        double M = pepmass * charge;

        int i = 1;
        for (Peak c : ic.getIsotopicCluster()) {
            if (c.getMz() == x.getMz() && c.getIntensity() == x.getIntensity()) {
                break;
            }
            i++;
        }

        if (M + 2 * i + 2 * H_MASS - e < sum1(x, y) && sum1(x, y) < M + 2 * i + 2 * H_MASS + e) {
            F2.add(y);
        } else if ((M + 2 * i) / 2 + 2 * H_MASS - e < sum1(x, y) && sum1(x, y) < (M + 2 * i) / 2 + 2 * H_MASS + e) {
            F2.add(y);
        } else if ((M + 2 * i) / 3 + 2 * H_MASS - e < sum1(x, y) && sum1(x, y) < (M + 2 * i) / 3 + 2 * H_MASS + e) {
            F2.add(y);
        } else if ((M + 2 * i) / 2 + 2 * H_MASS - e < sum2(x, y) && sum2(x, y) < (M + 2 * i) / 2 + 2 * H_MASS + e) {
            F2.add(y);
        } else if ((M + 2 * i) / 2 + 2 * H_MASS - e < sum2(y, x) && sum2(y, x) < (M + 2 * i) / 2 + 2 * H_MASS + e) {
            F2.add(y);
        } else if ((M + 2 * i) / 3 + 2 * H_MASS - e < sum3(x, y) && sum3(x, y) < (M + 2 * i) / 3 + 2 * H_MASS + e) {
            F2.add(y);
        } else if ((M + 2 * i) / 3 + 2 * H_MASS - e < sum3(y, x) && sum3(y, x) < (M + 2 * i) / 3 + 2 * H_MASS + e) {
            F2.add(y);
        } else if ((M + 2 * i) / 3 + 2 * H_MASS - e < sum4(x, y) && sum4(x, y) < (M + 2 * i) / 3 + 2 * H_MASS + e) {
            F2.add(y);
        } else if ((M + 2 * i) / 3 + 2 * H_MASS - e < sum4(y, x) && sum4(y, x) < (M + 2 * i) / 3 + 2 * H_MASS + e) {
            F2.add(y);
        }

        return F2.size();
    }

    /**
     * The third nonintensity feature considers that the side chains of some amino acids residues of fragment ions can lose a water molecule (H2O) or an ammonia molecule (NH3). The number of peaks y
     * whose mass differences with x approximate the mass of a water molecule (H2O) or an ammonia molecule (NH3) is collected.
     * 
     * @param x Peak
     * @param y Peak
     * @param e Errortolerance
     * @return Cardinality of List F3
     */
    protected static int thirdNonintensityFeature(Peak x, Peak y, double e) {
        List<Peak> F3 = new ArrayList<>();

        if (H2O_MASS - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < H2O_MASS + e) {
            F3.add(y);
        } else if (NH3_MASS - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < NH3_MASS + e) {
            F3.add(y);
        } else if (H2O_MASS / 2 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < H2O_MASS / 2 + e) {
            F3.add(y);
        } else if (NH3_MASS / 2 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < NH3_MASS / 2 + e) {
            F3.add(y);
        } else if (H2O_MASS / 3 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < H2O_MASS / 3 + e) {
            F3.add(y);
        } else if (NH3_MASS / 3 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < NH3_MASS / 3 + e) {
            F3.add(y);
        } else if (H2O_MASS / 2 - e < Math.abs(diff2(x, y)) && Math.abs(diff2(x, y)) < H2O_MASS / 2 + e) {
            F3.add(y);
        } else if (NH3_MASS / 2 - e < Math.abs(diff2(x, y)) && Math.abs(diff2(x, y)) < NH3_MASS / 2 + e) {
            F3.add(y);
        } else if (H2O_MASS / 2 - e < Math.abs(diff2(y, x)) && Math.abs(diff2(y, x)) < H2O_MASS / 2 + e) {
            F3.add(y);
        } else if (NH3_MASS / 2 - e < Math.abs(diff2(y, x)) && Math.abs(diff2(y, x)) < NH3_MASS / 2 + e) {
            F3.add(y);
        } else if (H2O_MASS / 3 - e < Math.abs(diff3(x, y)) && Math.abs(diff3(x, y)) < H2O_MASS / 3 + e) {
            F3.add(y);
        } else if (NH3_MASS / 3 - e < Math.abs(diff3(x, y)) && Math.abs(diff3(x, y)) < NH3_MASS / 3 + e) {
            F3.add(y);
        } else if (H2O_MASS / 3 - e < Math.abs(diff3(y, x)) && Math.abs(diff3(y, x)) < H2O_MASS / 3 + e) {
            F3.add(y);
        } else if (NH3_MASS / 3 - e < Math.abs(diff3(y, x)) && Math.abs(diff3(y, x)) < NH3_MASS / 3 + e) {
            F3.add(y);
        } else if (H2O_MASS / 3 - e < Math.abs(diff4(x, y)) && Math.abs(diff4(x, y)) < H2O_MASS / 3 + e) {
            F3.add(y);
        } else if (NH3_MASS / 3 - e < Math.abs(diff4(x, y)) && Math.abs(diff4(x, y)) < NH3_MASS / 3 + e) {
            F3.add(y);
        } else if (H2O_MASS / 3 - e < Math.abs(diff4(y, x)) && Math.abs(diff4(y, x)) < H2O_MASS / 3 + e) {
            F3.add(y);
        } else if (NH3_MASS / 3 - e < Math.abs(diff4(y, x)) && Math.abs(diff4(y, x)) < NH3_MASS / 3 + e) {
            F3.add(y);
        }

        return F3.size();
    }

    /**
     * The fourth nonintensity feature considers two supportive ions a-ions and z-ions which can be used to indicate the existence of the corresponding b-ions and y-ions. The number of peaks
     * representing these kinds of supportive ions is collected.
     * 
     * @param x Peak
     * @param y Peak
     * @param e Errortolerance
     * @return Cardinality of List F4
     */
    protected static int fourthNonintensityFeature(Peak x, Peak y, double e) {
        List<Peak> F4 = new ArrayList<>();

        if (NH_MASS - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < NH_MASS + e) {
            F4.add(y);
        } else if (CO_MASS - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < CO_MASS + e) {
            F4.add(y);
        } else if (NH_MASS / 2 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < NH_MASS / 2 + e) {
            F4.add(y);
        } else if (CO_MASS / 2 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < CO_MASS / 2 + e) {
            F4.add(y);
        } else if (NH_MASS / 3 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < NH_MASS / 3 + e) {
            F4.add(y);
        } else if (CO_MASS / 3 - e < Math.abs(diff1(x, y)) && Math.abs(diff1(x, y)) < CO_MASS / 3 + e) {
            F4.add(y);
        } else if (NH_MASS / 2 - e < Math.abs(diff2(x, y)) && Math.abs(diff2(x, y)) < NH_MASS / 2 + e) {
            F4.add(y);
        } else if (CO_MASS / 2 - e < Math.abs(diff2(x, y)) && Math.abs(diff2(x, y)) < CO_MASS / 2 + e) {
            F4.add(y);
        } else if (NH_MASS / 2 - e < Math.abs(diff2(y, x)) && Math.abs(diff2(y, x)) < NH_MASS / 2 + e) {
            F4.add(y);
        } else if (CO_MASS / 2 - e < Math.abs(diff2(y, x)) && Math.abs(diff2(y, x)) < CO_MASS / 2 + e) {
            F4.add(y);
        } else if (NH_MASS / 3 - e < Math.abs(diff3(x, y)) && Math.abs(diff3(x, y)) < NH_MASS / 3 + e) {
            F4.add(y);
        } else if (CO_MASS / 3 - e < Math.abs(diff3(x, y)) && Math.abs(diff3(x, y)) < CO_MASS / 3 + e) {
            F4.add(y);
        } else if (NH_MASS / 3 - e < Math.abs(diff3(y, x)) && Math.abs(diff3(y, x)) < NH_MASS / 3 + e) {
            F4.add(y);
        } else if (CO_MASS / 3 - e < Math.abs(diff3(y, x)) && Math.abs(diff3(y, x)) < CO_MASS / 3 + e) {
            F4.add(y);
        } else if (NH_MASS / 3 - e < Math.abs(diff4(x, y)) && Math.abs(diff4(x, y)) < NH_MASS / 3 + e) {
            F4.add(y);
        } else if (CO_MASS / 3 - e < Math.abs(diff4(x, y)) && Math.abs(diff4(x, y)) < CO_MASS / 3 + e) {
            F4.add(y);
        } else if (NH_MASS / 3 - e < Math.abs(diff4(y, x)) && Math.abs(diff4(y, x)) < NH_MASS / 3 + e) {
            F4.add(y);
        } else if (CO_MASS / 3 - e < Math.abs(diff4(y, x)) && Math.abs(diff4(y, x)) < CO_MASS / 3 + e) {
            F4.add(y);
        }

        return F4.size();
    }

    // NOT FINISHED YET
    /**
     * The intensity feature determines if the experimental isotopic distribution of one possible isotopic cluster matches with the theoretical isotopic distribution or not with the consideration of
     * the relationship between adjacent isotopic clusters in the graph.
     * 
     * @param con Connection
     * @param icg IsotopicClusterGraph
     * @return Cardinality of List F5
     */
    protected static int fifthIntensityFeature(Connection con, IsotopicClusterGraph icg) {
        List<Peak> F5 = new ArrayList<>();
        double threshold = 0.3;

        int i = 0;
        for (Peak p : icg.getIsotopicclustergraph().getEdgeTarget(con).getIsotopicCluster()) {
            // System.out.println("PEAK: " + p.getMz() + " MZ, " + p.getIntensity() + " INTENSITY");
            double T_MIN = (p.getMz() / ASP_MASS) * p.getIntensity();
            // System.out.println("T_MIN: " + T_MIN);
            double T_MEAN = (p.getMz() / AVE_UPDATED_MASS) * p.getIntensity();
            // System.out.println("T_MEAN: " + T_MEAN);
            double T_MEAN_OVERLAP = 0;
            if (icg.getIsotopicclustergraph().getEdgeSource(con).getIsotopicCluster() != null) {
                if (i < icg.getIsotopicclustergraph().getEdgeSource(con).getIsotopicCluster().size()) {
                    T_MEAN_OVERLAP = (icg.getIsotopicclustergraph().getEdgeSource(con).getIsotopicCluster().get(i).getMz() / AVE_UPDATED_MASS) * p.getIntensity();
                } else {
                    T_MEAN_OVERLAP = (icg.getIsotopicclustergraph().getEdgeSource(con).getIsotopicCluster().get(icg.getIsotopicclustergraph().getEdgeSource(con).getIsotopicCluster().size() - 1)
                            .getMz() / AVE_UPDATED_MASS) * p.getIntensity();
                }
            }
            // System.out.println("T_MEAN_OVERLAP: " + T_MEAN_OVERLAP);
            double T_MAX = (p.getMz() / PHE_MASS) * p.getIntensity();
            // System.out.println("T_MAX: " + T_MAX);

            if (con.getColor() == "black") {
                // System.out.println("black: " + Math.min(Math.abs(p.getIntensity() - T_MIN), Math.abs(p.getIntensity() - T_MAX)) / T_MEAN);
                if (Math.min(Math.abs(p.getIntensity() - T_MIN), Math.abs(p.getIntensity() - T_MAX)) / T_MEAN <= threshold) {
                    F5.add(p);
                }
            }

            if (con.getColor() == "red") {
                // System.out.println("red: " + Math.min(Math.abs((p.getIntensity() - T_MEAN_OVERLAP) - T_MIN), Math.abs((p.getIntensity() - T_MEAN_OVERLAP) - T_MAX)));
                if (Math.min(Math.abs((p.getIntensity() - T_MEAN_OVERLAP) - T_MIN), Math.abs((p.getIntensity() - T_MEAN_OVERLAP) - T_MAX)) / T_MEAN <= threshold) {
                    F5.add(p);
                    // System.out.println("RED ADDED");
                }
            }
            i++;
        }

        // System.out.println("SCORE: " + F5.size());
        return F5.size();
    }
}
