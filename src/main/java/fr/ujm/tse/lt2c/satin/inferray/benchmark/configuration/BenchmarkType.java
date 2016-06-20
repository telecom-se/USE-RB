package fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration;

/**
 * @author Christophe Gravier, <christophe.gravier@univ-st-etienne.fr>
 *         Type of benchmark :
 *         <ul>
 *         <li>TEST</li> : BSBM100k, BSBM200k, BSBM500k (for testing purpose)
 *         <li>ALL</li> : all supported datasets (The real stuff).
 *         </ul>
 *         This parameter can be controlled using the \"DS\" (alias \"--datasets\") option with the following values : \"test\" or \"all\".
 */
public enum BenchmarkType {
    TEST, ALL;
}
