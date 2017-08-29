package main.java;

/**
 * @author Lucas Schmidt
 * @since 2017-08-28
 */

public class JsonToSummary {
	public static void main(String[] args) {
		System.out.println((Summary.makeSummary(GsonMSM.deserializeJsonToMSM(args[0]))));
	}
}
