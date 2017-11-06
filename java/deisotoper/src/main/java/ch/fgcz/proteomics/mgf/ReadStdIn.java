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
        InputStreamReader isr = new InputStreamReader(System.in);
        String source = null;

        BufferedReader bufferedreader = new BufferedReader(isr);
        try {
            String line = bufferedreader.readLine();
            String[] partequal = line.split("=");

            while (line != null) {
                if (line.equals("BEGIN IONS")) {
                    break;
                } else if (line.contains("COM")) {
                    source = partequal[1];
                }

                line = bufferedreader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (source == null) {
            source = "stdin";
        }

        // ______________________________________

        MassSpectrometryMeasurement MSM = new MassSpectrometryMeasurement(source);

        // ______________________________________

        try {
            String line = "";
            int chargestate = 0;
            int id = 0;
            String searchengine = null;
            String typ = null;
            double peptidmass = 0;
            double rt = 0;
            List<Double> mz = new ArrayList<>();
            List<Double> intensity = new ArrayList<>();

            while ((line = bufferedreader.readLine()) != null) {
                String[] partequal = line.split("=");
                String[] partspace = line.split(" ");

                if (line.equals("BEGIN IONS")) {
                    chargestate = 0;
                    id = MSM.getMSlist().size();
                    searchengine = null;
                    typ = null;
                    peptidmass = 0;
                    rt = 0;
                    mz = new ArrayList<>();
                    intensity = new ArrayList<>();
                } else if (line.equals("END IONS")) {
                    MSM.addMS(typ, searchengine, mz, intensity, peptidmass, rt, chargestate, id);
                } else if (line.contains("CHARGE")) {
                    chargestate = Integer.parseInt(partequal[1].substring(0, 1));
                } else if (line.contains("TITLE")) {
                    typ = partequal[1];
                } else if (line.contains("RTINSECONDS")) {
                    rt = Double.parseDouble(partequal[1]);
                } else if (line.contains("PEPMASS")) {
                    String[] pepmasssplit = partequal[1].split(" ");
                    peptidmass = Double.parseDouble(pepmasssplit[0]);
                } else if (partspace.length > 1) {
                    if (isDouble(partspace[0]) && isDouble(partspace[1])) {
                        mz.add(Double.parseDouble(partspace[0]));
                        intensity.add(Double.parseDouble(partspace[1]));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return MSM;
    }

    private static boolean isDouble(String str) {
        Scanner scanner = new Scanner(str);
        boolean b = scanner.hasNextDouble();
        scanner.close();

        return b;
    }
}
