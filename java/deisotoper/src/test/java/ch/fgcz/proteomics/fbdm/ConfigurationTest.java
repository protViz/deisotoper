package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-11-23
 */
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public class ConfigurationTest {
    private static final double MIN = Double.MIN_VALUE;

    @Test
    public void testConfigurationCreation() {
        Configuration config = new Configuration(Arrays.asList(100.0, 1000.0), 0.839, 0.013, 0.12, 0.171, 0.993, 0.0054,
                0.3, 1.003, 0, true, "first");

        assertEquals(Arrays.asList(100.0, 1000.0), config.getAaMass());
        assertEquals(Arrays.asList(50.0, 500.0), config.getAaMassDividedTwo());
        assertEquals(0.839, config.getF1(), MIN);
        assertEquals(0.013, config.getF2(), MIN);
        assertEquals(0.12, config.getF3(), MIN);
        assertEquals(0.171, config.getF4(), MIN);
        assertEquals(0.993, config.getF5(), MIN);
        assertEquals(0.0054, config.getDelta(), MIN);
        assertEquals(0.3, config.getErrorTolerance(), MIN);
        assertEquals(1.003, config.getIsotopicPeakDistance(), MIN);
        assertEquals("first", config.getModus());
        assertEquals(true, config.isDecharge());
    }
}
