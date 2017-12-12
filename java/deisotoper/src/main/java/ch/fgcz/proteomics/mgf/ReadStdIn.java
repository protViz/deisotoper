package ch.fgcz.proteomics.mgf;

/**
 * @author Lucas Schmidt
 * @since 2017-11-06
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.fgcz.proteomics.dto.MassSpecMeasure;
import ch.fgcz.proteomics.dto.MassSpecMeasureSerializer;

public class ReadStdIn {
    private static final Logger LOGGER = Logger.getLogger(MassSpecMeasureSerializer.class.getName());

    private ReadStdIn() {
        throw new IllegalStateException("Reader class");
    }

    public static MassSpecMeasure read() {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);

        MassSpecMeasure massSpectrometryMeasurement = new MassSpecMeasure(null);

        BufferedReader bufferedreader = new BufferedReader(inputStreamReader);
        try {
            String line = "";
            int chargeState = 0;
            int id = 0;
            String searchEngine = null;
            String typ = null;
            double peptidMass = 0;
            double rt = 0;
            List<Double> mz = new ArrayList<Double>();
            List<Double> intensity = new ArrayList<Double>();

            while ((line = bufferedreader.readLine()) != null) {
                String[] partEqual = line.split("=");
                String[] partSpace = line.split(" ");

                if (line.equals("BEGIN IONS")) {
                    chargeState = 0;
                    id = massSpectrometryMeasurement.getMSlist().size();
                    searchEngine = null;
                    typ = null;
                    peptidMass = 0;
                    rt = 0;
                    mz = new ArrayList<Double>();
                    intensity = new ArrayList<Double>();
                } else if (line.equals("END IONS")) {
                    massSpectrometryMeasurement.addMS(typ, searchEngine, mz, intensity, peptidMass, rt, chargeState,
                            id);
                } else if (line.contains("CHARGE")) {
                    chargeState = Integer.parseInt(partEqual[1].substring(0, 1));
                } else if (line.contains("TITLE")) {
                    typ = partEqual[1];
                } else if (line.contains("RTINSECONDS")) {
                    rt = Double.parseDouble(partEqual[1]);
                } else if (line.contains("PEPMASS")) {
                    String[] pepmasssplit = partEqual[1].split(" ");
                    peptidMass = Double.parseDouble(pepmasssplit[0]);
                } else if (partSpace.length > 1) {
                    if (isDouble(partSpace[0]) && isDouble(partSpace[1])) {
                        mz.add(Double.parseDouble(partSpace[0]));
                        intensity.add(Double.parseDouble(partSpace[1]));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }

        return massSpectrometryMeasurement;
    }

    private static boolean isDouble(String string) {
        Scanner scanner = new Scanner(string);
        boolean b = scanner.hasNextDouble();
        scanner.close();

        return b;
    }
}
