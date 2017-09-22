package ch.fgcz.proteomics.fdbm;

import java.io.FileNotFoundException;

/**
 * @author Lucas Schmidt
 * @since 2017-09-21
 */

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.GraphPath;

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;
import ch.fgcz.proteomics.dto.MassSpectrum;
import ch.fgcz.proteomics.fdbm.IsotopicClusterGraph;

public class Deisotope {
    public static MassSpectrometryMeasurement deisotopeMSM(MassSpectrometryMeasurement input) {
        MassSpectrometryMeasurement output = new MassSpectrometryMeasurement(input.getSource() + "_output");

        int msid = 0;
        for (MassSpectrum MS : input.getMSlist()) {

            IsotopicMassSpectrum ims = new IsotopicMassSpectrum(MS, 0.01);

            List<Integer> chargelist = new ArrayList<>();
            List<Double> isotopelist = new ArrayList<>();
            List<Double> mzlist = new ArrayList<>();
            List<Double> intensitylist = new ArrayList<>();

            for (IsotopicSet IS : ims.getIsotopicMassSpectrum()) {
                IsotopicClusterGraph ICG = new IsotopicClusterGraph(IS);

                IsotopicClusterGraph.scoreIsotopicClusterGraph(ICG, MS.getPeptideMass(), MS.getChargeState(), 0.3, new Peaklist(MS.getMz(), MS.getIntensity()));

                IsotopicCluster start = null;
                for (IsotopicCluster e : ICG.getIsotopicclustergraph().vertexSet()) {
                    if (e.getIsotopicCluster() == null && e.getStatus() == "start") {
                        start = e;
                    }
                }

                IsotopicCluster end = null;
                for (IsotopicCluster e : ICG.getIsotopicclustergraph().vertexSet()) {
                    if (e.getIsotopicCluster() == null && e.getStatus() == "end") {
                        end = e;
                    }
                }

                GraphPath<IsotopicCluster, Connection> bp = IsotopicClusterGraph.bestPath(start, end, ICG);

                try {
                    IsotopicClusterGraph.drawPNGIsotopicClusterGraph(ICG.getIsotopicclustergraph(), IS.getSetID(), msid);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }

                List<Double> clustermz = new ArrayList<>();
                List<Double> clusteri = new ArrayList<>();
                List<Double> clusteriso = new ArrayList<>();
                List<Integer> clustercharge = new ArrayList<>();

                for (IsotopicCluster cluster : bp.getVertexList()) {
                    if (cluster.getIsotopicCluster() != null) {
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
            }
            msid++;
            output.addMS(MS.getTyp(), MS.getSearchEngine(), mzlist, intensitylist, MS.getPeptideMass(), MS.getRt(), MS.getChargeState(), MS.getId(), chargelist, isotopelist);
        }

        return output;
    }

    public static void main(String[] args) {
        String s = "TesterinoData.RData";

        String typ = "MS2 Spectrum";
        String searchengine = "mascot";
        double[] mz = { 0.2, 1.0, 2.0, 2.5, 3.0, 10.0 };
        double[] intensity = { 0.5, 2.0, 1.0, 1.0, 1.0, 3.0 };
        double peptidmass = 309.22;
        double rt = 38383.34;
        int chargestate = 2;
        int id = 0;

        String typ2 = "MS2 Spectrum";
        String searchengine2 = "mascot";
        double[] mz2 = { 0.2, 2.0, 3.0, 4.0, 9.0, 10.0, 10.5, 11.0 };
        double[] intensity2 = { 0.5, 2.0, 1.0, 1.0, 1.0, 3.0, 2.0, 3.0 };
        double peptidmass2 = 376.170684814453;
        double rt2 = 36.6818232;
        int chargestate2 = 2;
        int id2 = 1;

        String typ3 = "MS2 Spectrum";
        String searchengine3 = "mascot";
        double[] mz3 = { 110.0713959, 114.0914307, 114.137085, 115.0392151, 115.050354, 115.0866852, 115.6391602, 116.0706482, 120.0808105, 125.8757782, 127.086586, 128.804184, 128.9461212,
                129.0657043, 129.102356, 129.8626099, 130.0863647, 130.1056519, 131.0813446, 132.1617889, 133.0429993, 133.0493927, 136.0757294, 137.0792084, 137.3896637, 140.0817871, 141.1019897,
                146.6971588, 147.1128387, 149.0959778, 149.5975342, 155.0812683, 155.1175385, 156.0765686, 156.9156647, 156.9970856, 157.0606995, 158.0925446, 162.0988617, 165.1634369, 172.8946075,
                173.0105286, 173.1283722, 174.0873108, 175.1189728, 176.0070801, 183.1126862, 183.1490173, 184.0713959, 191.0210419, 191.6132507, 191.7319794, 192.8726501, 193.2059326, 197.1286926,
                200.138916, 201.0237579, 202.0229797, 202.0279541, 202.0819855, 204.1338806, 209.0917358, 209.1280365, 211.0968781, 211.1074066, 211.143631, 212.1391449, 213.1842499, 215.0389862,
                218.1496735, 223.1074677, 224.1105957, 225.0424652, 225.1227417, 226.1177673, 235.1074219, 235.8813477, 238.1180115, 239.1021881, 241.1540985, 241.4818878, 242.149353, 244.1648407,
                245.0767212, 249.1593781, 250.1883698, 251.6472321, 253.1174622, 255.1448975, 255.1699829, 256.1287537, 260.1964111, 263.9211731, 266.9976501, 268.9777222, 269.9775085, 270.1441345,
                270.9747314, 271.973877, 272.1714783, 273.17453, 275.9412231, 280.1291504, 281.112854, 283.2613831, 284.0343323, 285.0093689, 285.2781372, 286.0086365, 287.0059509, 288.0047302,
                292.1404724, 293.5869141, 296.6628723, 298.1390076, 302.0438232, 303.0192566, 304.0190735, 305.0160522, 306.0170288, 312.028595, 313.0361328, 315.1650391, 324.986084, 325.2117004,
                325.9839478, 326.1808777, 326.2150269, 326.6832581, 329.1763916, 329.2738647, 329.6730042, 331.6659851, 333.8161621, 334.8553467, 335.1712341, 341.0167847, 341.0348816, 342.9963074,
                343.9957886, 355.4973755, 357.0659485, 357.1871033, 357.2120056, 357.2252808, 357.2371216, 357.2486267, 357.3142395, 358.0656128, 358.2073364, 358.2528687, 359.0280762, 359.0405884,
                359.0449524, 360.027832, 365.1921997, 375.0761414, 375.9066772, 382.218811, 383.2034607, 400.2298584, 401.2324524, 425.2122498, 484.3762512, 487.2605591, 502.2973633, 503.834198,
                513.2964478, 513.3140869, 514.315918, 561.286438, 572.7901001, 583.8394165, 600.3455811, 601.3478394, 610.3301392, 611.3327026, };
        double[] intensity3 = { 2254.873046875, 3130.0, 586.6217041016, 17169.427734375, 1029.8999023438, 1075.2316894531, 508.8424682617, 1054.2337646484, 2588.6833496094, 516.8454589844,
                2436.0417480469, 600.4471435547, 656.7220458984, 3192.3530273438, 7762.7866210938, 675.3552856445, 4266.4448242188, 537.1724853516, 624.8748168945, 583.2230834961, 739.80859375,
                2589.583984375, 5080.7573242188, 641.6088867188, 474.8549804688, 755.8770141602, 822.0127563477, 502.3515625, 7464.513671875, 779.1889648438, 605.8829956055, 2241.859375,
                602.4045410156, 3107.0541992188, 482.6743469238, 1276.6114501953, 4209.7407226563, 2029.4914550781, 1014.102722168, 497.4643249512, 523.8324584961, 683.2845458984, 3555.3930664063,
                8643.361328125, 11929.7158203125, 516.8757324219, 4779.2866210938, 3364.3305664063, 2509.7268066406, 616.3309326172, 1277.6853027344, 556.8056640625, 542.2151489258, 567.0905761719,
                668.0389404297, 782.164855957, 11058.88671875, 1041.0152587891, 599.6173095703, 1232.5723876953, 542.1079711914, 1002.8142700195, 2249.716796875, 2272.2238769531, 3482.2084960938,
                909.7071533203, 1015.8739624023, 740.1159057617, 1015.5428466797, 1253.6051025391, 845.5424194336, 667.3259277344, 660.7888183594, 983.2106933594, 634.9072265625, 957.8540649414,
                539.1542358398, 656.7269287109, 714.7437133789, 755.3983154297, 514.3703613281, 1059.1479492188, 2636.5146484375, 594.383972168, 612.1343383789, 4018.03515625, 530.1975708008,
                2581.8171386719, 9953.2763671875, 10477.6640625, 865.5728149414, 1169.3836669922, 531.0369262695, 708.8749389648, 1184.6853027344, 1036.6330566406, 1887.1302490234, 5223.7333984375,
                1055.1431884766, 23609.794921875, 985.6216430664, 521.5093383789, 754.4852294922, 907.1423339844, 570.1965942383, 1730.0170898438, 12378.5712890625, 2437.2192382813, 8958.099609375,
                45414.92578125, 7166.74609375, 528.1646728516, 493.6032409668, 1016.477722168, 1287.8963623047, 2464.4729003906, 752.209777832, 585.606628418, 5735.296875, 627.829284668,
                976.0142211914, 2433.9970703125, 851.0481567383, 973.3766479492, 38369.66015625, 595.0002441406, 3462.4829101563, 6373.6342773438, 555.4959106445, 606.7036743164, 575.8446655273,
                971.2493896484, 657.0405883789, 768.4083862305, 690.0958251953, 616.078918457, 887.439453125, 1140.2591552734, 3375.732421875, 1185.9674072266, 572.606262207, 28814.376953125,
                689.9335327148, 652.3018188477, 682.4819335938, 6538.7446289063, 2910.8986816406, 1210.8598632813, 11098.2314453125, 1962.9514160156, 589.4377441406, 13202.185546875, 966.5912475586,
                482.2605285645, 4956.09765625, 814.169921875, 2164.6887207031, 594.3911132813, 2438.1279296875, 4699.2495117188, 12356.970703125, 1079.9952392578, 731.9503173828, 601.4769897461,
                2768.8037109375, 671.4821777344, 526.8400878906, 643.116394043, 11972.9921875, 1237.7651367188, 2388.5466308594, 532.4990234375, 558.966003418, 9239.3349609375, 1964.7623291016,
                12367.5673828125, 2328.0783691406 };
        double peptidmass3 = 357.697749043842;
        double rt3 = 36.3270726;
        int chargestate3 = 2;
        int id3 = 2;

        MassSpectrometryMeasurement MSM = new MassSpectrometryMeasurement(s);
        MSM.addMS(typ, searchengine, mz, intensity, peptidmass, rt, chargestate, id);
        MSM.addMS(typ2, searchengine2, mz2, intensity2, peptidmass2, rt2, chargestate2, id2);
        MSM.addMS(typ3, searchengine3, mz3, intensity3, peptidmass3, rt3, chargestate3, id3);

        // Make Summary of MSM
        System.out.println("Input summary:");
        System.out.println(ch.fgcz.proteomics.dto.Summary.makeSummary(MSM));

        // Make Summary of MSM deisotoped
        System.out.println("Output summary:");
        System.out.println(ch.fgcz.proteomics.dto.Summary.makeSummary(Deisotope.deisotopeMSM(MSM)));

        // for (MassSpectrum x : Deisotope.deisotopeMSM(MSM).getMSlist()) {
        // for (Double y : x.getMz()) {
        // System.out.println(y);
        // }
        // System.out.println();
        // for (Double y : x.getIntensity()) {
        // System.out.println(y);
        // }
        // }
    }
}
