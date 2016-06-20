package fr.ujm.tse.lt2c.inferray.benchmark.perfstataprsing;

import org.junit.Assert;
import org.junit.Test;

import fr.ujm.tse.lt2c.satin.perfstat.PerfResult;

/**
 * @author Christophe Gravier, <christophe.gravier@univ-st-etienne.fr>
 * 
 */
public class ParseResultTest {

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

		final String test = " 31 723 260      branch-misses             #    2,95% of all branches         [23,54%]"+
       " 33 097 060      LLC-loads                 #   15,691 M/sec                   [24,78%]"+
       "  5 612 377      LLC-load-misses           #   16,96% of all LL-cache hits    [26,53%]"+
      "   4 090 910      LLC-stores                #    1,939 M/sec                   [13,41%]"+
       "    574 364      LLC-store-misses          #    0,272 M/sec                   [12,62%]"+
      "  14 793 183      LLC-prefetches            #    7,013 M/sec                   [12,40%]"+
      "   1 843 774      LLC-prefetch-misses       #    0,874 M/sec                   [12,33%]"+
      "  57 849 053      cache-references          #   27,426 M/sec                   [17,75%]"+
     "    6 712 633      cache-misses              #   11,604 % of all cache refs     [22,47%]"+
     "6 500 299 813      cycles                    #    3,082 GHz                     [21,53%]"+
     "5 790 136 675      instructions              #    0,91  insns per cycle         [28,60%]"+
     "1 073 977 215      branches                  #  509,168 M/sec                   [27,16%]"+
     "            0      L1-dcache-prefetches      #    0,000 K/sec                  "+
     "            0      L1-dcache-prefetch-misses #    0,000 K/sec                   [25,58%]"+
     "  2109,097375      cpu-clock (msec)                                            "+
     "  2109,277313      task-clock (msec)         #    1,971 CPUs utilized          "+
    " 6 206 950 608      cpu-cycles                #    2,943 GHz                     [26,65%]"+
     "            0      stalled-cycles-frontend   #    0,00% frontend cycles idle   "+
      "           0      stalled-cycles-backend    #    0,00% backend  cycles idle   "+
     "1 702 279 604      L1-dcache-loads           #  807,044 M/sec                   [26,83%]"+
    "   111 889 010      L1-dcache-load-misses     #    6,57% of all L1-dcache hits   [21,84%]"+
   "  1 729 924 053      dTLB-loads                #  820,150 M/sec                   [23,08%]"+
  "       2 115 445      dTLB-load-misses          #    0,12% of all dTLB cache hits  [23,05%]"+
 "        9 179 909      node-loads                #    4,352 M/sec                   [21,67%]"+
"                 0      node-load-misses          #    0,000 K/sec                   [20,55%]"+
          "  18 480      page-faults               #    0,009 M/sec                  "+
         "        0      L1-icache-loads           #    0,000 K/sec                  "+
        "23 660 902      L1-icache-load-misses     #   11,218 M/sec                   [21,04%]"+
        "		"+
       "1,070027935 seconds time elapsed";

		final PerfResult perfResult = new PerfResult(test);
		Assert.assertEquals("31 723 260", perfResult.getBranch_misses());
		Assert.assertEquals("33 097 060", perfResult.getLLCLoads());
		// Assert.assertEquals("16,535,082", perfResult.getLLCLoadMisses());
		// Assert.assertEquals("14,582",
		// perfResult.getPercentageOfAllCacheHits());
		// Assert.assertEquals("45 085 361", perfResult.getLLCStores());
		Assert.assertEquals("57 849 053", perfResult.getCacheReferences());
		Assert.assertEquals("11,604", perfResult.getPercentageOfCacheMisses());
		Assert.assertEquals("6 500 299 813", perfResult.getCycles());
		Assert.assertEquals("5 790 136 675", perfResult.getInstructions());
		Assert.assertEquals("0,91", perfResult.getInstructionPerCycles());
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
		Assert.assertEquals("1 729 924 053", perfResult.getdTLB_loads());
		Assert.assertEquals("2 115 445", perfResult.getdTLB_load_misses());
		Assert.assertEquals("111 889 010",
				perfResult.getL1_dcache_load_misses());
		Assert.assertEquals("1 702 279 604", perfResult.getL1_dcache_loads());
		Assert.assertEquals("1,070027935", perfResult.getExecTime());
		Assert.assertEquals("0,12", perfResult.getPercentageOfdTLBMisses());
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
