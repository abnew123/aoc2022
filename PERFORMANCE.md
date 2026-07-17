# Advent of Code 2022 performance

## Methodology and current full suite

All timings use the original personal input corpus with OpenJDK 23.0.1 on Apple silicon. The harness compiles the fixed pre-speed source and current source into separate empty classpaths, excludes one true-cold launch per side, then runs 10 separate JVM pairs in counterbalanced A/B order. Solver is the sum of the 25 `fullSolve` calls; main includes solver plus in-process harness work; startup is process launch through the child start marker; harness is main minus solver; wall is the complete child process.

Reproduce after compiling both trees with `java -Daoc.data.dir=<data-dir> -cp <current-cp> aoc2022.FreshJvmBenchmark --compare <previous-cp> <current-cp>`. Every published state passes all 50 independent solves, all 25 combined solves, and independent/`fullSolve` equivalence.

Current 25-day means:

| Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
|---:|---:|---:|---:|---:|
| 937.862388 | 882.164904 | 848.084658 | 33.162904 | 34.080246 |

Latest publication gate versus the preceding replay tip:

| Metric | Previous mean (ms) | Current mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|---:|
| wall | 1166.624654 | 937.862388 | -228.762267 | [-246.087998, -211.436535] |
| main | 1111.511963 | 882.164904 | -229.347059 | [-246.192188, -212.501930] |
| solver | 1078.476539 | 848.084658 | -230.391881 | [-247.092444, -213.691319] |
| startup | 33.105663 | 33.162904 | 0.057242 | [-4.300247, 4.414730] |
| harness | 33.035424 | 34.080246 | 1.044823 | [0.314770, 1.774875] |

## Day 01

Unchanged from the pre-PR implementation. Current solver mean: 9.739688 ms.

## Day 02

Unchanged from the pre-PR implementation. Current solver mean: 6.369429 ms.

## Day 03

Unchanged from the pre-PR implementation. Current solver mean: 4.051646 ms.

## Day 04

Unchanged from the pre-PR implementation. Current solver mean: 2.871683 ms.

## Day 05

Unchanged from the pre-PR implementation. Current solver mean: 2.368521 ms.

## Day 06

Unchanged from the pre-PR implementation. Current solver mean: 3.734933 ms.

## Day 07

Unchanged from the pre-PR implementation. Current solver mean: 5.099150 ms.

## Day 08

Unchanged from the pre-PR implementation. Current solver mean: 11.778175 ms.

## Day 09

Unchanged from the pre-PR implementation. Current solver mean: 7.698654 ms.

## Day 10

Unchanged from the pre-PR implementation. Current solver mean: 0.477488 ms.

## Day 11

Unchanged from the pre-PR implementation. Current solver mean: 18.413217 ms.

## Day 12

Unchanged from the pre-PR implementation. Current solver mean: 29.260267 ms.

## Day 13

Unchanged from the pre-PR implementation. Current solver mean: 16.214700 ms.

## Day 14

Unchanged from the pre-PR implementation. Current solver mean: 14.504067 ms.

## Day 15

Unchanged from the pre-PR implementation. Current solver mean: 2.080929 ms.

## Day 16

Useful valves are compressed into a bitmask search with memoized pressure states and direct disjoint-mask pairing for the elephant.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 231.947104 | — | — |
| Current | 16.801325 | -215.145780 | [-250.301082, -179.990477] |

## Day 17

Unchanged from the pre-PR implementation. Current solver mean: 253.923396 ms.

## Day 18

Unchanged from the pre-PR implementation. Current solver mean: 7.941800 ms.

## Day 19

Unchanged from the pre-PR implementation. Current solver mean: 135.663671 ms.

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

Unchanged from the pre-PR implementation. Current solver mean: 8.876133 ms.

## Day 23

Unchanged from the pre-PR implementation. Current solver mean: 226.742904 ms.

## Day 24

Periodic blizzard occupancy and packed frontier traversal replace repeated hazard simulation and share the three required valley legs.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 702.492042 | — | — |
| Current | 9.913908 | -692.578133 | [-705.041534, -680.114732] |

## Day 25

Unchanged from the pre-PR implementation. Current solver mean: 2.569042 ms.
