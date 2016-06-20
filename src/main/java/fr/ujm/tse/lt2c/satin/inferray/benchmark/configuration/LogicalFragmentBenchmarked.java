package fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration;

import fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.utils.ListableValues;
import fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.utils.ListableValuesHelper;

/**
 * @author Julien Subercaze, <julien/subercaze@univ-st-etienne.fr>
 *         This class list the different logical fragment used in this benchmark.
 */

public enum LogicalFragmentBenchmarked implements ListableValues {

    /**
     * The different logical fragment, that are :
     * <ul>
     * <li>RDFS DEFAULT</li>
     * <li>RDFS FULL</li>
     * <li>rho-DF</li>
     * <li>RDF+</li>
     * </ul>
     */
    RDFS_DEFAULT {
        @Override
        public String getFragmentName() {
            return "RDFS default";
        }

        @Override
        public String getSatinBenchmarkName() {
            return "rdfs-default";
        }
    },
    RDFS_FULL {
        @Override
        public String getFragmentName() {
            return "RDFS Full";
        }

        @Override
        public String getSatinBenchmarkName() {
            return "rdfs-full";
        }
    },
    RHODF {
        @Override
        public String getFragmentName() {
            return "Rho-DF";
        }

        @Override
        public String getSatinBenchmarkName() {
            return "rho-df";
        }
    },
    RDFSPLUS {
        @Override
        public String getFragmentName() {
            return "RDFS+";
        }

        @Override
        public String getSatinBenchmarkName() {
            return "rdfs++";
        }
    };

    /**
     * A string label for presentation and logging purpose.
     * 
     * @return
     */
    public abstract String getFragmentName();

    public abstract String getSatinBenchmarkName();

    /**
     * List all possible values for <code>this</code> as a String array. Implementing factorized methods in a enum is a trick I learnt from
     * <code>http://stackoverflow.com/questions/77213/eliminating-duplicate-enum-code</code>
     */
    public static String[] names() {
        return ListableValuesHelper.names(LogicalFragmentBenchmarked.values());
    }
}
