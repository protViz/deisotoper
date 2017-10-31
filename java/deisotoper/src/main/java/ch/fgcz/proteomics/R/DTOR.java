package ch.fgcz.proteomics.R;

/**
 * @author Lucas Schmidt
 * @since 2017-10-31
 */

import ch.fgcz.proteomics.dto.*;

public class DTOR {
    public String SummaryR(MassSpectrometryMeasurement m) {
        return Summary.makeSummary(m);
    }

    public String serializeMSMToJsonR(String filename, MassSpectrometryMeasurement m) {
        return Serialize.serializeMSMToJson(filename, m);
    }

    public MassSpectrometryMeasurement deserializeJsonToMSMR(String filename) {
        return Serialize.deserializeJsonToMSM(filename);
    }

    public String versionR() {
        return MassSpectrometryMeasurement.version();
    }

    public MassSpectrometryMeasurement massSpectrometryMeasurementR(String src) {
        return new MassSpectrometryMeasurement(src);
    }
}
