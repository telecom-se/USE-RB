package fr.ujm.tse.lt2c.satin.reasoner.benchmark.runners;

import java.io.File;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.DatasetsBenchmarked;
import fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.LogicalFragmentBenchmarked;
import fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.ReasonerBenchmarked;
import fr.ujm.tse.lt2c.satin.inferray.benchmark.configuration.utils.BenchmarkUtil;
import fr.ujm.tse.lt2c.satin.reasoner.benchmark.main.Benchmark;

/**
 * @author Christophe Gravier, <christophe.gravier@univ-st-etienne.fr> Provides
 *         functionality to run a benchmark on all reasoners in
 *         {@link ReasonerBenchmarked} for all logical Fragment in
 *         {@link LogicalFragmentBenchmarked} for the given dataset.
 */
public class BenchmarkRunner {

	public static String KILL_BENCHMARK_COMMAND = "pkill -9 -f .*fr.ujm.tse.lt2c.satin.Benchmark.*";

	/**
	 * Maximum number of seconds allocated to the reasoner to perform each run
	 */
	private int timeout = 0;

	/**
	 * Provides logging facility
	 */
	private static final Logger LOGGER = Logger.getLogger(BenchmarkRunner.class);

	/**
	 * The dataset under test folder when decompressed.
	 */
	private static final String DUT_FOLDER_NAME = "dataset-under-test";

	/**
	 * The dataset under test (DUT) for this run.
	 */
	private DatasetsBenchmarked dataset = null;

	/**
	 * Where to dump intermediate results on the filesystem.
	 */
	private File outputFile = null;

	/**
	 * Where to dump the benchmark result at the end of all benchmarks.
	 */
	private String benchmarkResultFile = null;

	/**
	 * Where to dump the benchmark errors messages
	 */
	private String benchmarkResultErrorFile = null;

	/**
	 * Absolute path to the external benchmark command. Will be parsed from
	 */
	private String executableFile = null;

	/**
	 * Allocated memory to the external benchmark
	 */
	private int memory = 1;

	/**
	 * The property from System.getProperty for getting the file separator.
	 */
	private static final String PROPERTY_FILESEPARATOR = "file.separator";

	/**
	 * Number of iterations for running the external benchmark (1 if you do not
	 * want to depend on just in time java compiler)
	 */
	private int iteration = 0;

	/**
	 * Hides default constructor that should not be called. Call instead
	 */
	@SuppressWarnings("unused")
	private BenchmarkRunner() {
		LOGGER.fatal("Unexpected constructor");
	}

	/**
	 * @param dataset
	 */
	public BenchmarkRunner(final DatasetsBenchmarked dataset, final int maxSeconds, final int nbIterations,
			final File output, final String executableFile, final int memory, final String resFile,
			final String errFile) {
		super();
		this.dataset = dataset;
		this.timeout = maxSeconds;
		this.iteration = nbIterations;
		this.outputFile = output;
		this.executableFile = executableFile;
		this.memory = memory;
		this.benchmarkResultFile = resFile;
		this.benchmarkResultErrorFile = errFile;
	}

	/**
	 * Launch a run for the DUT. The run is as follows :
	 * <ol>
	 * <li>For each Reasoner <code>r</code> in {@link ReasonerBenchmarked}</li>
	 * :
	 * <ol>
	 * <li>For each Logical Fragment <code>f</code> in
	 * {@link LogicalFragmentBenchmarked}</li> :
	 * <ul>
	 * <li>Run the <code>r</code> as an external command for reasoning on
	 * <code>this.dataset</code> using rules of the fragment <code>f</code>. The
	 * reasoner must perform this task with respect to the timeout
	 * <code>this.timeout</code></li>
	 * </ul>
	 * </ol>
	 * </ol>
	 */
	public void run() {

		if (datasetElligible(dataset)) {

			try {
				LOGGER.info("Decompressing dataset files... ");
				final String localFolderUncompressed = this.decompressDataset();
				LOGGER.info("Decompressing files for " + this.dataset.getDatasetName() + " to temporary folder : "
						+ localFolderUncompressed + ".");

				for (final ReasonerBenchmarked reasoner : ReasonerBenchmarked.values()) {

					boolean parseRecorded = false;
					for (final LogicalFragmentBenchmarked fragment : LogicalFragmentBenchmarked.values()) {

						if (isElligibleForBenchmark(fragment, reasoner, dataset)) {

							LOGGER.info("Running benchmark for :");
							LOGGER.info("\tReasoner : " + reasoner.getReasonerName());
							LOGGER.info("\tFragment : " + fragment.getFragmentName());
							LOGGER.info("\tDataset : " + localFolderUncompressed);

							if (parseRecorded) {
								final BenchRunConfiguration runConfigParse = new BenchRunConfiguration(
										localFolderUncompressed, fragment.getSatinBenchmarkName(), this.iteration,
										"true", reasoner.getReasonerSatinBenchmarkName(), this.getTimeout(),
										this.getOutputFile(), this.executableFile, this.memory,
										this.getBenchmarkResultFile(), this.getBenchmarkResultErrorFile());

								final BenchmarkResult parseResult = BenchmarkRunnerUtils
										.launchSingleBenchmark(runConfigParse);
								BenchmarkRunnerUtils.reccordBenchmarkResult(parseResult, runConfigParse);
								parseRecorded = true;
							}

							final BenchRunConfiguration runConfigParseAndInfer = new BenchRunConfiguration(
									localFolderUncompressed, fragment.getSatinBenchmarkName(), this.iteration, "false",
									reasoner.getReasonerSatinBenchmarkName(), this.getTimeout(), this.getOutputFile(),
									this.executableFile, this.memory, this.getBenchmarkResultFile(),
									this.getBenchmarkResultErrorFile());
							final BenchmarkResult inferResult = BenchmarkRunnerUtils
									.launchSingleBenchmark(runConfigParseAndInfer);

							BenchmarkRunnerUtils.reccordBenchmarkResult(inferResult, runConfigParseAndInfer);
						}
					}
				}
				LOGGER.info("Removing uncompressed files for " + this.dataset.getDatasetName()
						+ " from temporary folder : " + localFolderUncompressed + "...");
				this.removeUncompressDataset(localFolderUncompressed);
				LOGGER.info("Temporary folder removed.");

				LOGGER.info("Removing daemon processes ...");
				BenchmarkRunnerUtils.fragDaemonBenchmark();
				LOGGER.info("Daemon processes killed");
			} catch (final ConfigurationException e) {
				LOGGER.fatal(
						"Cannot find inferray benchmark configration file. This shoud not happen at it has normally already been found in the classpath by previosu method.",
						e);
				throw new RuntimeErrorException(new Error("No inferray-benchmark.properties in classpath"));
			}
		}
	}

	// /**
	// * Check if a previous benchmark in this reasoner + dataset configuration
	// on
	// * a lower fragment had not timeout in the past. It assumes that
	// inferences
	// * times using rho-df
	// *
	// * @param fragment
	// * @param reasoner
	// * @param dataset2
	// * @return
	// */
	// private boolean
	// hadNotTimeoutOnLowerComplexityFragment(LogicalFragmentBenchmarked
	// fragment, ReasonerBenchmarked reasoner,
	// DatasetsBenchmarked dataset2, String localFolderUncompressed) {
	//
	// if (fragment.equals(LogicalFragmentBenchmarked.RHODF)) {
	// return true;
	// }
	//
	// final BenchRunConfiguration runConfigParseAndInfer = new
	// BenchRunConfiguration(localFolderUncompressed,
	// fragment.getSatinBenchmarkName(),
	// this.iteration, "false", reasoner.getReasonerSatinBenchmarkName(),
	// this.getTimeout(), this.getOutputFile(), this.executableFile,
	// this.memory, this.getBenchmarkResultFile(),
	// this.getBenchmarkResultErrorFile());
	//
	// BenchmarkRunnerUtils.readBenchResultFrombenchmarkFile(runConfigParseAndInfer);
	// return false;
	// }

	/**
	 * A method used for restricting eligible benchmark configurations. Mainly
	 * used when debugging for convenience, but could be enhance in the future
	 * and extracted in a sole class in order to provide a selector framework
	 * for benchmarks.
	 * 
	 * @param fragment
	 *            the fragment candidate to a benchmark
	 * @param reasoner
	 *            the reasoner candidate to a benchmark
	 * @param dut
	 *            the dataset under test
	 * @return true when the benchmark must be run under the configuration
	 *         provided by the parameter, false otherwise.
	 */
	private boolean isElligibleForBenchmark(LogicalFragmentBenchmarked fragment, ReasonerBenchmarked reasoner,
			DatasetsBenchmarked dut) {

		// if (fragment.equals(LogicalFragmentBenchmarked.RDFSPLUS)
		// && reasoner.equals(ReasonerBenchmarked.JENA)
		// && dut.equals(DatasetsBenchmarked.BSBM_500)) {
		// this.setTimeout(8);
		// return true;
		// }
		//
		// if (fragment.equals(LogicalFragmentBenchmarked.RDFSPLUS)
		// && reasoner.equals(ReasonerBenchmarked.STREAM)
		// && dut.equals(DatasetsBenchmarked.BSBM_100)) {
		// this.setTimeout(300);
		// return true;
		// }
		//
		// if (fragment.equals(LogicalFragmentBenchmarked.RHODF)
		// && reasoner.equals(ReasonerBenchmarked.INFERRAY)
		// && dut.equals(DatasetsBenchmarked.BSBM_100)) {
		// this.setTimeout(300);
		// return true;
		// }
		//
		// return false;

		// if (fragment.equals(LogicalFragmentBenchmarked.RHODF)
		// && reasoner.equals(ReasonerBenchmarked.OWLIMSE)
		// && dut.equals(DatasetsBenchmarked.BSBM_100)) {
		// this.setTimeout(3000);
		// return true;
		// }
		//
		// if (reasoner.equals(ReasonerBenchmarked.OWLIMSE)
		// && dut.equals(DatasetsBenchmarked.BSBM_100)) {
		// this.setTimeout(3000);
		// return true;
		// }

		// disable jena for current benchmark

		List<LogicalFragmentBenchmarked> toRun = Benchmark.benchmarkTable.get(dut, reasoner);
		if (toRun != null && toRun.contains(fragment)) {
			return true;
		}

		return false;
	}

	/**
	 * Will the dataset be used in the benchmark ?
	 * 
	 * @param dut
	 * @return <code>true</code> if a configuration depends on this benchamrk,
	 *         <code>false</code> otherwise.
	 */
	private boolean datasetElligible(DatasetsBenchmarked dut) {
		boolean res = Benchmark.benchmarkTable.rowKeySet().contains(dut);
		return res;
	}

	/**
	 * Remove all temporary folder and file for the current dataset.
	 */
	private void removeUncompressDataset(final String tempDatasetFile) {
		File toDelete = new File(tempDatasetFile);
//		if (toDelete.exists()) {
//			FileUtils.deleteQuietly(toDelete);
//		}
	}

	/**
	 * Decompress <code>this.dataset</code> locally.
	 * 
	 * @throws ConfigurationException
	 *             when the configuration file for inferray benchmark cannot be
	 *             found.
	 */
	private String decompressDataset() throws ConfigurationException {
		final PropertiesConfiguration config = new PropertiesConfiguration("inferray-benchmark.properties");
		final String localStorage = config.getString("INFERRAY_DATASETS_LOCALSTORAGE");
		final String localCompressedFile = localStorage + System.getProperty(PROPERTY_FILESEPARATOR)
				+ config.getString(this.dataset.getPropertyFilename());
		final String localFolderUncompressed = localStorage + System.getProperty(PROPERTY_FILESEPARATOR)
				+ DUT_FOLDER_NAME;
		LOGGER.debug("Decompressing dataset " + this.dataset.getDatasetName() + " from " + localCompressedFile
				+ " to folder " + localFolderUncompressed);

		File localFolderUncompressedFile = new File(localFolderUncompressed);

		if (localFolderUncompressedFile.exists() && !localFolderUncompressedFile.isDirectory()) {
			localFolderUncompressedFile.delete();
		}
		if (!localFolderUncompressedFile.isDirectory()) {
			localFolderUncompressedFile.mkdirs();
			LOGGER.info("Creating temporary folder " + localFolderUncompressed + " for dataset under test");
		}
		 for (File file : localFolderUncompressedFile.listFiles()) {
		 file.delete();
		 }

		System.out.println("Decomp " + localCompressedFile + " to " + localFolderUncompressed);
		BenchmarkUtil.decompress(localCompressedFile, localFolderUncompressed);

		// sends back first .nt found here.
		String benchmarkedFile = null;
		for (File file : new File(localFolderUncompressed).listFiles()) {
			if (file.getName().endsWith((".nt")) && benchmarkedFile == null) {
				benchmarkedFile = localFolderUncompressed + System.getProperty("file.separator") + file.getName();
			}
		}

		if ((new File(localFolderUncompressed)).exists()) {
			if (benchmarkedFile == null) {
				throw new RuntimeException(new Error("The archive " + localCompressedFile
						+ " does not contain any n-triple file in its root directory. You can check the decompress archive at "
						+ localFolderUncompressed));
			}
			return benchmarkedFile;
		} else {
			throw new RuntimeErrorException(new Error("No n-triple file in " + localFolderUncompressed));
		}
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
	 * @return the dataset
	 */
	public DatasetsBenchmarked getDataset() {
		return this.dataset;
	}

	public String getBenchmarkResultErrorFile() {
		return benchmarkResultErrorFile;
	}

	public void setBenchmarkResultErrorFile(String benchmarkResultErrorFile) {
		this.benchmarkResultErrorFile = benchmarkResultErrorFile;
	}

	/**
	 * @param dataset
	 *            the dataset to set
	 */
	public void setDataset(final DatasetsBenchmarked dataset) {
		this.dataset = dataset;
	}

	/**
	 * @return the dutfoldername
	 */
	public static String getDutfoldername() {
		return DUT_FOLDER_NAME;
	}

	/**
	 * @return the outputFile
	 */
	public File getOutputFile() {
		return this.outputFile;
	}

	/**
	 * @param outputFile
	 *            the outputFile to set
	 */
	public void setOutputFile(final File outputFile) {
		this.outputFile = outputFile;
	}

	public String getBenchmarkResultFile() {
		return benchmarkResultFile;
	}

	public void setBenchmarkResultFile(String benchmarkResultFile) {
		this.benchmarkResultFile = benchmarkResultFile;
	}

	public String getExecutableFile() {
		return executableFile;
	}

	public void setExecutableFile(String executableFile) {
		this.executableFile = executableFile;
	}

	public int getMemory() {
		return memory;
	}

	public void setMemory(int memory) {
		this.memory = memory;
	}

	/**
	 * @return the iteration
	 */
	public int getIteration() {
		return this.iteration;
	}

	/**
	 * @param iteration
	 *            the iteration to set
	 */
	public void setIteration(final int iteration) {
		this.iteration = iteration;
	}
}
