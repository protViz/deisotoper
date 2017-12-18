package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-11-23
 */
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ConfigurationTest {
    private static final double MIN = Double.MIN_VALUE;

    @Test
    public void testConfigurationCreation() {
        Configuration config = new Configuration(0.0054, 0.3, 1.003, 0, true, "first");

        assertEquals(0.8, config.getF(1), MIN);
        assertEquals(0.5, config.getF(2), MIN);
        assertEquals(0.1, config.getF(3), MIN);
        assertEquals(0.1, config.getF(4), MIN);
        assertEquals(0.1, config.getF(5), MIN);
        assertEquals(0.0054, config.getDelta(), MIN);
        assertEquals(0.3, config.getErrorTolerance(), MIN);
        assertEquals(1.003, config.getIsotopicPeakDistance(), MIN);
        assertEquals("first", config.getModus());
        assertEquals(true, config.isDecharge());
    }
}
