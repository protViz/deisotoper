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

public class MassSpecMeasureSerializer {
    public static String serializeToJson(String fileName, MassSpecMeasure massSpectrometryMeasurement) {
        Gson gson = new Gson();

        String data = gson.toJson(massSpectrometryMeasurement);

        PrintWriter out = null;
        try {
            out = new PrintWriter(fileName);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        out.println(data);

        return data;
    }

    public static MassSpecMeasure deserializeFromJson(String fileName) {
        Gson gson = new Gson();

        String data = null;

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        try {
            StringBuilder stringbBuilder = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                stringbBuilder.append(line);
                stringbBuilder.append(System.lineSeparator());
                line = br.readLine();
            }
            data = stringbBuilder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        java.lang.reflect.Type type = new TypeToken<MassSpecMeasure>() {
        }.getType();

        return gson.fromJson(data, type);
    }
}