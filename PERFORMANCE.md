# Advent of Code 2022 performance

## Methodology and current full suite

All timings use the original personal input corpus with OpenJDK 23.0.1 on Apple silicon. The pre-speed baseline is the normal Java solvers at pre-campaign main commit `821fb777`, compiled with the current benchmark harness into its own classpath. The day tables below come from one 100-pair pre-speed-versus-current run (2026-08-06): every measured side is a separate fresh JVM, within-pair order is seeded-random, and one true-cold launch per side is excluded. Solver is the sum of the 25 `fullSolve` calls; main includes solver plus in-process harness work; startup is process launch through the child start marker; harness is main minus solver; wall is the complete child process. The checked-in n=10 counterbalanced `--compare` mode remains the quick reproduction default.

Reproduce after compiling both trees with `java -Daoc.data.dir=<data-dir> -cp <current-cp> aoc2022.FreshJvmBenchmark --compare <previous-cp> <current-cp>`. Every published state passes all 50 independent solves, all 25 combined solves, and independent/`fullSolve` equivalence. The tables below come from a 100-pair run of the same child protocol with seeded random within-pair order and the cold pair excluded; the checked-in n=10 counterbalanced mode remains the quick reproduction default.

Current 25-day means (n=100):

| Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
|---:|---:|---:|---:|---:|
| 162.142 | 128.011 | 99.736 | 20.712 | 28.275 |

Latest publication gate versus the preceding replay tip (n=100):

| Metric | Previous mean (ms) | Current mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|---:|
| wall | 158.965 | 162.142 | +3.177 | [+1.245, +5.108] |
| main | 128.788 | 128.011 | -0.778 | [-1.167, -0.388] |
| solver | 100.586 | 99.736 | -0.850 | [-1.218, -0.482] |
| startup | 20.733 | 20.712 | -0.021 | [-0.217, +0.174] |
| harness | 28.202 | 28.275 | +0.073 | [-0.027, +0.173] |

## Day 01

One slurped buffer and a single digit-and-newline scan accumulate each inventory in long registers and keep the top three totals directly, replacing the line-oriented scanner and per-line arbitrary-precision arithmetic.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 9.021869 | — | — |
| Current | 1.69724 | -7.324629 | [-7.388401, -7.260858] |

## Day 02

Direct token decoding and score tables replace repeated splitting and branching while preserving exact total promotion.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 6.186076 | — | — |
| Current | 5.698855 | -0.487221 | [-0.544934, -0.429509] |

## Day 03

Both parts share one pass of compact 52-bit item masks instead of rebuilding sets and rescanning compartments and groups.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 3.865567 | — | — |
| Current | 1.365998 | -2.499569 | [-2.526719, -2.472418] |

## Day 04

Each assignment pair is parsed once and tested directly for containment and overlap with exact numeric fallback.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 2.728932 | — | — |
| Current | 3.190119 | 0.461187 | [0.425684, 0.496691] |

## Day 05

The stack drawing and moves are parsed once, then the two crane models advance together over independent compact stack state.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 2.271267 | — | — |
| Current | 1.350722 | -0.920545 | [-0.944843, -0.896246] |

## Day 06

One marker scan maintains the necessary recent-character state for both requested window sizes instead of searching twice.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 3.628152 | — | — |
| Current | 1.041071 | -2.587081 | [-2.617976, -2.556186] |

## Day 07

One transcript parse accumulates exact directory sizes through ancestor links and derives both thresholds from that shared model.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 4.775992 | — | — |
| Current | 2.025813 | -2.75018 | [-2.815838, -2.684522] |

## Day 08

A shared primitive grid analysis computes visibility and scenic distances for both parts without rebuilding rows, columns, or trees.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 11.90659 | — | — |
| Current | 1.718041 | -10.188549 | [-10.316031, -10.061067] |

## Day 09

One ten-knot simulation records visits for knot 1 and knot 9 together using unbounded packed coordinates.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 7.705925 | — | — |
| Current | 5.629668 | -2.076257 | [-2.378118, -1.774396] |

## Day 10

One instruction interpretation computes signal strength and the 40×6 raster together; generic OCR replaces the personal hardcoded display answer.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 0.388629 | — | — |
| Current | 0.779058 | 0.390429 | [0.368848, 0.41201] |

## Day 11

Primitive monkey queues and shared parsed operations replace boxed queue churn while preserving the two required worry simulations.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 19.241567 | — | — |
| Current | 6.105633 | -13.135934 | [-13.571352, -12.700517] |

## Day 12

One reverse breadth-first traversal from the endpoint supplies both the start distance and the best elevation-zero distance.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 28.228053 | — | — |
| Current | 1.433566 | -26.794487 | [-27.206401, -26.382573] |

## Day 13

Packets are parsed once; pair ordering is accumulated while reading and divider ranks are derived directly without sorting every packet.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 15.574664 | — | — |
| Current | 4.425127 | -11.149537 | [-11.366805, -10.93227] |

## Day 14

A flat bitset cave with a linear rock parser answers part 1 by an order-exact explicit-stack descent stopped at the first grain below the lowest rock, and part 2 by a branchless row-closure sweep counting sixty-four cells per word operation.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 14.116496 | — | — |
| Current | 1.575866 | -12.54063 | [-12.715181, -12.36608] |

## Day 15

Sensors are parsed once; merged row intervals and exact boundary-line intersections replace repeated scans of the search area.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 1.908207 | — | — |
| Current | 2.442435 | 0.534228 | [0.502166, 0.56629] |

## Day 16

Part 1 runs a branch-and-bound depth-first search whose admissible bound opens every remaining valve at its direct shortest-path arrival, replacing a memo table that zeroed sixteen million entries per call; part 2 keeps the bitmask best-per-subset table with subset-max pairing, now behind an input-adaptive fallback for larger valve counts.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 187.805766 | — | — |
| Current | 9.125765 | -178.68 | [-183.164186, -174.195815] |

## Day 17

One compact rock simulation detects a verified cycle and shares checkpoints for 2,022 and 1,000,000,000,000 rocks.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 243.830219 | — | — |
| Current | 4.352457 | -239.477762 | [-240.780706, -238.174819] |

## Day 18

Packed cube membership supplies total exposed faces, while one bounded exterior traversal identifies only faces reachable from outside.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 7.454357 | — | — |
| Current | 3.776764 | -3.677593 | [-3.760026, -3.59516] |

## Day 19

A proof-carrying branch-and-bound replaces the memoized search: next-robot branching with per-type useless-build time cutoffs, an exact saturation cutoff, the geometric geode bound, and an ore-relaxed simulation bound, with the memo tables and an unsound-in-general force-build-geode heuristic removed outright. A follow-up correctness pass makes the parse layout-independent (the official example wraps each blueprint across indented lines), weights part 1 by the parsed blueprint id, caps part 2 at the first `min(3, n)` blueprints so the two-blueprint official example passes (33 / 3472), and lets the precursor gates tolerate zero-cost blueprints (oracle-verified); answers on both corpora are unchanged and a paired n=100 run measured the fix as timing-neutral (day 19: -0.061 ms [-0.087, -0.034]).

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 123.423861 | — | — |
| Current | 1.853062 | -121.570799 | [-122.646597, -120.495] |

## Day 20

Mixing runs on a blocked order-statistic sequence over flat primitive arrays: fixed-stride blocks in one int array with small sequentially-scanned block indexes, every move a pure arraycopy with constant bookkeeping, long modular rotations, and a linear digit-scan parse replacing BigInteger arithmetic end to end.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 260.812499 | — | — |
| Current | 11.375177 | -249.437322 | [-250.092152, -248.782493] |

## Day 21

One exact expression graph supports numeric evaluation and inverse symbolic solving without reparsing or floating-point arithmetic.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 237.8846 | — | — |
| Current | 4.933586 | -232.951014 | [-233.971113, -231.930914] |

## Day 22

The current solver parses the board once, infers and folds any valid six-face cube net, and shares primitive face-edge transitions between the flat and cube walkers instead of allocating and scanning a fixed million-cell board twice. The generic fold also replaces two hardcoded edge transitions that were geometrically impossible (one collapsed an entire edge onto a single cell); the previous answers survived on this input only because the diverging trajectory happened to re-merge. Verified against the official example, an independent literal-3D-folding oracle across all eleven cube nets, and a site-verified answer on a second input.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 8.209422 | — | — |
| Current | 6.219048 | -1.990374 | [-2.074659, -1.906088] |

## Day 23

Occupancy lives in bitmask rows: each round computes horizontal dilations, the four rotated direction proposals, destination shifts, and exact pairwise-intersection collision cancellation as whole-word operations over an adaptive, self-growing band, advancing up to sixty-four cells per instruction.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 212.539597 | — | — |
| Current | 16.547287 | -195.99231 | [-197.752041, -194.232578] |

*Pre-PR is the documented pre-speed measurement; Current is the 2026-08-07 n=100 confirming run. The measured step versus the preceding tip (n=100) was -1.064 [-1.345, -0.783].*

## Day 24

Reachability advances as multi-word bitmask rows — precomputed rotation and permutation phase tables give each minute's blizzard occupancy, and one five-way shift-OR-mask update moves the whole frontier — with the three valley legs chained off absolute time and no per-call table rebuilds.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 694.604007 | — | — |
| Current | 2.603782 | -692.000225 | [-694.054028, -689.946422] |

## Day 25

One exact SNAFU accumulation converts the final sum directly without retaining all intermediate values.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 2.498165 | — | — |
| Current | 0.380285 | -2.11788 | [-2.157572, -2.078189] |
