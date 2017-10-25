package ch.fgcz.proteomics.fdbm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-19
 */

import org.jgrapht.graph.DefaultDirectedWeightedGraph;

public class Score {
    // public static double timescoref1 = 0;
    // public static double timescoref2 = 0;
    // public static double timescoref3 = 0;
    // public static double timescoref4 = 0;
    // public static double timescoref5 = 0;

    private double score;

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    /**
     * To thoroughly assess each possible isotopic cluster, those five features above are combined in a score function.
     * 
     * @param x Peak
     * @param y Peak
     * @param error Errortolerance
     * @param mspepmass PeptidMass of MS
     * @param mscharge ChargeState of MS
     * @param icx IsotopicCluster which contains x
     * @param con Connection
     * @param isotopicclustergraph IsotopicClusterGraph
     * @return score
     */
    public Score(Peak x, Peak y, double error, double mspepmass, double mscharge, IsotopicCluster icx, Connection con, DefaultDirectedWeightedGraph<IsotopicCluster, Connection> isotopicclustergraph,
            ScoreConfig config) {
        this.score = config.getFM1() * firstNonintensityFeature(x, y, error, config) + config.getFM2() * secondNonintensityFeature(x, y, error, mspepmass, mscharge, icx, config)
                + config.getFM3() * thirdNonintensityFeature(x, y, error, config) + config.getFM4() * fourthNonintensityFeature(x, y, error, config)
                + config.getFM5() * fifthIntensityFeature(con, isotopicclustergraph, config);
    }

    /**
     * Empty constructor.
     */
    public Score() {
        this.score = 0;
    }

    /**
     * diff1 considers that two fragment ions represented by x and y have the same charge state.
     * 
     * @param Peak of the IsotopicCluster x
     * @param Peak of the MassSpectrum y
     * @return
     */
    protected double diff1(Peak x, Peak y, ScoreConfig config) {
        return x.getMz() - y.getMz();
    }

    /**
     * diff2 considers that the fragment ion represented by x is doubly charged and that represented by y is singly charged.
     * 
     * @param Peak of the IsotopicCluster x
     * @param Peak of the MassSpectrum y
     * @return
     */
    protected double diff2(Peak x, Peak y, ScoreConfig config) {
        return x.getMz() - ((y.getMz() + config.getH_MASS()) / 2);
    }

    /**
     * diff3 considers that the fragment ion represented by x is triply charged and that represented by y is singly charged.
     * 
     * @param Peak of the IsotopicCluster x
     * @param Peak of the MassSpectrum y
     * @return
     */
    protected double diff3(Peak x, Peak y, ScoreConfig config) {
        return x.getMz() - ((y.getMz() + (2 * config.getH_MASS())) / 3);
    }

    /**
     * diff4 considers that fragment ion represented by x is triply charged and that represented by y is doubly charged.
     * 
     * @param Peak of the IsotopicCluster x
     * @param Peak of the MassSpectrum y
     * @return
     */
    protected double diff4(Peak x, Peak y, ScoreConfig config) {
        return x.getMz() - (((y.getMz() * 2) + config.getH_MASS()) / 3);
    }

    /**
     * sum1 considers that two fragment ions represented by x and y have the same charge state.
     * 
     * @param x
     * @param y
     * @return
     */
    protected double sum1(Peak x, Peak y, ScoreConfig config) {
        return x.getMz() + y.getMz();
    }

    /**
     * sum2 considers that the fragment ion represented by x is doubly charged and that represented by y is singly charged.
     * 
     * @param Peak of the IsotopicCluster x
     * @param Peak of the MassSpectrum y
     * @return
     */
    protected double sum2(Peak x, Peak y, ScoreConfig config) {
        return x.getMz() + ((y.getMz() + config.getH_MASS()) / 2);
    }

    /**
     * sum3 considers that the fragment ion represented by x is triply charged and that represented by y is singly charged.
     * 
     * @param Peak of the IsotopicCluster x
     * @param Peak of the MassSpectrum y
     * @return
     */
    protected double sum3(Peak x, Peak y, ScoreConfig config) {
        return x.getMz() + ((y.getMz() + (2 * config.getH_MASS())) / 3);
    }

    /**
     * sum4 considers that fragment ion represented by x is triply charged and that represented by y is doubly charged.
     * 
     * @param Peak of the IsotopicCluster x
     * @param Peak of the MassSpectrum y
     * @return
     */
    protected double sum4(Peak x, Peak y, ScoreConfig config) {
        return x.getMz() + (((y.getMz() * 2) + config.getH_MASS()) / 3);
    }

    /**
     * The first nonintensity feature is based on the number collection of peaks y whose mass differences with x approximate the residue mass of one of the twenty amino acids.
     * 
     * @param x Peak
     * @param y Peak
     * @param errortolerance Errortolerance
     * @return F1
     */
    protected int firstNonintensityFeature(Peak x, Peak y, double errortolerance, ScoreConfig config) {
        // long startTime = System.currentTimeMillis();

        int F1 = 0;

        double d1xy = Math.abs(diff1(x, y, config));
        double d2xy = Math.abs(diff2(x, y, config));
        double d2yx = Math.abs(diff2(y, x, config));
        double d3xy = Math.abs(diff3(x, y, config));
        double d3yx = Math.abs(diff3(y, x, config));
        double d4xy = Math.abs(diff4(x, y, config));
        double d4yx = Math.abs(diff4(y, x, config));

        double min = config.getMin() - 2;
        double max = config.getMax() + 2;

        if ((min < d1xy && d1xy < max) || (min < d2xy && d2xy < max) || (min < d2yx && d2yx < max) || (min < d3xy && d3xy < max) || (min < d3yx && d3yx < max) || (min < d4xy && d4xy < max)
                || (min < d4yx && d4yx < max)) {
            for (int i = 0; i < config.getAA_MASS().size(); i++) {
                double aa = config.getAA_MASS().get(i);
                double aa2 = config.getAA_MASS2().get(i);
                double aa3 = config.getAA_MASS3().get(i);
                double aape = aa + errortolerance;
                double aame = aa - errortolerance;
                double aa2pe = aa2 + errortolerance;
                double aa2me = aa2 - errortolerance;
                double aa3pe = aa3 + errortolerance;
                double aa3me = aa3 - errortolerance;

                if ((aame < d1xy && d1xy < aape) || (aa2me < d1xy && d1xy < aa2pe) || (aa3me < d1xy && d1xy < aa3pe) || (aa2me < d2xy && d2xy < aa2pe) || (aa2me < d2yx && d2yx < aa2pe)
                        || (aa3me < d3xy && d3xy < aa3pe) || (aa3me < d3yx && d3yx < aa3pe) || (aa3me < d4xy && d4xy < aa3pe) || (aa3me < d4yx && d4yx < aa3pe)) {
                    F1++;
                }

                // if (aame < d1xy && d1xy < aape) {
                // F1++;
                // } else if (aa2me < d1xy && d1xy < aa2pe) {
                // F1++;
                // } else if (aa3me < d1xy && d1xy < aa3pe) {
                // F1++;
                // } else if (aa2me < d2xy && d2xy < aa2pe) {
                // F1++;
                // } else if (aa2me < d2yx && d2yx < aa2pe) {
                // F1++;
                // } else if (aa3me < d3xy && d3xy < aa3pe) {
                // F1++;
                // } else if (aa3me < d3yx && d3yx < aa3pe) {
                // F1++;
                // } else if (aa3me < d4xy && d4xy < aa3pe) {
                // F1++;
                // } else if (aa3me < d4yx && d4yx < aa3pe) {
                // F1++;
                // }
            }
        }

        // long endTime = System.currentTimeMillis();

        // timescoref1 = timescoref1 + (endTime - startTime);

        return F1;
    }

    /**
     * The second nonintensity feature is based on the number collection of peaks y representing fragment ions that complement with fragment ion represented by x.
     * 
     * @param x Peak
     * @param y Peak
     * @param errortolerance Errortolerance
     * @param pepmass PeptidMass of MS
     * @param charge ChargeState of MS
     * @param ic IsotopicCluster
     * @return F2
     */
    protected int secondNonintensityFeature(Peak x, Peak y, double errortolerance, double pepmass, double charge, IsotopicCluster ic, ScoreConfig config) {
        // long startTime = System.currentTimeMillis();

        int F2 = 0;
        int i = 0;
        for (Peak c : ic.getIsotopicCluster()) {
            if (c.getMz() == x.getMz() && c.getIntensity() == x.getIntensity()) {
                break;
            }
            i++;
        }
        double s1xy = sum1(x, y, config);
        double s2xy = sum2(x, y, config);
        double s2yx = sum2(y, x, config);
        double s3xy = sum3(x, y, config);
        double s3yx = sum3(y, x, config);
        double s4xy = sum4(x, y, config);
        double s4yx = sum4(y, x, config);
        double m2i = (pepmass * charge - charge * config.getH_MASS()) + 2 * i;

        if (m2i + config.getH_MASSx2() - errortolerance < s1xy && s1xy < m2i + config.getH_MASSx2() + errortolerance) {
            F2++;
        } else if (m2i / 2 + config.getH_MASSx2() - errortolerance < s1xy && s1xy < m2i / 2 + config.getH_MASSx2() + errortolerance) {
            F2++;
        } else if (m2i / 3 + config.getH_MASSx2() - errortolerance < s1xy && s1xy < m2i / 3 + config.getH_MASSx2() + errortolerance) {
            F2++;
        } else if (m2i / 2 + config.getH_MASSx2() - errortolerance < s2xy && s2xy < m2i / 2 + config.getH_MASSx2() + errortolerance) {
            F2++;
        } else if (m2i / 2 + config.getH_MASSx2() - errortolerance < s2yx && s2yx < m2i / 2 + config.getH_MASSx2() + errortolerance) {
            F2++;
        } else if (m2i / 3 + config.getH_MASSx2() - errortolerance < s3xy && s3xy < m2i / 3 + config.getH_MASSx2() + errortolerance) {
            F2++;
        } else if (m2i / 3 + config.getH_MASSx2() - errortolerance < s3yx && s3yx < m2i / 3 + config.getH_MASSx2() + errortolerance) {
            F2++;
        } else if (m2i / 3 + config.getH_MASSx2() - errortolerance < s4xy && s4xy < m2i / 3 + config.getH_MASSx2() + errortolerance) {
            F2++;
        } else if (m2i / 3 + config.getH_MASSx2() - errortolerance < s4yx && s4yx < m2i / 3 + config.getH_MASSx2() + errortolerance) {
            F2++;
        }

        // long endTime = System.currentTimeMillis();

        // timescoref2 = timescoref2 + (endTime - startTime);

        return F2;
    }

    /**
     * The third nonintensity feature considers that the side chains of some amino acids residues of fragment ions can lose a water molecule (H2O) or an ammonia molecule (NH3). The number of peaks y
     * whose mass differences with x approximate the mass of a water molecule (H2O) or an ammonia molecule (NH3) is collected.
     * 
     * @param x Peak
     * @param y Peak
     * @param errortolerance Errortolerance
     * @return F3
     */
    protected int thirdNonintensityFeature(Peak x, Peak y, double errortolerance, ScoreConfig config) {
        // long startTime = System.currentTimeMillis();

        int F3 = 0;

        double d1xy = Math.abs(diff1(x, y, config));
        double d2xy = Math.abs(diff2(x, y, config));
        double d2yx = Math.abs(diff2(y, x, config));
        double d3xy = Math.abs(diff3(x, y, config));
        double d3yx = Math.abs(diff3(y, x, config));
        double d4xy = Math.abs(diff4(x, y, config));
        double d4yx = Math.abs(diff4(y, x, config));

        if (config.getH2O_MASS() - errortolerance < d1xy && d1xy < config.getH2O_MASS() + errortolerance) {
            F3++;
        } else if (config.getNH3_MASS() - errortolerance < d1xy && d1xy < config.getNH3_MASS() + errortolerance) {
            F3++;
        } else if (config.getH2O_MASSd2() - errortolerance < d1xy && d1xy < config.getH2O_MASSd2() + errortolerance) {
            F3++;
        } else if (config.getNH3_MASSd2() - errortolerance < d1xy && d1xy < config.getNH3_MASSd2() + errortolerance) {
            F3++;
        } else if (config.getH2O_MASSd3() - errortolerance < d1xy && d1xy < config.getH2O_MASSd3() + errortolerance) {
            F3++;
        } else if (config.getNH3_MASSd3() - errortolerance < d1xy && d1xy < config.getNH3_MASSd3() + errortolerance) {
            F3++;
        } else if (config.getH2O_MASSd2() - errortolerance < d2xy && d2xy < config.getH2O_MASSd2() + errortolerance) {
            F3++;
        } else if (config.getNH3_MASSd2() - errortolerance < d2xy && d2xy < config.getNH3_MASSd2() + errortolerance) {
            F3++;
        } else if (config.getH2O_MASSd2() - errortolerance < d2yx && d2yx < config.getH2O_MASSd2() + errortolerance) {
            F3++;
        } else if (config.getNH3_MASSd2() - errortolerance < d2yx && d2yx < config.getNH3_MASSd2() + errortolerance) {
            F3++;
        } else if (config.getH2O_MASSd3() - errortolerance < d3xy && d3xy < config.getH2O_MASSd3() + errortolerance) {
            F3++;
        } else if (config.getNH3_MASSd3() - errortolerance < d3xy && d3xy < config.getNH3_MASSd3() + errortolerance) {
            F3++;
        } else if (config.getH2O_MASSd3() - errortolerance < d3yx && d3yx < config.getH2O_MASSd3() + errortolerance) {
            F3++;
        } else if (config.getNH3_MASSd3() - errortolerance < d3yx && d3yx < config.getNH3_MASSd3() + errortolerance) {
            F3++;
        } else if (config.getH2O_MASSd3() - errortolerance < d4xy && d4xy < config.getH2O_MASSd3() + errortolerance) {
            F3++;
        } else if (config.getNH3_MASSd3() - errortolerance < d4xy && d4xy < config.getNH3_MASSd3() + errortolerance) {
            F3++;
        } else if (config.getH2O_MASSd3() - errortolerance < d4yx && d4yx < config.getH2O_MASSd3() + errortolerance) {
            F3++;
        } else if (config.getNH3_MASSd3() - errortolerance < d4yx && d4yx < config.getNH3_MASSd3() + errortolerance) {
            F3++;
        }

        // long endTime = System.currentTimeMillis();

        // timescoref3 = timescoref3 + (endTime - startTime);

        return F3;
    }

    /**
     * The fourth nonintensity feature considers two supportive ions a-ions and z-ions which can be used to indicate the existence of the corresponding b-ions and y-ions. The number of peaks
     * representing these kinds of supportive ions is collected.
     * 
     * @param x Peak
     * @param y Peak
     * @param errortolerance Errortolerance
     * @return F4
     */
    protected int fourthNonintensityFeature(Peak x, Peak y, double errortolerance, ScoreConfig config) {
        // long startTime = System.currentTimeMillis();

        int F4 = 0;

        double d1xy = Math.abs(diff1(x, y, config));
        double d2xy = Math.abs(diff2(x, y, config));
        double d2yx = Math.abs(diff2(y, x, config));
        double d3xy = Math.abs(diff3(x, y, config));
        double d3yx = Math.abs(diff3(y, x, config));
        double d4xy = Math.abs(diff4(x, y, config));
        double d4yx = Math.abs(diff4(y, x, config));

        if (config.getNH_MASS() - errortolerance < d1xy && d1xy < config.getCO_MASS() + errortolerance) {
            F4++;
        } else if (config.getCO_MASS() - errortolerance < d1xy && d1xy < config.getCO_MASS() + errortolerance) {
            F4++;
        } else if (config.getNH_MASSd2() - errortolerance < d1xy && d1xy < config.getNH_MASSd2() + errortolerance) {
            F4++;
        } else if (config.getCO_MASSd2() - errortolerance < d1xy && d1xy < config.getCO_MASSd2() + errortolerance) {
            F4++;
        } else if (config.getNH_MASSd3() - errortolerance < d1xy && d1xy < config.getNH_MASSd3() + errortolerance) {
            F4++;
        } else if (config.getCO_MASSd3() - errortolerance < d1xy && d1xy < config.getCO_MASSd3() + errortolerance) {
            F4++;
        } else if (config.getNH_MASSd2() - errortolerance < d2xy && d2xy < config.getNH_MASSd2() + errortolerance) {
            F4++;
        } else if (config.getCO_MASSd2() - errortolerance < d2xy && d2xy < config.getCO_MASSd2() + errortolerance) {
            F4++;
        } else if (config.getNH_MASSd2() - errortolerance < d2yx && d2yx < config.getNH_MASSd2() + errortolerance) {
            F4++;
        } else if (config.getCO_MASSd2() - errortolerance < d2yx && d2yx < config.getCO_MASSd2() + errortolerance) {
            F4++;
        } else if (config.getNH_MASSd3() - errortolerance < d3xy && d3xy < config.getNH_MASSd3() + errortolerance) {
            F4++;
        } else if (config.getCO_MASSd3() - errortolerance < d3xy && d3xy < config.getCO_MASSd3() + errortolerance) {
            F4++;
        } else if (config.getNH_MASSd3() - errortolerance < d3yx && d3yx < config.getNH_MASSd3() + errortolerance) {
            F4++;
        } else if (config.getCO_MASSd3() - errortolerance < d3yx && d3yx < config.getCO_MASSd3() + errortolerance) {
            F4++;
        } else if (config.getNH_MASSd3() - errortolerance < d4xy && d4xy < config.getNH_MASSd3() + errortolerance) {
            F4++;
        } else if (config.getCO_MASSd3() - errortolerance < d4xy && d4xy < config.getCO_MASSd3() + errortolerance) {
            F4++;
        } else if (config.getNH_MASSd3() - errortolerance < d4yx && d4yx < config.getNH_MASSd3() + errortolerance) {
            F4++;
        } else if (config.getCO_MASSd3() - errortolerance < d4yx && d4yx < config.getCO_MASSd3() + errortolerance) {
            F4++;
        }

        // long endTime = System.currentTimeMillis();

        // timescoref4 = timescoref4 + (endTime - startTime);

        return F4;
    }

    // NOT FINISHED YET
    /**
     * The intensity feature determines if the experimental isotopic distribution of one possible isotopic cluster matches with the theoretical isotopic distribution or not with the consideration of
     * the relationship between adjacent isotopic clusters in the graph.
     * 
     * @param con Connection
     * @param isotopicclustergraph IsotopicClusterGraph
     * @return F5
     */
    protected int fifthIntensityFeature(Connection con, DefaultDirectedWeightedGraph<IsotopicCluster, Connection> isotopicclustergraph, ScoreConfig config) {
        // long startTime = System.currentTimeMillis();

        int F5 = 0;
        double threshold = 0.3;

        int i = 0;
        for (Peak p : isotopicclustergraph.getEdgeTarget(con).getIsotopicCluster()) {
            // System.out.println("PEAK: " + p.getMz() + " MZ, " + p.getIntensity() + " INTENSITY");
            double T_MIN = (p.getMz() / config.getASP_MASS()) * p.getIntensity();
            // System.out.println("T_MIN: " + T_MIN);
            double T_MEAN = (p.getMz() / config.getAVE_UPDATED_MASS()) * p.getIntensity();
            // System.out.println("T_MEAN: " + T_MEAN);
            double T_MEAN_OVERLAP = 0;
            if (isotopicclustergraph.getEdgeSource(con).getIsotopicCluster() != null) {
                if (i < isotopicclustergraph.getEdgeSource(con).getIsotopicCluster().size()) {
                    T_MEAN_OVERLAP = (isotopicclustergraph.getEdgeSource(con).getIsotopicCluster().get(i).getMz() / config.getAVE_UPDATED_MASS()) * p.getIntensity();
                } else {
                    T_MEAN_OVERLAP = (isotopicclustergraph.getEdgeSource(con).getIsotopicCluster().get(isotopicclustergraph.getEdgeSource(con).getIsotopicCluster().size() - 1).getMz()
                            / config.getAVE_UPDATED_MASS()) * p.getIntensity();
                }
            }
            // System.out.println("T_MEAN_OVERLAP: " + T_MEAN_OVERLAP);
            double T_MAX = (p.getMz() / config.getPHE_MASS()) * p.getIntensity();
            // System.out.println("T_MAX: " + T_MAX);

            if (con.getColor() == "black") {
                // System.out.println("black: " + Math.min(Math.abs(p.getIntensity() - T_MIN), Math.abs(p.getIntensity() - T_MAX)) / T_MEAN);
                if (Math.min(Math.abs(p.getIntensity() - T_MIN), Math.abs(p.getIntensity() - T_MAX)) / T_MEAN <= threshold) {
                    F5++;
                }
            }

            if (con.getColor() == "red") {
                // System.out.println("red: " + Math.min(Math.abs((p.getIntensity() - T_MEAN_OVERLAP) - T_MIN), Math.abs((p.getIntensity() - T_MEAN_OVERLAP) - T_MAX)));
                if (Math.min(Math.abs((p.getIntensity() - T_MEAN_OVERLAP) - T_MIN), Math.abs((p.getIntensity() - T_MEAN_OVERLAP) - T_MAX)) / T_MEAN <= threshold) {
                    F5++;
                    // System.out.println("RED ADDED");
                }
            }
            i++;
        }

        // long endTime = System.currentTimeMillis();

        // timescoref5 = timescoref5 + (endTime - startTime);

        // System.out.println("SCORE: " + F5.size());
        return F5;
    }
}
