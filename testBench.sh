perf stat -B  --repeat 1  -e branch-misses,LLC-loads,LLC-load-misses,LLC-stores,LLC-store-misses,LLC-prefetches,LLC-prefetch-misses,cache-references,cache-misses,cycles,instructions,branches,L1-dcache-prefetches,L1-dcache-prefetch-misses,cpu-clock,task-clock,cpu-cycles,stalled-cycles-frontend,stalled-cycles-backend,L1-dcache-loads,L1-dcache-load-misses,dTLB-loads,dTLB-load-misses,node-loads,node-load-misses,page-faults,L1-icache-loads,L1-icache-load-misses sh tmp.tmp