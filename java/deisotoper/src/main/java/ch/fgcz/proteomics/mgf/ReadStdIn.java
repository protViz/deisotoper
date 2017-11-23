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

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;

public class ReadStdIn {
    public static MassSpectrometryMeasurement read() {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);

        MassSpectrometryMeasurement massSpectrometryMeasurement = new MassSpectrometryMeasurement(null);

        BufferedReader bufferedreader = new BufferedReader(inputStreamReader);
        try {
            String line = "";
            int chargeState = 0;
            int id = 0;
            String searchEngine = null;
            String typ = null;
            double peptidMass = 0;
            double rt = 0;
            List<Double> mz = new ArrayList<>();
            List<Double> intensity = new ArrayList<>();

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
                    mz = new ArrayList<>();
                    intensity = new ArrayList<>();
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
