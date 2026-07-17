# Advent of Code 2022 performance

## Methodology and current full suite

The baseline is the normal Java solver source at pre-speed commit c2ea88ffcf3741d775ef773d630e2549931bd82a, compiled with the current measurement adapters. The current revision uses the same adapters, JDK, input bytes, and process protocol. Each comparison used one excluded cold JVM per revision followed by 10 separate JVM pairs in alternating order. Values are means in milliseconds; confidence intervals are paired two-sided 95% intervals for current minus baseline.

Solver sums the 25 fullSolve calls. Startup ends at the child start marker, harness is main minus solver, and wall is parent-observed process lifetime. Verification reran all 50 independent answers, all 25 combined solves, independent/fullSolve equivalence, and the frozen trusted answer record. The excluded cold solver values were 1828.972 ms baseline and 196.885 ms current.

| Metric | Pre-PR | Current | Delta | Paired 95% CI |
| --- | ---: | ---: | ---: | ---: |
| Wall | 1958.195 | 285.664 | -1672.532 | [-1689.271, -1655.793] |
| Main | 1909.136 | 243.787 | -1665.349 | [-1682.491, -1648.207] |
| Solver | 1877.792 | 212.294 | -1665.498 | [-1682.735, -1648.260] |
| Startup | 28.523 | 28.621 | +0.098 | [-3.286, +3.482] |
| Harness | 31.344 | 31.492 | +0.149 | [-0.724, +1.022] |

The publication gate compared the previous replacement tip e8c68c3 with this replay. Solver time fell from 1797.919 ms to 198.085 ms, a -1599.834 ms paired delta with 95% CI [-1622.199, -1577.470].

Reproduce on an idle machine by compiling separate baseline and current classpaths, then run aoc2022.FreshJvmBenchmark --verify and aoc2022.FreshJvmBenchmark --compare BASELINE_CP CURRENT_CP with -Daoc.data.dir pointing to the 25 input files.

## Day 01

A single pass accumulates each inventory and maintains the largest three exact totals instead of retaining and sorting every group.

| Pre-PR | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 9.191 | 7.955 | -1.236 | [-1.635, -0.837] |

## Day 02

Direct token decoding and score tables replace repeated splitting and branching while preserving exact total promotion.

| Pre-PR | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 6.359 | 2.405 | -3.954 | [-4.172, -3.736] |

## Day 03

Both parts share one pass of compact 52-bit item masks instead of rebuilding sets and rescanning compartments and groups.

| Pre-PR | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 3.608 | 1.510 | -2.098 | [-2.385, -1.810] |

## Day 04

Each assignment pair is parsed once and tested directly for containment and overlap with exact numeric fallback.

| Pre-PR | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 2.720 | 1.704 | -1.016 | [-1.215, -0.817] |

## Day 05

The stack drawing and moves are parsed once, then the two crane models advance together over independent compact stack state.

| Pre-PR | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 2.242 | 1.409 | -0.833 | [-0.957, -0.709] |

## Day 06

One marker scan maintains the necessary recent-character state for both requested window sizes instead of searching twice.

| Pre-PR | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 3.736 | 0.814 | -2.922 | [-3.138, -2.706] |

## Day 07

One transcript parse accumulates exact directory sizes through ancestor links and derives both thresholds from that shared model.

| Pre-PR | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 4.654 | 1.560 | -3.093 | [-3.250, -2.936] |

## Day 08

A shared primitive grid analysis computes visibility and scenic distances for both parts without rebuilding rows, columns, or trees.

| Pre-PR | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 12.162 | 1.625 | -10.537 | [-10.892, -10.182] |

## Day 09

One ten-knot simulation records visits for knot 1 and knot 9 together using unbounded packed coordinates.

| Pre-PR | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 7.679 | 5.714 | -1.966 | [-2.656, -1.275] |

## Day 10

One instruction interpretation computes signal strength and the 40×6 raster together; generic OCR replaces the personal hardcoded display answer.

| Pre-PR | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 0.668 | 0.750 | +0.082 | [-0.006, +0.170] |

## Day 11

Primitive monkey queues and shared parsed operations replace boxed queue churn while preserving the two required worry simulations.

| Pre-PR | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 20.560 | 6.498 | -14.062 | [-15.345, -12.778] |

## Day 12

One reverse breadth-first traversal from the endpoint supplies both the start distance and the best elevation-zero distance.

| Pre-PR | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 32.188 | 1.363 | -30.825 | [-36.300, -25.350] |

## Day 13

Packets are parsed once; pair ordering is accumulated while reading and divider ranks are derived directly without sorting every packet.

| Pre-PR | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 3.796 | 3.536 | -0.260 | [-0.650, +0.129] |

## Day 14

A single sand simulation with the floor tracks the abyss threshold and final blockage instead of rebuilding the cave for each part.

| Pre-PR | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 14.620 | 5.446 | -9.174 | [-10.325, -8.023] |

## Day 15

Sensors are parsed once; merged row intervals and exact boundary-line intersections replace repeated scans of the search area.

| Pre-PR | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 1.834 | 2.427 | +0.593 | [+0.375, +0.810] |

## Day 16

Useful valves are compressed into a bitmask search with memoized pressure states and direct disjoint-mask pairing for the elephant.

| Pre-PR | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 16.511 | 14.938 | -1.574 | [-2.226, -0.921] |

## Day 17

One compact rock simulation detects a verified cycle and shares checkpoints for 2,022 and 1,000,000,000,000 rocks.

| Pre-PR | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 260.850 | 4.449 | -256.401 | [-273.522, -239.280] |

## Day 18

Packed cube membership supplies total exposed faces, while one bounded exterior traversal identifies only faces reachable from outside.

| Pre-PR | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 8.881 | 3.787 | -5.094 | [-5.404, -4.784] |

## Day 19

A resource-capped memoized search uses admissible geode bounds and dominance pruning instead of exploring redundant robot schedules.

| Pre-PR | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 133.003 | 22.533 | -110.470 | [-113.236, -107.704] |

## Day 20

Numbers are parsed once and mixed through indexed treap storage for both decryption keys instead of repeated linear list movement.

| Pre-PR | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 267.452 | 48.407 | -219.045 | [-224.435, -213.655] |

## Day 21

One exact expression graph supports numeric evaluation and inverse symbolic solving without reparsing or floating-point arithmetic.

| Pre-PR | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 245.881 | 4.332 | -241.549 | [-245.160, -237.938] |

## Day 22

Unchanged from the pre-PR implementation. Current solver mean: 14.149 ms.

## Day 23

Packed elf positions and move counts detect expansion and stabilization directly, avoiding repeated full-boundary rescans.

| Pre-PR | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 35.428 | 44.505 | +9.077 | [+1.498, +16.656] |

## Day 24

Periodic blizzard occupancy and packed frontier traversal replace repeated hazard simulation and share the three required valley legs.

| Pre-PR | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 767.709 | 10.182 | -757.528 | [-765.230, -749.826] |

## Day 25

One exact SNAFU accumulation converts the final sum directly without retaining all intermediate values.

| Pre-PR | Current | Delta | 95% CI |
| ---: | ---: | ---: | ---: |
| 2.874 | 0.296 | -2.578 | [-2.707, -2.449] |
