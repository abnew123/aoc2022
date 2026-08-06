# Advent of Code 2022 performance

## Methodology and current full suite

All timings use the original personal input corpus with OpenJDK 23.0.1 on Apple silicon. The harness compiles the fixed pre-speed source and current source into separate empty classpaths, excludes one true-cold launch per side, then runs 10 separate JVM pairs in counterbalanced A/B order. Solver is the sum of the 25 `fullSolve` calls; main includes solver plus in-process harness work; startup is process launch through the child start marker; harness is main minus solver; wall is the complete child process.

Reproduce after compiling both trees with `java -Daoc.data.dir=<data-dir> -cp <current-cp> aoc2022.FreshJvmBenchmark --compare <previous-cp> <current-cp>`. Every published state passes all 50 independent solves, all 25 combined solves, and independent/`fullSolve` equivalence. The tables below come from a 100-pair run of the same child protocol with seeded random within-pair order and the cold pair excluded; the checked-in n=10 counterbalanced mode remains the quick reproduction default.

Current 25-day means (n=100):

| Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
|---:|---:|---:|---:|---:|
| 164.788 | 134.414 | 103.322 | 22.552 | 31.091 |

Latest publication gate versus the preceding replay tip (n=100):

| Metric | Previous mean (ms) | Current mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|---:|
| wall | 176.600 | 164.788 | -11.812 | [-13.634, -9.990] |
| main | 147.969 | 134.414 | -13.556 | [-14.608, -12.503] |
| solver | 116.580 | 103.322 | -13.258 | [-14.278, -12.237] |
| startup | 22.780 | 22.552 | -0.229 | [-0.614, 0.157] |
| harness | 31.389 | 31.091 | -0.298 | [-0.505, -0.091] |

## Day 01

One slurped buffer and a single digit-and-newline scan accumulate each inventory in long registers and keep the top three totals directly, replacing the line-oriented scanner and per-line arbitrary-precision arithmetic.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Previous | 7.973420 | — | — |
| Current | 1.802825 | -6.170595 | [-6.224161, -6.117029] |

## Day 02

Direct token decoding and score tables replace repeated splitting and branching while preserving exact total promotion.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 6.701937 | — | — |
| Current | 2.463218 | -4.238719 | [-4.359793, -4.117645] |

## Day 03

Both parts share one pass of compact 52-bit item masks instead of rebuilding sets and rescanning compartments and groups.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 3.352615 | — | — |
| Current | 1.521379 | -1.831236 | [-1.970948, -1.691523] |

## Day 04

Each assignment pair is parsed once and tested directly for containment and overlap with exact numeric fallback.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 2.842785 | — | — |
| Current | 1.875266 | -0.967518 | [-1.041189, -0.893848] |

## Day 05

The stack drawing and moves are parsed once, then the two crane models advance together over independent compact stack state.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 2.490979 | — | — |
| Current | 1.506810 | -0.984169 | [-1.036418, -0.931920] |

## Day 06

One marker scan maintains the necessary recent-character state for both requested window sizes instead of searching twice.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 4.194467 | — | — |
| Current | 0.916146 | -3.278321 | [-3.364472, -3.192170] |

## Day 07

One transcript parse accumulates exact directory sizes through ancestor links and derives both thresholds from that shared model.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 5.165429 | — | — |
| Current | 1.680439 | -3.484990 | [-3.622744, -3.347235] |

## Day 08

A shared primitive grid analysis computes visibility and scenic distances for both parts without rebuilding rows, columns, or trees.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 12.359738 | — | — |
| Current | 1.545867 | -10.813871 | [-11.186713, -10.441028] |

## Day 09

One ten-knot simulation records visits for knot 1 and knot 9 together using unbounded packed coordinates.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 7.980317 | — | — |
| Current | 6.115329 | -1.864988 | [-2.711609, -1.018366] |

## Day 10

One instruction interpretation computes signal strength and the 40×6 raster together; generic OCR replaces the personal hardcoded display answer.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 0.489419 | — | — |
| Current | 0.838140 | 0.348721 | [0.299263, 0.398179] |

## Day 11

Primitive monkey queues and shared parsed operations replace boxed queue churn while preserving the two required worry simulations.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 20.400708 | — | — |
| Current | 6.010183 | -14.390525 | [-15.609892, -13.171158] |

## Day 12

One reverse breadth-first traversal from the endpoint supplies both the start distance and the best elevation-zero distance.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 30.211234 | — | — |
| Current | 1.214496 | -28.996738 | [-30.632040, -27.361435] |

## Day 13

Packets are parsed once; pair ordering is accumulated while reading and divider ranks are derived directly without sorting every packet.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 14.090446 | — | — |
| Current | 3.700104 | -10.390342 | [-11.228022, -9.552661] |

## Day 14

A flat bitset cave with a linear rock parser answers part 1 by an order-exact explicit-stack descent stopped at the first grain below the lowest rock, and part 2 by a branchless row-closure sweep counting sixty-four cells per word operation.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Previous | 5.499029 | — | — |
| Current | 1.464094 | -4.034935 | [-4.132255, -3.937616] |

## Day 15

Sensors are parsed once; merged row intervals and exact boundary-line intersections replace repeated scans of the search area.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 2.081554 | — | — |
| Current | 2.672327 | 0.590773 | [0.453341, 0.728204] |

## Day 16

Part 1 runs a branch-and-bound depth-first search whose admissible bound opens every remaining valve at its direct shortest-path arrival, replacing a memo table that zeroed sixteen million entries per call; part 2 keeps the bitmask best-per-subset table with subset-max pairing, now behind an input-adaptive fallback for larger valve counts.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Previous | 15.143382 | — | — |
| Current | 9.377223 | -5.766159 | [-5.945755, -5.586563] |

## Day 17

One compact rock simulation detects a verified cycle and shares checkpoints for 2,022 and 1,000,000,000,000 rocks.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 255.557587 | — | — |
| Current | 4.167775 | -251.389813 | [-264.410494, -238.369131] |

## Day 18

Packed cube membership supplies total exposed faces, while one bounded exterior traversal identifies only faces reachable from outside.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 10.037810 | — | — |
| Current | 3.919654 | -6.118156 | [-6.403313, -5.832999] |

## Day 19

A proof-carrying branch-and-bound replaces the memoized search: next-robot branching with per-type useless-build time cutoffs, an exact saturation cutoff, the geometric geode bound, and an ore-relaxed simulation bound, with the memo tables and an unsound-in-general force-build-geode heuristic removed outright.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Previous | 21.322199 | — | — |
| Current | 1.862119 | -19.460080 | [-19.607108, -19.313052] |

## Day 20

Mixing runs on a blocked order-statistic sequence over flat primitive arrays: fixed-stride blocks in one int array with small sequentially-scanned block indexes, every move a pure arraycopy with constant bookkeeping, long modular rotations, and a linear digit-scan parse replacing BigInteger arithmetic end to end.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Previous | 22.259182 | — | — |
| Current | 12.335832 | -9.923350 | [-10.233227, -9.613473] |

## Day 21

One exact expression graph supports numeric evaluation and inverse symbolic solving without reparsing or floating-point arithmetic.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 252.670108 | — | — |
| Current | 6.839296 | -245.830813 | [-258.655057, -233.006568] |

## Day 22

The current solver parses the board once, infers and folds any valid six-face cube net, and shares primitive face-edge transitions between the flat and cube walkers instead of allocating and scanning a fixed million-cell board twice. The generic fold also replaces two hardcoded edge transitions that were geometrically impossible (one collapsed an entire edge onto a single cell); the previous answers survived on this input only because the diverging trajectory happened to re-merge. Verified against the official example, an independent literal-3D-folding oracle across all eleven cube nets, and a site-verified answer on a second input.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 13.540065 | — | — |
| Current | 6.155384 | -7.384680 | [-7.552188, -7.217173] |

## Day 23

Occupancy lives in bitmask rows: each round computes horizontal dilations, the four rotated direction proposals, destination shifts, and exact pairwise-intersection collision cancellation as whole-word operations over an adaptive, self-growing band, advancing up to sixty-four cells per instruction.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Previous | 34.685634 | — | — |
| Current | 16.183391 | -18.502243 | [-18.993427, -18.011059] |

## Day 24

Reachability advances as multi-word bitmask rows — precomputed rotation and permutation phase tables give each minute's blizzard occupancy, and one five-way shift-OR-mask update moves the whole frontier — with the three valley legs chained off absolute time and no per-call table rebuilds.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Previous | 9.345993 | — | — |
| Current | 2.669663 | -6.676330 | [-6.744613, -6.608047] |

## Day 25

One exact SNAFU accumulation converts the final sum directly without retaining all intermediate values.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 3.210615 | — | — |
| Current | 0.315656 | -2.894958 | [-3.069140, -2.720777] |
