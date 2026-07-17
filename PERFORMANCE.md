# Advent of Code 2022 performance

## Methodology and current full suite

All timings use the original personal input corpus with OpenJDK 23.0.1 on Apple silicon. The harness compiles the fixed pre-speed source and current source into separate empty classpaths, excludes one true-cold launch per side, then runs 10 separate JVM pairs in counterbalanced A/B order. Solver is the sum of the 25 `fullSolve` calls; main includes solver plus in-process harness work; startup is process launch through the child start marker; harness is main minus solver; wall is the complete child process.

Reproduce after compiling both trees with `java -Daoc.data.dir=<data-dir> -cp <current-cp> aoc2022.FreshJvmBenchmark --compare <previous-cp> <current-cp>`. Every published state passes all 50 independent solves, all 25 combined solves, and independent/`fullSolve` equivalence.

Current 25-day means:

| Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
|---:|---:|---:|---:|---:|
| 319.651300 | 272.072417 | 238.409387 | 35.115767 | 33.663029 |

Latest publication gate versus the preceding replay tip:

| Metric | Previous mean (ms) | Current mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|---:|
| wall | 357.026733 | 319.651300 | -37.375433 | [-47.226521, -27.524346] |
| main | 306.971937 | 272.072417 | -34.899521 | [-39.589270, -30.209772] |
| solver | 272.383079 | 238.409387 | -33.973692 | [-38.130396, -29.816987] |
| startup | 34.216117 | 35.115767 | 0.899650 | [-6.349787, 8.149087] |
| harness | 34.588858 | 33.663029 | -0.925829 | [-1.957404, 0.105746] |

## Day 01

Unchanged from the pre-PR implementation. Current solver mean: 9.707483 ms.

## Day 02

Unchanged from the pre-PR implementation. Current solver mean: 6.373817 ms.

## Day 03

Unchanged from the pre-PR implementation. Current solver mean: 4.048258 ms.

## Day 04

Unchanged from the pre-PR implementation. Current solver mean: 2.799062 ms.

## Day 05

Unchanged from the pre-PR implementation. Current solver mean: 2.449625 ms.

## Day 06

Unchanged from the pre-PR implementation. Current solver mean: 3.770134 ms.

## Day 07

Unchanged from the pre-PR implementation. Current solver mean: 5.039763 ms.

## Day 08

Unchanged from the pre-PR implementation. Current solver mean: 12.382267 ms.

## Day 09

One ten-knot simulation records visits for knot 1 and knot 9 together using unbounded packed coordinates.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 7.980317 | — | — |
| Current | 6.115329 | -1.864988 | [-2.711609, -1.018366] |

## Day 10

Unchanged from the pre-PR implementation. Current solver mean: 0.593054 ms.

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

A single sand simulation with the floor tracks the abyss threshold and final blockage instead of rebuilding the cave for each part.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 14.404017 | — | — |
| Current | 4.831075 | -9.572942 | [-9.971650, -9.174233] |

## Day 15

Unchanged from the pre-PR implementation. Current solver mean: 2.071021 ms.

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

Unchanged from the pre-PR implementation. Current solver mean: 9.014988 ms.

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

Unchanged from the pre-PR implementation. Current solver mean: 13.512796 ms.

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

Unchanged from the pre-PR implementation. Current solver mean: 3.619567 ms.
