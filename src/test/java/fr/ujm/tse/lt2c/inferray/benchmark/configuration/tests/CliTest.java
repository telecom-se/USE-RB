package fr.ujm.tse.lt2c.inferray.benchmark.configuration.tests;

import java.io.OutputStream;
import java.io.PrintStream;

import org.junit.Assert;
import org.junit.Test;

import fr.ujm.tse.lt2c.satin.reasoner.benchmark.main.Benchmark;

/**
 * @author Christophe Gravier, <christophe.gravier@univ-st-etienne.fr>
 *         Testing the program CLI
 */
public class CliTest {

    /**
     * Testing if the CLI is built as expected.
     */
    @Test
    public void OptionsTest() {
        final String[] args = new String[1];
        args[0] = "--help";
        final PrintStream original = System.out;
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(final int b) {
                // DO NOTHING, we do not want help info to be printed to std out at test time.
            }
        }));
        System.out.println("this should go to /dev/null, but it doesn't because it's not supported on other platforms");
        final Benchmark bench = new Benchmark(args);

        System.setOut(original);
        Assert.assertTrue(bench.getOptions().hasOption("help"));
    }
}
