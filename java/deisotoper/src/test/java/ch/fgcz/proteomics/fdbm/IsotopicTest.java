package ch.fgcz.proteomics.fdbm;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class IsotopicTest {
    Peaklist peaklist;
    Peaklist peaklistr;

    @Before
    public void setUp() {
        // Example
        List<Double> mz = Arrays.asList(0.2, 1.0, 2.0, 2.5, 3.0, 10.0);
        List<Double> intensity = Arrays.asList(0.5, 2.0, 1.0, 1.0, 1.0, 3.0);
        peaklist = new Peaklist(mz, intensity);

        // Example Result
        List<Double> mzr = Arrays.asList(1.0, 2.0, 2.5, 3.0);
        List<Double> intensityr = Arrays.asList(2.0, 1.0, 1.0, 1.0);
        peaklistr = new Peaklist(mzr, intensityr);
    }

    @Test
    public void testIsotopeSet() {
        fail("Not yet implemented");
    }

    @Test
    public void testIsotopeCluster() {
        fail("Not yet implemented");
    }

}
