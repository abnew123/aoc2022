# Advent of Code 2022 performance

## Methodology and current full suite

The baseline uses the Day 01–25 solver sources from pre-PR commit `c2ea88ffcf3741d775ef773d630e2549931bd82a`. Because that commit predates the benchmark API, those solver sources are compiled unchanged with the current `DayTemplate`, `SolverFactory`, and `FreshJvmBenchmark` as measurement adapters. The default `fullSolve` runs the two original `solve` entry points on fresh solver instances and scanners; optimized days may override it to share work. Both sides use identical benchmark code, JDK, process protocol, and input bytes.

Each comparison launches one uncounted true-cold JVM per side, then 10 baseline/current pairs in alternating order. `wall` is parent-observed process time; `main` is time inside the child JVM; `solver` sums the 25 timed `fullSolve` calls; `startup` ends at the child start marker; and `harness` is `main - solver`. A change is faster only when the paired 95% confidence interval for current-minus-baseline summed solver time is entirely below zero. Answer validation is separate, and the harness also checks independent `solve` calls against `fullSolve`.

To reproduce, compile the baseline Day files with the three current measurement adapters into one classpath, compile the current sources into another, and run:

```sh
java -Daoc.data.dir=/path/to/data -cp current-classes \
  aoc2022.FreshJvmBenchmark --verify
java -Daoc.data.dir=/path/to/data -cp current-classes \
  aoc2022.FreshJvmBenchmark --compare baseline-classes current-classes
```

The pre-PR Day 13 parser expects a terminal blank separator, so the benchmark data copy includes one extra line feed there and supplies those identical bytes to both sides. Times below are milliseconds from one clean run. Uncounted cold solver times were 2189.384 pre-PR and 1762.879 current.

| Metric | Pre-PR mean | Current mean | Delta | Paired 95% CI |
| --- | ---: | ---: | ---: | ---: |
| Wall | 2257.012 | 1831.009 | -426.003 | [-459.263, -392.743] |
| Main | 2214.473 | 1792.670 | -421.802 | [-455.150, -388.454] |
| Solver | 2185.251 | 1764.343 | -420.908 | **[-454.319, -387.497]** |
| Startup | 26.034 | 26.070 | +0.037 | [-1.677, +1.750] |
| Harness | 29.221 | 28.327 | -0.894 | [-1.501, -0.287] |

## Day 01

Unchanged from the pre-PR implementation.

## Day 02

Unchanged from the pre-PR implementation.

## Day 03

Unchanged from the pre-PR implementation.

## Day 04

Unchanged from the pre-PR implementation.

## Day 05

Unchanged from the pre-PR implementation.

## Day 06

Unchanged from the pre-PR implementation.

## Day 07

Unchanged from the pre-PR implementation.

## Day 08

Unchanged from the pre-PR implementation.

## Day 09

Unchanged from the pre-PR implementation.

## Day 10

The pre-PR solver retained every cycle value and returned a personal hardcoded OCR answer for Part 2. The current solver interprets instructions once for `fullSolve`, accumulates signal strength exactly with overflow fallback, renders the 40×6 raster, and decodes known glyphs generically while returning the raster for unknown glyphs.

| Pre-PR mean | Current mean | Delta | Paired 95% CI |
| ---: | ---: | ---: | ---: |
| 0.426 | 0.660 | +0.234 | [+0.204, +0.263] |

## Day 11

Unchanged from the pre-PR implementation.

## Day 12

Unchanged from the pre-PR implementation.

## Day 13

The pre-PR solver reparsed the input for each part and retained, sorted, and reversed every packet for Part 2. The current solver parses each packet once, accumulates ordered-pair indices as pairs arrive, and derives the stable ranks of the two divider packets directly; comparisons use a `long` fast path with `BigInteger` fallback for arbitrary signed integers.

| Pre-PR mean | Current mean | Delta | Paired 95% CI |
| ---: | ---: | ---: | ---: |
| 16.756 | 3.870 | -12.886 | **[-14.522, -11.250]** |

## Day 14

Unchanged from the pre-PR implementation.

## Day 15

Unchanged from the pre-PR implementation.

## Day 16

The pre-PR solver combined mutable all-pairs distances with recursive map caching and an all-pairs mask scan. The current solver parses once, computes BFS distances only among `AA` and positive-flow valves, memoizes Part 1 in dense `(valve, time, mask)` state, and solves Part 2 by recording exact-mask pressure followed by subset-max DP and complementary-mask pairing.

| Pre-PR mean | Current mean | Delta | Paired 95% CI |
| ---: | ---: | ---: | ---: |
| 261.205 | 17.649 | -243.556 | **[-269.906, -217.206]** |

## Day 17

Unchanged from the pre-PR implementation.

## Day 18

Unchanged from the pre-PR implementation.

## Day 19

Unchanged from the pre-PR implementation.

## Day 20

Unchanged from the pre-PR implementation.

## Day 21

Unchanged from the pre-PR implementation.

## Day 22

Unchanged from the pre-PR implementation.

## Day 23

The pre-PR simulation allocated boxed coordinates and proposal maps around a fixed 300×300 grid. The current solver keeps primitive row/column arrays and a dynamically expanding flat stamped grid, updating accepted moves in place without clearing or rebuilding the full grid each round; `fullSolve` parses once and shares the simulation through Part 1's ten rounds.

| Pre-PR mean | Current mean | Delta | Paired 95% CI |
| ---: | ---: | ---: | ---: |
| 222.809 | 38.795 | -184.015 | **[-189.970, -178.059]** |

## Day 24

Unchanged from the pre-PR implementation.

## Day 25

Unchanged from the pre-PR implementation.
