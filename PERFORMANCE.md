# Advent of Code 2022 performance

## Methodology and current full suite

All timings use the original personal input corpus with OpenJDK 23.0.1 on Apple silicon. The harness compiles the fixed pre-speed source and current source into separate empty classpaths, excludes one true-cold launch per side, then runs 10 separate JVM pairs in counterbalanced A/B order. Solver is the sum of the 25 `fullSolve` calls; main includes solver plus in-process harness work; startup is process launch through the child start marker; harness is main minus solver; wall is the complete child process.

Reproduce after compiling both trees with `java -Daoc.data.dir=<data-dir> -cp <current-cp> aoc2022.FreshJvmBenchmark --compare <previous-cp> <current-cp>`. Every published state passes all 50 independent solves, all 25 combined solves, and independent/`fullSolve` equivalence.

Current 25-day means:

| Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
|---:|---:|---:|---:|---:|
| 475.636029 | 434.691183 | 403.410209 | 27.908796 | 31.280974 |

Latest publication gate versus the preceding replay tip:

| Metric | Previous mean (ms) | Current mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|---:|
| wall | 661.436496 | 475.636029 | -185.800467 | [-199.101555, -172.499378] |
| main | 615.331388 | 434.691183 | -180.640204 | [-191.296889, -169.983519] |
| solver | 583.620280 | 403.410209 | -180.210070 | [-190.493930, -169.926211] |
| startup | 28.078950 | 27.908796 | -0.170154 | [-2.871031, 2.530723] |
| harness | 31.711108 | 31.280974 | -0.430134 | [-1.521252, 0.660985] |

## Day 01

Unchanged from the pre-PR implementation. Current solver mean: 9.255046 ms.

## Day 02

Unchanged from the pre-PR implementation. Current solver mean: 6.257304 ms.

## Day 03

Unchanged from the pre-PR implementation. Current solver mean: 3.860558 ms.

## Day 04

Unchanged from the pre-PR implementation. Current solver mean: 2.680354 ms.

## Day 05

Unchanged from the pre-PR implementation. Current solver mean: 2.253112 ms.

## Day 06

Unchanged from the pre-PR implementation. Current solver mean: 3.685471 ms.

## Day 07

Unchanged from the pre-PR implementation. Current solver mean: 4.870429 ms.

## Day 08

Unchanged from the pre-PR implementation. Current solver mean: 11.874425 ms.

## Day 09

Unchanged from the pre-PR implementation. Current solver mean: 8.441604 ms.

## Day 10

Unchanged from the pre-PR implementation. Current solver mean: 0.437829 ms.

## Day 11

Unchanged from the pre-PR implementation. Current solver mean: 19.132983 ms.

## Day 12

Unchanged from the pre-PR implementation. Current solver mean: 29.032050 ms.

## Day 13

Unchanged from the pre-PR implementation. Current solver mean: 15.950525 ms.

## Day 14

Unchanged from the pre-PR implementation. Current solver mean: 13.485304 ms.

## Day 15

Unchanged from the pre-PR implementation. Current solver mean: 1.931696 ms.

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

Unchanged from the pre-PR implementation. Current solver mean: 8.631900 ms.

## Day 19

Unchanged from the pre-PR implementation. Current solver mean: 135.003842 ms.

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

Unchanged from the pre-PR implementation. Current solver mean: 8.587096 ms.

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

Unchanged from the pre-PR implementation. Current solver mean: 2.471621 ms.
