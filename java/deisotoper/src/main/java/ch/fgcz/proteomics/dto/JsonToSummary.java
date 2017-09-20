package ch.fgcz.proteomics.dto;

/**
 * @author Lucas Schmidt
 * @since 2017-08-28
 */

public class JsonToSummary {

    //TODO: what happens if no arguments are given?
    public static void main(String[] args) {
        System.out.println((Summary.makeSummary(Serialize.deserializeJsonToMSM(args[0]))));
    }
}
