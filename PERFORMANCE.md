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
| Cold | 294.266 | 247.368 | 218.403 | 27.355 | 28.965 |

The following 10 fresh JVM processes form the summary sample:

| Run | Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
| --- | ---: | ---: | ---: | ---: | ---: |
| 1 | 300.397 | 256.094 | 227.994 | 24.668 | 28.100 |
| 2 | 292.539 | 248.219 | 219.634 | 24.851 | 28.585 |
| 3 | 278.118 | 248.805 | 220.667 | 24.969 | 28.138 |
| 4 | 296.520 | 251.521 | 223.604 | 25.260 | 27.916 |
| 5 | 281.002 | 251.631 | 223.590 | 24.989 | 28.042 |
| 6 | 278.512 | 247.575 | 219.177 | 26.493 | 28.398 |
| 7 | 307.145 | 260.380 | 229.579 | 27.617 | 30.802 |
| 8 | 310.837 | 276.587 | 247.112 | 30.024 | 29.475 |
| 9 | 306.041 | 259.169 | 227.850 | 27.485 | 31.319 |
| 10 | 286.740 | 254.069 | 224.811 | 27.965 | 29.259 |

| Metric | Mean (ms) | Median (ms) | Sample standard deviation (ms) |
| --- | ---: | ---: | ---: |
| Wall | 293.785 | 294.530 | 12.311 |
| Main | 255.405 | 252.850 | 8.665 |
| Solver | 226.402 | 224.208 | 8.116 |
| Startup | 26.432 | 25.877 | 1.794 |
| Harness | 29.003 | 28.492 | 1.206 |

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

## Day 17 shared simulation and checkpointed cycle jump

Day 17's `fullSolve` previously parsed the jet pattern twice and ran two independent cycle-detecting simulations. It now simulates once, records the exact tower height at rock 2,022, and continues to one trillion rocks. When a repeated normalized state is found before the checkpoint, the solver jumps by whole cycles without passing 2,022, records the translated checkpoint height, then reuses the same cycle toward the final goal. Individual `solve` calls retain the same generic single-goal path.

An isolated runner constructed the solver and file `Scanner` before timing the exact `fullSolve` call, verified both answers, and ran one excluded cold JVM per variant followed by 10 counterbalanced pairs of separate JVMs. Odd pairs ran the `19b15c9` separate-simulation baseline then candidate (`B-C`); even pairs reversed the order (`C-B`).

| Pair | Order | Separate simulations (ms) | Shared simulation (ms) | Delta (ms) |
| ---: | :---: | ---: | ---: | ---: |
| 1 | B-C | 12.714 | 9.718 | -2.996 |
| 2 | C-B | 12.547 | 9.279 | -3.268 |
| 3 | B-C | 12.512 | 9.316 | -3.197 |
| 4 | C-B | 12.655 | 9.289 | -3.367 |
| 5 | B-C | 12.820 | 9.259 | -3.561 |
| 6 | C-B | 13.010 | 9.298 | -3.712 |
| 7 | B-C | 12.724 | 9.238 | -3.486 |
| 8 | C-B | 12.833 | 9.379 | -3.454 |
| 9 | B-C | 12.747 | 9.258 | -3.489 |
| 10 | C-B | 13.022 | 10.780 | -2.242 |

The excluded cold values were 12.752 ms separate and 9.682 ms shared. The measured means were **12.758 ms separate** and **9.481 ms shared**, a **3.277 ms (25.7%) reduction**. The paired-delta sample standard deviation was 0.415 ms and the t(9) 95% confidence interval was **[-3.57 ms, -2.98 ms]**.

The exact final classpaths also ran through the authoritative whole-suite child. Its counterbalanced 10-pair solver means were 220.458 ms baseline and 222.481 ms candidate; the +2.023 ms paired delta had a wide 95% confidence interval of [-0.31 ms, +4.36 ms]. The standard phase runs had these excluded cold processes:

| Variant | Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
| --- | ---: | ---: | ---: | ---: | ---: |
| Baseline | 279.909 | 246.534 | 217.914 | 28.735 | 28.620 |
| Candidate | 283.166 | 249.346 | 220.618 | 29.062 | 28.728 |

Their 10-process means were:

| Metric | Baseline mean (ms) | Candidate mean (ms) | Change (ms) |
| --- | ---: | ---: | ---: |
| Wall | 293.423 | 288.281 | -5.142 |
| Main | 251.786 | 252.786 | +1.000 |
| Solver | 222.546 | 223.893 | +1.347 |
| Startup | 28.088 | 25.470 | -2.618 |
| Harness | 29.241 | 28.893 | -0.348 |

Interactive machine load was explicitly nonuniform, so the aggregate movements across 24 unrelated days are retained transparently but are not used to judge the change. The accepted evidence is the isolated paired Day 17 interval. Verification retained all 50 independent answers, all 25 combined-solve pairs, and the established checksum. The official sample returned `3068 / 1514285714288`; 500 deterministic jet patterns, checkpoints, and small final goals also made the shared run match two independent single-goal simulations exactly.

## Day 9 shared rope simulation and unbounded coordinates

Day 9's `fullSolve` previously parsed every motion twice, simulated separate 2-knot and 10-knot ropes, and stored visits in fixed 1000×1000 grids offset by 500. Knot 1 in the 10-knot rope follows exactly the same trajectory as the 2-knot tail, so one simulation now counts knot 1 and knot 9 together. Signed `long` coordinates and a primitive open-addressed pair set remove the fixed coordinate bound; malformed and numerically unrepresentable motions fail explicitly. The SplitMix64 finalizer used for the pair hash is credited beside the implementation.

An isolated runner constructed the solver and file `Scanner` before timing the exact `fullSolve` call, checked both answers, and ran one excluded cold JVM per variant followed by 10 counterbalanced pairs of separate JVMs. Odd pairs ran the `3b7db1e` two-simulation baseline then candidate (`B-C`); even pairs reversed the order (`C-B`).

| Pair | Order | Baseline (ms) | Candidate (ms) | Delta (ms) |
| ---: | :---: | ---: | ---: | ---: |
| 1 | B-C | 13.728 | 10.225 | -3.503 |
| 2 | C-B | 13.258 | 10.220 | -3.038 |
| 3 | B-C | 13.501 | 10.752 | -2.749 |
| 4 | C-B | 13.339 | 10.310 | -3.029 |
| 5 | B-C | 13.422 | 10.253 | -3.169 |
| 6 | C-B | 13.269 | 10.152 | -3.117 |
| 7 | B-C | 13.306 | 9.961 | -3.345 |
| 8 | C-B | 13.411 | 9.908 | -3.503 |
| 9 | B-C | 13.067 | 9.840 | -3.227 |
| 10 | C-B | 13.356 | 10.229 | -3.127 |

The excluded cold values were 13.008 ms baseline and 9.975 ms candidate. The measured means were **13.366 ms baseline** and **10.185 ms candidate**, a **3.181 ms (23.8%) reduction**. The paired-delta sample standard deviation was 0.229 ms and the t(9) 95% confidence interval was **[-3.34 ms, -3.02 ms]**.

The authoritative whole-suite child comparison was flat under interactive load: its counterbalanced 10-pair solver means were 222.796 ms baseline and 222.603 ms candidate, a -0.193 ms delta with a 95% confidence interval of [-4.90 ms, +4.51 ms]. Separate standard phase runs had these excluded cold processes:

| Variant | Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
| --- | ---: | ---: | ---: | ---: | ---: |
| Baseline | 299.171 | 251.189 | 222.315 | 27.839 | 28.874 |
| Candidate | 294.266 | 247.368 | 218.403 | 27.355 | 28.965 |

Their 10-process means were:

| Metric | Baseline mean (ms) | Candidate mean (ms) | Change (ms) |
| --- | ---: | ---: | ---: |
| Wall | 288.487 | 293.785 | +5.298 |
| Main | 249.441 | 255.405 | +5.964 |
| Solver | 220.639 | 226.402 | +5.763 |
| Startup | 26.913 | 26.432 | -0.480 |
| Harness | 28.802 | 29.003 | +0.201 |

The accepted evidence is the isolated paired Day 9 interval; the aggregate phase run contains visible outliers and is not interpreted as a code effect. Verification retained all 50 answers, all 25 combined solves, and the established checksum. Both official examples (`13 / 1` and `88 / 36`), 1,000 deterministic randomized motion lists against a reference `HashSet` simulator, whitespace and malformed-input cases, and a `R 5000` regression (`5000 / 4992`) all passed.

## Day 6 single-pass marker detection

Day 6 previously built a new boxed `HashSet<Character>` for every candidate window, rescanned each window, and repeated the input and marker scan for the combined solve. It now tracks each UTF-16 character's last-seen position in a primitive array, maintains the longest distinct suffix, and records the first suffix lengths reaching 4 and 14 in one pass. This retains the previous arbitrary Java-character domain and the previous zero result when no marker exists.

An isolated runner constructed the solver and file `Scanner` before timing the exact `fullSolve` call, checked both answers, and ran one excluded cold JVM per variant followed by 10 counterbalanced pairs of separate JVMs. Odd pairs ran the `992e334` boxed-window baseline then candidate (`B-C`); even pairs reversed the order (`C-B`).

| Pair | Order | Boxed windows (ms) | Last-seen scan (ms) | Delta (ms) |
| ---: | :---: | ---: | ---: | ---: |
| 1 | B-C | 8.525 | 3.941 | -4.584 |
| 2 | C-B | 8.501 | 3.797 | -4.705 |
| 3 | B-C | 8.838 | 3.811 | -5.027 |
| 4 | C-B | 9.033 | 3.706 | -5.327 |
| 5 | B-C | 9.559 | 3.957 | -5.602 |
| 6 | C-B | 8.743 | 3.908 | -4.834 |
| 7 | B-C | 8.553 | 3.740 | -4.813 |
| 8 | C-B | 8.643 | 3.684 | -4.959 |
| 9 | B-C | 8.894 | 3.759 | -5.135 |
| 10 | C-B | 8.262 | 4.162 | -4.100 |

Table deltas and summary statistics use the unrounded nanosecond records. The excluded cold values were 8.431 ms baseline and 3.973 ms candidate. The measured means were **8.755 ms baseline** and **3.846 ms candidate**, a **4.909 ms (56.1%) reduction**. The paired-delta sample standard deviation was 0.413 ms and the t(9) 95% confidence interval was **[-5.204 ms, -4.613 ms]**.

The authoritative whole-suite child comparison had a lower but noisy candidate point estimate: its counterbalanced 10-pair solver means were 235.746 ms baseline and 229.776 ms candidate, a -5.970 ms delta with a 95% confidence interval of [-15.780 ms, +3.840 ms]. Separate standard phase runs had these excluded cold processes:

| Variant | Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
| --- | ---: | ---: | ---: | ---: | ---: |
| Baseline | 307.578 | 261.151 | 229.459 | 31.600 | 31.693 |
| Candidate | 296.407 | 262.935 | 229.327 | 29.248 | 33.608 |

Their 10-process means were:

| Metric | Baseline mean (ms) | Candidate mean (ms) | Change (ms) |
| --- | ---: | ---: | ---: |
| Wall | 306.179 | 298.929 | -7.250 |
| Main | 264.011 | 262.836 | -1.175 |
| Solver | 231.986 | 230.324 | -1.661 |
| Startup | 28.149 | 29.462 | +1.313 |
| Harness | 32.025 | 32.512 | +0.486 |

The accepted evidence is the isolated paired Day 6 interval; the whole-suite interval is explicitly inconclusive under the user's nonuniform interactive load, and the complete phase split is retained transparently. Verification retained all 50 independent answers, all 25 combined solves, and checksum `5f79ad374b42c8a37382972ee3158f645c2260d01e08f33e59c12cfb9f60932b`. All five official examples, absent-marker and exact-length edge cases, and 1,000 deterministic randomized streams containing ASCII and non-ASCII Java characters matched the exact pre-change implementation with and without final newlines.

## Day 8 shared tree-grid analysis

Day 8's default combined solve previously materialized the input, created two more Scanners, parsed the grid twice, and tested tree visibility in both parts even though part 2 only returns the scenic score. It now parses once and accumulates both answers in one grid traversal. Independent part 1 checks visibility only, while independent part 2 computes scenic scores only.

An isolated runner constructed the solver and file `Scanner` before timing the exact `fullSolve` call, checked both answers, and ran one excluded cold JVM per variant followed by 10 counterbalanced pairs of separate JVMs. Odd pairs ran the `560b9b8` duplicate-analysis baseline then candidate (`B-C`); even pairs reversed the order (`C-B`).

| Pair | Order | Duplicate analysis (ms) | Shared analysis (ms) | Delta (ms) |
| ---: | :---: | ---: | ---: | ---: |
| 1 | B-C | 6.485 | 4.078 | -2.408 |
| 2 | C-B | 6.474 | 4.049 | -2.425 |
| 3 | B-C | 7.488 | 4.406 | -3.082 |
| 4 | C-B | 6.074 | 3.987 | -2.087 |
| 5 | B-C | 6.426 | 4.297 | -2.129 |
| 6 | C-B | 6.243 | 3.809 | -2.434 |
| 7 | B-C | 6.384 | 4.298 | -2.086 |
| 8 | C-B | 5.975 | 3.905 | -2.070 |
| 9 | B-C | 5.865 | 3.829 | -2.036 |
| 10 | C-B | 6.006 | 4.043 | -1.963 |

Table deltas and summary statistics use the unrounded nanosecond records. The excluded cold values were 6.427 ms baseline and 4.008 ms candidate. The measured means were **6.342 ms baseline** and **4.070 ms candidate**, a **2.272 ms (35.8%) reduction**. The paired-delta sample standard deviation was 0.334 ms and the t(9) 95% confidence interval was **[-2.511 ms, -2.033 ms]**.

The authoritative whole-suite child comparison was noisy: its counterbalanced 10-pair solver means were 230.254 ms baseline and 233.104 ms candidate, a +2.850 ms delta with a 95% confidence interval of [-2.619 ms, +8.318 ms]. Separate standard phase runs had these excluded cold processes:

| Variant | Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
| --- | ---: | ---: | ---: | ---: | ---: |
| Baseline | 334.833 | 281.474 | 251.348 | 35.716 | 30.127 |
| Candidate | 300.537 | 253.855 | 223.206 | 28.696 | 30.649 |

Their 10-process means were:

| Metric | Baseline mean (ms) | Candidate mean (ms) | Change (ms) |
| --- | ---: | ---: | ---: |
| Wall | 312.576 | 305.008 | -7.568 |
| Main | 271.479 | 265.006 | -6.473 |
| Solver | 241.518 | 235.691 | -5.828 |
| Startup | 27.277 | 26.437 | -0.840 |
| Harness | 29.961 | 29.315 | -0.646 |

The accepted evidence is the isolated paired Day 8 interval; the whole-suite paired interval is explicitly inconclusive under nonuniform interactive load, while the standard phase table is retained transparently. Verification retained all 50 independent answers, all 25 combined solves, and the established checksum. The official sample returned `21 / 8`, and 1,000 deterministic rectangular grids of varied sizes matched the exact pre-change implementation with separate/combined agreement and optional final newlines.

## Day 23 constant-time expansion detection

Day 23 previously rescanned every elf's row and column after every moving round solely to determine whether the padded stamped grid needed expansion. The simulation now checks only successfully moved destinations against the same two-cell boundary invariant and performs the existing full rebuild only when one crosses it. A cardinal move from a margin-two starting cell remains safely in bounds until that end-of-round rebuild. The bounds comparisons also use `long` intermediates consistently to avoid integer wraparound.

The combined solve now records a first no-movement round encountered during its mandatory first 10 rounds, while still completing all 10 rounds for part 1. This makes combined part 2 agree with the independent solver on already-stable and early-stabilizing maps; the personal input's first stopped round remains 1,016.

An isolated runner constructed the solver and file `Scanner` before timing the exact `fullSolve` call, checked both answers, and ran one excluded cold JVM per variant followed by 10 counterbalanced pairs of separate JVMs. Odd pairs ran the `c42622c` full-boundary-scan baseline then candidate (`B-C`); even pairs reversed the order (`C-B`).

| Pair | Order | Full boundary scan (ms) | Moved-cell check (ms) | Delta (ms) |
| ---: | :---: | ---: | ---: | ---: |
| 1 | B-C | 41.082 | 35.243 | -5.840 |
| 2 | C-B | 45.977 | 34.817 | -11.160 |
| 3 | B-C | 43.303 | 39.504 | -3.799 |
| 4 | C-B | 43.710 | 35.047 | -8.663 |
| 5 | B-C | 46.313 | 39.782 | -6.531 |
| 6 | C-B | 46.604 | 40.295 | -6.309 |
| 7 | B-C | 49.841 | 36.949 | -12.892 |
| 8 | C-B | 44.644 | 39.833 | -4.811 |
| 9 | B-C | 49.052 | 35.025 | -14.027 |
| 10 | C-B | 48.464 | 40.502 | -7.962 |

Table deltas and summary statistics use the unrounded nanosecond records. The excluded cold values were 51.085 ms baseline and 35.086 ms candidate. The measured means were **45.899 ms baseline** and **37.700 ms candidate**, an **8.199 ms (17.9%) reduction**. The paired-delta sample standard deviation was 3.461 ms and the t(9) 95% confidence interval was **[-10.675 ms, -5.724 ms]**.

The authoritative whole-suite counterbalanced comparison also showed a statistically clear solver reduction: its 10-pair means were 233.936 ms baseline and 226.219 ms candidate, a -7.717 ms delta with a 95% confidence interval of **[-13.184 ms, -2.251 ms]**. Separate standard phase runs had these excluded cold processes:

| Variant | Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
| --- | ---: | ---: | ---: | ---: | ---: |
| Baseline | 295.762 | 259.918 | 230.067 | 30.120 | 29.851 |
| Candidate | 312.885 | 261.649 | 230.584 | 36.221 | 31.065 |

Their 10-process means were:

| Metric | Baseline mean (ms) | Candidate mean (ms) | Change (ms) |
| --- | ---: | ---: | ---: |
| Wall | 308.512 | 309.805 | +1.293 |
| Main | 271.709 | 269.979 | -1.730 |
| Solver | 241.885 | 239.033 | -2.853 |
| Startup | 27.015 | 27.561 | +0.546 |
| Harness | 29.824 | 30.947 | +1.123 |

All 50 independent answers and 25 combined solves retain checksum `5f79ad374b42c8a37382972ee3158f645c2260d01e08f33e59c12cfb9f60932b`. The official sample returned `110 / 20`; empty and single-elf maps now return `0 / 1` consistently through separate and combined entry points; and 300 deterministic varied rectangular maps matched the exact pre-change independent answers.

## Day 20 single parse and exact treap storage

Day 20's default combined solve previously materialized the input, created two additional Scanners, parsed 5,000 numbers twice, and repeatedly grew six parallel treap arrays from capacity 16. It now parses once, allocates exact node arrays for each independent mix, and precomputes every mover's remainder modulo `n - 1` before the rounds.

The prompt gives no numeric bound, so values and coordinate sums now use `BigInteger` rather than retaining the old `long` overflow restriction. Movement still depends only on the small modular remainder, preserving the primitive treap hot path. Arbitrary whitespace is accepted, a missing or ambiguous duplicate zero is rejected explicitly, and a single zero value returns zero without dividing by `n - 1`.

An isolated runner constructed the solver and file `Scanner` before timing the exact `fullSolve` call, checked both answers, and ran one excluded cold JVM per variant followed by 10 counterbalanced pairs of separate JVMs. Odd pairs ran the `bb03bc4` duplicate-parse/growing-array baseline then candidate (`B-C`); even pairs reversed the order (`C-B`).

| Pair | Order | Duplicate/growing (ms) | Parse-once/exact (ms) | Delta (ms) |
| ---: | :---: | ---: | ---: | ---: |
| 1 | B-C | 52.488 | 48.801 | -3.687 |
| 2 | C-B | 51.868 | 48.598 | -3.270 |
| 3 | B-C | 51.420 | 48.626 | -2.794 |
| 4 | C-B | 53.527 | 48.331 | -5.196 |
| 5 | B-C | 53.486 | 48.580 | -4.906 |
| 6 | C-B | 52.970 | 48.671 | -4.299 |
| 7 | B-C | 52.656 | 48.105 | -4.551 |
| 8 | C-B | 52.066 | 48.741 | -3.325 |
| 9 | B-C | 52.320 | 49.367 | -2.953 |
| 10 | C-B | 52.408 | 49.057 | -3.351 |

Table deltas and summary statistics use the unrounded nanosecond records. The excluded cold values were 52.044 ms baseline and 47.937 ms candidate. The measured means were **52.521 ms baseline** and **48.688 ms candidate**, a **3.833 ms (7.3%) reduction**. The paired-delta sample standard deviation was 0.845 ms and the t(9) 95% confidence interval was **[-4.437 ms, -3.229 ms]**.

The authoritative whole-suite child comparison was directionally consistent but noisy: its counterbalanced 10-pair solver means were 228.037 ms baseline and 225.319 ms candidate, a -2.718 ms delta with a 95% confidence interval of [-5.881 ms, +0.444 ms]. Separate standard phase runs had these excluded cold processes:

| Variant | Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
| --- | ---: | ---: | ---: | ---: | ---: |
| Baseline | 290.477 | 252.817 | 221.461 | 32.950 | 31.356 |
| Candidate | 310.031 | 272.151 | 240.068 | 30.475 | 32.083 |

Their 10-process means were:

| Metric | Baseline mean (ms) | Candidate mean (ms) | Change (ms) |
| --- | ---: | ---: | ---: |
| Wall | 301.287 | 304.201 | +2.914 |
| Main | 260.202 | 263.060 | +2.859 |
| Solver | 228.440 | 230.484 | +2.044 |
| Startup | 28.863 | 29.768 | +0.905 |
| Harness | 31.762 | 32.577 | +0.815 |

The accepted evidence is the isolated paired Day 20 interval; the whole-suite paired interval is explicitly inconclusive under nonuniform interactive load, and the complete phase split is retained transparently. All 50 independent answers and 25 combined solves retain the established checksum. The official sample returned `3 / 1623178306`; 300 deterministic signed single-zero lists matched the exact pre-change implementation; and candidate-only checks covered a one-element zero list plus an integer far beyond `long` range.

## Day 3 shared 52-bit item masks

Day 3 previously converted each rucksack to character arrays and built/intersected boxed `HashSet<Character>` instances separately for both parts. Its default combined solve also copied the input into two new Scanners. It now maps the prompt's 52 letter item types to one `long` mask and accumulates compartment and three-elf intersections in one input pass. Totals use `long`; invalid letters, uneven compartments, incomplete groups, and nonsingleton shared-item sets are rejected explicitly according to the prompt grammar.

An isolated runner constructed the solver and input `Scanner` before timing the exact `fullSolve` call, checked both answers, and ran one excluded cold JVM per variant followed by 10 counterbalanced pairs of separate JVMs against baseline `fbd06e5`.

| Pair | Order | Hash sets/two scans (ms) | Shared bit masks (ms) | Delta (ms) |
| ---: | :---: | ---: | ---: | ---: |
| 1 | B-C | 7.504 | 4.013 | -3.492 |
| 2 | C-B | 9.896 | 3.701 | -6.195 |
| 3 | B-C | 8.098 | 4.310 | -3.788 |
| 4 | C-B | 7.987 | 4.926 | -3.061 |
| 5 | B-C | 8.118 | 3.733 | -4.385 |
| 6 | C-B | 7.001 | 4.016 | -2.985 |
| 7 | B-C | 7.354 | 3.562 | -3.793 |
| 8 | C-B | 7.529 | 3.701 | -3.828 |
| 9 | B-C | 7.605 | 3.608 | -3.997 |
| 10 | C-B | 7.619 | 3.615 | -4.004 |

The excluded cold values were 7.202ms baseline and 3.661ms candidate. The measured means were **7.871ms baseline** and **3.918ms candidate**, a **3.953ms (50.2%) reduction**. The paired-delta sample standard deviation was 0.896ms and the t(9) 95% confidence interval was **[-4.594ms, -3.312ms]**.

The whole-suite comparison was directionally consistent but noisy: its counterbalanced 10-pair solver means were 230.686ms baseline and 229.040ms candidate, a -1.646ms delta with a 95% confidence interval of [-7.116ms, +3.824ms]. Separate standard phase runs had these excluded cold processes:

| Variant | Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
| --- | ---: | ---: | ---: | ---: | ---: |
| Baseline | 324.643 | 277.152 | 242.720 | 30.356 | 34.432 |
| Candidate | 320.156 | 271.905 | 238.138 | 30.765 | 33.767 |

Their 10-process means were:

| Metric | Baseline mean (ms) | Candidate mean (ms) | Change (ms) |
| --- | ---: | ---: | ---: |
| Wall | 314.294 | 310.700 | -3.594 |
| Main | 269.511 | 266.311 | -3.200 |
| Solver | 236.991 | 233.851 | -3.140 |
| Startup | 30.176 | 28.610 | -1.566 |
| Harness | 32.520 | 32.459 | -0.061 |

The accepted evidence is the isolated paired interval; aggregate movement across the other 24 days is explicitly inconclusive under interactive load. All 50 independent answers and 25 combined solves retain checksum `5f79ad374b42c8a37382972ee3158f645c2260d01e08f33e59c12cfb9f60932b`. The official sample returned `157 / 70`, and 300 generated prompt-valid groups spanning all 52 priority values matched the exact pre-change implementation through separate and combined entry points.

## Day 1 exact single-pass top three

Day 1's default combined solve previously copied the input into two Scanners, parsed every calorie value twice, boxed every elf total, and sorted the complete list twice. It now parses once with `BigInteger`, maintains only the three largest totals, and derives both answers together. This also removes the previous `int` overflow restriction without assuming a minimum number of elves.

An isolated runner constructed the solver and input `Scanner` before timing the exact `fullSolve` call, checked both answers, and ran one excluded cold JVM per variant followed by 10 counterbalanced pairs of separate JVMs against baseline `25644bb`.

| Pair | Order | Duplicate list/sorts (ms) | Exact top three (ms) | Delta (ms) |
| ---: | :---: | ---: | ---: | ---: |
| 1 | B-C | 8.884 | 7.012 | -1.873 |
| 2 | C-B | 8.765 | 7.058 | -1.707 |
| 3 | B-C | 8.726 | 7.000 | -1.726 |
| 4 | C-B | 8.365 | 7.122 | -1.243 |
| 5 | B-C | 8.778 | 7.242 | -1.536 |
| 6 | C-B | 8.642 | 7.214 | -1.427 |
| 7 | B-C | 9.068 | 7.209 | -1.859 |
| 8 | C-B | 8.712 | 7.394 | -1.318 |
| 9 | B-C | 8.915 | 7.447 | -1.469 |
| 10 | C-B | 9.216 | 6.894 | -2.321 |

The excluded cold values were 8.714ms baseline and 7.288ms candidate. The measured means were **8.807ms baseline** and **7.159ms candidate**, a **1.648ms (18.7%) reduction**. The paired-delta sample standard deviation was 0.320ms and the t(9) 95% confidence interval was **[-1.877ms, -1.419ms]**.

The whole-suite comparison was directionally consistent but noisy: its counterbalanced 10-pair solver means were 221.078ms baseline and 219.783ms candidate, a -1.295ms delta with a 95% confidence interval of [-4.762ms, +2.173ms]. Separate standard phase runs had these excluded cold processes:

| Variant | Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
| --- | ---: | ---: | ---: | ---: | ---: |
| Baseline | 294.079 | 244.401 | 213.516 | 28.499 | 30.885 |
| Candidate | 280.547 | 247.583 | 216.898 | 28.127 | 30.686 |

Their 10-process means were:

| Metric | Baseline mean (ms) | Candidate mean (ms) | Change (ms) |
| --- | ---: | ---: | ---: |
| Wall | 294.101 | 286.549 | -7.551 |
| Main | 249.100 | 251.420 | +2.319 |
| Solver | 218.855 | 220.765 | +1.910 |
| Startup | 25.822 | 25.725 | -0.097 |
| Harness | 30.245 | 30.655 | +0.410 |

The accepted evidence is the isolated paired interval; aggregate movement across the other 24 days is explicitly inconclusive under interactive load. All 50 independent answers and 25 combined solves retain the established checksum. The official sample returned `24000 / 45000`; 300 deterministic prompt-valid inventories matched the exact pre-change implementation; and a candidate-only inventory beyond `long` range matched an independent `BigInteger` calculation.

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
