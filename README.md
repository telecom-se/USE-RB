# USE-RB


<h1 align="center">
	<img width="30%" src="https://github.com/telecom-se/USE-RB/blob/master/userb.png" alt="USE-RB">
</h1>


![tag v0.5](https://img.shields.io/badge/version-0.5-green.svg)


**USE-RB** stands for University Saint-Etienne Reasoner Benchmark. 

USE-RB aims at evaluating how a reasoner is working in harmony with the CPU and the memory. It integrates vanilla supported datasets, reasoners, and logic fragments, but also the facilities to plug any existing / to-be reasoners or datasets.

USE-RB not only monitor how a reasoner temporally perform (in number of cpu cycles for running a benchmark), it also encompass various hardware metrics in order to evaluate how the reasoner works with the hardware. 

These metrics falls into three categories :
1. **Instructions metrics ** : Instructions metrics includes the number of branch mispredictions (branch misses), total number of instructions, the
number of instructions per CPU cycle, and the number of stalled CPU cycle
per instruction.
2. **Memory metrics** includes the number of page faults, the number of
transactional lookaside buer loads and misses (total number and hit ratio), the number of stall CPU cycles when accessing any hierarchy of the memory.
3. **Cache metrics** include the miss rate on all levels of cache, per cache hierarchy level information on cache hit and misses, data and instruction L1 caches hit/miss ratios.

All these metrics are serialized in a result file, whose path is given as argument `BRES` (see [how to launch USE-RB](#launch)).

Here is an example on what can be drawn with the result file :
<h1 align="center">
	<img src="https://github.com/telecom-se/USE-RB/blob/master/example-figure.png" alt="example" />
</h1>

## Table of Contents

- [USE-RB](#)
	- [Prerequisites](#prerequisites)
    - [Installation](#installation)
	    - [Step 1 : Build ReasonersBenchmarked](#resonersbenchmarked)
	    - [Step 2 : datasets storage configuration](#dataset)
	    - [Step 3 : Set the benchmark table](#table)
	    - [Step 4 : Settings the Java VM memory footprint](#javavm)
	    - [Step 5 : Build USE-RB](#build)
	- [Launch USE-RB](#launch)
	- [Special citizen : OWLIM-SE](#owlimse)
	- [Licence](#licence)
	- [Cite this work](#cite)


<a name="prerequisites"></a>
## Prerequisites

You need a *linux* box of your favorite distribution with the following installed software :

1. [Java DK with version >= 7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html)
2. [Apache maven](https://maven.apache.org/)
3. [Perf software](https://perf.wiki.kernel.org/index.php/Main_Page)

<a name="installation"></a>
## Installation

This benchmark started with the idea to create a benchmark for a given reasoner but end up being a complete framework for benchmarking any reasoner, for given datasets and logic fragment. Being also a research implementation, several configurations are not yet fully automatic. In order to compile USE-RB you need to perform the following steps :

<a name="resonersbenchmarked"></a>
### Step 1 : Build ReasonersBenchmarked

[ReasonersBenchmark](https://github.com/telecom-se/ReasonersBenchmarked) is the sub-project that is launched as an external program for each benchmark to be ran by USE-RB. This is typically the benchmark of a given reasoner, for a given dataset, and a given logic fragment.

You first need to clone and compile this project, as the absolute path to the package jar file will be needed as a parameter [when running USE-RB](#launch).

This is a two-step process :

1. Clone it : `git clone https://github.com/telecom-se/ReasonersBenchmarked.git`
1. Few libraries are not online and are therefore packaged with `ReasonerBenchmark` project. We setup a local repository within the project. The repository is named `<id>in-project</id>` in the `pom.xml` file. You must modify the value of the location of this repository in the `pom.xml`file to where you actually clone the project. This step will be removed in next release, but until then, manual editing, sorry !
3. Create a uber JAR file :  `mvn clean install package`

You should now have a uber jar in the `target`folger which is named `ReasonersBenchmarked-0.0.1-SNAPSHOT.jar`.

<a name="dataset"></a>
### Step 2 : datasets storage configuration

USE-RB automatically download and extract datasets that are needed in the benchmark for which it has already been configured. If you want to browse them, the datasets are located at : [http://datasets-satin.telecom-st-etienne.fr/cgravier/reasoning/](http://datasets-satin.telecom-st-etienne.fr/cgravier/reasoning/)

Once these dataset are downloaded, they are extracted on your computer. You are to st the extract path in USE-RB before compiling. For this, you need to edit two variables in `src/main/resources/inferray-benchmark.properties` :

* `INFERRAY_DATASETS_LOCALSTORAGE` : the local path to folder where you want the datasets to be downloaded

* `INFERRAY_DATASETS_REMOTESTORAGE` : the URL of the online folder exposing the datasets. (By default `http://datasets-satin.telecom-st-etienne.fr/cgravier/reasoning/` but feel free to mirror for your convenience).

<a name="table"></a>
### Step 3 : Set the benchmark table

The benchmark table is the set of `(reasoner, dataset, logic fragment)` triplets corresponding to as many benchmarks to be ran.

As for now, the benchmark table had to be completed in the source code in `fr.ujm.tse.lt2c.satin.reasoner.benchmark.main.Benchmark.java` class. The method `setBenchmarkTable()` is the one responsible to actually fill the benchmark table.

We provide some example for your convenience : 
* [Example 1](https://github.com/telecom-se/USE-RB/blob/master/src/main/java/fr/ujm/tse/lt2c/satin/reasoner/benchmark/main/Benchmark.java#L229) : Benchmarking Slider reasoner on RHO-DF fragment and Yago ontology
* [Example 2](https://github.com/telecom-se/USE-RB/blob/master/src/main/java/fr/ujm/tse/lt2c/satin/reasoner/benchmark/main/Benchmark.java#L248) : RDFOx and Inferray reasoners on all vanilla BSBM datasets on RHO-DF, RDFS-default and RDFS-Full logic fragments
* [Example 3](https://github.com/telecom-se/USE-RB/blob/master/src/main/java/fr/ujm/tse/lt2c/satin/reasoner/benchmark/main/Benchmark.java#L396) : Inferray, Owlim-SE, Jena reasoners on seven LUBM files and wikipedia, yago and wordnet ontologies, for RDFSPlus logic fragment.

It should be straightforward to write your own benchmark table from them but if you have any issue with configuring your own benchmark, [feel free to ask](https://github.com/telecom-se/USE-RB/issues).

<a name="javavm"></a>
### Step 4 : Settings the Java VM memory footprint

The class [BenchmarkRunnerUtils.java](https://github.com/telecom-se/USE-RB/blob/master/src/main/java/fr/ujm/tse/lt2c/satin/reasoner/benchmark/runners/BenchmarkRunnerUtils.java) is responsible to launch the external benchmark program. It [specified the size of the Java VM](https://github.com/telecom-se/USE-RB/blob/master/src/main/java/fr/ujm/tse/lt2c/satin/reasoner/benchmark/runners/BenchmarkRunnerUtils.java#L64) that will be spawned. Until this parameter is handle in a property file, modify this value in the source code to your hardware configuration. 

For your information, when running the benchmark for all vanilla reasoners, datasets and logic fragment, we are used to set `Xms` and `Xmx` to `22G`.

<a name="build"></a>
### Step 5 : Build USE-RB

Once you have adapted the framework to your need via the aforementioned steps, you can compile and assemble it using the following : 

` mvn clean install assembly:single`

<a name="launch"></a>
## Launch the benchmark

Go to the `target` folder and run the assembled jar file as follows (example values, parameters discussed right after) :

```
java -jar inferray-benchmark-0.0.1-SNAPSHOT-jar-with-dependencies.jar \
-T 900 \
-I 5 \
-M 30000 \
-E "/home/ubuntu/Code/ReasonersBenchmarked/target" \
-O /home/ubuntu/benchmark-runs.txt \
-DS  all \
-BRES /home/ubuntu/benchmarkSmallTest.csv \
-BERR /home/ubuntu/benchmark-errors.log
```

You can get help from the CLI especially for the parameters values using `java -jar inferray-benchmark-0.0.1-SNAPSHOT-jar-with-dependencies.jar - H`. 

Here is the complete list of supported arguments :
```
-BERR,--benchmark-error-file <arg>    Output file for errors that occured in the benchmark.

-BRES,--benchmark-result-file <arg>   Output file for final results of the benchmark.
 
-DS,--datasets <arg>                  Datasets to benchmark. Possible values: "test" (BSM 100k, 200K, and 500k for testing purpose) and "all" (all supported datasets present). Default : "all"

-E,--benchmark-exe-dir <arg>          Folder where you built the
ReasonersBenchmarked JAR files.

-F,--force-download                   forces downloading all datasets for benchmarking, even if already present in the local filsystem at location specified at ${INFERRAY_DATASETS_REMOTESTORAGE} in inferray-benchmark.properties file. Activating this option may take some time depending on your Internet connection.

-H,--help                             prints program CLI usage.

-I,--iterations <arg>                 Number of iterations.

-M,--memory <arg>                     Memory in Megabyte for running a single benchmark. E.g. 2048 (unit is not required, only an integer is accepted). Ignored but will be the configuration endpoint for JVM memory in next update.

-O,--outputfile <arg>                 Output file for intermediate results of runs.

-T,--timeout <arg>                    Max number of seconds for a reasoner to perform the inference task on a given fragment and a given dataset under test. Benchmark processes exceeding this value will be killed and reported as timeout in the benchmark result file.
```
<a name="owlimse"></a>
## Special citizen : OWLIM-SE

[OWLIM-SE](https://confluence.ontotext.com/display/OWLIMv54/OWLIM-SE+Installation) (at the time of writing USE-RB, now known as [GraphDB](http://ontotext.com/products/graphdb/)) cannot be distributed. 

It is supported by USE-RB but deactivated by default. 
If you want to activate OWLIM-SE and make it available for your benchmarks, here are the required steps :
1. Put your OLWIM-SE copy into the `lib` folder of `ReasonersBenchmarked`
2. Uncomment all classes in the package `fr.ujm.tse.lt2c.satin.owlim`
3. Uncomment OWLIM dependancies that are commented in the `pom.xml` file
4. Rebuild all (start from step 1 in this guide).

<a name="licence"></a>
## License

Copyright © 2016 Christophe Gravier <christophe.gravier@univ-st-etienne.fr>
This work is free. You can redistribute it and/or modify it under the terms of the Do What The Fuck You Want To Public License, Version 2, as published by Sam Hocevar. See the [LICENCE.md](https://github.com/telecom-se/USE-RB/blob/master/LICENCE.md) file for more details.
![http://www.wtfpl.net](http://www.wtfpl.net/wp-content/uploads/2012/12/wtfpl-badge-4.png)

<a name="cite"></a>
## Cite this work

**How to cite this work :** 
* USE-RB paper has been submitted (Pending notification).
* until acceptance of the paper dedicate to USE-RB, one can get some side information from our VLDB 2016 paper on our reasoner [Inferray](https://github.com/jsubercaze/inferray) which contains to information on datasets, vanilla reasoners, and additional comments on how a reasoner work in harmony with the hardware : 
<hr />
*Julien Subercaze, Christophe Gravier, Jules Chevalier, Frederique Laforest. Inferray: fast in-memory RDF inference. PVLDB, Sep 2016, New Delhi, India. 9, 2016, PVLDB, pp. 468--480*
<hr />
