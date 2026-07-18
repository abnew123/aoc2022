# Advent of Code 2022 performance

## Methodology and current full suite

All timings use the original personal input corpus with OpenJDK 23.0.1 on Apple silicon. The harness compiles the fixed pre-speed source and current source into separate empty classpaths, excludes one true-cold launch per side, then runs 10 separate JVM pairs in counterbalanced A/B order. Solver is the sum of the 25 `fullSolve` calls; main includes solver plus in-process harness work; startup is process launch through the child start marker; harness is main minus solver; wall is the complete child process.

Reproduce after compiling both trees with `java -Daoc.data.dir=<data-dir> -cp <current-cp> aoc2022.FreshJvmBenchmark --compare <previous-cp> <current-cp>`. Every published state passes all 50 independent solves, all 25 combined solves, and independent/`fullSolve` equivalence.

Current 25-day means (n=10):

| Wall (ms) | Main (ms) | Solver (ms) | Startup (ms) | Harness (ms) |
|---:|---:|---:|---:|---:|
| 260.321825 | 218.544000 | 184.569366 | 30.660492 | 33.974634 |

Latest publication gate versus the preceding replay tip (n=10):

| Metric | Previous mean (ms) | Current mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|---:|
| wall | 275.818117 | 257.142346 | -18.675771 | [-34.685966, -2.665576] |
| main | 234.623358 | 211.268567 | -23.354792 | [-28.485481, -18.224102] |
| solver | 202.271246 | 178.734321 | -23.536926 | [-28.585316, -18.488535] |
| startup | 28.713712 | 38.143450 | 9.429737 | [-12.025944, 30.885419] |
| harness | 32.352112 | 32.534246 | 0.182134 | [-0.965303, 1.329571] |

## Day 01

A single pass accumulates each inventory and maintains the largest three exact totals instead of retaining and sorting every group.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 10.238506 | — | — |
| Current | 8.526150 | -1.712356 | [-1.996534, -1.428179] |

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

A single sand simulation with the floor tracks the abyss threshold and final blockage instead of rebuilding the cave for each part.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 14.404017 | — | — |
| Current | 4.831075 | -9.572942 | [-9.971650, -9.174233] |

## Day 15

Sensors are parsed once; merged row intervals and exact boundary-line intersections replace repeated scans of the search area.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 2.081554 | — | — |
| Current | 2.672327 | 0.590773 | [0.453341, 0.728204] |

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

Packed cube membership supplies total exposed faces, while one bounded exterior traversal identifies only faces reachable from outside.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 10.037810 | — | — |
| Current | 3.919654 | -6.118156 | [-6.403313, -5.832999] |

## Day 19

A resource-capped memoized search uses admissible geode bounds and dominance pruning instead of exploring redundant robot schedules.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 139.012754 | — | — |
| Current | 20.951042 | -118.061712 | [-121.556491, -114.566933] |

## Day 20

Numbers are parsed once and mixed through linked unrolled blocks for both decryption keys. Stable ID-to-block locations make removal and reinsertion local while block traversal supplies sequence indexes, replacing the pre-PR linear list movement.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 270.777979 | — | — |
| Current | 23.647137 | -247.130842 | [-249.966862, -244.294822] |

## Day 21

One exact expression graph supports numeric evaluation and inverse symbolic solving without reparsing or floating-point arithmetic.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 252.670108 | — | — |
| Current | 6.839296 | -245.830813 | [-258.655057, -233.006568] |

## Day 22

Unchanged from the pre-PR implementation. Current solver mean: 14.684996 ms.

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

One exact SNAFU accumulation converts the final sum directly without retaining all intermediate values.

| Version | Solver mean (ms) | Delta (ms) | Paired 95% CI (ms) |
|---|---:|---:|---:|
| Pre-PR | 3.210615 | — | — |
| Current | 0.315656 | -2.894958 | [-3.069140, -2.720777] |
