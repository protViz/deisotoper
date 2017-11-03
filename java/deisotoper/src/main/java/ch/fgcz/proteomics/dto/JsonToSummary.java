package ch.fgcz.proteomics.dto;

/**
 * @author Lucas Schmidt
 * @since 2017-08-28
 */

public class JsonToSummary {
    public static void main(String[] args) {
        if (args.length != 0) {
            System.out.println((Summary.makeSummary(MassSpectrometricMeasurementSerializer.deserializeFromJson(args[0]))));
        } else {
            System.err.println("WARNING: The arguments are missing!");
        }
    }
}
