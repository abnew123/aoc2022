# Advent of Code 2022 performance

## Methodology and current full suite

All timings use the original personal input corpus with OpenJDK 23.0.1 on Apple silicon. The harness compiles the fixed pre-speed source and current source into separate empty classpaths, excludes one true-cold launch per side, then runs 10 separate JVM pairs in counterbalanced A/B order. Solver is the sum of the 25 `fullSolve` calls; main includes solver plus in-process harness work; startup is process launch through the child start marker; harness is main minus solver; wall is the complete child process.

Reproduce after compiling both trees with `java -Daoc.data.dir=<data-dir> -cp <current-cp> aoc2022.FreshJvmBenchmark --compare <previous-cp> <current-cp>`. Every published state passes all 50 independent solves, all 25 combined solves, and independent/`fullSolve` equivalence.

Current 25-day means:

| Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
|---:|---:|---:|---:|---:|
| 1179.468633 | 1126.006833 | 1093.983895 | 30.372229 | 32.022938 |

Latest publication gate versus the preceding replay tip:

| Metric | Previous mean (ms) | Current mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|---:|
| wall | 1376.406812 | 1179.468633 | -196.938179 | [-236.836984, -157.039374] |
| main | 1320.853546 | 1126.006833 | -194.846713 | [-236.198988, -153.494437] |
| solver | 1287.590891 | 1093.983895 | -193.606996 | [-234.816237, -152.397756] |
| startup | 32.759888 | 30.372229 | -2.387658 | [-5.927647, 1.152330] |
| harness | 33.262655 | 32.022938 | -1.239716 | [-2.359833, -0.119599] |

## Day 01

Unchanged from the pre-PR implementation. Current solver mean: 9.555137 ms.

## Day 02

Unchanged from the pre-PR implementation. Current solver mean: 6.367683 ms.

## Day 03

Unchanged from the pre-PR implementation. Current solver mean: 3.582037 ms.

## Day 04

Unchanged from the pre-PR implementation. Current solver mean: 2.712042 ms.

## Day 05

Unchanged from the pre-PR implementation. Current solver mean: 2.292425 ms.

## Day 06

Unchanged from the pre-PR implementation. Current solver mean: 3.804916 ms.

## Day 07

Unchanged from the pre-PR implementation. Current solver mean: 4.881979 ms.

## Day 08

Unchanged from the pre-PR implementation. Current solver mean: 12.308471 ms.

## Day 09

Unchanged from the pre-PR implementation. Current solver mean: 8.380363 ms.

## Day 10

Unchanged from the pre-PR implementation. Current solver mean: 0.437512 ms.

## Day 11

Unchanged from the pre-PR implementation. Current solver mean: 21.933867 ms.

## Day 12

Unchanged from the pre-PR implementation. Current solver mean: 29.428562 ms.

## Day 13

Unchanged from the pre-PR implementation. Current solver mean: 15.509642 ms.

## Day 14

Unchanged from the pre-PR implementation. Current solver mean: 14.016734 ms.

## Day 15

Unchanged from the pre-PR implementation. Current solver mean: 1.931700 ms.

## Day 16

Useful valves are compressed into a bitmask search with memoized pressure states and direct disjoint-mask pairing for the elephant.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 231.947104 | — | — |
| Current | 16.801325 | -215.145780 | [-250.301082, -179.990477] |

## Day 17

Unchanged from the pre-PR implementation. Current solver mean: 279.672671 ms.

## Day 18

Unchanged from the pre-PR implementation. Current solver mean: 8.679600 ms.

## Day 19

Unchanged from the pre-PR implementation. Current solver mean: 128.740508 ms.

## Day 20

Unchanged from the pre-PR implementation. Current solver mean: 262.538729 ms.

## Day 21

One exact expression graph supports numeric evaluation and inverse symbolic solving without reparsing or floating-point arithmetic.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 252.670108 | — | — |
| Current | 6.839296 | -245.830813 | [-258.655057, -233.006568] |

## Day 22

Unchanged from the pre-PR implementation. Current solver mean: 8.687913 ms.

## Day 23

Unchanged from the pre-PR implementation. Current solver mean: 233.317396 ms.

## Day 24

Periodic blizzard occupancy and packed frontier traversal replace repeated hazard simulation and share the three required valley legs.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 702.492042 | — | — |
| Current | 9.913908 | -692.578133 | [-705.041534, -680.114732] |

## Day 25

Unchanged from the pre-PR implementation. Current solver mean: 2.425312 ms.
