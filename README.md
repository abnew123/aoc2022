# aoc2022

Repo for advent of code 2022 problems. 

To run, go into class MasterSolver.java

Inputs:
* runTimer - if true, will run the timer method to gather information on execution times
* totalTimer - if true, timer will return just the total execution time. Otherwise, will return every day's execution time
* int[] days  - determines which days to solve
* boolean[] parts - determines which parts to solve

To see a given day's solution, go to DayX.java, where X is the day in question. 

[`FreshJvmBenchmark`](src/aoc2022/FreshJvmBenchmark.java) is the reproducible answer-consistency and fresh-process benchmark runner. On a 2024 MacBook Pro using OpenJDK 23.0.1, the latest clean counterbalanced current-source 10-process means are 290.869 ms wall time, 251.811 ms in child `main`, and 219.933 ms in the 25 `fullSolve` calls. See [performance notes](PERFORMANCE.md) for every sample, metric definitions, recovery details, and the paired optimization comparisons.

A clean counterbalanced cumulative comparison against pristine commit `a107970` measured the 25-day solver mean falling from 226.621ms to 210.329ms (-7.19%), with a paired 95% confidence interval of [-20.596ms, -11.988ms]. The paired process-wall interval also excluded zero.

From the repository root, compile for the project's Java 16 target, verify all answers, and run one cold process plus 10 measured fresh JVM processes with:

```sh
rm -rf /tmp/aoc2022-classes
mkdir -p /tmp/aoc2022-classes
javac --release 16 -d /tmp/aoc2022-classes $(git ls-files '*.java')
java -cp /tmp/aoc2022-classes aoc2022.FreshJvmBenchmark --verify
java -cp /tmp/aoc2022-classes aoc2022.FreshJvmBenchmark
```

The runner checks that 50 individual `solve` results equal the corresponding 25 two-answer `fullSolve` results; it does not independently know the expected puzzle answers. The ordered checksum is `5f79ad374b42c8a37382972ee3158f645c2260d01e08f33e59c12cfb9f60932b`, unchanged from the pristine known-good `a107970` pre-change snapshot. Correctness evidence also includes byte-identical before/after records for all 50 answers, the official Day 18 sample, and translated-coordinate regressions.

Day 18 now computes both surface areas from one parse with dynamically checked padded bounds, flat primitive occupancy, and one exterior flood fill. It accepts signed coordinates whose one-cell-padded volume is representable by the flat arrays, and explicitly rejects coordinates outside those arithmetic or indexing limits rather than wrapping; the official sample remains 64/58 and all 50 repository answers are unchanged.

Day 17 now obtains both answers from one simulation, using a detected cycle first to approach the 2,022-rock checkpoint without passing it and then to approach one trillion rocks. A counterbalanced isolated fresh-process comparison reduced its mean from 12.758ms to 9.481ms (-25.7%), with a paired 95% confidence interval of [-3.57ms, -2.98ms]; the whole-suite comparison remained inconclusive under interactive load.

Day 3 now represents item sets as 52-bit masks and computes both priority totals in one input pass. A clean recovery after removal of leaked background JVMs measured its isolated mean falling from 7.337ms to 3.920ms (-46.6%), with a paired 95% confidence interval of [-3.603ms, -3.231ms]; the clean whole-suite paired interval remained inconclusive.

Day 1 now computes both calorie answers from one exact-number pass while retaining only the top three elf totals. A clean recovery after removal of leaked background JVMs measured its isolated mean falling from 8.714ms to 7.310ms (-16.1%), with a paired 95% confidence interval of [-1.603ms, -1.204ms]; the clean whole-suite paired interval remained inconclusive.

Day 4 now parses each assignment pair once with exact section IDs and computes containment plus overlap together. A clean recovery after removal of leaked background JVMs measured its isolated mean falling from 10.068ms to 7.600ms (-24.5%), with a paired 95% confidence interval of [-2.871ms, -2.066ms]; the clean whole-suite paired interval remained inconclusive.

Day 9 now counts both tails in one ten-knot simulation and stores full signed coordinates in primitive hash sets instead of fixed offset grids. Its isolated mean fell from 13.366ms to 10.185ms (-23.8%), with a paired 95% confidence interval of [-3.34ms, -3.02ms]; it also accepts the 5,000-step straight path that exceeded the old bounds.

Day 6 now finds both distinct-character markers in one last-seen scan, eliminating per-window boxed sets and the duplicate combined-solve scan. Its isolated mean fell from 8.755ms to 3.846ms (-56.1%), with a paired 95% confidence interval of [-5.204ms, -4.613ms]; the whole-suite comparison remained inconclusive under interactive load.

Day 8 now parses the tree grid once for the combined solve and computes visibility and scenic scores in one grid traversal. Its isolated mean fell from 6.342ms to 4.070ms (-35.8%), with a paired 95% confidence interval of [-2.511ms, -2.033ms]; the whole-suite paired interval remained inconclusive under interactive load.

Day 23 now detects grid-boundary expansion while applying successful moves instead of rescanning every elf after every moving round. Its isolated mean fell from 45.899ms to 37.700ms (-17.9%), with a paired 95% confidence interval of [-10.675ms, -5.724ms]; the whole-suite paired interval also excluded zero.

Day 20 now parses the number list once, allocates exact treap storage, and precomputes modular rotations while retaining arbitrary-size integers. Its isolated mean fell from 52.521ms to 48.688ms (-7.3%), with a paired 95% confidence interval of [-4.437ms, -3.229ms]; the whole-suite paired interval remained inconclusive under interactive load.

Day 14 now parses and builds the cave once, snapshots part 1 during the floor-enabled simulation, and continues the same run for part 2. Its isolated mean fell from 14.214ms to 10.652ms (-25.1%), with a paired 95% confidence interval of [-3.976ms, -3.149ms]. The reachable sand cone also bounds storage and rock drawing, so valid far-away rock segments no longer inflate the dense grid.

The table below is the historical July warm 10-run per-part table from `DayTemplate.timer`. It is retained for solver-level context, but it is not directly comparable to the fresh-JVM numbers above.

| Day | Problem | Solution | Part 1 (ms) | Part 2 (ms) |
| --- | --- | --- |------------:|------------:|
| 1 | [Calorie Counting](https://adventofcode.com/2022/day/1) | [Source](src/aoc2022/Day01.java) | 1.835 | 1.843 |
| 2 | [Rock Paper Scissors](https://adventofcode.com/2022/day/2) | [Source](src/aoc2022/Day02.java) | 1.597 | 1.593 |
| 3 | [Rucksack Reorganization](https://adventofcode.com/2022/day/3) | [Source](src/aoc2022/Day03.java) | 1.394 | 1.403 |
| 4 | [Camp Cleanup](https://adventofcode.com/2022/day/4) | [Source](src/aoc2022/Day04.java) | 2.129 | 1.991 |
| 5 | [Supply Stacks](https://adventofcode.com/2022/day/5) | [Source](src/aoc2022/Day05.java) | 1.554 | 1.586 |
| 6 | [Tuning Trouble](https://adventofcode.com/2022/day/6) | [Source](src/aoc2022/Day06.java) | 1.355 | 2.123 |
| 7 | [No Space Left On Device](https://adventofcode.com/2022/day/7) | [Source](src/aoc2022/Day07.java) | 2.005 | 2.162 |
| 8 | [Treetop Tree House](https://adventofcode.com/2022/day/8) | [Source](src/aoc2022/Day08.java) | 1.355 | 1.714 |
| 9 | [Rope Bridge](https://adventofcode.com/2022/day/9) | [Source](src/aoc2022/Day09.java) | 2.378 | 6.588 |
| 10 | [Cathode-Ray Tube](https://adventofcode.com/2022/day/10) | [Source](src/aoc2022/Day10.java) | 0.787 | 0.777 |
| 11 | [Monkey in the Middle](https://adventofcode.com/2022/day/11) | [Source](src/aoc2022/Day11.java) | 0.986 | 2.648 |
| 12 | [Hill Climbing Algorithm](https://adventofcode.com/2022/day/12) | [Source](src/aoc2022/Day12.java) | 1.255 | 1.306 |
| 13 | [Distress Signal](https://adventofcode.com/2022/day/13) | [Source](src/aoc2022/Day13.java) | 1.697 | 1.817 |
| 14 | [Regolith Reservoir](https://adventofcode.com/2022/day/14) | [Source](src/aoc2022/Day14.java) | 2.206 | 2.713 |
| 15 | [Beacon Exclusion Zone](https://adventofcode.com/2022/day/15) | [Source](src/aoc2022/Day15.java) | 1.089 | 1.145 |
| 16 | [Proboscidea Volcanium](https://adventofcode.com/2022/day/16) | [Source](src/aoc2022/Day16.java) | 7.275 | 3.414 |
| 17 | [Pyroclastic Flow](https://adventofcode.com/2022/day/17) | [Source](src/aoc2022/Day17.java) | 2.729 | 2.790 |
| 18 | [Boiling Boulders](https://adventofcode.com/2022/day/18) | [Source](src/aoc2022/Day18.java) | 3.520 | 3.743 |
| 19 | [Not Enough Minerals](https://adventofcode.com/2022/day/19) | [Source](src/aoc2022/Day19.java) | 7.140 | 7.702 |
| 20 | [Grove Positioning System](https://adventofcode.com/2022/day/20) | [Source](src/aoc2022/Day20.java) | 5.741 | 32.145 |
| 21 | [Monkey Math](https://adventofcode.com/2022/day/21) | [Source](src/aoc2022/Day21.java) | 3.364 | 4.817 |
| 22 | [Monkey Map](https://adventofcode.com/2022/day/22) | [Source](src/aoc2022/Day22.java) | 5.053 | 4.466 |
| 23 | [Unstable Diffusion](https://adventofcode.com/2022/day/23) | [Source](src/aoc2022/Day23.java) | 2.656 | 25.245 |
| 24 | [Blizzard Basin](https://adventofcode.com/2022/day/24) | [Source](src/aoc2022/Day24.java) | 6.860 | 10.805 |
| 25 | [Full of Hot Air](https://adventofcode.com/2022/day/25) | [Source](src/aoc2022/Day25.java) | 1.109 | 1.079 |
