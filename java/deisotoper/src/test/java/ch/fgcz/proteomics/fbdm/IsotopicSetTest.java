package ch.fgcz.proteomics.fbdm;

/**
 * @author Lucas Schmidt
 * @since 2017-10-26
 */

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import ch.fgcz.proteomics.dto.MassSpectrum;
import org.junit.Test;

public class IsotopicSetTest {
    @Test
    public void testClusterCreation() {
        Configuration config = new Configuration();

        double startMass = 100.;
        List<Double> mz = Arrays.asList(startMass, startMass + config.getIsotopicPeakDistance(),
                startMass + config.getIsotopicPeakDistance() * 1.5, startMass + config.getIsotopicPeakDistance() * 2);
        List<Double> intensity = Arrays.asList(1., 1., 1., 1.);
        MassSpectrum massSpectrum = new MassSpectrum(mz, intensity);
        List<Peak> isotpicSet = Arrays.asList(new Peak(mz.get(0), 1., 1), new Peak(mz.get(1), 1., 1),
                new Peak(mz.get(2), 1., 1), new Peak(mz.get(3), 1., 1));

        IsotopicSet isotopicSet = new IsotopicSet(massSpectrum, isotpicSet, 1, config);
        assertEquals(6, isotopicSet.getIsotopicSet().size());
    }
}