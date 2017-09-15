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

public class Serialize {
    /**
     * Serializes a MassSpectrometryMeasurement Object to a JSON-file.
     * 
     * @param filename
     * @param m
     * @return String
     * @see MassSpectrometryMeasurement
     */
    public static String serializeMSMToJson(String filename, MassSpectrometryMeasurement m) {
        Gson gson = new Gson();

        String jsonMSMlist = gson.toJson(m);

        try (PrintWriter out = new PrintWriter(filename)) {
            out.println(jsonMSMlist);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return jsonMSMlist;
    }

    /**
     * Deserializes a JSON-file to a MassSpectrometryMeasurement Object.
     * 
     * @param filename
     * @return MassSpectrometryMeasurement
     */
    public static MassSpectrometryMeasurement deserializeJsonToMSM(String filename) {
        Gson gson = new Gson();

        String data = null;

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            data = sb.toString();
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