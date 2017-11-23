package ch.fgcz.proteomics.R;

/**
 * @author Lucas Schmidt
 * @since 2017-10-31
 */

import ch.fgcz.proteomics.dto.*;

public class DataTransferObjectR {
    public static String serializeMSMToJsonR(String fileName, MassSpectrometryMeasurement massSpectrometryMeasurement) {
        return MassSpectrometricMeasurementSerializer.serializeToJson(fileName, massSpectrometryMeasurement);
    }

    public static MassSpectrometryMeasurement deserializeJsonToMSMR(String fileName) {
        return MassSpectrometricMeasurementSerializer.deserializeFromJson(fileName);
    }

    public static MassSpectrometryMeasurement massSpectrometryMeasurementR(String source) {
        return new MassSpectrometryMeasurement(source);
    }
}