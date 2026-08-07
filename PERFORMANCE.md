# Advent of Code 2022 performance

## Methodology and current full suite

All timings use the original personal input corpus with OpenJDK 23.0.1 on Apple silicon. The pre-speed baseline is the normal Java solvers at pre-campaign main commit `821fb777`, compiled with the current benchmark harness into its own classpath. The day tables below come from one 100-pair pre-speed-versus-current run (2026-08-06): every measured side is a separate fresh JVM, within-pair order is seeded-random, and one true-cold launch per side is excluded. Solver is the sum of the 25 `fullSolve` calls; main includes solver plus in-process harness work; startup is process launch through the child start marker; harness is main minus solver; wall is the complete child process. The checked-in n=10 counterbalanced `--compare` mode remains the quick reproduction default.

Reproduce after compiling both trees with `java -Daoc.data.dir=<data-dir> -cp <current-cp> aoc2022.FreshJvmBenchmark --compare <previous-cp> <current-cp>`. Every published state passes all 50 independent solves, all 25 combined solves, and independent/`fullSolve` equivalence. The tables below come from a 100-pair run of the same child protocol with seeded random within-pair order and the cold pair excluded; the checked-in n=10 counterbalanced mode remains the quick reproduction default.

Current 25-day means (n=100):

| Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
|---:|---:|---:|---:|---:|
| 152.082 | 120.399 | 89.531 | 21.937 | 30.869 |

Latest publication gate versus the preceding replay tip (n=100):

| Metric | Previous mean (ms) | Current mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|---:|
| wall | 168.267 | 153.224 | -15.043 | [-17.067, -13.019] |
| main | 133.982 | 121.714 | -12.269 | [-13.185, -11.352] |
| solver | 102.744 | 90.058 | -12.686 | [-13.575, -11.797] |
| startup | 22.454 | 22.590 | +0.137 | [-0.166, +0.439] |
| harness | 31.238 | 31.656 | +0.417 | [+0.095, +0.740] |

## Day 01

One slurped buffer and a single digit-and-newline scan accumulate each inventory in long registers and keep the top three totals directly, replacing the line-oriented scanner and per-line arbitrary-precision arithmetic.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 9.353822 | — | — |
| Current | 1.77564 | -7.578182 | [-7.655559, -7.500805] |

## Day 02

Direct token decoding and score tables replace repeated splitting and branching while preserving exact total promotion.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 6.288261 | — | — |
| Current | 2.129632 | -4.158629 | [-4.239602, -4.077655] |

## Day 03

Both parts share one pass of compact 52-bit item masks instead of rebuilding sets and rescanning compartments and groups.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 4.003829 | — | — |
| Current | 1.752808 | -2.251021 | [-2.32847, -2.173572] |

## Day 04

Each assignment pair is parsed once and tested directly for containment and overlap with exact numeric fallback.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 2.757025 | — | — |
| Current | 3.684514 | 0.927489 | [0.848521, 1.006456] |

## Day 05

The stack drawing and moves are parsed once, then the two crane models advance together over independent compact stack state.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 2.355861 | — | — |
| Current | 1.719292 | -0.63657 | [-0.716214, -0.556925] |

## Day 06

One marker scan maintains the necessary recent-character state for both requested window sizes instead of searching twice.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 3.574701 | — | — |
| Current | 0.683687 | -2.891014 | [-2.939389, -2.842639] |

## Day 07

One transcript parse accumulates exact directory sizes through ancestor links and derives both thresholds from that shared model.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 5.006996 | — | — |
| Current | 2.296546 | -2.71045 | [-2.802068, -2.618832] |

## Day 08

A shared primitive grid analysis computes visibility and scenic distances for both parts without rebuilding rows, columns, or trees.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 11.881821 | — | — |
| Current | 1.776998 | -10.104823 | [-10.279068, -9.930577] |

## Day 09

One ten-knot simulation records visits for knot 1 and knot 9 together using unbounded packed coordinates.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 8.255783 | — | — |
| Current | 5.550296 | -2.705487 | [-3.297714, -2.113259] |

## Day 10

One instruction interpretation computes signal strength and the 40×6 raster together; generic OCR replaces the personal hardcoded display answer.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 0.430586 | — | — |
| Current | 0.56344 | 0.132853 | [0.116448, 0.149259] |

## Day 11

Primitive monkey queues and shared parsed operations replace boxed queue churn while preserving the two required worry simulations.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 18.716218 | — | — |
| Current | 4.403636 | -14.312582 | [-14.707998, -13.917167] |

## Day 12

One reverse breadth-first traversal from the endpoint supplies both the start distance and the best elevation-zero distance.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 28.497805 | — | — |
| Current | 1.179386 | -27.318419 | [-27.793062, -26.843776] |

## Day 13

Packets are parsed once; pair ordering is accumulated while reading and divider ranks are derived directly without sorting every packet.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 16.104572 | — | — |
| Current | 4.173405 | -11.931166 | [-12.32543, -11.536902] |

## Day 14

A flat bitset cave with a linear rock parser answers part 1 by an order-exact explicit-stack descent stopped at the first grain below the lowest rock, and part 2 by a branchless row-closure sweep counting sixty-four cells per word operation.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 14.173855 | — | — |
| Current | 1.206961 | -12.966894 | [-13.142183, -12.791605] |

## Day 15

Sensors are parsed once; merged row intervals and exact boundary-line intersections replace repeated scans of the search area.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 1.986765 | — | — |
| Current | 0.43022 | -1.556546 | [-1.59977, -1.513322] |

## Day 16

Part 1 runs a branch-and-bound depth-first search whose admissible bound opens every remaining valve at its direct shortest-path arrival, replacing a memo table that zeroed sixteen million entries per call; part 2 keeps the bitmask best-per-subset table with subset-max pairing, now behind an input-adaptive fallback for larger valve counts.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 192.523312 | — | — |
| Current | 6.727801 | -185.795511 | [-190.972949, -180.618073] |

## Day 17

One compact rock simulation detects a verified cycle and shares checkpoints for 2,022 and 1,000,000,000,000 rocks.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 246.028467 | — | — |
| Current | 2.960953 | -243.067514 | [-244.124992, -242.010035] |

## Day 18

Packed cube membership supplies total exposed faces, while one bounded exterior traversal identifies only faces reachable from outside.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 7.65224 | — | — |
| Current | 3.080615 | -4.571625 | [-4.656355, -4.486895] |

## Day 19

A proof-carrying branch-and-bound replaces the memoized search: next-robot branching with per-type useless-build time cutoffs, an exact saturation cutoff, the geometric geode bound, and an ore-relaxed simulation bound, with the memo tables and an unsound-in-general force-build-geode heuristic removed outright. A follow-up correctness pass makes the parse layout-independent (the official example wraps each blueprint across indented lines), weights part 1 by the parsed blueprint id, caps part 2 at the first `min(3, n)` blueprints so the two-blueprint official example passes (33 / 3472), and lets the precursor gates tolerate zero-cost blueprints (oracle-verified); answers on both corpora are unchanged and a paired n=100 run measured the fix as timing-neutral (day 19: -0.061 ms [-0.087, -0.034]).

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 124.895268 | — | — |
| Current | 1.856786 | -123.038482 | [-123.841638, -122.235326] |

## Day 20

Mixing runs on a blocked order-statistic sequence over flat primitive arrays: fixed-stride blocks in one int array with small sequentially-scanned block indexes, every move a pure arraycopy with constant bookkeeping, long modular rotations, and a linear digit-scan parse replacing BigInteger arithmetic end to end.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 265.026543 | — | — |
| Current | 10.90382 | -254.122722 | [-254.645032, -253.600413] |

## Day 21

One exact expression graph supports numeric evaluation and inverse symbolic solving without reparsing or floating-point arithmetic.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 241.22366 | — | — |
| Current | 5.61906 | -235.6046 | [-236.515415, -234.693785] |

## Day 22

The current solver parses the board once, infers and folds any valid six-face cube net, and shares primitive face-edge transitions between the flat and cube walkers instead of allocating and scanning a fixed million-cell board twice. The generic fold also replaces two hardcoded edge transitions that were geometrically impossible (one collapsed an entire edge onto a single cell); the previous answers survived on this input only because the diverging trajectory happened to re-merge. Verified against the official example, an independent literal-3D-folding oracle across all eleven cube nets, and a site-verified answer on a second input.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 8.195785 | — | — |
| Current | 6.520315 | -1.675471 | [-1.737791, -1.613151] |

## Day 23

Occupancy lives in bitmask rows: each round computes horizontal dilations, the four rotated direction proposals, destination shifts, and exact pairwise-intersection collision cancellation as whole-word operations over an adaptive, self-growing band, advancing up to sixty-four cells per instruction.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 213.037436 | — | — |
| Current | 15.571077 | -197.466359 | [-198.993989, -195.93873] |


## Day 24

Reachability advances as multi-word bitmask rows — precomputed rotation and permutation phase tables give each minute's blizzard occupancy, and one five-way shift-OR-mask update moves the whole frontier — with the three valley legs chained off absolute time and no per-call table rebuilds.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 708.913054 | — | — |
| Current | 2.583624 | -706.32943 | [-708.211263, -704.447596] |

## Day 25

One exact SNAFU accumulation converts the final sum directly without retaining all intermediate values.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 2.533846 | — | — |
| Current | 0.380257 | -2.15359 | [-2.19787, -2.10931] |
