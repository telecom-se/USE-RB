package fr.ujm.tse.lt2c.inferray.benchmark.perfstataprsing;

import org.junit.Assert;
import org.junit.Test;

import fr.ujm.tse.lt2c.satin.perfstat.PerfResult;

/**
 * @author Christophe Gravier, <christophe.gravier@univ-st-etienne.fr>
 * 
 */
public class ParseResultTest39 {

	// @Test
	// public void parseExampleTest() {
	//
	// final String test =
	// "Performance counter stats for 'java -jar -Xmx14336m /home/satin/inferray/ReasonersBenchmarked/target/ReasonersBenchmarked-0.0.1-SNAPSHOT-jar-with-dependencies.jar -D /home/satin/datasets/dataset_5M.nt -F rdfs-default -I 1 -P false -R inferray -T 3000 -O /home/satin/benchmark.txt'\n"
	// +
	// "98,992,874 branch-misses             #    6.95% of all branches         [17.16%]"
	// +
	// "3,454,573,582 LLC-loads                 #   58.035 M/sec                   [15.60%]\n"
	// +
	// "\t149,111,514 LLC-load-misses           #    4.32% of all LL-cache hits    [15.69%]\n"
	// +
	// "  491,524,262 LLC-stores                #    8.257 M/sec                   [15.43%]\n"
	// + "<not supported> LLC-store-misses        \n"
	// + "<not supported> LLC-prefetches          \n"
	// + "<not supported> LLC-prefetch-misses     \n"
	// +
	// "41,753,860,592 cache-references          #  701.442 M/sec                   [15.53%]\n"
	// +
	// "1,338,702,176 cache-misses              #    3.206 % of all cache refs     [15.74%]\n"
	// +
	// "212,296,752,511 cycles                    #    3.566 GHz                     [15.48%]\n"
	// +
	// "182,943,139,734 instructions              #    0.86  insns per cycle        \n"
	// +
	// "                                         #    0.22  stalled cycles per insn [23.26%]\n"
	// +
	// "37,248,320,984 branches                  #  625.752 M/sec                   [30.97%]\n"
	// +
	// "2,042,452,181 L1-dcache-prefetches      #   34.312 M/sec                   [30.84%]\n"
	// +
	// "           0 L1-dcache-prefetch-misses #    0.000 K/sec                   [30.84%]\n"
	// +
	// "59557.760079 cpu-clock                                                   \n"
	// +
	// "59525.708294 task-clock                #    1.837 CPUs utilized          \n"
	// +
	// "215,509,293,800 cpu-cycles                #    3.620 GHz                     [15.32%]\n"
	// +
	// "31,923,620,568 stalled-cycles-frontend   #   14.92% frontend cycles idle    [15.30%]\n"
	// +
	// "40,877,940,650 stalled-cycles-backend    #   19.11% backend  cycles idle    [15.34%]\n"
	// + "\n"
	// + " 32.410017117 seconds time elapsed;\n";
	//
	// final PerfResult perfResult = new PerfResult(test);
	// Assert.assertEquals("3,454,573,582", perfResult.getLLCLoads());
	// Assert.assertEquals("149,111,514", perfResult.getLLCLoadMisses());
	// Assert.assertEquals("4.32", perfResult.getPercentageOfAllCacheHits());
	// Assert.assertEquals("491,524,262", perfResult.getLLCStores());
	// Assert.assertEquals("41,753,860,592", perfResult.getCacheReferences());
	// Assert.assertEquals("3.206", perfResult.getPercentageOfCacheMisses());
	// Assert.assertEquals("212,296,752,511", perfResult.getCycles());
	// Assert.assertEquals("182,943,139,734", perfResult.getInstructions());
	// Assert.assertEquals("0.86", perfResult.getInstructionPerCycles());
	// Assert.assertEquals("0.22", perfResult.getStalledCyclesPerInstruction());
	// Assert.assertEquals("2,042,452,181", perfResult.getL1DcachePrefetches());
	// Assert.assertEquals("31,923,620,568",
	// perfResult.getStalledCyclesFrontend());
	// Assert.assertEquals("40,877,940,650",
	// perfResult.getStalledCyclesBackend());
	// Assert.assertEquals("14.92", perfResult.getPercentageOfCycleFrontend());
	// Assert.assertEquals("19.11", perfResult.getPercentageOfCycleBackend());
	// Assert.assertEquals("1,338,702,176", perfResult.getCache_misses());
	// Assert.assertEquals("98,992,874", perfResult.getBranch_misses());
	// Assert.assertEquals("6.95", perfResult.getPercentageOfBranchMisses());
	// }

	@Test
	public void perfWithReapeatTest() {

		final String test = " 236 573 753 branch-misses             #    3,50% of all branches          ( +-  1,99% ) [25,52%]"
				+ "251 747 216 LLC-loads                 #   24,173 M/sec                    ( +-  2,02% ) [25,59%]"
				+ "<not supported> LLC-load-misses         "
				+ "45 085 361 LLC-stores                #    4,329 M/sec                    ( +-  6,95% ) [25,64%]"
				+ "<not supported> LLC-store-misses        "
				+ "      118 253 780 LLC-prefetches            #   11,355 M/sec                    ( +-  6,97% ) [12,93%]"
				+ "  <not supported> LLC-prefetch-misses"
				+ "     381 208 150 cache-references          #   36,604 M/sec                    ( +-  0,96% ) [19,83%]"
				+ "      55 587 468 cache-misses              #   14,582 % of all cache refs      ( +-  3,81% ) [26,27%]"
				+ "   37 167 666 901 cycles                    #    3,569 GHz                      ( +-  2,35% ) [25,98%]"
				+ "   38 153 697 776 instructions              #    1,02  insns per cycle          ( +-  1,01% ) [32,34%]"
				+ "    6 759 097 757 branches                  #  649,012 M/sec                    ( +-  1,20% ) [32,02%]"
				+ "  <not supported> L1-dcache-prefetches    "
				+ "                0 L1-dcache-prefetch-misses #    0,000 K/sec                   [31,97%]"
				+ "     10414,712798 cpu-clock (msec)                                              ( +-  2,16% )"
				+ "     10414,444794 task-clock (msec)         #    2,078 CPUs utilized            ( +-  2,16% )"
				+ "   37 517 364 013 cpu-cycles                #    3,602 GHz                      ( +-  1,73% ) [31,87%]"
				+ "  <not supported> stalled-cycles-frontend "
				+ "  <not supported> stalled-cycles-backend  "
				+ "  12 922 766 863 L1-dcache-loads           # 1240,850 M/sec                    ( +-  1,81% ) [31,93%]"
				+ "   1 054 560 323 L1-dcache-load-misses     #    8,16% of all L1-dcache hits    ( +-  2,13% ) [25,59%]"
				+ "  12 911 337 103 dTLB-loads                # 1239,753 M/sec                    ( +-  2,02% ) [25,35%]"
				+ "      14 738 223 dTLB-load-misses          #    0,11% of all dTLB cache hits   ( +-  1,93% ) [25,40%]"
				+ " <not supported> node-loads              "
				+ " <not supported> node-load-misses        "
				+ "          56 978 page-faults               #    0,005 M/sec                    ( +-  6,46% )"
				+ " <not supported> L1-icache-loads         "
				+ "     163 506 343 L1-icache-load-misses     #    0,00% of all L1-icache hits    ( +-  1,55% ) [25,35%]"
				+ "      5,012619010 seconds time elapsed                                          ( +-  1,53% )";

		final PerfResult perfResult = new PerfResult(test);
		Assert.assertEquals("236 573 753", perfResult.getBranch_misses());
		Assert.assertEquals("251 747 216", perfResult.getLLCLoads());
		// Assert.assertEquals("16,535,082", perfResult.getLLCLoadMisses());
		// Assert.assertEquals("14,582",
		// perfResult.getPercentageOfAllCacheHits());
		// Assert.assertEquals("45 085 361", perfResult.getLLCStores());
		Assert.assertEquals("381 208 150", perfResult.getCacheReferences());
		Assert.assertEquals("14,582", perfResult.getPercentageOfCacheMisses());
		Assert.assertEquals("37 167 666 901", perfResult.getCycles());
		Assert.assertEquals("38 153 697 776", perfResult.getInstructions());
		Assert.assertEquals("1,02", perfResult.getInstructionPerCycles());
		// Assert.assertEquals("0.42",
		// perfResult.getStalledCyclesPerInstruction());
		// Assert.assertEquals("466,882,756",
		// perfResult.getL1DcachePrefetches());
		// Assert.assertEquals("3,661,384,744",
		// perfResult.getStalledCyclesFrontend());
		// Assert.assertEquals("5,015,615,370",
		// // perfResult.getStalledCyclesBackend());
		// Assert.assertEquals("17.56",
		// perfResult.getPercentageOfCycleFrontend());
		// Assert.assertEquals("24.05",
		// perfResult.getPercentageOfCycleBackend());
		Assert.assertEquals("12 911 337 103", perfResult.getdTLB_loads());
		Assert.assertEquals("14 738 223", perfResult.getdTLB_load_misses());
		Assert.assertEquals("1 054 560 323",
				perfResult.getL1_dcache_load_misses());
		Assert.assertEquals("12 922 766 863", perfResult.getL1_dcache_loads());
		Assert.assertEquals("5,012619010", perfResult.getExecTime());
		Assert.assertEquals("0,11", perfResult.getPercentageOfdTLBMisses());
	}
	//
	// @Test
	// public void anotherTest() {
	// final String test =
	// "Performance final counter stats for 'java -jar -Xmx14336m /home/satin/inferray/ReasonersBenchmarked/target/ReasonersBenchmarked-0.0.1-SNAPSHOT-jar-with-dependencies.jar -D /home/satin/datasets/dataset_500k.nt -F rdfs-default -I 1 -P false -R inferray -T 3000 -O /home/satin/benchmark.txt':"
	// + ""
	// +
	// "     1,003,405,609 LLC-loads                 #   84.837 M/sec                   [15.97%]"
	// +
	// "        33,473,819 LLC-load-misses           #    3.34% of all LL-cache hits    [16.05%]"
	// +
	// "        80,828,779 LLC-stores                #    6.834 M/sec                   [15.86%]"
	// + "   <not supported> LLC-store-misses        "
	// + "   <not supported> LLC-prefetches          "
	// + "   <not supported> LLC-prefetch-misses     "
	// +
	// "     8,617,570,466 cache-references          #  728.611 M/sec                   [16.22%]"
	// +
	// "       291,969,573 cache-misses              #    3.388 % of all cache refs     [16.26%]"
	// +
	// "    41,250,405,394 cycles                    #    3.488 GHz                     [16.02%]"
	// +
	// "    28,469,411,384 instructions              #    0.69  insns per cycle        "
	// +
	// "                                             #    0.35  stalled cycles per insn [23.74%]"
	// +
	// "     5,521,468,850 branches                  #  466.837 M/sec                   [31.49%]"
	// +
	// "       729,474,703 L1-dcache-prefetches      #   61.677 M/sec                   [31.54%]"
	// +
	// "                 0 L1-dcache-prefetch-misses #    0.000 K/sec                   [31.43%]"
	// +
	// "      11831.207655 cpu-clock                                                   "
	// +
	// "      11827.397861 task-clock                #    1.963 CPUs utilized          "
	// +
	// "    41,394,132,190 cpu-cycles                #    3.500 GHz                     [15.38%]"
	// +
	// "     6,604,091,322 stalled-cycles-frontend   #   15.98% frontend cycles idle    [15.48%]"
	// +
	// "     9,846,264,532 stalled-cycles-backend    #   23.83% backend  cycles idle    [15.63%]"
	// + "       6.023713919 seconds time elapsed";
	// final PerfResult perfResult = new PerfResult(test);
	// System.out.println(perfResult.getLLCLoads());
	// Assert.assertEquals("1,003,405,609", perfResult.getLLCLoads());
	// Assert.assertEquals("33,473,819", perfResult.getLLCLoadMisses());
	// Assert.assertEquals("6,604,091,322",
	// perfResult.getStalledCyclesFrontend());
	// Assert.assertEquals("23.83", perfResult.getPercentageOfCycleBackend());
	// }
}
