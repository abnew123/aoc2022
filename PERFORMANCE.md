# Advent of Code 2022 performance

## Methodology and current full suite

All timings use the original personal input corpus with OpenJDK 23.0.1 on Apple silicon. The harness compiles the fixed pre-speed source and current source into separate empty classpaths, excludes one true-cold launch per side, then runs 10 separate JVM pairs in counterbalanced A/B order. Solver is the sum of the 25 `fullSolve` calls; main includes solver plus in-process harness work; startup is process launch through the child start marker; harness is main minus solver; wall is the complete child process.

Reproduce after compiling both trees with `java -Daoc.data.dir=<data-dir> -cp <current-cp> aoc2022.FreshJvmBenchmark --compare <previous-cp> <current-cp>`. Every published state passes all 50 independent solves, all 25 combined solves, and independent/`fullSolve` equivalence.

Current 25-day means:

| Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
|---:|---:|---:|---:|---:|
| 400.662254 | 354.241338 | 319.256613 | 31.679546 | 34.984725 |

Latest publication gate versus the preceding replay tip:

| Metric | Previous mean (ms) | Current mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|---:|
| wall | 499.831275 | 400.662254 | -99.169021 | [-116.357641, -81.980401] |
| main | 449.827092 | 354.241338 | -95.585754 | [-115.289278, -75.882230] |
| solver | 415.293970 | 319.256613 | -96.037357 | [-115.647134, -76.427580] |
| startup | 32.782233 | 31.679546 | -1.102688 | [-2.869517, 0.664142] |
| harness | 34.533122 | 34.984725 | 0.451603 | [-0.585720, 1.488926] |

## Day 01

Unchanged from the pre-PR implementation. Current solver mean: 9.936675 ms.

## Day 02

Unchanged from the pre-PR implementation. Current solver mean: 6.471992 ms.

## Day 03

Unchanged from the pre-PR implementation. Current solver mean: 3.895192 ms.

## Day 04

Unchanged from the pre-PR implementation. Current solver mean: 2.838671 ms.

## Day 05

Unchanged from the pre-PR implementation. Current solver mean: 2.372154 ms.

## Day 06

Unchanged from the pre-PR implementation. Current solver mean: 3.657096 ms.

## Day 07

Unchanged from the pre-PR implementation. Current solver mean: 5.192075 ms.

## Day 08

Unchanged from the pre-PR implementation. Current solver mean: 12.065683 ms.

## Day 09

Unchanged from the pre-PR implementation. Current solver mean: 7.238017 ms.

## Day 10

Unchanged from the pre-PR implementation. Current solver mean: 0.457392 ms.

## Day 11

Unchanged from the pre-PR implementation. Current solver mean: 20.421254 ms.

## Day 12

Unchanged from the pre-PR implementation. Current solver mean: 30.025471 ms.

## Day 13

Unchanged from the pre-PR implementation. Current solver mean: 21.205108 ms.

## Day 14

Unchanged from the pre-PR implementation. Current solver mean: 17.518771 ms.

## Day 15

Unchanged from the pre-PR implementation. Current solver mean: 2.841592 ms.

## Day 16

Useful valves are compressed into a bitmask search with memoized pressure states and direct disjoint-mask pairing for the elephant.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 231.947104 | — | — |
| Current | 16.801325 | -215.145780 | [-250.301082, -179.990477] |

## Day 17

One compact rock simulation detects a verified cycle and shares checkpoints for 2,022 and 1,000,000,000,000 rocks.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 255.557587 | — | — |
| Current | 4.167775 | -251.389813 | [-264.410494, -238.369131] |

## Day 18

Unchanged from the pre-PR implementation. Current solver mean: 9.026254 ms.

## Day 19

A resource-capped memoized search uses admissible geode bounds and dominance pruning instead of exploring redundant robot schedules.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 139.012754 | — | — |
| Current | 20.951042 | -118.061712 | [-121.556491, -114.566933] |

## Day 20

Numbers are parsed once and mixed through indexed treap storage for both decryption keys instead of repeated linear list movement.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 267.824579 | — | — |
| Current | 44.321871 | -223.502709 | [-226.001735, -221.003682] |

## Day 21

One exact expression graph supports numeric evaluation and inverse symbolic solving without reparsing or floating-point arithmetic.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 252.670108 | — | — |
| Current | 6.839296 | -245.830813 | [-258.655057, -233.006568] |

## Day 22

Unchanged from the pre-PR implementation. Current solver mean: 9.642254 ms.

## Day 23

Packed elf positions and move counts detect expansion and stabilization directly, avoiding repeated full-boundary rescans.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 215.934833 | — | — |
| Current | 35.681138 | -180.253696 | [-185.622593, -174.884798] |

## Day 24

Periodic blizzard occupancy and packed frontier traversal replace repeated hazard simulation and share the three required valley legs.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 702.492042 | — | — |
| Current | 9.913908 | -692.578133 | [-705.041534, -680.114732] |

## Day 25

Unchanged from the pre-PR implementation. Current solver mean: 2.645338 ms.
