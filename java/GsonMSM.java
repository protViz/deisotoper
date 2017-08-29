
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
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GsonMSM {
    /**
     * Serializes a List of MassSpectrum to a JSON-file.
     * 
     * @param filename
     * @param list
     * @return JSON-formatted string
     * @see MassSpectrometryMeasurement
     */
    public static String serializeMSMToJson(String filename, List<MassSpectrometryMeasurement.MassSpectrum> list) {
        Gson gson = new Gson();

        String jsonMSMlist = gson.toJson(list);

        try (PrintWriter out = new PrintWriter(filename)) {
            out.println(jsonMSMlist);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return jsonMSMlist;
    }

    /**
     * Deserializes a JSON-file to a List of MassSpectrum.
     * 
     * @param filename
     * @return List<MassSpectrum>
     */
    public static List<MassSpectrometryMeasurement.MassSpectrum> deserializeJsonToMSM(String filename) {
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

        java.lang.reflect.Type type = new TypeToken<List<MassSpectrometryMeasurement.MassSpectrum>>() {
        }.getType();

        List<MassSpectrometryMeasurement.MassSpectrum> list = gson.fromJson(data, type);

        return list;
    }
}
