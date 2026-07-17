# Advent of Code 2022 performance

## Methodology and current full suite

All timings use the original personal input corpus with OpenJDK 23.0.1 on Apple silicon. The harness compiles the fixed pre-speed source and current source into separate empty classpaths, excludes one true-cold launch per side, then runs 10 separate JVM pairs in counterbalanced A/B order. Solver is the sum of the 25 `fullSolve` calls; main includes solver plus in-process harness work; startup is process launch through the child start marker; harness is main minus solver; wall is the complete child process.

Reproduce after compiling both trees with `java -Daoc.data.dir=<data-dir> -cp <current-cp> aoc2022.FreshJvmBenchmark --compare <pre-pr-cp> <current-cp>`. The current state also passes all 50 independent solves, all 25 combined solves, and independent/`fullSolve` equivalence.

| Metric | Pre-PR mean (ms) | Current mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|---:|
| wall | 2215.079592 | 1514.094688 | -700.984904 | [-750.179648, -651.790160] |
| main | 2171.698850 | 1470.163313 | -701.535537 | [-752.400626, -650.670448] |
| solver | 2139.876113 | 1438.374618 | -701.501495 | [-752.429491, -650.573498] |
| startup | 28.554225 | 28.534617 | -0.019608 | [-1.856064, 1.816847] |
| harness | 31.822737 | 31.788694 | -0.034042 | [-0.913721, 0.845636] |

Publication gate versus the preceding replay tip: summed solver 2139.876113 ms → 1438.374618 ms; delta -701.501495 ms, paired 95% CI [-752.429491, -650.573498] ms.

## Day 01

Unchanged from the pre-PR implementation. Current solver mean: 9.513184 ms.

## Day 02

Unchanged from the pre-PR implementation. Current solver mean: 6.321688 ms.

## Day 03

Unchanged from the pre-PR implementation. Current solver mean: 4.099034 ms.

## Day 04

Unchanged from the pre-PR implementation. Current solver mean: 2.770904 ms.

## Day 05

Unchanged from the pre-PR implementation. Current solver mean: 2.333683 ms.

## Day 06

Unchanged from the pre-PR implementation. Current solver mean: 3.787675 ms.

## Day 07

Unchanged from the pre-PR implementation. Current solver mean: 5.084925 ms.

## Day 08

Unchanged from the pre-PR implementation. Current solver mean: 11.711096 ms.

## Day 09

Unchanged from the pre-PR implementation. Current solver mean: 8.301842 ms.

## Day 10

Unchanged from the pre-PR implementation. Current solver mean: 0.407617 ms.

## Day 11

Unchanged from the pre-PR implementation. Current solver mean: 20.864904 ms.

## Day 12

Unchanged from the pre-PR implementation. Current solver mean: 29.595642 ms.

## Day 13

Unchanged from the pre-PR implementation. Current solver mean: 16.617425 ms.

## Day 14

Unchanged from the pre-PR implementation. Current solver mean: 14.598604 ms.

## Day 15

Unchanged from the pre-PR implementation. Current solver mean: 1.926396 ms.

## Day 16

Unchanged from the pre-PR implementation. Current solver mean: 195.234875 ms.

## Day 17

Unchanged from the pre-PR implementation. Current solver mean: 245.516458 ms.

## Day 18

Unchanged from the pre-PR implementation. Current solver mean: 7.631542 ms.

## Day 19

Unchanged from the pre-PR implementation. Current solver mean: 121.416004 ms.

## Day 20

Unchanged from the pre-PR implementation. Current solver mean: 261.910046 ms.

## Day 21

Unchanged from the pre-PR implementation. Current solver mean: 237.783204 ms.

## Day 22

Unchanged from the pre-PR implementation. Current solver mean: 8.174721 ms.

## Day 23

Unchanged from the pre-PR implementation. Current solver mean: 210.278967 ms.

## Day 24

Periodic blizzard occupancy and packed frontier traversal replace repeated hazard simulation and share the three required valley legs.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 702.492042 | — | — |
| Current | 9.913908 | -692.578133 | [-705.041534, -680.114732] |

## Day 25

Unchanged from the pre-PR implementation. Current solver mean: 2.580275 ms.
