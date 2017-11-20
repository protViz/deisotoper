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

    public String getAnnotatedSpectrum() {
	return this.deisotoper.getAnnotatedSpectrum();
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
		if (ic.getIsotopicCluster() != null) {
		    peakic.addAll(ic.getIsotopicCluster());
		}
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

    // public static void main(String[] args) {
    // double[] mz = { 110.0715485, 110.1648788, 112.0395432, 113.0712204,
    // 114.0746384, 115.05056, 115.9784622,
    // 120.080986, 120.0833893, 121.0843735, 125.7498245, 129.0659332, 129.1023102,
    // 130.0863342, 130.1057281,
    // 131.0815735, 132.0849609, 132.5098419, 137.0346222, 141.0658417, 143.0816193,
    // 145.0608368, 147.1128235,
    // 149.0233917, 150.0266876, 153.0657654, 157.0970917, 159.076416, 160.0798035,
    // 162.0875397, 162.0912323,
    // 167.0816345, 170.092392, 171.0762177, 172.8989868, 175.118927, 180.0767212,
    // 185.0920105, 189.1023407,
    // 198.0873413, 199.0907135, 204.0980682, 205.0570374, 207.1127777, 208.0717316,
    // 210.0866547, 210.7322388,
    // 211.5193329, 212.10289, 214.0822296, 216.0977783, 223.0636444, 224.1031799,
    // 225.0603638, 226.0820923,
    // 227.0399933, 227.1023407, 228.098175, 230.1135101, 231.1125488, 231.1492462,
    // 232.0926361, 232.2126312,
    // 233.1646729, 235.1072845, 240.09758, 241.129364, 242.1128387, 246.1234741,
    // 249.098175, 253.0928345,
    // 254.1314087, 254.6329193, 255.1085358, 258.1084595, 259.1398315, 263.1368713,
    // 267.1086426, 268.1119995,
    // 269.1243591, 271.1038513, 273.1199646, 274.1184998, 276.1701965, 277.0926819,
    // 277.1541138, 278.4438171,
    // 279.108429, 285.1191101, 287.1348877, 289.650177, 292.1772461, 294.1808777,
    // 295.1038513, 295.184082,
    // 296.1346436, 297.1190491, 299.0617065, 300.133606, 301.0583801, 303.1295166,
    // 310.4591064, 314.1459961,
    // 315.129303, 318.1446228, 318.1605225, 324.1297607, 328.1278992, 331.8494568,
    // 332.1560364, 333.1555786,
    // 334.114502, 338.1456299, 342.1403198, 343.1411133, 346.176239, 351.1661377,
    // 356.1558228, 357.1579285,
    // 357.6632385, 358.1647949, 360.1513672, 360.6679077, 361.1502075, 363.2023926,
    // 366.6683655, 368.6846924,
    // 375.1660156, 375.2023926, 379.1607361, 381.2128296, 382.2158813, 387.1657104,
    // 388.1895447, 390.9455261,
    // 395.1658325, 397.1503906, 397.1950684, 397.6967163, 400.1613159, 403.135437,
    // 403.1973877, 411.1614075,
    // 418.1715698, 420.2235107, 421.1462402, 421.2265015, 426.1759949, 438.2343445,
    // 439.2384644, 443.2040405,
    // 444.1874084, 454.1725464, 461.2140198, 462.1977844, 471.1980896, 472.2180786,
    // 479.2248535, 482.1990967,
    // 489.2085571, 490.2118835, 490.2296448, 495.2563477, 507.2199402, 507.2555847,
    // 508.2598572, 525.2663574,
    // 526.2697754, 577.2515259, 578.2937012, 584.8656006, 596.3037109, 597.3069458,
    // 617.3034668, 618.2872925,
    // 635.3146362, 636.3175049, 653.3252563, 654.3284302 };
    // double[] intensity = { 6250.6640625, 563.5891113281, 787.7743530273,
    // 42913.109375, 927.4467773438,
    // 2023.1707763672, 529.0408325195, 25228.6796875, 1165.1545410156,
    // 603.5643310547, 528.4467773438,
    // 7070.5766601563, 31543.533203125, 19388.078125, 833.7221679688, 81864.546875,
    // 3091.2973632813,
    // 582.6003417969, 6660.9750976563, 34947.4921875, 684.2407836914,
    // 3036.4206542969, 36999.45703125,
    // 2973.7102050781, 829.9125366211, 6804.5180664063, 1259.5620117188,
    // 46302.8125, 2067.9748535156,
    // 1154.1105957031, 765.7245483398, 794.5812988281, 4291.3037109375,
    // 888.9245605469, 606.1282958984,
    // 766.3591918945, 14302.1279296875, 3288.0661621094, 688.5368652344,
    // 35533.54296875, 2225.658203125,
    // 670.2319335938, 677.4953613281, 6984.8188476563, 2347.3420410156,
    // 594.0815429688, 2258.9313964844,
    // 568.3290405273, 802.1146850586, 3599.0229492188, 7246.6596679688,
    // 2139.9233398438, 828.2638549805,
    // 884.8049316406, 1193.3479003906, 566.4050292969, 786.0283813477,
    // 615.2380981445, 1079.8623046875,
    // 1339.6888427734, 806.2881469727, 9631.443359375, 569.907409668,
    // 1065.3564453125, 991.0939331055,
    // 690.4099731445, 3580.0187988281, 672.0654296875, 845.2703857422,
    // 1281.3441162109, 559.6304931641,
    // 4143.1162109375, 1155.7521972656, 2598.6123046875, 953.2352905273,
    // 3944.8764648438, 7301.6743164063,
    // 4961.2807617188, 569.8205566406, 2566.8024902344, 795.173828125,
    // 757.6403808594, 3254.8908691406,
    // 7315.53515625, 1220.8388671875, 1115.0716552734, 524.3715820313,
    // 676.455871582, 2324.4851074219,
    // 808.6432495117, 1269.125, 581.4161376953, 31443.404296875, 2546.380859375,
    // 2911.2058105469,
    // 780.770324707, 738.4011230469, 1135.4598388672, 1176.7568359375,
    // 3533.0815429688, 2457.2565917969,
    // 562.0629882813, 928.1300048828, 1034.6174316406, 1035.1872558594,
    // 2155.4260253906, 3214.2192382813,
    // 586.0258178711, 545.24609375, 2240.283203125, 3230.6267089844,
    // 1323.4107666016, 924.3392944336,
    // 8073.1616210938, 767.8354492188, 789.9375610352, 2135.9985351563,
    // 999.5626831055, 642.1485595703,
    // 7692.0913085938, 1246.6643066406, 4829.984375, 799.643371582,
    // 3193.9008789063, 1350.830078125,
    // 901.1105957031, 687.5123901367, 996.9341430664, 1064.1864013672,
    // 911.1711425781, 16409.24609375,
    // 1171.5484619141, 960.3439331055, 935.4268188477, 677.4844970703,
    // 657.7783813477, 2241.4836425781,
    // 8700.474609375, 782.1491699219, 686.3291015625, 785.7287597656,
    // 753.0210571289, 714.055847168,
    // 2145.2751464844, 5911.2006835938, 1220.7615966797, 623.0693969727,
    // 634.6696777344, 33137.39453125,
    // 5135.7211914063, 813.663269043, 1231.4427490234, 933.5162963867,
    // 3532.599609375, 1009.3364868164,
    // 3219.3342285156, 800.7995605469, 3228.8891601563, 1368.7080078125,
    // 6327.9125976563, 810.8509521484,
    // 1239.9750976563, 791.6758422852, 1281.634765625, 6519.0903320313,
    // 575.096496582, 65968.4140625,
    // 11777.5908203125, 2284.7736816406, 2809.2331542969, 622.4571533203,
    // 22998.53515625, 3657.8000488281,
    // 1279.3221435547, 1236.6485595703, 9816.9814453125, 1331.3529052734,
    // 87058.9140625, 21567.640625 };
    //
    // FeaturesBasedDeisotoping dtoper = new FeaturesBasedDeisotoping();
    //
    // double[] aa = { 99.06841 };
    //
    // dtoper.setConfiguration(aa, 0.8, 0.5, 0.1, 0.1, 0.1, 0.03, 0.3, 1.0, 0,
    // true);
    //
    // dtoper.setMz(mz);
    // dtoper.setIntensity(intensity);
    // dtoper.setPepMass(1.2345);
    // dtoper.setCharge(2);
    //
    // dtoper.deisotope("none");
    //
    // double[] mzout = dtoper.getMz();
    // double[] intensityout = dtoper.getIntensity();
    //
    // System.out.println("Output Peaklist:");
    // for (int i = 0; i < mzout.length || i < intensityout.length; i++) {
    // System.out.print(mzout[i] + " ");
    // System.out.println(intensityout[i]);
    // }
    // System.out.println();
    //
    // System.out.println(dtoper.getSummary());
    // }
}