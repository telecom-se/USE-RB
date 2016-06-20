package fr.ujm.tse.lt2c.satin.reasoner.benchmark.runners;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javax.management.RuntimeErrorException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.log4j.Logger;

/**
 * @author Christophe Gravier, <christophe.gravier@univ-st-etienne.fr>
 * 
 *         This class offers the functionality to run a benchmark as an external
 *         command. It is an asynch call that promises a {@link BenchmarkResult}
 *         .
 */
public class BenchmarkExternalRun implements Callable<BenchmarkResult> {

	/**
	 * Logging facility
	 */
	private static final Logger LOGGER = Logger
			.getLogger(BenchmarkExternalRun.class);

	/**
	 * The {@link BenchRunConfiguration} containing the setup for running
	 * <code>this</code>
	 */
	private BenchRunConfiguration runConfig = null;

	/**
	 * <code>this</code> should not be ran without given
	 * {@link RunConfiguration} as in the expected constructor
	 */
	@SuppressWarnings("unused")
	private BenchmarkExternalRun() {

	}

	/**
	 * Expected constructor
	 * 
	 * @param service
	 */
	public BenchmarkExternalRun(BenchRunConfiguration rConfig,
			ExecutorService service) {
		this.runConfig = rConfig;
	}

	/**
	 * Launch the benchmark using commons exec framework over a
	 * <code>perf stat</code> command. It also streams the stdout of the running
	 * benchmark to a {@link CollectingLogOutputStream} instance that will
	 * especially watch for exception (and report it accordingly) and merge all
	 * lines in the stdout for further extraction of perf stat event values.
	 * 
	 * @return
	 */
	public BenchmarkResult launchSingleBenchmark() {

		final CommandLine externalCmdLine = BenchmarkRunnerUtils
				.createPerfStatExternalCommand(runConfig);

		final DefaultExecutor executor = new DefaultExecutor();

		/**
		 * Accepted return types are 0 (terminated successfully) or 143
		 * (interrupted because of a timeout)
		 */
		int[] acceptedReturnTypes = new int[2];
		acceptedReturnTypes[0] = 0;
		acceptedReturnTypes[1] = 143;

		executor.setExitValues(acceptedReturnTypes);
		final ExecuteWatchdog watchdog = new ExecuteWatchdog(
				TimeUnit.MILLISECONDS.convert(runConfig.getTimeoutInSeconds(),
						TimeUnit.SECONDS));
		final CollectingLogOutputStream out = new CollectingLogOutputStream(
				Thread.currentThread(), executor);

		Thread.currentThread().setUncaughtExceptionHandler(
				new UncaughtExceptionHandler() {

					@Override
					public void uncaughtException(Thread thread, Throwable ex) {
						LOGGER.warn("Benchmark "
								+ runConfig
								+ " could not be performed because of an error at runtime. Error will be reported in the file specified by the -BERR parameter.");
					}
				});

		final PumpStreamHandler psh = new PumpStreamHandler(out);

		executor.setStreamHandler(psh);
		executor.setWatchdog(watchdog);
		try {
			
			executor.execute(externalCmdLine);

			final String stdout = out.getStdOutputAsString();
			LOGGER.debug("Result of the perf stat command for this benchmark :"
					+ stdout);
			return new BenchmarkResult(stdout);
		} catch (final ExecuteException e) {

			if (out.getErrorMessage().equals("")) {
				LOGGER.warn(
						"Interupted external command line (most probably the reasoning task exceeded the timeout) for "
								+ externalCmdLine, e);
				executor.getWatchdog().destroyProcess();
				BenchmarkRunnerUtils.reportBenchmarkInError(runConfig,
						"Timeout");
			}
			return new BenchmarkResult("");

		} catch (final IOException e) {
			LOGGER.info(e.getMessage());
			throw new RuntimeErrorException(new Error(
					"Error processing external commandline " + externalCmdLine));
		} finally {
			if (out.isErroneous()) {
				BenchmarkRunnerUtils.reportBenchmarkInError(runConfig,
						out.getErrorMessage());
			}
		}
	}

	@Override
	public BenchmarkResult call() throws Exception {
		return launchSingleBenchmark();
	}
}
