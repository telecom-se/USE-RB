package fr.ujm.tse.lt2c.inferray.benchmark.reasoners;

import fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.DatasetsBenchmarked;
import fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.LogicalFragmentBenchmarked;

/**
 * @author Christophe Gravier, <christophe.gravier@univ-st-etienne.fr>
 * 
 *         Reasoner interface describing the behavior of a reasoner :
 *         <ul>
 *         <li>Load n times the model programatically (measuring time and returning average parsing time). Param (n, dataset, ruleset, timeout)</li>
 *         <li>Load+infer m times programatically by launching it as an external tool (m-1 w/o measuring, last one measuring perf with perfstat) (m, dataset, ruleset, timeout)</li>
 *         </ul>
 */
public interface Reasonable {

    /**
     * Launch programatically the parsing of given dataset in a given fragment <code>n</code> times without performing inference. This is only the <code>n</code> times in-memory
     * model creation from n-triples file parsing. The average build-time is returned.
     * 
     * @param n
     *            the number of parsing run on which to compute the average parse time.
     * @param dataset
     *            the {@link DatasetsBenchmarked} to parse.
     * @param fragment
     *            the {@link LogicalFragmentBenchmarked} that <code>this</code> should use for parsing <code>dataset</code>.
     * @param timeout
     *            the maximum time allows for a single parsing.
     * @return average parse time for this reasoner.
     */
    public abstract int parseOnly(final int n);

    /**
     * Launch programmaticaly as an external command a parse+infer task for a given dataset and logical fragment. The external command is monitored using the perfstat software
     * whose result is parsed and loaed in in-memory model provided by RunConfigurationResult. The system warms up the JVM <code>m-1</code> times, and the <code>m-th</code> time is
     * actually measure performance using perfstats.
     * 
     * @param m
     *            the number of warm-ups +actually measure of performance.
     * @param dataset
     *            the dataset under test.
     * @param fragment
     *            the logical fragment used
     * @param timeout
     *            the timeout that each parse+run calls cannot exceed.
     * @return the measured perforamnce for this run
     */
    public abstract RunConfigurationResult parseAndInfer(int m);

}
