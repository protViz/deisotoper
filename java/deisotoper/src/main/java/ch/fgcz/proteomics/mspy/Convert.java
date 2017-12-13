package ch.fgcz.proteomics.mspy;

/**
 * @author Lucas Schmidt
 * @since 2017-09-15
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.fgcz.proteomics.dto.MassSpecMeasureSerializer;

/**
 * @deprecated isn't up to date anymore.
 */
@Deprecated
public class Convert {
    private static final Logger LOGGER = Logger.getLogger(MassSpecMeasureSerializer.class.getName());

    private Convert() {
        throw new IllegalStateException("Convert class");
    }

    public static Peaklist msdToPeaklist(String file) {
        List<Double> mz = new ArrayList<Double>();
        List<Double> intensity = new ArrayList<Double>();
        List<Integer> charge = new ArrayList<Integer>();
        List<Double> isotope = new ArrayList<Double>();

        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(file));

            msdToPeaklistHelper(bufferedReader, mz, intensity, charge, isotope);
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                }
            }
        }

        return new Peaklist(mz, intensity, charge, isotope);
    }

    private static void msdToPeaklistHelper(BufferedReader bufferedReader, List<Double> mz, List<Double> intensity,
            List<Integer> charge, List<Double> isotope) {
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                msdToPeaklistHelperWhile(line, mz, intensity, charge, isotope);
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    private static void msdToPeaklistHelperWhile(String line, List<Double> mz, List<Double> intensity,
            List<Integer> charge, List<Double> isotope) {
        if (line.contains("peak mz")) {
            String[] parts = line.split("\"");
            if (parts.length <= 7) {
                charge.add(-1);
                isotope.add(-1.0);
            }
            for (int i = 0; i < parts.length; i++) {
                if (i == 1) {
                    mz.add(Double.parseDouble(parts[i]));
                } else if (i == 3) {
                    intensity.add(Double.parseDouble(parts[i]));
                } else if (i == 7) {
                    charge.add(Integer.parseInt(parts[i]));
                } else if (i == 9) {
                    isotope.add(Double.parseDouble(parts[i]));
                }
            }
        }
    }

    /**
     * Attention: The input from a file must be in this format:
     * 
     * MZ INTENSITY
     * 
     * MZ INTENSITY
     * 
     * ...
     * 
     * For example:
     * 
     * 123.43 3823.65
     * 
     * 125.23 373.33
     * 
     * 127.26 4848.43
     * 
     * ...
     */
    public static Peaklist mgfToPeaklist(String file) {
        List<Double> mz = new ArrayList<Double>();
        List<Double> intensity = new ArrayList<Double>();

        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(" ");
                for (int i = 0; i < parts.length; i++) {
                    if (i == 0) {
                        mz.add(Double.parseDouble(parts[i]));
                    } else if (i == 1) {
                        intensity.add(Double.parseDouble(parts[i]));
                    }
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
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                }
            }
        }

        return new ch.fgcz.proteomics.mspy.Peaklist(mz, intensity);
    }
}
