
/**
 * @author Lucas Schmidt
 * @since 2017-08-23
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class XICMSM {
    public static List<String> xicMSM(List<MassSpectrometryMeasurement.MassSpectrum> list) {
        List<String> slist = new ArrayList<String>();

        for (MassSpectrometryMeasurement.MassSpectrum e : list) {
            String summary = null;
            double intensitysum = 0;
            double rt = 0;

            for (double i : e.getIntensity()) {
                intensitysum += i;
            }
            rt = e.getRt();

            summary = intensitysum + ", " + rt;
            slist.add(summary);
        }

        return slist;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 8000; i++) {
            String typ = "MS Spectrum";
            String searchengine = "mascot";
            List<Double> mz2 = new ArrayList<Double>();
            List<Double> intensity2 = new ArrayList<Double>();

            for (int j = 0; j < 350; j++) {
                double random = ThreadLocalRandom.current().nextDouble(50, 500);
                mz2.add(random);
                intensity2.add(random * 2.34);

            }
            double peptidmass = ThreadLocalRandom.current().nextDouble(50, 500);
            double rt = ThreadLocalRandom.current().nextDouble(50, 500);
            int chargestate = 2;
            Collections.sort(mz2);
            Collections.sort(intensity2);

            double[] mz = mz2.stream().mapToDouble(Double::doubleValue).toArray();
            double[] intensity = intensity2.stream().mapToDouble(Double::doubleValue).toArray();

            MassSpectrometryMeasurement.addMSM(typ, searchengine, mz, intensity, peptidmass, rt, chargestate);

        }

        List<String> r = xicMSM(MassSpectrometryMeasurement.getMSMlist());

        for (String s : r) {
            System.out.println(s);
        }
    }
}
