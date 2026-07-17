# Advent of Code 2022 performance

## Methodology and current full suite

All timings use the original personal input corpus with OpenJDK 23.0.1 on Apple silicon. The harness compiles the fixed pre-speed source and current source into separate empty classpaths, excludes one true-cold launch per side, then runs 10 separate JVM pairs in counterbalanced A/B order. Solver is the sum of the 25 `fullSolve` calls; main includes solver plus in-process harness work; startup is process launch through the child start marker; harness is main minus solver; wall is the complete child process.

Reproduce after compiling both trees with `java -Daoc.data.dir=<data-dir> -cp <current-cp> aoc2022.FreshJvmBenchmark --compare <previous-cp> <current-cp>`. Every published state passes all 50 independent solves, all 25 combined solves, and independent/`fullSolve` equivalence.

Current 25-day means:

| Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
|---:|---:|---:|---:|---:|
| 696.359067 | 640.951271 | 607.483520 | 32.405588 | 33.467751 |

Latest publication gate versus the preceding replay tip:

| Metric | Previous mean (ms) | Current mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|---:|
| wall | 926.257858 | 696.359067 | -229.898792 | [-258.533051, -201.264532] |
| main | 875.277783 | 640.951271 | -234.326512 | [-261.003632, -207.649393] |
| solver | 842.022478 | 607.483520 | -234.538958 | [-260.197797, -208.880119] |
| startup | 30.819879 | 32.405588 | 1.585708 | [-0.858939, 4.030356] |
| harness | 33.255305 | 33.467751 | 0.212446 | [-1.091996, 1.516888] |

## Day 01

Unchanged from the pre-PR implementation. Current solver mean: 9.673629 ms.

## Day 02

Unchanged from the pre-PR implementation. Current solver mean: 6.438592 ms.

## Day 03

Unchanged from the pre-PR implementation. Current solver mean: 3.989267 ms.

## Day 04

Unchanged from the pre-PR implementation. Current solver mean: 2.941475 ms.

## Day 05

Unchanged from the pre-PR implementation. Current solver mean: 2.475846 ms.

## Day 06

Unchanged from the pre-PR implementation. Current solver mean: 3.735583 ms.

## Day 07

Unchanged from the pre-PR implementation. Current solver mean: 5.524821 ms.

## Day 08

Unchanged from the pre-PR implementation. Current solver mean: 12.914275 ms.

## Day 09

Unchanged from the pre-PR implementation. Current solver mean: 9.126792 ms.

## Day 10

Unchanged from the pre-PR implementation. Current solver mean: 0.536867 ms.

## Day 11

Unchanged from the pre-PR implementation. Current solver mean: 20.122479 ms.

## Day 12

Unchanged from the pre-PR implementation. Current solver mean: 31.771621 ms.

## Day 13

Unchanged from the pre-PR implementation. Current solver mean: 16.300738 ms.

## Day 14

Unchanged from the pre-PR implementation. Current solver mean: 13.909641 ms.

## Day 15

Unchanged from the pre-PR implementation. Current solver mean: 2.030175 ms.

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

Unchanged from the pre-PR implementation. Current solver mean: 8.900846 ms.

## Day 19

Unchanged from the pre-PR implementation. Current solver mean: 136.076709 ms.

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

Unchanged from the pre-PR implementation. Current solver mean: 9.041650 ms.

## Day 23

Unchanged from the pre-PR implementation. Current solver mean: 227.289287 ms.

## Day 24

Periodic blizzard occupancy and packed frontier traversal replace repeated hazard simulation and share the three required valley legs.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 702.492042 | — | — |
| Current | 9.913908 | -692.578133 | [-705.041534, -680.114732] |

## Day 25

Unchanged from the pre-PR implementation. Current solver mean: 2.546438 ms.
