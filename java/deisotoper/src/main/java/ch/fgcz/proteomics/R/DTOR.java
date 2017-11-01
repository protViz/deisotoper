package ch.fgcz.proteomics.R;

/**
 * @author Lucas Schmidt
 * @since 2017-10-31
 */

import ch.fgcz.proteomics.dto.*;

public class DTOR {
    public static String summaryMSMR(MassSpectrometryMeasurement m) {
        return Summary.makeSummary(m);
    }
    
    public static String summaryMSR(MassSpectrum m) {
        MassSpectrometryMeasurement msm = new MassSpectrometryMeasurement("");
        msm.getMSlist().add(m);
        
        return Summary.makeSummary(msm);
    }

    public static String serializeMSMToJsonR(String filename, MassSpectrometryMeasurement m) {
        return Serialize.serializeMSMToJson(filename, m);
    }

    public static MassSpectrometryMeasurement deserializeJsonToMSMR(String filename) {
        return Serialize.deserializeJsonToMSM(filename);
    }

    public static String versionR() {
        return MassSpectrometryMeasurement.version();
    }

    public static MassSpectrometryMeasurement massSpectrometryMeasurementR(String src) {
        return new MassSpectrometryMeasurement(src);
    }
}
