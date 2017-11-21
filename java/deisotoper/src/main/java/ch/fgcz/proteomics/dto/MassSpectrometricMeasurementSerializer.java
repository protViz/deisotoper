package ch.fgcz.proteomics.dto;

/**
 * @author Lucas Schmidt
 * @since 2017-08-24
 * @url https://github.com/google/gson and https://mvnrepository.com/artifact/com.google.code.gson/gson/2.8.1
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MassSpectrometricMeasurementSerializer {
    public static String serializeToJson(String filename, MassSpectrometryMeasurement massspectrometrymeasurement) {
	Gson gson = new Gson();

	String data = gson.toJson(massspectrometrymeasurement);

	try (PrintWriter out = new PrintWriter(filename)) {
	    out.println(data);
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}

	return data;
    }

    public static MassSpectrometryMeasurement deserializeFromJson(String filename) {
	Gson gson = new Gson();

	String data = null;

	try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
	    StringBuilder stringbuilder = new StringBuilder();
	    String line = br.readLine();

	    while (line != null) {
		stringbuilder.append(line);
		stringbuilder.append(System.lineSeparator());
		line = br.readLine();
	    }
	    data = stringbuilder.toString();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	java.lang.reflect.Type type = new TypeToken<MassSpectrometryMeasurement>() {
	}.getType();

	return gson.fromJson(data, type);
    }
}