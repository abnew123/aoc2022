# Advent of Code 2022 performance

## Methodology and current full suite

All timings use the original personal input corpus with OpenJDK 23.0.1 on Apple silicon. The harness compiles the fixed pre-speed source and current source into separate empty classpaths, excludes one true-cold launch per side, then runs 10 separate JVM pairs in counterbalanced A/B order. Solver is the sum of the 25 `fullSolve` calls; main includes solver plus in-process harness work; startup is process launch through the child start marker; harness is main minus solver; wall is the complete child process.

Reproduce after compiling both trees with `java -Daoc.data.dir=<data-dir> -cp <current-cp> aoc2022.FreshJvmBenchmark --compare <previous-cp> <current-cp>`. Every published state passes all 50 independent solves, all 25 combined solves, and independent/`fullSolve` equivalence.

Current 25-day means:

| Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
|---:|---:|---:|---:|---:|
| 1355.041500 | 1306.904021 | 1273.913183 | 29.120979 | 32.990838 |

Latest publication gate versus the preceding replay tip:

| Metric | Previous mean (ms) | Current mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|---:|
| wall | 1634.394342 | 1355.041500 | -279.352842 | [-355.353662, -203.352022] |
| main | 1580.014304 | 1306.904021 | -273.110283 | [-347.738845, -198.481722] |
| solver | 1546.139729 | 1273.913183 | -272.226546 | [-346.640815, -197.812277] |
| startup | 31.854629 | 29.120979 | -2.733650 | [-6.745360, 1.278060] |
| harness | 33.874575 | 32.990838 | -0.883737 | [-1.979565, 0.212091] |

## Day 01

Unchanged from the pre-PR implementation. Current solver mean: 9.598588 ms.

## Day 02

Unchanged from the pre-PR implementation. Current solver mean: 6.300758 ms.

## Day 03

Unchanged from the pre-PR implementation. Current solver mean: 3.641533 ms.

## Day 04

Unchanged from the pre-PR implementation. Current solver mean: 2.817225 ms.

## Day 05

Unchanged from the pre-PR implementation. Current solver mean: 2.368396 ms.

## Day 06

Unchanged from the pre-PR implementation. Current solver mean: 3.756887 ms.

## Day 07

Unchanged from the pre-PR implementation. Current solver mean: 5.187812 ms.

## Day 08

Unchanged from the pre-PR implementation. Current solver mean: 12.730280 ms.

## Day 09

Unchanged from the pre-PR implementation. Current solver mean: 11.180600 ms.

## Day 10

Unchanged from the pre-PR implementation. Current solver mean: 0.462833 ms.

## Day 11

Unchanged from the pre-PR implementation. Current solver mean: 21.178971 ms.

## Day 12

Unchanged from the pre-PR implementation. Current solver mean: 28.577146 ms.

## Day 13

Unchanged from the pre-PR implementation. Current solver mean: 15.571071 ms.

## Day 14

Unchanged from the pre-PR implementation. Current solver mean: 13.785929 ms.

## Day 15

Unchanged from the pre-PR implementation. Current solver mean: 1.835929 ms.

## Day 16

Unchanged from the pre-PR implementation. Current solver mean: 224.739642 ms.

## Day 17

Unchanged from the pre-PR implementation. Current solver mean: 254.272837 ms.

## Day 18

Unchanged from the pre-PR implementation. Current solver mean: 8.209921 ms.

## Day 19

Unchanged from the pre-PR implementation. Current solver mean: 131.419229 ms.

## Day 20

Unchanged from the pre-PR implementation. Current solver mean: 268.968371 ms.

## Day 21

One exact expression graph supports numeric evaluation and inverse symbolic solving without reparsing or floating-point arithmetic.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 252.670108 | — | — |
| Current | 6.839296 | -245.830813 | [-258.655057, -233.006568] |

## Day 22

Unchanged from the pre-PR implementation. Current solver mean: 8.968121 ms.

## Day 23

Unchanged from the pre-PR implementation. Current solver mean: 218.904367 ms.

## Day 24

Periodic blizzard occupancy and packed frontier traversal replace repeated hazard simulation and share the three required valley legs.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 702.492042 | — | — |
| Current | 9.913908 | -692.578133 | [-705.041534, -680.114732] |

## Day 25

Unchanged from the pre-PR implementation. Current solver mean: 2.481067 ms.
