package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-09-19
 */

import org.jgrapht.graph.DefaultDirectedWeightedGraph;

public class Score {
    private double errorvalue;
    private double pepmassvalue;
    private double chargevalue;
    private DefaultDirectedWeightedGraph<IsotopicCluster, Connection> icg;
    private Configuration config;

    public Score(double error, double pepmass, double charge,
	    DefaultDirectedWeightedGraph<IsotopicCluster, Connection> isotopicclustergraph, Configuration config) {
	this.errorvalue = error;
	this.pepmassvalue = pepmass;
	this.chargevalue = charge;
	this.icg = isotopicclustergraph;
	this.config = config;
    }

    public double calculateScore(Peak peakx, Peak peaky, IsotopicCluster isotopicclusterofx, Connection connection) {
	return this.config.getFM1() * calculateFirstScoringFeature(peakx, peaky, this.errorvalue, this.config)
		+ this.config.getFM2() * calculateSecondScoringFeature(peakx, peaky, this.errorvalue, this.pepmassvalue,
			this.chargevalue, isotopicclusterofx, this.config)
		+ this.config.getFM3() * calculateThirdScoringFeature(peakx, peaky, this.errorvalue, this.config)
		+ this.config.getFM4() * calculateFourthScoringFeature(peakx, peaky, this.errorvalue, this.config)
		+ this.config.getFM5() * Score5.calculateFifthScoringFeature(connection, this.icg, this.config);
    }

    protected static double diff1(Peak x, Peak y, Configuration config) {
	return x.getMz() - y.getMz();
    }

    protected static double diff2(Peak x, Peak y, Configuration config) {
	return x.getMz() - ((y.getMz() + config.getH_MASS()) / 2);
    }

    protected static double diff3(Peak x, Peak y, Configuration config) {
	return x.getMz() - ((y.getMz() + (2 * config.getH_MASS())) / 3);
    }

    protected static double diff4(Peak x, Peak y, Configuration config) {
	return x.getMz() - (((y.getMz() * 2) + config.getH_MASS()) / 3);
    }

    protected static double sum1(Peak x, Peak y, Configuration config) {
	return x.getMz() + y.getMz();
    }

    protected static double sum2(Peak x, Peak y, Configuration config) {
	return x.getMz() + ((y.getMz() + config.getH_MASS()) / 2);
    }

    protected static double sum3(Peak x, Peak y, Configuration config) {
	return x.getMz() + ((y.getMz() + (2 * config.getH_MASS())) / 3);
    }

    protected static double sum4(Peak x, Peak y, Configuration config) {
	return x.getMz() + (((y.getMz() * 2) + config.getH_MASS()) / 3);
    }

    public static int calculateFirstScoringFeature(Peak x, Peak y, double errortolerance, Configuration config) {
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

	if ((min < d1xy && d1xy < max) || (min < d2xy && d2xy < max) || (min < d2yx && d2yx < max)
		|| (min < d3xy && d3xy < max) || (min < d3yx && d3yx < max) || (min < d4xy && d4xy < max)
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

		if ((aame < d1xy && d1xy < aape) || (aa2me < d1xy && d1xy < aa2pe) || (aa3me < d1xy && d1xy < aa3pe)
			|| (aa2me < d2xy && d2xy < aa2pe) || (aa2me < d2yx && d2yx < aa2pe)
			|| (aa3me < d3xy && d3xy < aa3pe) || (aa3me < d3yx && d3yx < aa3pe)
			|| (aa3me < d4xy && d4xy < aa3pe) || (aa3me < d4yx && d4yx < aa3pe)) {
		    F1++;
		}
	    }
	}

	return F1;
    }

    public static int calculateSecondScoringFeature(Peak x, Peak y, double errortolerance, double pepmass,
	    double charge, IsotopicCluster ic, Configuration config) {
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
	double m2i = (pepmass * charge - charge * config.getH_MASS()) + 2 * config.getDistance() * i;

	if (m2i + config.getH_MASSx2() - errortolerance < s1xy && s1xy < m2i + config.getH_MASSx2() + errortolerance) {
	    F2++;
	} else if (m2i / 2 + config.getH_MASSx2() - errortolerance < s1xy
		&& s1xy < m2i / 2 + config.getH_MASSx2() + errortolerance) {
	    F2++;
	} else if (m2i / 3 + config.getH_MASSx2() - errortolerance < s1xy
		&& s1xy < m2i / 3 + config.getH_MASSx2() + errortolerance) {
	    F2++;
	} else if (m2i / 2 + config.getH_MASSx2() - errortolerance < s2xy
		&& s2xy < m2i / 2 + config.getH_MASSx2() + errortolerance) {
	    F2++;
	} else if (m2i / 2 + config.getH_MASSx2() - errortolerance < s2yx
		&& s2yx < m2i / 2 + config.getH_MASSx2() + errortolerance) {
	    F2++;
	} else if (m2i / 3 + config.getH_MASSx2() - errortolerance < s3xy
		&& s3xy < m2i / 3 + config.getH_MASSx2() + errortolerance) {
	    F2++;
	} else if (m2i / 3 + config.getH_MASSx2() - errortolerance < s3yx
		&& s3yx < m2i / 3 + config.getH_MASSx2() + errortolerance) {
	    F2++;
	} else if (m2i / 3 + config.getH_MASSx2() - errortolerance < s4xy
		&& s4xy < m2i / 3 + config.getH_MASSx2() + errortolerance) {
	    F2++;
	} else if (m2i / 3 + config.getH_MASSx2() - errortolerance < s4yx
		&& s4yx < m2i / 3 + config.getH_MASSx2() + errortolerance) {
	    F2++;
	}

	return F2;
    }

    public static int calculateThirdScoringFeature(Peak x, Peak y, double errortolerance, Configuration config) {
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

	return F3;
    }

    public static int calculateFourthScoringFeature(Peak x, Peak y, double errortolerance, Configuration config) {
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

	return F4;
    }
}
