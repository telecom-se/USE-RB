package fr.ujm.tse.lt2c.satin.reasoner.benchmark.runners;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.management.RuntimeErrorException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import fr.ujm.tse.lt2c.satin.perfstat.PerfResult;

/**
 * @author Christophe Gravier, <christophe.gravier@univ-st-etienne.fr>
 * 
 *         Some utilities used in the benchmark framework.
 */
public class BenchmarkRunnerUtils {

    /**
     * The external command for killing a benchmark process previously launched
     */
    private final String killBenchmarksCommand = "pkill -f fr.ujm.tse.lt2c.satin.Benchmark";

    /**
     * Logging facility
     */
    private static final Logger LOGGER = Logger.getLogger(BenchmarkRunnerUtils.class);

    /**
     * hide default, unexpected, constructor
     */
    private BenchmarkRunnerUtils() {

    }

    /**
     * @param bench
     * @param outputFile
     * @param perfstat
     * @return
     */
    static CommandLine createPerfStatExternalCommand(final BenchRunConfiguration runConfig) {

        String perfstat = "perf stat -B ";
        // if (runConfig.getParseOnly().equals("true")) {
        //perfstat += " --repeat " + runConfig.getIterations() + " ";
        // }
        perfstat += " -e branch-misses,LLC-loads,LLC-load-misses,LLC-stores,LLC-store-misses,LLC-prefetches,LLC-prefetch-misses,cache-references,cache-misses,cycles,instructions,branches,L1-dcache-prefetches,L1-dcache-prefetch-misses,cpu-clock,task-clock,cpu-cycles,stalled-cycles-frontend,stalled-cycles-backend,L1-dcache-loads,L1-dcache-load-misses,dTLB-loads,dTLB-load-misses,node-loads,node-load-misses,page-faults,L1-icache-loads,L1-icache-load-misses  ";

//        String scriptCode = "cd " + runConfig.getExecutableDirectory() + " && java -Xmx" + runConfig.getMemory()
//                + "m -cp .:./libs/* fr.ujm.tse.lt2c.satin.Benchmark ";
        
        String scriptCode = "cd " + runConfig.getExecutableDirectory() + " && java -Xms1G -Xmx1G "
        + " -cp .:./libs/* fr.ujm.tse.lt2c.satin.Benchmark ";

        scriptCode += " -D '" + runConfig.getDataset() + "' ";
        scriptCode += " -F '" + runConfig.getFragment() + "' ";
        scriptCode += " -I " + runConfig.getIterations() + " "; // now handle
        // by perf.
        //scriptCode += " -I 5";
        scriptCode += " -P '" + runConfig.getParseOnly() + "' ";
        scriptCode += " -R '" + runConfig.getReasoner() + "' ";
        scriptCode += " -T " + runConfig.getTimeoutInSeconds() + " ";
        scriptCode += " -O '" + runConfig.getOutputFile() + "' ";

        // dump scriptCode to temporary file
        try {
            File temp = File.createTempFile("temp-file-name", ".tmp");
            FileUtils.writeStringToFile(temp, scriptCode);

            LOGGER.info("Running new benchmark :" + perfstat + " sh " + temp.getAbsolutePath());
            final CommandLine externalCmdLine = CommandLine.parse(perfstat + " sh " + temp.getAbsolutePath());
            runConfig.setCurentCmdLine(perfstat + " sh " + temp.getAbsolutePath());
            return externalCmdLine;
        } catch (IOException e) {
            throw new RuntimeErrorException(new Error(
                    "Cannot create a temporary file for dumping the script code to launch a benchmarck (script code : " + scriptCode + ")"));
        }
    }

    /**
     * Records a successful benchmark (terminated or that had timed out) that
     * gave a {@link BenchmarkResult} for a given {@link BenchRunConfiguration}
     * in the result file.
     * 
     * @param benchResult
     *            the result of the benchmark
     * @param runConfig
     *            the configuration used in the benchmark
     */
    public static void reccordBenchmarkResult(final BenchmarkResult benchResult, final BenchRunConfiguration runConfig) {

        // parse perf stat result
        final PerfResult perfResult = new PerfResult(benchResult.getRes());

        String configResult = readBenchResultFrombenchmarkFile(runConfig);
        
        try {
            LOGGER.info("Writing benchmark results to " + runConfig.getBenchmarkoutputFile());
            if (!configResult.equals("")) {
                FileUtils.writeStringToFile(new File(runConfig.getBenchmarkoutputFile()), "\n" + configResult + ";" + perfResult.toCsv() + ";"
                        + runConfig.getCmd(), true);
            }
        } catch (IOException e) {
            throw new RuntimeException(new Error("Cannot write benchmark result to " + runConfig.getBenchmarkoutputFile() + " because of "
                    + e.getMessage() + " for configuration " + runConfig.toString()));
        }

        /**
         * Build the INFER ONLY result by fetching parse result and substracting
         * exec and perf stats from it.
         */
        // if (runConfig.getParseOnly().equals("false")) {
        // // ConfigResult contains infer+parse infos.
        // // Fetch parse only information
        // BenchRunConfiguration parseOnlyConfig = new
        // Cloner().deepClone(runConfig);
        // parseOnlyConfig.setParseOnly("true");
        // String parseConfigResult =
        // readBenchResultFrombenchmarkFile(parseOnlyConfig);
        // // will give something like
        // //
        // inferray,PARSE,/home/satin/datasets/dataset-under-test/dataset_100k.nt,rdfs++,1,4,2220,0
        // // and we want to substract exec time for inference from infer+parse
        // // to parseconfig.exectime
        // String[] commaSeparatedValues = parseConfigResult.split(";");
        // long parseExecTime = Long.parseLong(commaSeparatedValues[6]);
        //
        // //
        // inferray,PARSE,/home/satin/datasets/dataset-under-test/dataset_100k.nt,rdfs++,1,4,
        // String inferOnlyConfigResult = runConfig.getReasoner() + ",INFER," +
        // runConfig.getDataset() + "," + runConfig.getFragment() + ","
        // + runConfig.getIterations() + "," + runConfig.getTimeoutInSeconds() +
        // ",";
        //
        // long triples =
        // Long.parseLong(configResult.substring(configResult.lastIndexOf(";")).replaceAll(";",
        // ""));
        // String _buf =
        // configResult.substring(0,configResult.lastIndexOf(";"));
        //
        // long parseAndInferExecTime =
        // Long.parseLong(_buf.substring(_buf.lastIndexOf(";")).replaceAll(";",
        // ""));
        //
        // inferOnlyConfigResult += (parseAndInferExecTime-parseExecTime) + ","
        // + triples;
        // try {
        // LOGGER.info("Writing infer only benchmark results to " +
        // runConfig.getBenchmarkoutputFile());
        // if (!configResult.equals("")) {
        // FileUtils.writeStringToFile(new
        // File(runConfig.getBenchmarkoutputFile()), "\n" +
        // inferOnlyConfigResult + ";" + perfResult.toCsv()
        // + ";" + "(extrapolated)", true);
        // }
        // } catch (IOException e) {
        // throw new RuntimeException(new
        // Error("Cannot write benchmark result to " +
        // runConfig.getBenchmarkoutputFile() + " because of "
        // + e.getMessage() + " for configuration " + runConfig.toString()));
        // }
        // }
    }

    /**
     * The benchmark outputs their results to
     * {@link BenchmarkRunner#getOutputFile()}. This method provides a
     * convenient way to fetch the line logged for a given benchmark using its
     * configuration. It first build the begining of the line in the output file
     * that consist in the benchmark configuration, and then it scans the file
     * in order to find the relevant line, and then
     * 
     * @param runConfig
     *            the benchmark configuration
     * @return the line in <code>runConfig.getOutputFile()</code> containing the
     *         result for the benchmark with configuration
     *         <code>runConfig</code>
     */
    public static String readBenchResultFrombenchmarkFile(final BenchRunConfiguration runConfig) {
        String separator = ",";
        StringBuffer configBuffer = new StringBuffer();
        configBuffer.append(runConfig.getReasoner()).append(separator);

        if (runConfig.getParseOnly().equals("true")) {
            configBuffer.append("PARSE").append(separator);
        } else {
            configBuffer.append("PARSE+INFER").append(separator);
        }
        configBuffer.append(runConfig.getDataset()).append(separator);
        configBuffer.append(runConfig.getFragment());

        // take result out of output file
        try {
            Scanner scanner = new Scanner(runConfig.getOutputFile());

            // now read the file line by line...
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith(configBuffer.toString())) {
                    scanner.close();
                    // separator for file benchmark outputfile is semicolumn.
                    line = line.replaceAll(",", ";");
                    String dataset = runConfig.getDataset();
                    String shortDatasetName = dataset.contains("/") ? dataset.substring(dataset.lastIndexOf("/"), dataset.length()) : dataset;
                    line = line.replaceAll(dataset, shortDatasetName);
                    return line;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            LOGGER.warn("Cannot write result for benchmark " + runConfig + " in " + runConfig.getOutputFile());
            // throw new RuntimeException("Cannot find benchmark result file "
            // + runConfig.getOutputFile());
        }

        LOGGER.warn("Cannot find benchmark result in file " + runConfig.getOutputFile() + "for the configuration " + configBuffer.toString());
        return "";
    }

    /**
     * The method calls the external benchmark using
     * {@link BenchmarkExternalRun}. The timeout for the following synchronous
     * <code>get()</code> call is set the the benchamrk timeout allowed plus 10
     * seconds (allowing it to initialize and properly terminate, just in case).
     * It handles timeouts occurences, as well as external program interruption,
     * usually due to the thread killing the external command when it encounters
     * an exception in the standard output.
     * 
     * @param runConfigParse
     *            the benchmark configuration to use.
     * @return the result of the benchmark
     */
    public static BenchmarkResult launchSingleBenchmark(BenchRunConfiguration runConfigParse) {

        ExecutorService service = Executors.newSingleThreadExecutor();
        try {
            BenchmarkExternalRun call = new BenchmarkExternalRun(runConfigParse, service);
            Future<BenchmarkResult> ft = service.submit(call);

            try {
                BenchmarkResult res = ft.get(runConfigParse.getTimeoutInSeconds() + 10, TimeUnit.SECONDS);
                fragDaemonBenchmark();
                return res;
            } catch (TimeoutException to) {
                ft.cancel(true);
                fragDaemonBenchmark();
                return createBlankResult();
            } catch (InterruptedException e) {
                LOGGER.warn("Benchmark " + runConfigParse + " was interrupted.");

                fragDaemonBenchmark();
                return createBlankResult();
            } catch (ExecutionException e) {
                LOGGER.error("Benchmark " + runConfigParse + " contains errors.");
                fragDaemonBenchmark();
                return createBlankResult();
            }
        } finally {
        	fragDaemonBenchmark();
            service.shutdown();
        }
    }

    /**
     * Create what is an empty {@link BenchmarkResult} for the program.
     * 
     * @return
     */
    public static BenchmarkResult createBlankResult() {
        return new BenchmarkResult("");
    }

    /**
     * Reports benchmark in errors (benchmarks that didn't terminates or which
     * did not timed out) to a log file on filesystem.
     * 
     * @param runConfig
     *            the benchmark configuration
     * @param error
     *            the error that occured and was fetched on the file system.
     */
    public static void reportBenchmarkInError(BenchRunConfiguration runConfig, String error) {
        try {
            FileUtils.writeStringToFile(new File(runConfig.getBenchmarkoutputErrorFile()), "\n" + "Benchmark with configuration " + runConfig
                    + " could not be ran because it encountered " + error + ". Command to reproduce the error : " + runConfig.getCmd(), true);
        } catch (IOException e) {
            throw new RuntimeException("Cannot find benchmark result file " + runConfig.getOutputFile());
        }
    }

    /**
     * When a benchmark fails, the external command launched stays a daemon in
     * the system and since it is not possible to get PID in Java external
     * launch whatever the framework, here is an external command to kill all
     * benchmark process when this method is called.
     */
    public static void fragDaemonBenchmark() {

        try {
            final DefaultExecutor executor = new DefaultExecutor();

            /**
             * Accepted return types are 0 (terminated successfully) or 143
             * (interrupted because of a timeout)
             */
            int[] acceptedReturnTypes = new int[2];
            acceptedReturnTypes[0] = 0;
            acceptedReturnTypes[1] = 1;
            executor.setExitValues(acceptedReturnTypes);
            executor.execute(CommandLine.parse(BenchmarkRunner.KILL_BENCHMARK_COMMAND));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
