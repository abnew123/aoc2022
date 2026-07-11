# Performance Notes

[`FreshJvmBenchmark`](src/aoc2022/FreshJvmBenchmark.java) is the canonical answer-consistency and fresh-process benchmark runner. Its default mode compares the two solver entry points, records one separately reported cold child, then launches 10 more child JVM processes serially. Each child solves all 25 days once through `fullSolve`; processes are never benchmarked concurrently.

## Reproducing the run

The recorded environment was:

- 2024 MacBook Pro
- macOS 15.6, build 24G84 (reported by Java as Mac OS X, `aarch64`)
- 14 available processors
- OpenJDK 23.0.1
- source and bytecode target Java 16

From the repository root, with the ignored `data/day01.txt` through `data/day25.txt` files present:

```sh
rm -rf /tmp/aoc2022-classes
mkdir -p /tmp/aoc2022-classes
javac --release 16 -d /tmp/aoc2022-classes $(git ls-files '*.java')
java -cp /tmp/aoc2022-classes aoc2022.FreshJvmBenchmark --verify
java -cp /tmp/aoc2022-classes aoc2022.FreshJvmBenchmark
```

The input directory defaults to `data`. To use another location, add `-Daoc.data.dir=/absolute/path` before `-cp` on either Java command.

`--verify` runs all 50 individual `solve` calls, then runs 25 two-answer `fullSolve` calls and requires every pair to match. This proves entry-point equivalence and detects changes through the checksum, but the runner does not independently know the expected puzzle answers. The ordered `fullSolve` checksum is:

```text
5f79ad374b42c8a37382972ee3158f645c2260d01e08f33e59c12cfb9f60932b
```

That checksum is unchanged from the pristine known-good `a107970` pre-change snapshot, and all 50 captured before/after answer records were byte-identical. The Day 18 official sample (64/58), a negative-coordinate translation, and a greater-than-23 translation provide additional problem-level regression evidence.

## Metric definitions

- **wall**: parent time from launching the child JVM through child exit. It includes process launch, child work, protocol output, shutdown, and parent observation overhead.
- **main**: child time from the first statement in `main` until just before the final result record is formatted and printed.
- **solver**: the sum of the 25 intervals around each day's `fullSolve` call. Construction of the outer file-backed `Scanner` occurs before these intervals; any scanner construction performed inside a solver remains part of solver time.
- **startup**: parent time from child launch until the child's start marker is observed.
- **harness**: `main - solver`, covering outer file-`Scanner` construction, solver setup, validation of returned values, checksum work, and other runner work inside `main`.

The startup marker is emitted just after entry into child `main`, so `startup` overlaps a tiny early-`main` interval. Wall time also contains shutdown and output after the measured `main` interval. These fields therefore explain different portions of a fresh process but are not disjoint values that should be summed.

## Current fresh-JVM results

The separately reported cold run was:

| Run | Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
| --- | ---: | ---: | ---: | ---: | ---: |
| Cold | 296.779 | 253.532 | 223.092 | 24.813 | 30.439 |

The following 10 fresh JVM processes form the summary sample:

| Run | Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
| --- | ---: | ---: | ---: | ---: | ---: |
| 1 | 296.221 | 257.026 | 225.633 | 24.397 | 31.393 |
| 2 | 301.574 | 258.153 | 225.137 | 24.182 | 33.016 |
| 3 | 285.422 | 254.968 | 224.110 | 26.100 | 30.858 |
| 4 | 278.571 | 250.291 | 221.143 | 23.801 | 29.148 |
| 5 | 281.964 | 253.506 | 223.210 | 23.774 | 30.296 |
| 6 | 295.040 | 253.459 | 223.278 | 24.188 | 30.182 |
| 7 | 294.405 | 250.667 | 222.021 | 24.382 | 28.646 |
| 8 | 284.041 | 252.397 | 222.384 | 27.291 | 30.012 |
| 9 | 273.567 | 245.596 | 215.951 | 23.402 | 29.645 |
| 10 | 285.458 | 256.721 | 226.900 | 24.233 | 29.821 |

| Metric | Mean (ms) | Median (ms) | Sample standard deviation (ms) |
| --- | ---: | ---: | ---: |
| Wall | 287.626 | 285.440 | 8.841 |
| Main | 253.278 | 253.483 | 3.773 |
| Solver | 222.977 | 223.244 | 3.028 |
| Startup | 24.575 | 24.210 | 1.193 |
| Harness | 30.302 | 30.097 | 1.234 |

## Alternating baseline comparison

The final branch was also compared with pristine commit `a107970` using the same Java `ProcessBuilder` child protocol, inputs, JVM, answer checksum, and runner source in separately compiled pristine and candidate classpaths. The orchestrator ran a cold base child and then a cold candidate child. It then ran 10 counterbalanced pairs: odd pairs base then candidate, even pairs candidate then base. Every child ran serially; base and candidate were never benchmarked concurrently.

The raw paired measurements were:

| Pair | Order | Base wall (ms) | Candidate wall (ms) | Base solver (ms) | Candidate solver (ms) |
| ---: | :---: | ---: | ---: | ---: | ---: |
| 1 | B-C | 318.353 | 301.753 | 243.198 | 221.031 |
| 2 | C-B | 297.679 | 293.028 | 224.653 | 217.361 |
| 3 | B-C | 288.877 | 295.968 | 230.607 | 222.700 |
| 4 | C-B | 283.920 | 277.804 | 223.509 | 217.558 |
| 5 | B-C | 285.308 | 292.296 | 226.690 | 215.337 |
| 6 | C-B | 306.040 | 296.157 | 231.765 | 221.365 |
| 7 | B-C | 279.456 | 292.386 | 221.147 | 218.368 |
| 8 | C-B | 300.489 | 295.026 | 225.733 | 219.382 |
| 9 | B-C | 303.779 | 294.062 | 232.550 | 220.090 |
| 10 | C-B | 298.691 | 292.273 | 223.036 | 221.019 |

The preceding cold base/candidate runs were:

| Revision | Wall (ms) | Solver (ms) |
| --- | ---: | ---: |
| Pristine `a107970` | 294.579 | 229.116 |
| Current branch | 281.492 | 219.640 |

The 10-pair means were:

| Metric | Base mean (ms) | Candidate mean (ms) | Candidate - base (ms) | Relative change | Paired-delta standard deviation (ms) |
| --- | ---: | ---: | ---: | ---: | ---: |
| Wall | 296.259 | 293.075 | -3.184 | -1.07% | 9.205 |
| Main | 258.515 | 249.832 | -8.683 | -3.36% | 5.747 |
| Solver | 228.289 | 219.421 | -8.868 | -3.88% | 5.779 |
| Startup | 24.962 | 25.642 | +0.680 | — | — |
| Harness | 30.226 | 30.411 | +0.185 | — | — |

The paired solver reduction has an approximate 95% confidence interval of **[-13.00, -4.74] ms**, which excludes zero. The main and solver measurements show the useful improvement; the wall delta is small relative to its 9.205 ms paired standard deviation and is not statistically clear. The decision to keep the change is therefore based on the repeatable solver/main win, not the noisier wall measurement.

## Day 18: Boiling Boulders

Day 18 previously assumed nonnegative coordinates inside a fixed 25×25×25 array and repeatedly rescanned that volume. It now parses signed coordinates whose checked one-cell-padded volume is representable by its flat primitive arrays, stores occupancy in one such array, and performs one primitive-queue exterior flood fill. Coordinates whose padding, dimensions, or volume cannot be represented safely are rejected explicitly. `fullSolve` computes total and exterior surface area together from one parse.

The official sample remains 64/58, translated negative and greater-than-23 coordinate cases produce the same result, overflow or unrepresentable flat bounds fail explicitly, and the full 50-answer verification remains unchanged.

## Direct solver factory

The fresh-JVM and normal master harnesses now share a direct switch-based solver factory instead of formatting 25 class names and using `Class.forName`, constructor lookup, and reflective instantiation. Construction stays outside the solver timer, so this change targets harness work without moving parsing or solver logic across a timing boundary.

Under nonuniform interactive machine load, the exact final factory and reflective baseline first ran a cold child each and then 10 counterbalanced pairs of fresh child JVMs. All children returned the established checksum. The cold harness values (`main - solver`) were 37.930ms reflection and 30.390ms factory.

| Pair | Order | Reflection harness (ms) | Factory harness (ms) | Delta (ms) |
| ---: | :---: | ---: | ---: | ---: |
| 1 | B-C | 33.989 | 31.284 | -2.705 |
| 2 | C-B | 33.071 | 31.105 | -1.966 |
| 3 | B-C | 31.666 | 31.086 | -0.580 |
| 4 | C-B | 32.083 | 32.423 | +0.339 |
| 5 | B-C | 32.168 | 30.138 | -2.030 |
| 6 | C-B | 31.943 | 30.497 | -1.446 |
| 7 | B-C | 31.411 | 30.615 | -0.796 |
| 8 | C-B | 31.077 | 31.493 | +0.416 |
| 9 | B-C | 32.656 | 28.662 | -3.995 |
| 10 | C-B | 31.401 | 29.104 | -2.297 |

The paired harness means were **32.147ms reflection** and **30.641ms factory**, a 1.506ms reduction. The paired-delta sample standard deviation was 1.383ms and the approximate 95% confidence interval was **[-2.50ms, -0.52ms]**.

Separate standard cold-plus-10 parent runs recorded every phase:

| Metric | Reflection mean (ms) | Factory mean (ms) | Change (ms) |
| --- | ---: | ---: | ---: |
| Wall | 326.803 | 333.055 | +6.252 |
| Main | 281.042 | 286.189 | +5.147 |
| Solver | 247.687 | 254.866 | +7.179 |
| Startup | 29.735 | 29.791 | +0.057 |
| Harness | 33.355 | 31.323 | **-2.032** |

The full-run harness phase independently confirms the targeted reduction. Wall, main, and solver moved with unrelated interactive load and are not treated as evidence for or against this harness-only change. All 50 independent answers and all 25 combined solves remain unchanged.

## Historical warm measurements

The per-part table in the README is retained as a historical July warm 10-run snapshot using `DayTemplate.timer`. It is useful for the relative shape of individual solvers, but it excludes fresh JVM startup and is not directly comparable with the process-level results above.

The first invocation of a Java solver is often slower because the JVM is still loading classes, verifying bytecode, linking methods, compiling hot paths with the JIT, filling CPU caches, and sometimes paying one-time allocation or GC costs. These costs are part of the fresh-process results.

## Visual Examples

Bars are scaled to combined part 1 + part 2 time. Lower is better.

### Day 2: Rock Paper Scissors

The solver still scores the same rounds, but it no longer builds a list, splits each line, or looks up throws in a `Map`.

```text
Before  19.7 ms | ####################
After    3.2 ms | ###
```

### Day 8: Treetop Tree House

The solver still scans the tree grid, but the optimized version keeps the grid in primitive arrays and avoids repeated object-heavy lookups.

```text
Before   8.2 ms | ########
After    3.1 ms | ###
```

### Day 13: Distress Signal

The solver still compares packet ordering by the puzzle rules, but it parses and compares without building as much temporary nested structure.

```text
Before   9.6 ms | ##########
After    3.5 ms | ####
```
