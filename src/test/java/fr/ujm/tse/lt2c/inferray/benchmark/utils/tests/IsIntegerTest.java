package fr.ujm.tse.lt2c.inferray.benchmark.utils.tests;

import org.junit.Assert;
import org.junit.Test;

import fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.utils.BenchmarkUtil;

/**
 * @author Christophe Gravier, <christophe.gravier@univ-st-etienne.fr>
 *         Testing {@link BenchmarkUtil#isInteger(String, int)}
 */
public class IsIntegerTest {

    @Test
    public void isIntegerTest() {
        final String good = "23";
        final String[] bads = { "1.2", "2e12", "a23", "094z" };

        Assert.assertTrue(BenchmarkUtil.isInteger(good, 10));
        for (final String bad : bads) {
            Assert.assertFalse(BenchmarkUtil.isInteger(bad, 10));
        }
    }
}
