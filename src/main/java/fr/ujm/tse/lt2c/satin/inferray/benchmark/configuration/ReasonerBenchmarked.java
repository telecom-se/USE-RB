package fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration;

import fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.utils.ListableValues;
import fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.utils.ListableValuesHelper;

/**
 * @author Christophe Gravier, <christophe.gravier@univ-st-etienne.fr>
 * 
 *         These are all the possible reasoners that we will use in the
 *         Benchmark, along their name for logging (
 *         {@link ReasonerBenchmarked#getReasonerName()}) and their name in the
 *         external benchmark program (
 *         {@link ReasonerBenchmarked#getReasonerSatinBenchmarkName()})
 */
public enum ReasonerBenchmarked implements ListableValues {

	/**
	 * Reasoner from Jules Chevalier's PhD.
	 */
	STREAM {
		@Override
		public String getReasonerName() {
			return "Slider";
		}

		@Override
		public String getReasonerSatinBenchmarkName() {
			return "slider";
		}
	},
	/**
	 * Reasoner from Jules Chevalier's PhD.
	 */
	RDFOX {
		@Override
		public String getReasonerName() {
			return "RDFOx";
		}

		@Override
		public String getReasonerSatinBenchmarkName() {
			return "rdfox";
		}
	},

	/**
	 * Apache <a href="https://jena.apache.org/">Jena</a>
	 */
	JENA {
		@Override
		public String getReasonerName() {
			return "Apache Jena";
		}

		@Override
		public String getReasonerSatinBenchmarkName() {
			return "jena";
		}
	},
	/**
	 * OWLIM SE from <a href="http://www.ontotext.com/">Ontotext</a>. You must
	 * ask an evaluation licence.
	 */
	OWLIMSE {
		@Override
		public String getReasonerName() {
			return "OWLIM SE";
		}

		@Override
		public String getReasonerSatinBenchmarkName() {
			return "owlimse";
		}
	},
	/**
	 * Inferray, a new undisclosed reasoner that we want to benchmark against
	 * other baselines.
	 */
	INFERRAY {
		@Override
		public String getReasonerName() {
			return "Inferray";
		}

		@Override
		public String getReasonerSatinBenchmarkName() {
			return "inferray";
		}
	},
	/**
	 * The default sesame reasoner.
	 */
	SESAME {
		@Override
		public String getReasonerName() {
			return "Sesame";
		}

		@Override
		public String getReasonerSatinBenchmarkName() {
			return "sesame";
		}
	};

	/**
	 * A string label for presentation and logging purpose.
	 * 
	 * @return
	 */
	public abstract String getReasonerName();

	/**
	 * 
	 * @return The name used by the ReasoenrsBenchmarked jar file that is called
	 *         as an external command when benchmarking a given reasoner, for a
	 *         given dataset and a given fragment. The application requires to
	 *         specify the reasoner using its own reasoners IDs as string, so
	 *         this string set this ID for the corresponding reasoner in our
	 *         in-memory model in this program.
	 */
	public abstract String getReasonerSatinBenchmarkName();

	/**
	 * List all possible values for <code>this</code> as a String array.
	 * Implementing factorized methods in a enum is a trick I learnt from
	 * <code>http://stackoverflow.com/questions/77213/eliminating-duplicate-enum-code</code>
	 */
	public static String[] names() {
		return ListableValuesHelper.names(ReasonerBenchmarked.values());
	}
}
