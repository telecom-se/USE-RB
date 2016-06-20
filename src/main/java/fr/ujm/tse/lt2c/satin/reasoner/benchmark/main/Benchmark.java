package fr.ujm.tse.lt2c.satin.reasoner.benchmark.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.BenchmarkType;
import fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.DatasetsBenchmarked;
import fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.LogicalFragmentBenchmarked;
import fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.ReasonerBenchmarked;
import fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.utils.BenchmarkUtil;
import fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.utils.DatasetsUtil;
import fr.ujm.tse.lt2c.satin.perfstat.PerfResult;
import fr.ujm.tse.lt2c.satin.reasoner.benchmark.runners.BenchmarkRunner;

/**
 * Main Benchmark class, selects the ontology, logical fragment and reasoner to
 * start
 * 
 * @author Julien Subercaze, <julien.subercaze@univ-st-etienne.fr>
 * @author Christophe Gravier, <christophe.gravier@univ-st-etienne.fr>
 * 
 */
public class Benchmark {

	private BenchmarkType benchmarkType = BenchmarkType.ALL;

	/**
	 * A logging facility using log4j.
	 */
	private static final Logger LOGGER = Logger.getLogger(Benchmark.class);

	/**
	 * The program CLI options.
	 */
	private Options options = null;

	private int timeout = 300;

	private String outputRunFile = null;

	private int iterations;

	private String executable = null;

	private String benchmarkResultFile = null;
	private String benchmarkErrorFile = null;

	List<DatasetsBenchmarked> datasetUnderTest = new ArrayList<>();
	/**
	 * Memory in megabytes
	 */
	private int memory = 1;

	/**
	 * Whether including Jena or not in the benchmark, default to
	 * <code>false</code>. Is set to <code>true</code> when using \"-jena\̀"
	 * program option.
	 */
	private boolean withJena = false;

	private CommandLine cmdLine;

	private static final String FORCEDOWNLOAD_OPTION = "force-download";
	private static final String DATASET_OPTION = "datasets";
	private static final String HELP_OPTION = "help";
	private static final String TIMEOUT_OPTION = "timeout";
	private static final String RUNS_OUTPUTFILE = "outputfile";
	private static final String ITERATIONS = "iterations";
	private static final String RUNS_EXECUTABLE_DIR = "benchmark-exe-dir";
	private static final String RUNS_MEMORY = "memory";
	private static final String BENCHMARK_OUTPUTFILE = "benchmark-result-file";
	private static final String BENCHMARK_OUTPUT_ERROR_FILE = "benchmark-error-file";
	private static final String JENA_ON = "with_jena";

	/**
	 * If a dataset, reasoner, fragment triple is in this table, the benchmark
	 * will be run for this configuration. At initialisation time (see
	 * constructor implementation), the table is populated with all combinaison.
	 * As some configuration timesout, more complex combinaisons that we know
	 * will timeout will be removed. <br />
	 * The table is populated in the constructor of <code>this</code> using the
	 * enum types {@link DatasetsBenchmarked},
	 * {@link LogicalFragmentBenchmarked}, {@link ReasonerBenchmarked}.<br />
	 * The table is checked for each benchmark to be run in
	 * {@link BenchmarkRunner#run()} (using isElligbile private method).<br />
	 * Upon a timeout, the table is updated with further more complex
	 * configuration being disabled.
	 */
	public static Table<DatasetsBenchmarked, ReasonerBenchmarked, List<LogicalFragmentBenchmarked>> benchmarkTable = null;

	/**
	 * Hiding the default, unexpected, constructor to force usage of
	 * {@link Benchmark}
	 */
	@SuppressWarnings("unused")
	private Benchmark() {

		LOGGER.error("This constructor should not be called explicitly");
	}

	/**
	 * Expected constructor.
	 * 
	 * @param args
	 *            the command line arguments as provided to
	 *            {@link Benchmark#main(String[])}.
	 */
	public Benchmark(final String[] args) {

		this.options = this.createCliOptions();
		try {
			final CommandLineParser parser = new GnuParser();
			final CommandLine cmd = parser.parse(this.options, args);
			if (cmd.hasOption(HELP_OPTION)) {
				this.printHelp();
			} else {
				this.setProgramOptions(cmd);
				this.run();
			}
		} catch (final ParseException e) {
			LOGGER.error("Cannot parse program command line. Usage :", e);
			this.printHelp();
		}
	}

	/**
	 * Including Jena depends if the \"with-jena\" option was set.<br />
	 * Sesame only supports rdfs-full<br />
	 * Slider only supports rho-df<br />
	 * 
	 * @param includeJena
	 */
	private void setBenchmarkTable() {
		benchmarkTable = HashBasedTable.create();
		/*
		 * for (DatasetsBenchmarked db : this.datasetUnderTest) { for
		 * (ReasonerBenchmarked res : ReasonerBenchmarked.values()) { if
		 * ((res.equals(ReasonerBenchmarked.JENA) && this.withJena) ||
		 * !(res.equals(ReasonerBenchmarked.JENA))) { for
		 * (LogicalFragmentBenchmarked frag :
		 * LogicalFragmentBenchmarked.values()) { if
		 * (!(res.equals(ReasonerBenchmarked.SESAME) &&
		 * !frag.equals(LogicalFragmentBenchmarked.RDFS_FULL)) &&
		 * !(res.equals(ReasonerBenchmarked.STREAM) &&
		 * !frag.equals(LogicalFragmentBenchmarked.RHODF))) { if
		 * (benchmarkTable.get(db, res) == null) { benchmarkTable.put(db, res,
		 * new ArrayList<LogicalFragmentBenchmarked>()); }
		 * benchmarkTable.get(db, res).add(frag); } } } } }
		 */
		rdfoxForSlider(benchmarkTable);
//		rdfoxLubm(benchmarkTable);
		// lubmOnly(benchmarkTable);
		// owlimAndSliderAll(benchmarkTable);
		// rdfoxBsbm(benchmarkTable);
		// rdfoxForSlider(benchmarkTable);
		// inferrayOwlim(benchmarkTable);
		// subclassOflevels(benchmarkTable);
		// taxonomySubClassReal(benchmarkTable);
		// allButSubclassOfSliderNoRDFSPlus(benchmarkTable);
		// fullBenchmarkSigmod(benchmarkTable);
		// subclassOf(benchmarkTable);
		// rDFSPlusBench(benchmarkTable);
		LOGGER.info("The benchmark table was set to :\n" + humanReadble(benchmarkTable));
	}

	private void rdfoxLubm(
			Table<DatasetsBenchmarked, ReasonerBenchmarked, List<LogicalFragmentBenchmarked>> benchmarkTable2) {
		// List<DatasetsBenchmarked> datasetsUnderTest = new ArrayList<>();
		// this.datasetUnderTest.add(DatasetsBenchmarked.SUBCLASSOF5000);
		// this.datasetUnderTest.add(DatasetsBenchmarked.SUBCLASSOF10000);
		// this.datasetUnderTest.add(DatasetsBenchmarked.SUBCLASSOF25000);
		// this.datasetUnderTest.add(DatasetsBenchmarked.BSBM_1000);
		// this.datasetUnderTest.add(DatasetsBenchmarked.BSBM_5000);
		// this.datasetUnderTest.add(DatasetsBenchmarked.BSBM_10000);
//		 this.datasetUnderTest.add(DatasetsBenchmarked.BSBM_25000);
		 //this.datasetUnderTest.add(DatasetsBenchmarked.BSBM_50000);
		 this.datasetUnderTest.add(DatasetsBenchmarked.WIKIONTOLOGY);
		 this.datasetUnderTest.add(DatasetsBenchmarked.WORDNET);
		 this.datasetUnderTest.add(DatasetsBenchmarked.YAGO);
		// this.datasetUnderTest.add(DatasetsBenchmarked.LUBM_1);
		// this.datasetUnderTest.add(DatasetsBenchmarked.LUBM_5);
		// this.datasetUnderTest.add(DatasetsBenchmarked.LUBM_10);
//		this.datasetUnderTest.add(DatasetsBenchmarked.LUBM_25);
//		this.datasetUnderTest.add(DatasetsBenchmarked.LUBM_50);
		// this.datasetUnderTest.add(DatasetsBenchmarked.LUBM_75);
		// this.datasetUnderTest.add(DatasetsBenchmarked.LUBM_100);

		// this.datasetUnderTest.add(DatasetsBenchmarked.LUBM_75);
		// this.datasetUnderTest.add(DatasetsBenchmarked.LUBM_100);
		List<LogicalFragmentBenchmarked> fragments = new ArrayList<>(1);
		//fragments.add(LogicalFragmentBenchmarked.RHODF);
		//fragments.add(LogicalFragmentBenchmarked.RDFS_DEFAULT);
		//fragments.add(LogicalFragmentBenchmarked.RDFS_FULL);
		 fragments.add(LogicalFragmentBenchmarked.RDFSPLUS);

		for (DatasetsBenchmarked ds : this.datasetUnderTest) {
			List<ReasonerBenchmarked> allReasoners = new ArrayList<>(2);
			 allReasoners.add(ReasonerBenchmarked.INFERRAY);
			 allReasoners.add(ReasonerBenchmarked.RDFOX);
//			allReasoners.add(ReasonerBenchmarked.OWLIMSE);
			for (ReasonerBenchmarked reasoner : allReasoners) {
				benchmarkTable.put(ds, reasoner, new ArrayList<LogicalFragmentBenchmarked>());
				for (LogicalFragmentBenchmarked frag : fragments) {
					benchmarkTable.get(ds, reasoner).add(frag);
				}
			}
		}
	}

	private void rdfoxBsbm(
			Table<DatasetsBenchmarked, ReasonerBenchmarked, List<LogicalFragmentBenchmarked>> benchmarkTable2) {
		List<DatasetsBenchmarked> datasetsUnderTest = new ArrayList<>();
		datasetsUnderTest.add(DatasetsBenchmarked.BSBM_1000);
		datasetsUnderTest.add(DatasetsBenchmarked.BSBM_5000);
		datasetsUnderTest.add(DatasetsBenchmarked.BSBM_10000);
		datasetsUnderTest.add(DatasetsBenchmarked.BSBM_25000);
		datasetsUnderTest.add(DatasetsBenchmarked.BSBM_50000);
		List<LogicalFragmentBenchmarked> fragments = new ArrayList<>(3);
		fragments.add(LogicalFragmentBenchmarked.RHODF);
		fragments.add(LogicalFragmentBenchmarked.RDFS_DEFAULT);
		fragments.add(LogicalFragmentBenchmarked.RDFS_FULL);

		for (DatasetsBenchmarked ds : datasetsUnderTest) {
			List<ReasonerBenchmarked> allReasoners = new ArrayList<>(2);
			allReasoners.add(ReasonerBenchmarked.RDFOX);
			allReasoners.add(ReasonerBenchmarked.INFERRAY);
			for (ReasonerBenchmarked reasoner : allReasoners) {
				benchmarkTable.put(ds, reasoner, new ArrayList<LogicalFragmentBenchmarked>());
				for (LogicalFragmentBenchmarked frag : fragments) {
					benchmarkTable.get(ds, reasoner).add(frag);
				}
			}
		}
	}

	private void rdfoxForSlider(
			Table<DatasetsBenchmarked, ReasonerBenchmarked, List<LogicalFragmentBenchmarked>> benchmarkTable2) {
		datasetUnderTest = new ArrayList<>();
//		datasetUnderTest.add(DatasetsBenchmarked.BSBM_100);
//		datasetUnderTest.add(DatasetsBenchmarked.BSBM_200);
//		datasetUnderTest.add(DatasetsBenchmarked.BSBM_500);
//		datasetUnderTest.add(DatasetsBenchmarked.BSBM_1000);
//		 datasetUnderTest.add(DatasetsBenchmarked.BSBM_5000);
		datasetUnderTest.add(DatasetsBenchmarked.SUBCLASSOF50);
		datasetUnderTest.add(DatasetsBenchmarked.SUBCLASSOF100);
		 datasetUnderTest.add(DatasetsBenchmarked.SUBCLASSOF200);
		 datasetUnderTest.add(DatasetsBenchmarked.SUBCLASSOF500);
		 datasetUnderTest.add(DatasetsBenchmarked.SUBCLASSOF1000);
//		datasetUnderTest.add(DatasetsBenchmarked.WIKIONTOLOGY);
//		datasetUnderTest.add(DatasetsBenchmarked.WORDNET);
//		datasetUnderTest.add(DatasetsBenchmarked.YAGO);
		List<LogicalFragmentBenchmarked> fragments = new ArrayList<>(2);
		fragments.add(LogicalFragmentBenchmarked.RHODF);
		fragments.add(LogicalFragmentBenchmarked.RDFS_DEFAULT);
//		fragments.add(LogicalFragmentBenchmarked.RDFSPLUS);

		for (DatasetsBenchmarked ds : datasetUnderTest) {
			List<ReasonerBenchmarked> allReasoners = new ArrayList<>(1);
//			allReasoners.add(ReasonerBenchmarked.RDFOX);
//			allReasoners.add(ReasonerBenchmarked.STREAM);
			allReasoners.add(ReasonerBenchmarked.OWLIMSE);
			for (ReasonerBenchmarked reasoner : allReasoners) {
				benchmarkTable.put(ds, reasoner, new ArrayList<LogicalFragmentBenchmarked>());
				for (LogicalFragmentBenchmarked frag : fragments) {
					benchmarkTable.get(ds, reasoner).add(frag);
				}
			}
		}
	}

	// private void taxonomySubClassReal(
	// Table<DatasetsBenchmarked, ReasonerBenchmarked,
	// List<LogicalFragmentBenchmarked>> benchmarkTable2) {
	// List<DatasetsBenchmarked> datasetsUnderTest = new ArrayList<>();
	// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASS_REAL_PRODUCTS);
	// List<LogicalFragmentBenchmarked> rhodfFragment = new ArrayList<>(1);
	// rhodfFragment.add(LogicalFragmentBenchmarked.RDFS_FULL);
	//
	// for (DatasetsBenchmarked ds : datasetsUnderTest) {
	// List<ReasonerBenchmarked> allReasoners = new ArrayList<>(2);
	// allReasoners.add(ReasonerBenchmarked.INFERRAY);
	// allReasoners.add(ReasonerBenchmarked.OWLIMSE);
	//
	// for (ReasonerBenchmarked reasoner : allReasoners) {
	// benchmarkTable.put(ds, reasoner, new
	// ArrayList<LogicalFragmentBenchmarked>());
	// for (LogicalFragmentBenchmarked frag : rhodfFragment) {
	// benchmarkTable.get(ds, reasoner).add(frag);
	// }
	// }
	// }
	// }

	private void subclassOflevels(
			Table<DatasetsBenchmarked, ReasonerBenchmarked, List<LogicalFragmentBenchmarked>> benchmarkTable2) {
		List<DatasetsBenchmarked> datasetsUnderTest = new ArrayList<>();
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOFBINLVL_10);
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOFBINLVL_11);
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOFBINLVL_12);
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOFBINLVL_13);
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOFBINLVL_14);
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOFBINLVL_15);
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOFBINLVL_16);
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOFBINLVL_17);
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOFBINLVL_18);
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOFBINLVL_19);
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOFBINLVL_20);
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOFBINLVL_21);

		List<LogicalFragmentBenchmarked> rhodfFragment = new ArrayList<>(2);
		rhodfFragment.add(LogicalFragmentBenchmarked.RDFS_FULL);

		for (DatasetsBenchmarked ds : datasetsUnderTest) {
			List<ReasonerBenchmarked> allReasoners = new ArrayList<>(4);
			allReasoners.add(ReasonerBenchmarked.INFERRAY);
			// allReasoners.add(ReasonerBenchmarked.JENA);
			allReasoners.add(ReasonerBenchmarked.OWLIMSE);
			// allReasoners.add(ReasonerBenchmarked.SESAME);

			for (ReasonerBenchmarked reasoner : allReasoners) {
				benchmarkTable.put(ds, reasoner, new ArrayList<LogicalFragmentBenchmarked>());
				for (LogicalFragmentBenchmarked frag : rhodfFragment) {
					benchmarkTable.get(ds, reasoner).add(frag);
				}
			}
		}
	}

	private void sliderRDFSdefault(
			Table<DatasetsBenchmarked, ReasonerBenchmarked, List<LogicalFragmentBenchmarked>> benchmarkTable2) {
		List<DatasetsBenchmarked> datasetsUnderTest = new ArrayList<>();
		datasetsUnderTest.add(DatasetsBenchmarked.BSBM_100);
		datasetsUnderTest.add(DatasetsBenchmarked.BSBM_500);
		datasetsUnderTest.add(DatasetsBenchmarked.BSBM_1000);
		datasetsUnderTest.add(DatasetsBenchmarked.BSBM_5000);
		datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF50);
		datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF100);
		datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF500);
		datasetsUnderTest.add(DatasetsBenchmarked.WIKIONTOLOGY);
		datasetsUnderTest.add(DatasetsBenchmarked.WORDNET);
		//

		List<LogicalFragmentBenchmarked> rdfsFragment = new ArrayList<>(2);
		rdfsFragment.add(LogicalFragmentBenchmarked.RDFS_DEFAULT);

		for (DatasetsBenchmarked ds : datasetsUnderTest) {
			List<ReasonerBenchmarked> owlimAndSlider = new ArrayList<>(2);
			owlimAndSlider.add(ReasonerBenchmarked.STREAM);
			for (ReasonerBenchmarked reasoner : owlimAndSlider) {
				benchmarkTable.put(ds, reasoner, new ArrayList<LogicalFragmentBenchmarked>());
				for (LogicalFragmentBenchmarked frag : rdfsFragment) {
					benchmarkTable.get(ds, reasoner).add(frag);
				}
			}
		}
	}

	private void rDFSPlusBench(
			Table<DatasetsBenchmarked, ReasonerBenchmarked, List<LogicalFragmentBenchmarked>> benchmarkTable2) {
		List<DatasetsBenchmarked> datasetsUnderTest = new ArrayList<>();
		datasetsUnderTest.add(DatasetsBenchmarked.LUBM_1);
		datasetsUnderTest.add(DatasetsBenchmarked.LUBM_5);
		datasetsUnderTest.add(DatasetsBenchmarked.LUBM_10);
		datasetsUnderTest.add(DatasetsBenchmarked.LUBM_25);
		datasetsUnderTest.add(DatasetsBenchmarked.LUBM_50);
		datasetsUnderTest.add(DatasetsBenchmarked.LUBM_75);
		datasetsUnderTest.add(DatasetsBenchmarked.LUBM_100);

		datasetsUnderTest.add(DatasetsBenchmarked.WIKIONTOLOGY);
		datasetsUnderTest.add(DatasetsBenchmarked.WORDNET);
		datasetsUnderTest.add(DatasetsBenchmarked.YAGO);

		List<LogicalFragmentBenchmarked> rdfsFragment = new ArrayList<>(4);
		rdfsFragment.add(LogicalFragmentBenchmarked.RDFSPLUS);

		for (DatasetsBenchmarked ds : datasetsUnderTest) {
			List<ReasonerBenchmarked> allReasonersButSlider = new ArrayList<>(2);
			allReasonersButSlider.add(ReasonerBenchmarked.INFERRAY);
			allReasonersButSlider.add(ReasonerBenchmarked.OWLIMSE);
			allReasonersButSlider.add(ReasonerBenchmarked.JENA);

			for (ReasonerBenchmarked reasoner : allReasonersButSlider) {
				benchmarkTable.put(ds, reasoner, new ArrayList<LogicalFragmentBenchmarked>());
				for (LogicalFragmentBenchmarked frag : rdfsFragment) {
					benchmarkTable.get(ds, reasoner).add(frag);
				}
			}
		}

	}

	/**
	 * Create table for all reasoners except slider, with all datasets suitable
	 * for RDFS, RDFSdefault and rhodf
	 * 
	 * @param benchmarkTable2
	 */
	private void fullBenchmarkSigmod(
			Table<DatasetsBenchmarked, ReasonerBenchmarked, List<LogicalFragmentBenchmarked>> benchmarkTable2) {
		List<DatasetsBenchmarked> datasetsUnderTest = new ArrayList<>();
		// BSBM
		// datasetsUnderTest.add(DatasetsBenchmarked.BSBM_100);
		// datasetsUnderTest.add(DatasetsBenchmarked.BSBM_500);
		datasetsUnderTest.add(DatasetsBenchmarked.BSBM_1000);
		datasetsUnderTest.add(DatasetsBenchmarked.BSBM_5000);
		datasetsUnderTest.add(DatasetsBenchmarked.BSBM_10000);
		datasetsUnderTest.add(DatasetsBenchmarked.BSBM_25000);
		datasetsUnderTest.add(DatasetsBenchmarked.BSBM_50000);
		// // LUBM
		datasetsUnderTest.add(DatasetsBenchmarked.LUBM_1);
		datasetsUnderTest.add(DatasetsBenchmarked.LUBM_5);
		datasetsUnderTest.add(DatasetsBenchmarked.LUBM_10);
		datasetsUnderTest.add(DatasetsBenchmarked.LUBM_25);
		datasetsUnderTest.add(DatasetsBenchmarked.LUBM_50);
		datasetsUnderTest.add(DatasetsBenchmarked.LUBM_75);
		datasetsUnderTest.add(DatasetsBenchmarked.LUBM_100);
		// // Real world
		datasetsUnderTest.add(DatasetsBenchmarked.WIKIONTOLOGY);
		datasetsUnderTest.add(DatasetsBenchmarked.WORDNET);
		datasetsUnderTest.add(DatasetsBenchmarked.YAGO);

		List<LogicalFragmentBenchmarked> rdfsFragment = new ArrayList<>(4);
		rdfsFragment.add(LogicalFragmentBenchmarked.RDFS_DEFAULT);
		rdfsFragment.add(LogicalFragmentBenchmarked.RDFS_FULL);
		rdfsFragment.add(LogicalFragmentBenchmarked.RHODF);
		rdfsFragment.add(LogicalFragmentBenchmarked.RDFSPLUS);

		for (DatasetsBenchmarked ds : datasetsUnderTest) {
			List<ReasonerBenchmarked> allReasonersButSlider = new ArrayList<>(4);
			allReasonersButSlider.add(ReasonerBenchmarked.INFERRAY);
			allReasonersButSlider.add(ReasonerBenchmarked.OWLIMSE);
			allReasonersButSlider.add(ReasonerBenchmarked.JENA);
			allReasonersButSlider.add(ReasonerBenchmarked.SESAME);
			for (ReasonerBenchmarked reasoner : allReasonersButSlider) {
				benchmarkTable.put(ds, reasoner, new ArrayList<LogicalFragmentBenchmarked>());
				for (LogicalFragmentBenchmarked frag : rdfsFragment) {
					benchmarkTable.get(ds, reasoner).add(frag);
				}
			}
		}
	}

	private void inferrayOwlim(
			Table<DatasetsBenchmarked, ReasonerBenchmarked, List<LogicalFragmentBenchmarked>> benchmarkTable2) {
		List<DatasetsBenchmarked> datasetsUnderTest = new ArrayList<>();

		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF10);
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF20);
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF50);
		datasetsUnderTest.add(DatasetsBenchmarked.WIKIONTOLOGY);
		// datasetsUnderTest.add(DatasetsBenchmarked.WORDNET);
		// datasetsUnderTest.add(DatasetsBenchmarked.BSBM_1000);
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF100);
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF200);
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF500);
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF1000);
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF2500);
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF5000);
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF7500);
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF10000);
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF15000);

		List<LogicalFragmentBenchmarked> rdfsFragment = new ArrayList<>(2);
		rdfsFragment.add(LogicalFragmentBenchmarked.RDFS_DEFAULT);

		for (DatasetsBenchmarked ds : datasetsUnderTest) {
			List<ReasonerBenchmarked> inferrayAndOwlim = new ArrayList<>(2);
			// inferrayAndOwlim.add(ReasonerBenchmarked.INFERRAY);
			inferrayAndOwlim.add(ReasonerBenchmarked.OWLIMSE);
			// inferrayAndOwlim.add(ReasonerBenchmarked.JENA);
			// inferrayAndOwlim.add(ReasonerBenchmarked.SESAME);
			for (ReasonerBenchmarked reasoner : inferrayAndOwlim) {
				benchmarkTable.put(ds, reasoner, new ArrayList<LogicalFragmentBenchmarked>());
				for (LogicalFragmentBenchmarked frag : rdfsFragment) {
					benchmarkTable.get(ds, reasoner).add(frag);
				}
			}
		}
	}

	private void owlimAndSliderAll(
			Table<DatasetsBenchmarked, ReasonerBenchmarked, List<LogicalFragmentBenchmarked>> benchmarkTable2) {
		List<DatasetsBenchmarked> datasetsUnderTest = new ArrayList<>();
		datasetsUnderTest.add(DatasetsBenchmarked.BSBM_100);
		datasetsUnderTest.add(DatasetsBenchmarked.BSBM_200);
		datasetsUnderTest.add(DatasetsBenchmarked.BSBM_500);
		datasetsUnderTest.add(DatasetsBenchmarked.BSBM_1000);
		datasetsUnderTest.add(DatasetsBenchmarked.BSBM_5000);
		datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF50);
		datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF100);
		datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF200);
		datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF500);
		datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF1000);
		datasetsUnderTest.add(DatasetsBenchmarked.WIKIONTOLOGY);
		datasetsUnderTest.add(DatasetsBenchmarked.WORDNET);
		// datasetsUnderTest.add(DatasetsBenchmarked.YAGO);
		// datasetsUnderTest.add(DatasetsBenchmarked.GRIDSUBCLASS_LVL7X5);
		// datasetsUnderTest.add(DatasetsBenchmarked.GRIDSUBCLASS_LVL10X5);
		// datasetsUnderTest.add(DatasetsBenchmarked.GRIDSUBCLASS_LVL10X10);
		//

		List<LogicalFragmentBenchmarked> rdfsFragment = new ArrayList<>(2);
		rdfsFragment.add(LogicalFragmentBenchmarked.RHODF);
		rdfsFragment.add(LogicalFragmentBenchmarked.RDFS_DEFAULT);

		for (DatasetsBenchmarked ds : datasetsUnderTest) {
			List<ReasonerBenchmarked> owlimAndSlider = new ArrayList<>(2);
			owlimAndSlider.add(ReasonerBenchmarked.OWLIMSE);
			owlimAndSlider.add(ReasonerBenchmarked.STREAM);
			for (ReasonerBenchmarked reasoner : owlimAndSlider) {
				benchmarkTable.put(ds, reasoner, new ArrayList<LogicalFragmentBenchmarked>());
				for (LogicalFragmentBenchmarked frag : rdfsFragment) {
					benchmarkTable.get(ds, reasoner).add(frag);
				}
			}
		}
	}

	private void subclassOf(
			Table<DatasetsBenchmarked, ReasonerBenchmarked, List<LogicalFragmentBenchmarked>> benchmarkTable2) {

		List<DatasetsBenchmarked> datasetsUnderTest = new ArrayList<>(3);
		datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF500);
		datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF1000);
		datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF2500);
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF5000);
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF10000);
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF15000);
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF20000);
		// datasetsUnderTest.add(DatasetsBenchmarked.SUBCLASSOF25000);

		List<LogicalFragmentBenchmarked> rdfsFragment = new ArrayList<>(2);
		rdfsFragment.add(LogicalFragmentBenchmarked.RDFS_FULL);

		for (DatasetsBenchmarked ds : datasetsUnderTest) {
			List<ReasonerBenchmarked> inferrayAndOwlim = new ArrayList<>(2);
			// inferrayAndOwlim.add(ReasonerBenchmarked.INFERRAY);
			// inferrayAndOwlim.add(ReasonerBenchmarked.OWLIMSE);
			// inferrayAndOwlim.add(ReasonerBenchmarked.JENA);
			inferrayAndOwlim.add(ReasonerBenchmarked.SESAME);
			for (ReasonerBenchmarked reasoner : inferrayAndOwlim) {
				benchmarkTable.put(ds, reasoner, new ArrayList<LogicalFragmentBenchmarked>());
				for (LogicalFragmentBenchmarked frag : rdfsFragment) {
					benchmarkTable.get(ds, reasoner).add(frag);
				}
			}
		}
	}

	/**
	 * Initialize the benchmark to only use theLUBM datasets (
	 * {@link DatasetsBenchmarked#LUBM1}, {@link DatasetsBenchmarked#LUBM_5},
	 * {@link DatasetsBenchmarked#LUBM_10}) datasets.
	 * 
	 * @param benchmarkTable2
	 *            the benchmark table that will be run and woill be populated
	 *            within this method.
	 */
	private void lubmOnly(
			Table<DatasetsBenchmarked, ReasonerBenchmarked, List<LogicalFragmentBenchmarked>> benchmarkTable2) {

		List<DatasetsBenchmarked> datasetsLUBM = new ArrayList<>(3);
		datasetsLUBM.add(DatasetsBenchmarked.LUBM_1);
		datasetsLUBM.add(DatasetsBenchmarked.LUBM_5);
		datasetsLUBM.add(DatasetsBenchmarked.LUBM_10);
		datasetsLUBM.add(DatasetsBenchmarked.LUBM_25);
		datasetsLUBM.add(DatasetsBenchmarked.LUBM_50);
		datasetsLUBM.add(DatasetsBenchmarked.LUBM_75);
		datasetsLUBM.add(DatasetsBenchmarked.LUBM_100);

		for (DatasetsBenchmarked ds : datasetsLUBM) {
			for (ReasonerBenchmarked reasoner : ReasonerBenchmarked.values()) {
				benchmarkTable.put(ds, reasoner, new ArrayList<LogicalFragmentBenchmarked>());
				for (LogicalFragmentBenchmarked frag : LogicalFragmentBenchmarked.values()) {
					benchmarkTable.get(ds, reasoner).add(frag);
				}
			}
		}
	}

	private String humanReadble(
			Table<DatasetsBenchmarked, ReasonerBenchmarked, List<LogicalFragmentBenchmarked>> benchmarkTable2) {
		StringBuffer buf = new StringBuffer();
		for (DatasetsBenchmarked db : benchmarkTable2.rowKeySet()) {
			buf.append("\n").append(db.getDatasetName()).append(" : \n");
			for (ReasonerBenchmarked res : benchmarkTable2.row(db).keySet()) {
				buf.append("\t" + res.getReasonerName() + " => ");
				buf.append(benchmarkTable2.get(db, res).toString());
				buf.append("\n");
			}
		}
		return buf.toString();
	}

	/**
	 * 
	 */
	private void run() {
		LOGGER.info("Running benchmark ...");

		// switch (this.benchmarkType) {
		// case TEST:
		// LOGGER.info("Adding test datasets ...");
		// this.datasetUnderTest.add(DatasetsBenchmarked.BSBM_100);
		// this.datasetUnderTest.add(DatasetsBenchmarked.BSBM_500);
		// this.datasetUnderTest.add(DatasetsBenchmarked.YAGO);
		// break;
		// case ALL:
		// LOGGER.info("Adding ALL datasets ...");
		// this.datasetUnderTest.addAll(Arrays.asList(DatasetsBenchmarked.values()));
		// break;
		// default:
		// LOGGER.info("Adding datasets : " +
		// Arrays.asList(DatasetsBenchmarked.values().toString()));
		// this.datasetUnderTest.addAll(Arrays.asList(DatasetsBenchmarked.values()));
		// break;
		// }

		setBenchmarkTable();

		try {
			this.initializeDatasetsOnFilesystem();
		} catch (final ConfigurationException e) {
			LOGGER.error("Cannot find inferray.properties in the classpath.", e);
		}

		// remove dataset dir if exist
		createBenchmarkResultFile();
		removeBenchmarkTemporaryResultFileIfExist();

		LOGGER.info("Unarchiving owlim custom pie files...");
		this.unarchiveOwlimPies();

		LOGGER.info("Unarchiving inferray custom files...");
		this.unarchiveInferray();

		for (final DatasetsBenchmarked dataset : this.datasetUnderTest) {
			final BenchmarkRunner runner = new BenchmarkRunner(dataset, this.timeout, this.iterations,
					new File(this.outputRunFile), this.executable, this.memory, this.benchmarkResultFile,
					this.benchmarkErrorFile);
			runner.run();
		}
	}

	private void unarchiveInferray() {
		new BenchmarkUtil().extractResourceToTemp("inferray-files/rdfsAxiomatic.triples");
	}

	private void unarchiveOwlimPies() {
		new BenchmarkUtil().extractResourceToTemp("owlim-fragments/rdfsplus.pie");
		new BenchmarkUtil().extractResourceToTemp("owlim-fragments/rhodf.pie");
	}

	private void removeBenchmarkTemporaryResultFileIfExist() {
		File f = new File(outputRunFile);
		if (f.exists()) {
			if (f.isDirectory()) {
				throw new RuntimeException(new Error("The file " + this.outputRunFile
						+ " cannot be created since it is already a directory. Please change the output-file parameter value or remove this folder."));
			} else {
				LOGGER.warn("File " + this.outputRunFile + " exists from previous runs, removing it....");
				f.delete();
			}
		}
	}

	private void createBenchmarkResultFile() {
		File benchmarkResult = new File(this.benchmarkResultFile);
		File benchmarkErrors = new File(this.benchmarkErrorFile);
		removeIfExistOrErrorIfDirectory(benchmarkResult);
		removeIfExistOrErrorIfDirectory(benchmarkErrors);

		try {
			benchmarkResult.createNewFile();
			FileUtils.writeStringToFile(benchmarkResult,
					"reasoner;task;dataset;fragment;iterations;timeout (seconds);exec time (first run, in milisec);"
							+ PerfResult.getCsvColumnsNames() + "benchmark command.");

		} catch (IOException e) {
			throw new RuntimeException(new Error("Cannot create " + this.benchmarkResultFile));
		}
	}

	private void removeIfExistOrErrorIfDirectory(File benchmarkResult) {
		if (benchmarkResult.exists()) {
			if (benchmarkResult.isDirectory()) {
				throw new RuntimeException(new Error("The file " + this.benchmarkResultFile
						+ " cannot be created since it is already a directory. Please change parameter value accordingly or remove this folder."));
			} else {
				LOGGER.warn("File " + benchmarkResultFile + " already exists, removing it....");
				benchmarkResult.delete();
			}
		}
	}

	/**
	 * Analyze the program options and set the Benchmark configuration
	 * accordingly.
	 * 
	 * @param cmd
	 *            Program command options
	 */
	private void setProgramOptions(final CommandLine cmd) {
		this.checkDatasetsOptions(cmd);
	}

	/**
	 * Checks option \"--datasets\" (alias \"DS\") :
	 * <ul>
	 * <li>if the option values <code>test</code>, the benchmark is set to run
	 * against BSBM100k, BSBM200k, BSBM500k
	 * <li>
	 * <li>otherwise it is set to run against all available datasets. However,
	 * if the option was set to another value than \"all\" in this case, an
	 * error message is set to the logger.</li>
	 * </ul>
	 * 
	 * @param cmd
	 */
	private void checkDatasetsOptions(final CommandLine cmd) {
		if (cmd.hasOption(DATASET_OPTION) && "test".equals(cmd.getOptionValue(DATASET_OPTION))) {
			LOGGER.info("Using test datasets ...");
			this.benchmarkType = BenchmarkType.TEST;
		} else if (cmd.hasOption(DATASET_OPTION) && !"all".equals(cmd.getOptionValue(DATASET_OPTION))) {
			LOGGER.fatal(
					"Invalid or ommitted value for option \"--datasets\" (alias \"DS\"); resuming with \"ALL\" datasets");
			throw new RuntimeErrorException(new Error("Invalid argument --datasets"));
		}

		if (cmd.hasOption(TIMEOUT_OPTION) && cmd.getOptionValue(TIMEOUT_OPTION) != null
				&& BenchmarkUtil.isInteger(cmd.getOptionValue(TIMEOUT_OPTION), 10)) {
			this.timeout = Integer.parseInt(cmd.getOptionValue(TIMEOUT_OPTION));
			LOGGER.info("Using timeout of " + this.timeout + " seconds.");
		} else {
			LOGGER.fatal(
					"Invalid value for option \"--timeout\" (alias \"T\"); resuming with a timeout of 60 seconds.");
			throw new RuntimeErrorException(new Error("Invalid argument --timeout"));
		}

		if (cmd.hasOption(ITERATIONS) && cmd.getOptionValue(ITERATIONS) != null
				&& BenchmarkUtil.isInteger(cmd.getOptionValue(ITERATIONS), 10)) {
			this.iterations = Integer.parseInt(cmd.getOptionValue(ITERATIONS));
			LOGGER.info("Using  " + this.iterations + " iterations.");
		} else {
			LOGGER.fatal("Invalid value for option \"--iterations\" (alias \"I\").");
			throw new RuntimeErrorException(new Error("Invalid argument --iterations"));
		}

		if (cmd.hasOption(RUNS_OUTPUTFILE) && cmd.getOptionValue(RUNS_OUTPUTFILE) != null) {
			this.outputRunFile = cmd.getOptionValue(RUNS_OUTPUTFILE);
			LOGGER.info("Using intermediate outputfile : " + this.outputRunFile);
		} else {
			LOGGER.fatal("Invalid value for option \"--outputfile\" (alias \"O\").");
			throw new RuntimeErrorException(new Error("Invalid argument --outputfile"));
		}
		if (cmd.hasOption(RUNS_EXECUTABLE_DIR) && cmd.getOptionValue(RUNS_EXECUTABLE_DIR) != null) {
			this.executable = cmd.getOptionValue(RUNS_EXECUTABLE_DIR);
			LOGGER.info("Using executable file : " + this.executable);
		} else {
			LOGGER.fatal("Invalid value for option \"--benchmark-exe_dir)\" (alias \"E\").");
			throw new RuntimeErrorException(new Error("Invalid argument --benchmark-exe-dir"));
		}

		if (cmd.hasOption(RUNS_MEMORY) && cmd.getOptionValue(RUNS_MEMORY) != null
				&& BenchmarkUtil.isInteger(cmd.getOptionValue(RUNS_MEMORY), 10)) {
			this.memory = Integer.parseInt(cmd.getOptionValue(RUNS_MEMORY));
			LOGGER.info("Using  " + this.memory + " Gb for running a single benchmark.");
		} else {
			LOGGER.fatal("Invalid value for option \"--memory\" (alias \"M\").");
			throw new RuntimeErrorException(new Error("Invalid argument --memory"));
		}

		if (cmd.hasOption(BENCHMARK_OUTPUTFILE) && cmd.getOptionValue(BENCHMARK_OUTPUTFILE) != null) {
			this.benchmarkResultFile = cmd.getOptionValue(BENCHMARK_OUTPUTFILE);
			LOGGER.info("Using  " + this.benchmarkResultFile + " for the final benchmark results output file.");
		} else {
			LOGGER.fatal("Invalid value for option \"--" + BENCHMARK_OUTPUTFILE + "\" (alias \"BRES\").");
			throw new RuntimeErrorException(new Error("Invalid argument --" + BENCHMARK_OUTPUTFILE));
		}

		if (cmd.hasOption(BENCHMARK_OUTPUT_ERROR_FILE) && cmd.getOptionValue(BENCHMARK_OUTPUT_ERROR_FILE) != null) {
			this.benchmarkErrorFile = cmd.getOptionValue(BENCHMARK_OUTPUT_ERROR_FILE);
			LOGGER.info("Using  " + this.benchmarkErrorFile + " for the final benchmark results error file.");
		} else {
			LOGGER.fatal("Invalid value for option \"--" + BENCHMARK_OUTPUT_ERROR_FILE + "\" (alias \"BERR\").");
			throw new RuntimeErrorException(new Error("Invalid argument --" + BENCHMARK_OUTPUT_ERROR_FILE));
		}

		if (cmd.hasOption(JENA_ON)) {
			this.withJena = true;
			LOGGER.info("Including Jena in the benchmark.");
		} else {
			LOGGER.info(
					"Jena will not be included in the benchmark (if you want Jena, add the the \"-jena\" option.).");
		}
		this.cmdLine = cmd;
	}

	/**
	 * @return the outputRunFile
	 */
	public String getOutputRunFile() {
		return this.outputRunFile;
	}

	/**
	 * @param outputRunFile
	 *            the outputRunFile to set
	 */
	public void setOutputRunFile(final String outputRunFile) {
		this.outputRunFile = outputRunFile;
	}

	/**
	 * @return the iterations
	 */
	public int getIterations() {
		return this.iterations;
	}

	/**
	 * @param iterations
	 *            the iterations to set
	 */
	public void setIterations(final int iterations) {
		this.iterations = iterations;
	}

	/**
	 * Display the command help to the standard output.
	 * 
	 * @param options
	 *            the options that can be used with the program.
	 */
	private void printHelp() {
		final HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("Benchmark", this.options);
	}

	/**
	 * Initialize the program CLI for parsing and help.
	 * 
	 * @return The CLI Options for <code>this</code>
	 */
	private Options createCliOptions() {
		this.options = new Options();

		this.options.addOption("H", HELP_OPTION, false, "prints program CLI usage.");
		this.options.addOption("DS", DATASET_OPTION, true,
				"Datasets to benchmark. Possible values: \"test\" (BSM 100k, 200K, and 500k for testing purpose) and \"all\" (all supported datasets present). Default : \"all\"");
		this.options.addOption("F", FORCEDOWNLOAD_OPTION, false,
				"forces downloading all datasets for benchmarking, even if already present in the local filsystem at location specified at ${INFERRAY_DATASETS_REMOTESTORAGE} in inferray-benchmark.properties file. Activating this option may take some time depending on your Internet connection.");
		this.options.addOption("T", TIMEOUT_OPTION, true,
				"Max number of seconds for a reasoner to perform the inference task on a given fragment and a given dataset under test.");
		this.options.addOption("I", ITERATIONS, true, "Number of iterations.");
		this.options.addOption("O", RUNS_OUTPUTFILE, true, "Output file for intermediate results of runs.");
		this.options.addOption("E", RUNS_EXECUTABLE_DIR, true,
				"Folder where you built the ReasonersBenchmarked JAR files.");
		this.options.addOption("M", RUNS_MEMORY, true,
				"Memory in Megabyte for running a single benchmark. E.g. 2048 (unit is not required, only an integer is accepted)");
		this.options.addOption("BRES", BENCHMARK_OUTPUTFILE, true, "Output file for final results of the benchmark.");

		this.options.addOption("BERR", BENCHMARK_OUTPUT_ERROR_FILE, true,
				"Output file for errors that occured in the benchmark.");

		this.options.addOption("jena", JENA_ON, false,
				"Include Jena in the Benchmark (warning: this can take a lot of times !).");

		return this.options;
	}

	/**
	 * This method initializes the datasets used in the benchmark on the local
	 * filesystem. It supports the following :
	 * <ul>
	 * <li>If the "force-datasets-download" option was passed on the CLI, it
	 * downloads the datasets from the Web (from
	 * ${INFERRAY_DATASETS_REMOTESTORAGE} in inferray-benchmark.properties) to
	 * the local filesystem (${INFERRAY_DATASETS_LOCALSTORAGE} in
	 * inferray-benchmark.properties). in case the option \"test\" (alias \"T\")
	 * was also given, it is limited to BSBM100k, BSBM200k, and BSBM500k
	 * datasets. Otherwise all datasets are downloaded.</li>
	 * <li>Otherwise, it performs this download only if the local folder does
	 * not contains all the required datasets (if any is missing, all datasets
	 * are downloaded again from the Web).</li>
	 * </ul>
	 * 
	 * @param cmd
	 *            the command line when invoking the benchmark.
	 * @throws ConfigurationException
	 *             the configuration file
	 *             <code>inferray-benchmark.properties</code> was not find on
	 *             the filesystem.
	 */
	private void initializeDatasetsOnFilesystem() throws ConfigurationException {
		final PropertiesConfiguration config = new PropertiesConfiguration("inferray-benchmark.properties");
		final String localStorage = config.getString("INFERRAY_DATASETS_LOCALSTORAGE");
		if (cmdLine.hasOption(FORCEDOWNLOAD_OPTION)
				|| !DatasetsUtil.hasDatasetsOnFilesystem(localStorage, this.datasetUnderTest)) {

			String reason = "cmd line has option force dowload";
			if (!DatasetsUtil.hasDatasetsOnFilesystem(localStorage, this.datasetUnderTest)) {
				reason = this.datasetUnderTest + " does not exist in " + localStorage;
			}

			LOGGER.info(
					"At least one dataset is missing or the dataset folder is corrupted, force re-downloading all of them... reason : "
							+ reason);
			DatasetsUtil.downloadDatasets(this.benchmarkType.equals(BenchmarkType.TEST), this);
		} else {
			LOGGER.info("Datasets exists on filesystem at " + localStorage);
		}
	}

	/**
	 * @return the options
	 */
	public Options getOptions() {
		return this.options;
	}

	/**
	 * @param options
	 *            the options to set
	 */
	public void setOptions(final Options options) {
		this.options = options;
	}

	/**
	 * @return the benchmarkType
	 */
	public BenchmarkType getBenchmarkType() {
		return this.benchmarkType;
	}

	/**
	 * @param benchmarkType
	 *            the benchmarkType to set
	 */
	public void setBenchmarkType(final BenchmarkType benchmarkType) {
		this.benchmarkType = benchmarkType;
	}

	/**
	 * @return the timeout
	 */
	public int getTimeout() {
		return this.timeout;
	}

	/**
	 * @param timeout
	 *            the timeout to set
	 */
	public void setTimeout(final int timeout) {
		this.timeout = timeout;
	}

	/**
	 * The program entrypoint
	 * 
	 * @param args
	 *            the CLI arguments. Use "help" option to print program usage.
	 */
	public static void main(final String[] args) {
		final Benchmark benchmark = new Benchmark(args);
		benchmark.toString();
	}
}
