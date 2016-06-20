#!/bin/bash

# T: timeout in seconds
# M: JVM max memory for each bench
# I: number of iterations to repeat for each bench
# E: location of ReasonersBenchamrked packaged jar file
# O: temporay output (where ReasonersBanchmarked wrote intermediate results)
# BRES: CSV file with all successful (timeout or not) run benchmarks)
# BERR: Error log file.
# DS: all for all available benchmark (filter in Benchmark#setBenchmarkTable) or test for a testing setting.
#Create jar for reasonersBenchmarked

results="/home/satin/inferray-slider-total.csv"

cd ../ReasonersBenchmarked
mvn clean install
cd ../Benchmark
#Create jar for benchmark
mvn clean install assembly:single
cd ./target
java -jar inferray-benchmark-0.0.1-SNAPSHOT-jar-with-dependencies.jar -T 900 -I 5 -M 30000 -E "/home/satin/inferray/ReasonersBenchmarked/target" -O /home/satin/benchmark-runs.txt -DS all -BRES $results -BERR /home/satin/benchmark-errors.log
