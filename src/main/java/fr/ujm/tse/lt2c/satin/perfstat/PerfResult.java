package fr.ujm.tse.lt2c.satin.perfstat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Christophe Gravier, <christophe.gravier@univ-st-etienne.fr>
 * 
 *         When running a Benchmark, we want to store some processors registries
 *         information. This class is a POJO for this. The registries are read
 *         using the <a
 *         href="https://perf.wiki.kernel.org/index.php/Main_Page">linux perf
 *         tool</a>, more information on each registry value can be found in
 *         perf stat wiki. More info on perf events can also be found <a href=
 *         "http://web.eece.maine.edu/~vweaver/projects/perf_events/perf_event_open.html"
 *         >here</a>.
 * 
 */
public class PerfResult {

    /**
     * Loads from L3
     */
    private String LLCLoads;

    /**
     * Missed load from L3 (needed to go to memory)
     */
    private String LLCLoadMisses;

    /**
     * Percentage of cache hits
     */
    private String PercentageOfAllCacheHits;

    /**
     * <i>To be documented.</i>
     */
    private String LLCStores;

    /**
     * Cache accesses. Usually this indicates Last Level Cache accesses but this
     * may vary depending on your CPU.
     */
    private String cacheReferences;

    /**
     * Percentage of memory access that could not be served by any level of
     * cache.
     */
    private String PercentageOfCacheMisses;

    /**
     * Number of cycles.
     */
    private String cycles;

    /**
     * Number of instructions made
     */
    private String instructions;

    /**
     * Number of instruction per cycle. Hopefully
     * <code>this.instructions / this.cycles</code> but we parse it from perf
     * stat output and donot check if this is consistent.
     */
    private String instructionPerCycles;

    /**
     * Mean of number of stalled instructions per cycle (both frontend and
     * backend cycles)
     */
    private String stalledCyclesPerInstruction;

    /**
     * Number of prefetches for Level 1 Data Cache
     */
    private String L1DcachePrefetches;

    /**
     * Stalled cycle at frontend
     */
    private String stalledCyclesFrontend;

    /**
     * Stalled cycles at backend
     */
    private String stalledCyclesBackend;

    /**
     * Percentage of stalled cycle at frontend
     */
    private String percentageOfCycleFrontend;

    /**
     * Percentage of stalled cycle at backend
     */
    private String percentageOfCycleBackend;

    private String L1_dcache_loads;
    private String L1_dcache_load_misses;
    private String L1_icache_loads;
    private String L1_icache_load_misses;
    private String dTLB_loads;
    private String dTLB_load_misses;
    private String page_faults;

    private String percentageOfL1DataCacheMisses;

    private String percentageOfdTLBMisses;

    private String percentageOfInstructionCacheMisses;

    private String execTime;

    private String cache_misses;

    private String branch_misses;

    private String percentageOfBranchMisses;

    /**
     * Separator for the output CSV when calling {@link PerfResult#toCsv()}
     */
    private static final String separator = ";";

    public String getExecTime() {
        return execTime;
    }

    public void setExecTime(String execTime) {
        this.execTime = execTime;
    }

    /**
     * @return the lLCLoads
     */
    public String getLLCLoads() {
        return this.LLCLoads;
    }

    /**
     * @param lLCLoads
     *            the lLCLoads to set
     */
    public void setLLCLoads(final String lLCLoads) {
        this.LLCLoads = lLCLoads;
    }

    /**
     * @return the lLCLoadMisses
     */
    public String getLLCLoadMisses() {
        return this.LLCLoadMisses;
    }

    /**
     * @param lLCLoadMisses
     *            the lLCLoadMisses to set
     */
    public void setLLCLoadMisses(final String lLCLoadMisses) {
        this.LLCLoadMisses = lLCLoadMisses;
    }

    public String getPercentageOfL1DataCacheMisses() {
        return percentageOfL1DataCacheMisses;
    }

    public void setPercentageOfL1DataCacheMisses(String percentageOfL1DataCacheMisses) {
        this.percentageOfL1DataCacheMisses = percentageOfL1DataCacheMisses;
    }

    public String getPercentageOfInstructionCacheMisses() {
        return percentageOfInstructionCacheMisses;
    }

    public void setPercentageOfInstructionCacheMisses(String percentageOfInstructionCacheMisses) {
        this.percentageOfInstructionCacheMisses = percentageOfInstructionCacheMisses;
    }

    /**
     * @return the percentageOfAllCacheHits
     */
    public String getPercentageOfAllCacheHits() {
        return this.PercentageOfAllCacheHits;
    }

    /**
     * @param percentageOfAllCacheHits
     *            the percentageOfAllCacheHits to set
     */
    public void setPercentageOfAllCacheHits(final String percentageOfAllCacheHits) {
        this.PercentageOfAllCacheHits = percentageOfAllCacheHits;
    }

    /**
     * @return the lLCStores
     */
    public String getLLCStores() {
        return this.LLCStores;
    }

    /**
     * @param lLCStores
     *            the lLCStores to set
     */
    public void setLLCStores(final String lLCStores) {
        this.LLCStores = lLCStores;
    }

    /**
     * @return the cacheReferences
     */
    public String getCacheReferences() {
        return this.cacheReferences;
    }

    /**
     * @param cacheReferences
     *            the cacheReferences to set
     */
    public void setCacheReferences(final String cacheReferences) {
        this.cacheReferences = cacheReferences;
    }

    /**
     * @return the percentageOfCacheMisses
     */
    public String getPercentageOfCacheMisses() {
        return this.PercentageOfCacheMisses;
    }

    /**
     * @param percentageOfCacheMisses
     *            the percentageOfCacheMisses to set
     */
    public void setPercentageOfCacheMisses(final String percentageOfCacheMisses) {
        this.PercentageOfCacheMisses = percentageOfCacheMisses;
    }

    /**
     * @return the cycles
     */
    public String getCycles() {
        return this.cycles;
    }

    /**
     * @param cycles
     *            the cycles to set
     */
    public void setCycles(final String cycles) {
        this.cycles = cycles;
    }

    /**
     * @return the instructions
     */
    public String getInstructions() {
        return this.instructions;
    }

    /**
     * @param instructions
     *            the instructions to set
     */
    public void setInstructions(final String instructions) {
        this.instructions = instructions;
    }

    /**
     * @return the instructionPerCycles
     */
    public String getInstructionPerCycles() {
        return this.instructionPerCycles;
    }

    /**
     * @param instructionPerCycles
     *            the instructionPerCycles to set
     */
    public void setInstructionPerCycles(final String instructionPerCycles) {
        this.instructionPerCycles = instructionPerCycles;
    }

    /**
     * @return the stalledCyclesPerInstruction
     */
    public String getStalledCyclesPerInstruction() {
        return this.stalledCyclesPerInstruction;
    }

    /**
     * @param stalledCyclesPerInstruction
     *            the stalledCyclesPerInstruction to set
     */
    public void setStalledCyclesPerInstruction(final String stalledCyclesPerInstruction) {
        this.stalledCyclesPerInstruction = stalledCyclesPerInstruction;
    }

    /**
     * @return the l1DcachePrefetches
     */
    public String getL1DcachePrefetches() {
        return this.L1DcachePrefetches;
    }

    /**
     * @param l1DcachePrefetches
     *            the l1DcachePrefetches to set
     */
    public void setL1DcachePrefetches(final String l1DcachePrefetches) {
        this.L1DcachePrefetches = l1DcachePrefetches;
    }

    /**
     * @return the stalledCyclesFrontend
     */
    public String getStalledCyclesFrontend() {
        return this.stalledCyclesFrontend;
    }

    /**
     * @param stalledCyclesFrontend
     *            the stalledCyclesFrontend to set
     */
    public void setStalledCyclesFrontend(final String stalledCyclesFrontend) {
        this.stalledCyclesFrontend = stalledCyclesFrontend;
    }

    /**
     * @return the stalledCyclesBackend
     */
    public String getStalledCyclesBackend() {
        return this.stalledCyclesBackend;
    }

    /**
     * @param stalledCyclesBackend
     *            the stalledCyclesBackend to set
     */
    public void setStalledCyclesBackend(final String stalledCyclesBackend) {
        this.stalledCyclesBackend = stalledCyclesBackend;
    }

    /**
     * @return the percentageOfCycleFrontend
     */
    public String getPercentageOfCycleFrontend() {
        return this.percentageOfCycleFrontend;
    }

    /**
     * @param percentageOfCycleFrontend
     *            the percentageOfCycleFrontend to set
     */
    public void setPercentageOfCycleFrontend(final String percentageOfCycleFrontend) {
        this.percentageOfCycleFrontend = percentageOfCycleFrontend;
    }

    /**
     * @return the percentageOfCycleBacbkend
     */
    public String getPercentageOfCycleBackend() {
        return this.percentageOfCycleBackend;
    }

    /**
     * @param percentageOfCycleBacbkend
     *            the percentageOfCycleBacbkend to set
     */
    public void setPercentageOfCycleBacbkend(final String percentageOfCycleBacbkend) {
        this.percentageOfCycleBackend = percentageOfCycleBacbkend;
    }

    public String getL1_dcache_loads() {
        return L1_dcache_loads;
    }

    public void setL1_dcache_loads(String l1_dcache_loads) {
        L1_dcache_loads = l1_dcache_loads;
    }

    public String getL1_dcache_load_misses() {
        return L1_dcache_load_misses;
    }

    public void setL1_dcache_load_misses(String l1_dcache_load_misses) {
        L1_dcache_load_misses = l1_dcache_load_misses;
    }

    public String getdTLB_loads() {
        return dTLB_loads;
    }

    public void setdTLB_loads(String dTLB_loads) {
        this.dTLB_loads = dTLB_loads;
    }

    public String getdTLB_load_misses() {
        return dTLB_load_misses;
    }

    public void setdTLB_load_misses(String dTLB_load_misses) {
        this.dTLB_load_misses = dTLB_load_misses;
    }

    public String getPage_faults() {
        return page_faults;
    }

    public void setPage_faults(String page_faults) {
        this.page_faults = page_faults;
    }

    public static String getSeparator() {
        return separator;
    }

    public void setPercentageOfCycleBackend(String percentageOfCycleBackend) {
        this.percentageOfCycleBackend = percentageOfCycleBackend;
    }

    public String getPercentageOfL1CacheMisses() {
        return percentageOfL1DataCacheMisses;
    }

    public void setPercentageOfL1CacheMisses(String percentageOfL1CacheMisses) {
        this.percentageOfL1DataCacheMisses = percentageOfL1CacheMisses;
    }

    public String getPercentageOfdTLBMisses() {
        return percentageOfdTLBMisses;
    }

    public void setPercentageOfdTLBMisses(String percentageOfdTLBMisses) {
        this.percentageOfdTLBMisses = percentageOfdTLBMisses;
    }

    public String getL1_icache_loads() {
        return L1_icache_loads;
    }

    public void setL1_icache_loads(String l1_icache_loads) {
        L1_icache_loads = l1_icache_loads;
    }

    public String getL1_icache_load_misses() {
        return L1_icache_load_misses;
    }

    public void setL1_icache_load_misses(String l1_icache_load_misses) {
        L1_icache_load_misses = l1_icache_load_misses;
    }

    public String getCache_misses() {
        return cache_misses;
    }

    public void setCache_misses(String cache_misses) {
        this.cache_misses = cache_misses;
    }

    public String getBranch_misses() {
        return branch_misses;
    }

    public void setBranch_misses(String branch_misses) {
        this.branch_misses = branch_misses;
    }

    public String getPercentageOfBranchMisses() {
        return percentageOfBranchMisses;
    }

    public void setPercentageOfBranchMisses(String percentageOfBranchMisses) {
        this.percentageOfBranchMisses = percentageOfBranchMisses;
    }

    /**
     * @param perfoutput
     *            A String containing, among other things, a perf stat output.
     *            Using regexp, this constructor extract relevant values for
     *            building <code>this</code>.
     */
    public PerfResult(final String perfoutput) {
        this.LLCLoads = this.extractValueUsingregexp(perfoutput, "([0-9, ]*)LLC-loads");
        this.LLCLoadMisses = this.extractValueUsingregexp(perfoutput, "([0-9, ]*)LLC-load-misses");
        this.PercentageOfAllCacheHits = this.extractValueUsingregexp(perfoutput, "([0-9\\.,]+)% of all LL-cache");
        this.LLCStores = this.extractValueUsingregexp(perfoutput, "([0-9, ]*)LLC-stores");
        this.cacheReferences = this.extractValueUsingregexp(perfoutput, "([0-9, ]*)cache-references");
        this.PercentageOfCacheMisses = this.extractValueUsingregexp(perfoutput, "([0-9\\.,]+) % of all cache refs");
        this.cycles = this.extractValueUsingregexp(perfoutput, "([0-9, ]*)cycles");
        this.instructions = this.extractValueUsingregexp(perfoutput, "([0-9, ]*)instructions");
        this.instructionPerCycles = this.extractValueUsingregexp(perfoutput, "([0-9\\.,]+)  insns per cycle");
        this.stalledCyclesPerInstruction = this.extractValueUsingregexp(perfoutput, "([0-9\\.]+)  stalled cycles per insn");
        this.L1DcachePrefetches = this.extractValueUsingregexp(perfoutput, "([0-9, ]*)L1-dcache-prefetches");
        this.stalledCyclesFrontend = this.extractValueUsingregexp(perfoutput, "([0-9, ]*)stalled-cycles-frontend");
        this.stalledCyclesBackend = this.extractValueUsingregexp(perfoutput, "([0-9, ]*)stalled-cycles-backend");
        this.percentageOfCycleFrontend = this.extractValueUsingregexp(perfoutput, "([0-9\\.]+)% frontend cycles");
        this.percentageOfCycleBackend = this.extractValueUsingregexp(perfoutput, "([0-9\\.]+)% backend  cycles idle");

        this.L1_dcache_loads = this.extractValueUsingregexp(perfoutput, "([0-9, ]*)L1-dcache-loads");
        this.L1_dcache_load_misses = this.extractValueUsingregexp(perfoutput, "([0-9, ]*)L1-dcache-load-misses");
        this.L1_icache_loads = this.extractValueUsingregexp(perfoutput, "([0-9, ]*)L1-icache-loads");
        this.L1_icache_load_misses = this.extractValueUsingregexp(perfoutput, "([0-9, ]*)L1-icache-load-misses");
        this.percentageOfL1DataCacheMisses = this.extractValueUsingregexp(perfoutput, "([0-9\\.,]+)% of all L1-dcache hits");
        this.percentageOfInstructionCacheMisses = this.extractValueUsingregexp(perfoutput, "([0-9\\.,]+)% of all L1-icache hits");
        this.dTLB_loads = this.extractValueUsingregexp(perfoutput, "([0-9, ]*)dTLB-loads");
        this.dTLB_load_misses = this.extractValueUsingregexp(perfoutput, "([0-9, ]*)dTLB-load-misses");
        this.percentageOfdTLBMisses = this.extractValueUsingregexp(perfoutput, "([0-9\\.,]+)% of all dTLB cache hits");
        this.page_faults = this.extractValueUsingregexp(perfoutput, "([0-9, ]*)page-faults");
        this.execTime = this.extractValueUsingregexp(perfoutput, "([0-9,\\. ]*) seconds time elapsed");

        this.branch_misses = this.extractValueUsingregexp(perfoutput, "([0-9, ]*)branch-misses");
        this.cache_misses = this.extractValueUsingregexp(perfoutput, "([0-9, ]*)cache-misses");
        this.percentageOfBranchMisses = this.extractValueUsingregexp(perfoutput, "([0-9\\.,]+)% of all branches");

    }

    /**
     * Return a prepared string for CSV output using <code>this.separator</code>
     * as CSV delimiter.
     * 
     * @return a new line representing <code>this</code> in a CSV containing
     *         {@link PerfResult} instances.
     */
    public String toCsv() {

        return new String(this.execTime + separator + this.branch_misses + separator + this.percentageOfBranchMisses + separator + this.cache_misses
                + separator + this.PercentageOfCacheMisses + separator + this.LLCLoads + separator + this.LLCLoadMisses + separator
                + this.PercentageOfAllCacheHits + separator + this.LLCStores + separator + this.cacheReferences + separator + this.cycles + separator
                + this.instructions + separator + this.instructionPerCycles + separator + this.stalledCyclesPerInstruction + separator
                + this.L1DcachePrefetches + separator + this.stalledCyclesFrontend + separator + this.stalledCyclesBackend + separator
                + this.percentageOfCycleFrontend + separator + this.percentageOfCycleBackend + separator + this.L1_dcache_loads + separator
                + this.L1_dcache_load_misses + separator + this.percentageOfL1DataCacheMisses + separator + this.dTLB_loads + separator
                + this.dTLB_load_misses + separator + this.percentageOfdTLBMisses + separator + this.page_faults + separator + this.L1_icache_loads
                + separator + this.L1_icache_load_misses + separator + this.percentageOfInstructionCacheMisses);
    }

    /**
     * Get the CSV for columns for explaining all values in a CSV serialization
     * of <code>this</code>
     * 
     * @return the columns in the CSV file for serialization <code>this</code>
     *         using {@link PerfResult#toCsv()}
     */
    public static String getCsvColumnsNames() {

        StringBuffer columns = new StringBuffer();
        columns.append("Perf wallclock time (s)").append(separator);
        columns.append("Branch misses").append(separator);
        columns.append("% of branches misses").append(separator);
        columns.append("Cache misses").append(separator);
        columns.append("% of all cache misses on all cache refs ").append(separator);
        columns.append("LLC-loads").append(separator);
        columns.append("LLC-load-misses").append(separator);
        columns.append("% of all LL-cache hits").append(separator);
        columns.append("LLC-stores").append(separator);
        columns.append("cache-references").append(separator);
        columns.append("cycles").append(separator);
        columns.append("instructions").append(separator);
        columns.append("insns per cycle ").append(separator);
        columns.append("stalled cycles per insn").append(separator);
        columns.append("L1-dcache-prefetches").append(separator);
        columns.append("stalled-cycles-frontend").append(separator);
        columns.append("stalled-cycles-backend").append(separator);
        columns.append("Percentage of stalled-cycles-frontend").append(separator);
        columns.append("Percentage of stalled-cycles-backend").append(separator);
        columns.append("L1-dcache-loads").append(separator);
        columns.append("L1-dcache-load-misses").append(separator);
        columns.append("Percentage of all L1-dcache hits").append(separator);
        columns.append("dTLB-loads").append(separator);
        columns.append("dTLB-load-misses").append(separator);
        columns.append("Percentage of all dTLB hits").append(separator);
        columns.append("page-faults").append(separator);
        columns.append("L1-icache-loads").append(separator);
        columns.append("L1-icache-load-misses").append(separator);
        columns.append("Percentage of all L1-icache-misses").append(separator);

        return columns.toString();
    }

    /**
     * Extract a String from a parameter using a regexp
     * 
     * @param perfoutput
     *            the input string containg a perstat value given to scan using
     *            a regexp
     * @param pattern
     *            the regexp for the value to find
     * @return the first occurence of the substring of <code>perfoutput</code>
     *         matching <code>pattern</code>
     */
    private String extractValueUsingregexp(final String perfoutput, final String pattern) {
        final Pattern p = Pattern.compile(pattern);
        final Matcher m = p.matcher(perfoutput);
        while (m.find()) {
            try {
                final String buff = m.group(1).trim();
                return (buff == null || buff.equals("")) ? "0" : buff;
            } catch (final Exception e) {
                return "0";
            }
        }
        return "0";
    }
}
