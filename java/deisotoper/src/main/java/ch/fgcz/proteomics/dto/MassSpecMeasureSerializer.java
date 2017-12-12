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
        try {
            PrintWriter out = new PrintWriter(fileName);
            out.println(data);
            out.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        return data;
    }

    public static MassSpecMeasure deserializeFromJson(String fileName) {
        Gson gson = new Gson();

        String data = null;

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(fileName));

            StringBuilder stringbBuilder = new StringBuilder();
            String line = bufferedReader.readLine();

            while (line != null) {
                stringbBuilder.append(line);
                // stringbBuilder.append();
                line = bufferedReader.readLine();
            }
            data = stringbBuilder.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                }
            }
        }

        java.lang.reflect.Type type = new TypeToken<MassSpecMeasure>() {
        }.getType();

        return gson.fromJson(data, type);
    }
}