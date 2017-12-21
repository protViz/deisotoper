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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MassSpecMeasureSerializer {
    private static final Logger LOGGER = Logger.getLogger(MassSpecMeasureSerializer.class.getName());

    public static String serializeToJson(String fileName, MassSpecMeasure massSpectrometryMeasurement) {
        Gson gson = new Gson();

        String data = gson.toJson(massSpectrometryMeasurement);
        try (PrintWriter out = new PrintWriter(fileName)) {
            out.println(data);
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }

        return data;
    }

    public static MassSpecMeasure deserializeFromJson(String fileName) {
        Gson gson = new Gson();

        String data = null;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder stringbBuilder = new StringBuilder();
            String line = bufferedReader.readLine();

            while (line != null) {
                stringbBuilder.append(line);
                stringbBuilder.append(System.lineSeparator());
                line = bufferedReader.readLine();
            }
            data = stringbBuilder.toString();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }

        java.lang.reflect.Type type = new TypeToken<MassSpecMeasure>() {
        }.getType();

        return gson.fromJson(data, type);
    }

    private MassSpecMeasureSerializer() {
        throw new IllegalStateException("Serializer class");
    }
}