# aoc2022

Repo for advent of code 2022 problems. 

To run, go into class MasterSolver.java

Inputs:
* runTimer - if true, will run the timer method to gather information on execution times
* totalTimer - if true, timer will return just the total execution time. Otherwise, will return every day's execution time
* exclusionTimer - if true, will skip days where answer cannot be displayed simply. if false, will run all days regardless (not recommended)
* int[] days  - determines which days to solve
* boolean[] parts - determines which parts to solve

To see a given day's solution, go to DayX.java, where X is the day in question. 

Current one-pass timing for `MasterSolver` is roughly 234ms on a 2024 MacBook Pro. The table below shows warm 10-run averages per part using the existing `DayTemplate.timer` convention; those numbers are useful for comparing individual solver changes, but they understate the first-run cost someone sees when running all 50 parts once.

See [performance notes](PERFORMANCE.md) for visual before/after examples and benchmark caveats.

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
| 19 | [Not Enough Minerals](https://adventofcode.com/2022/day/19) | [Source](src/aoc2022/Day19.java) | 6.983 | 9.972 |
| 20 | [Grove Positioning System](https://adventofcode.com/2022/day/20) | [Source](src/aoc2022/Day20.java) | 5.741 | 32.145 |
| 21 | [Monkey Math](https://adventofcode.com/2022/day/21) | [Source](src/aoc2022/Day21.java) | 3.364 | 4.817 |
| 22 | [Monkey Map](https://adventofcode.com/2022/day/22) | [Source](src/aoc2022/Day22.java) | 5.053 | 4.466 |
| 23 | [Unstable Diffusion](https://adventofcode.com/2022/day/23) | [Source](src/aoc2022/Day23.java) | 2.656 | 25.245 |
| 24 | [Blizzard Basin](https://adventofcode.com/2022/day/24) | [Source](src/aoc2022/Day24.java) | 6.860 | 10.805 |
| 25 | [Full of Hot Air](https://adventofcode.com/2022/day/25) | [Source](src/aoc2022/Day25.java) | 1.109 | 1.079 |
