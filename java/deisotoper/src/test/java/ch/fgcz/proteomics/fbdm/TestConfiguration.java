package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-11-23
 */

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class TestConfiguration {
    @Test
    public void testConfigurationCreation() {
        Configuration config = new Configuration(Arrays.asList(100.0, 1000.0), 0.839, 0.013, 0.12, 0.171, 0.993, 0.0054,
                0.3, 1.003, 0, true, "highest");

        assertEquals(config.getAaMass(), Arrays.asList(100.0, 1000.0));
        assertEquals(config.getAaMassDividedTwo(), Arrays.asList(50.0, 500.0));
        assertEquals(config.getF1(), 0.839, 0);
        assertEquals(config.getF2(), 0.013, 0);
        assertEquals(config.getF3(), 0.12, 0);
        assertEquals(config.getF4(), 0.171, 0);
        assertEquals(config.getF5(), 0.993, 0);
        assertEquals(config.getDelta(), 0.0054, 0);
        assertEquals(config.getErrortolerance(), 0.3, 0);
        assertEquals(config.getDistance(), 1.003, 0);
        assertEquals(config.isDecharge(), true);
        assertEquals(config.getModus(), "highest");
    }
}
