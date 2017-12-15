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
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.fgcz.proteomics.dto.MassSpecMeasure;
import ch.fgcz.proteomics.dto.MassSpecMeasureSerializer;

public class ReadMGF {
    private static final Logger LOGGER = Logger.getLogger(MassSpecMeasureSerializer.class.getName());

    private ReadMGF() {
        throw new IllegalStateException("Reader (mgf) class");
    }

    public static MassSpecMeasure read(String fileName) {
        String source = readHeader(fileName);

        if (source == null) {
            source = fileName;
        }

        MassSpecMeasure massSpectrometryMeasurement = new MassSpecMeasure(source);

        massSpectrometryMeasurement = readLocal(fileName, massSpectrometryMeasurement);

        return massSpectrometryMeasurement;
    }

    private static String readHeader(String fileName) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(fileName));
            String line = "";
            String[] partEqual = line.split("=");

            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals("BEGIN IONS")) {
                    break;
                } else if (line.contains("COM")) {
                    return partEqual[1];
                }
            }

        } catch (FileNotFoundException e1) {
            LOGGER.log(Level.SEVERE, e1.toString(), e1);
        } catch (IOException e2) {
            LOGGER.log(Level.SEVERE, e2.toString(), e2);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    // Should already catched!
                }
            }
        }

        return null;
    }

    private static MassSpecMeasure readLocal(String fileName, MassSpecMeasure massSpectrometryMeasurement) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(fileName));

            String line = "";
            int chargeState = 0;
            int id = 0;
            double peptidMass = 0;
            List<Double> mz = new ArrayList<Double>();
            List<Double> intensity = new ArrayList<Double>();

            while ((line = bufferedReader.readLine()) != null) {
                String[] partEqual = line.split("=");
                String[] partSpace = line.split(" ");

                if (line.equals("BEGIN IONS")) {
                    chargeState = 0;
                    id = massSpectrometryMeasurement.getMSlist().size();
                    peptidMass = 0;
                    mz = new ArrayList<Double>();
                    intensity = new ArrayList<Double>();
                } else if (line.equals("END IONS")) {
                    massSpectrometryMeasurement.addMS(mz, intensity, peptidMass, chargeState, id);
                } else if (line.contains("CHARGE")) {
                    chargeState = Integer.parseInt(partEqual[1].substring(0, 1));
                } else if (line.contains("PEPMASS")) {
                    String[] peptidMassSplit = partEqual[1].split(" ");
                    peptidMass = Double.parseDouble(peptidMassSplit[0]);
                } else if (isDouble(partSpace[0]) && isDouble(partSpace[1])) {
                    mz.add(Double.parseDouble(partSpace[0]));
                    intensity.add(Double.parseDouble(partSpace[1]));
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    // Should already catched!
                }
            }
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
