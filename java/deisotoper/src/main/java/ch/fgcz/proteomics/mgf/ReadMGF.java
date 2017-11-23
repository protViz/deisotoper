package ch.fgcz.proteomics.mgf;

/**
 * @author Lucas Schmidt
 * @since 2017-11-03
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ch.fgcz.proteomics.dto.MassSpectrometryMeasurement;

public class ReadMGF {
    public static MassSpectrometryMeasurement read(String fileName) {
        String source = readHeader(fileName);

        if (source == null) {
            source = fileName;
        }

        MassSpectrometryMeasurement massSpectrometryMeasurement = new MassSpectrometryMeasurement(source);

        massSpectrometryMeasurement = readLocal(fileName, massSpectrometryMeasurement);

        return massSpectrometryMeasurement;
    }

    private static String readHeader(String fileName) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line = "";
            String[] partEqual = line.split("=");

            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals("BEGIN IONS")) {
                    break;
                } else if (line.contains("COM")) {
                    return partEqual[1];
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static MassSpectrometryMeasurement readLocal(String fileName,
            MassSpectrometryMeasurement massSpectrometryMeasurement) {
        try (BufferedReader bufferedreader = new BufferedReader(new FileReader(fileName))) {
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
                    String[] peptidMassSplit = partEqual[1].split(" ");
                    peptidMass = Double.parseDouble(peptidMassSplit[0]);
                } else if (isDouble(partSpace[0]) && isDouble(partSpace[1])) {
                    mz.add(Double.parseDouble(partSpace[0]));
                    intensity.add(Double.parseDouble(partSpace[1]));
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
