package ch.fgcz.proteomics.R;

/**
 * @author Lucas Schmidt
 * @since 2017-10-31
 */

import ch.fgcz.proteomics.dto.*;

public class DataTransferObjectR {
    public static String serializeMSMToJsonR(String filename, MassSpectrometryMeasurement m) {
	return MassSpectrometricMeasurementSerializer.serializeToJson(filename, m);
    }

    public static MassSpectrometryMeasurement deserializeJsonToMSMR(String filename) {
	return MassSpectrometricMeasurementSerializer.deserializeFromJson(filename);
    }

    public static MassSpectrometryMeasurement massSpectrometryMeasurementR(String src) {
	return new MassSpectrometryMeasurement(src);
    }
}