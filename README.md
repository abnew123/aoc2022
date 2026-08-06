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

## Performance

Current 25-day timing from 100 randomized-order fresh-JVM pairs; values are arithmetic means in milliseconds.

| Wall | Main | Solver | Startup | Harness |
|---:|---:|---:|---:|---:|
| 160.410 | 130.855 | 101.646 | 20.965 | 29.209 |

| Days | 01 | 02 | 03 | 04 | 05 |
|---|---:|---:|---:|---:|---:|
| 01–05 | 1.697240 | 5.698855 | 1.365998 | 3.190119 | 1.350722 |
| 06–10 | 1.041071 | 2.025813 | 1.718041 | 5.629668 | 0.779058 |
| 11–15 | 6.105633 | 1.433566 | 4.425127 | 1.575866 | 2.442435 |
| 16–20 | 9.125765 | 4.352457 | 3.776764 | 1.853062 | 11.375177 |
| 21–25 | 4.933586 | 6.219048 | 16.547287 | 2.603782 | 0.380285 |

<!-- CHAR COUNTS BEGIN -->

## Character counts

The table below counts non-whitespace characters in each normal solution file and its golfed sibling. The golfed files are intentionally separate from the readable/performance-oriented `DayXX.java` files so the PR tracks can merge independently. Every golfed solver receives the same neutral `String[]` of logical input lines from `MasterSolver`; input parsing and all puzzle logic remain in its `DayXXGolfed.java` file.

| Day | Solution | Golfed solution | Source chars | Golfed chars |
| --- | --- | --- |-------------:|-------------:|
| 1 | [Source](src/aoc2022/Day01.java) | [Golfed](src/aoc2022/Day01Golfed.java) | 1,538 | 222 |
| 2 | [Source](src/aoc2022/Day02.java) | [Golfed](src/aoc2022/Day02Golfed.java) | 2,345 | 142 |
| 3 | [Source](src/aoc2022/Day03.java) | [Golfed](src/aoc2022/Day03Golfed.java) | 2,170 | 264 |
| 4 | [Source](src/aoc2022/Day04.java) | [Golfed](src/aoc2022/Day04Golfed.java) | 1,649 | 227 |
| 5 | [Source](src/aoc2022/Day05.java) | [Golfed](src/aoc2022/Day05Golfed.java) | 5,330 | 456 |
| 6 | [Source](src/aoc2022/Day06.java) | [Golfed](src/aoc2022/Day06Golfed.java) | 852 | 137 |
| 7 | [Source](src/aoc2022/Day07.java) | [Golfed](src/aoc2022/Day07Golfed.java) | 4,576 | 332 |
| 8 | [Source](src/aoc2022/Day08.java) | [Golfed](src/aoc2022/Day08Golfed.java) | 2,510 | 334 |
| 9 | [Source](src/aoc2022/Day09.java) | [Golfed](src/aoc2022/Day09Golfed.java) | 3,402 | 387 |
| 10 | [Source](src/aoc2022/Day10.java) | [Golfed](src/aoc2022/Day10Golfed.java) | 5,481 | 424 |
| 11 | [Source](src/aoc2022/Day11.java) | [Golfed](src/aoc2022/Day11Golfed.java) | 2,469 | 493 |
| 12 | [Source](src/aoc2022/Day12.java) | [Golfed](src/aoc2022/Day12Golfed.java) | 2,820 | 377 |
| 13 | [Source](src/aoc2022/Day13.java) | [Golfed](src/aoc2022/Day13Golfed.java) | 4,116 | 466 |
| 14 | [Source](src/aoc2022/Day14.java) | [Golfed](src/aoc2022/Day14Golfed.java) | 5,343 | 479 |
| 15 | [Source](src/aoc2022/Day15.java) | [Golfed](src/aoc2022/Day15Golfed.java) | 5,904 | 542 |
| 16 | [Source](src/aoc2022/Day16.java) | [Golfed](src/aoc2022/Day16Golfed.java) | 5,784 | 550 |
| 17 | [Source](src/aoc2022/Day17.java) | [Golfed](src/aoc2022/Day17Golfed.java) | 4,675 | 654 |
| 18 | [Source](src/aoc2022/Day18.java) | [Golfed](src/aoc2022/Day18Golfed.java) | 5,610 | 443 |
| 19 | [Source](src/aoc2022/Day19.java) | [Golfed](src/aoc2022/Day19Golfed.java) | 5,988 | 492 |
| 20 | [Source](src/aoc2022/Day20.java) | [Golfed](src/aoc2022/Day20Golfed.java) | 7,426 | 377 |
| 21 | [Source](src/aoc2022/Day21.java) | [Golfed](src/aoc2022/Day21Golfed.java) | 5,468 | 478 |
| 22 | [Source](src/aoc2022/Day22.java) | [Golfed](src/aoc2022/Day22Golfed.java) | 9,510 | 562 |
| 23 | [Source](src/aoc2022/Day23.java) | [Golfed](src/aoc2022/Day23Golfed.java) | 7,990 | 582 |
| 24 | [Source](src/aoc2022/Day24.java) | [Golfed](src/aoc2022/Day24Golfed.java) | 6,430 | 500 |
| 25 | [Source](src/aoc2022/Day25.java) | [Golfed](src/aoc2022/Day25Golfed.java) | 1,602 | 210 |
| Total |  |  | 110,988 | 10,130 |

<!-- CHAR COUNTS END -->
