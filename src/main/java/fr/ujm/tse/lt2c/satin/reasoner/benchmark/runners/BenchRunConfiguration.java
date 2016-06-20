package fr.ujm.tse.lt2c.satin.reasoner.benchmark.runners;

import java.io.File;

/**
 * @author Christophe Gravier, <christophe.gravier@univ-st-etienne.fr>
 * 
 *         A POJO for benchmark settings.
 */
public class BenchRunConfiguration {

    /**
     * Absolute path to the data set under test on the filsystem.M%
     */
    private String dataset = null;

    /**
     * Get the external command run for this benchmark.
     * 
     * @return
     */
    public String getCmd() {
        return cmd;
    }

    /**
     * Set the external benchmark
     * 
     * @param cmd
     */
    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    /**
     * 
     * @return The error absolute path to the logging file on the file system
     */
    public String getBenchmarkoutputErrorFile() {
        return benchmarkoutputErrorFile;
    }

    /**
     * Set the error absolute path to the logging file on the file system
     * 
     * @param benchmarkoutputErrorFile
     */
    public void setBenchmarkoutputErrorFile(String benchmarkoutputErrorFile) {
        this.benchmarkoutputErrorFile = benchmarkoutputErrorFile;
    }

    /**
     * Fragment used
     */
    private String fragment;

    /**
     * Number of iteration for the benchmark
     */
    private int iterations;

    /**
     * when set to \"true\", it will only parse the file and not infer from it,
     * otherwise it will alos infer the model. It is a string for the arguiment
     * of the program is a String. It should be refactored to a boolean in the
     * future. TODO
     */
    private String parseOnly;

    /**
     * Reasoner used for the benchmark
     */
    private String reasoner;

    /**
     * Timeout value for the benchmark
     */
    private int timeoutInSeconds;

    /**
     * Benchmark result outputfile (the temporary file that will host the result
     * for this benchmark in CSV format)
     */
    private File outputFile;

    /**
     * Where the benchmark exectuable is located on the filesystem
     */
    private final String executableDirectory;

    /**
     * Memory in Mb allocated to the JVM that will run the benchmark
     */
    private int memory;

    /**
     * the external command to run for lanching the benchmark
     */
    private String cmd = null;

    /**
     * The output file for the benchmark
     */
    private String benchmarkoutputFile = null;

    /**
     * The error file for the benchmark
     */
    private String benchmarkoutputErrorFile = null;

    /**
     * 
     * @return the benchmark result output file
     */
    public String getBenchmarkoutputFile() {
        return benchmarkoutputFile;
    }

    /**
     * Set the benchmark result output file
     * 
     * @param benchmarkoutputFile
     */
    public void setBenchmarkoutputFile(String benchmarkoutputFile) {
        this.benchmarkoutputFile = benchmarkoutputFile;
    }

    /**
     * @return the dataset
     */
    public String getDataset() {
        return this.dataset;
    }

    /**
     * @param dataset
     *            the dataset to set
     */
    public void setDataset(final String dataset) {
        this.dataset = dataset;
    }

    /**
     * @return the fragment
     */
    public String getFragment() {
        return this.fragment;
    }

    /**
     * @param fragment
     *            the fragment to set
     */
    public void setFragment(final String fragment) {
        this.fragment = fragment;
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
     * @return the parseOnly
     */
    public String getParseOnly() {
        return this.parseOnly;
    }

    /**
     * @param parseOnly
     *            the parseOnly to set
     */
    public void setParseOnly(final String parseOnly) {
        this.parseOnly = parseOnly;
    }

    /**
     * @return the reasoner
     */
    public String getReasoner() {
        return this.reasoner;
    }

    /**
     * @param reasoner
     *            the reasoner to set
     */
    public void setReasoner(final String reasoner) {
        this.reasoner = reasoner;
    }

    /**
     * @return the timeoutInSeconds
     */
    public int getTimeoutInSeconds() {
        return this.timeoutInSeconds;
    }

    /**
     * @param timeoutInSeconds
     *            the timeoutInSeconds to set
     */
    public void setTimeoutInSeconds(final int timeoutInSeconds) {
        this.timeoutInSeconds = timeoutInSeconds;
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

    /**
     * @return the memory
     */
    public int getMemory() {
        return this.memory;
    }

    /**
     * @param memory
     *            the memory to set
     */
    public void setMemory(final int memory) {
        this.memory = memory;
    }

    /**
     * Expected constructor
     * 
     * @param dataset
     *            dataset to benchmark
     * @param fragment
     *            the fragment to use
     * @param iterations
     *            the number of time to repeat the benchmark (perf values will
     *            be the average of the runs)
     * @param parseOnly
     *            if \"true\", do not infer the model using the fragment
     * @param reasoner
     *            the reasoner to use for inference
     * @param timeoutInSeconds
     *            the timeout in seconds
     * @param outputFile
     *            the output file for the result of this benchmark
     * @param executableFile
     *            where the executable file for the benchmark is
     *            (ReasonersBenchmarked.jar)
     * @param memory
     *            the allocated memory in Mb for the JVM
     */
    public BenchRunConfiguration(final String dataset, final String fragment, final int iterations, final String parseOnly, final String reasoner,
            final int timeoutInSeconds, final File outputFile, final String executableFile, final int memory, final String benchResFile,
            final String benchErrorFile) {
        super();
        this.dataset = dataset;
        this.fragment = fragment;
        this.iterations = iterations;
        this.parseOnly = parseOnly;
        this.reasoner = reasoner;
        this.timeoutInSeconds = timeoutInSeconds;
        this.outputFile = outputFile;
        this.executableDirectory = executableFile;
        this.memory = memory;
        this.benchmarkoutputFile = benchResFile;
        this.benchmarkoutputErrorFile = benchErrorFile;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.dataset == null) ? 0 : this.dataset.hashCode());
        result = prime * result + ((this.fragment == null) ? 0 : this.fragment.hashCode());
        result = prime * result + this.iterations;
        result = prime * result + ((this.parseOnly == null) ? 0 : this.parseOnly.hashCode());
        result = prime * result + this.timeoutInSeconds;
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final BenchRunConfiguration other = (BenchRunConfiguration) obj;
        if (this.dataset == null) {
            if (other.dataset != null) {
                return false;
            }
        } else if (!this.dataset.equals(other.dataset)) {
            return false;
        }
        if (this.fragment == null) {
            if (other.fragment != null) {
                return false;
            }
        } else if (!this.fragment.equals(other.fragment)) {
            return false;
        }
        if (this.iterations != other.iterations) {
            return false;
        }
        if (this.parseOnly == null) {
            if (other.parseOnly != null) {
                return false;
            }
        } else if (!this.parseOnly.equals(other.parseOnly)) {
            return false;
        }
        if (this.timeoutInSeconds != other.timeoutInSeconds) {
            return false;
        }
        return true;
    }

    /**
     * @return the executableFile
     */
    public String getExecutableDirectory() {
        return this.executableDirectory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "BenchRunConfiguration [dataset=" + this.dataset + ", fragment=" + this.fragment + ", iterations=" + this.iterations + ", parseOnly="
                + this.parseOnly + ", reasoner=" + this.reasoner + ", timeoutInSeconds=" + this.timeoutInSeconds + ", outputFile=" + this.outputFile
                + ", executableFile=" + this.executableDirectory + "]";
    }

    /**
     * Set the command line to use for launching the benchmark
     * 
     * @param externalCmdLine
     */
    public void setCurentCmdLine(String externalCmdLine) {
        this.cmd = externalCmdLine;
    }
}
