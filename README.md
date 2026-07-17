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

Current 25-day timing from 10 separate fresh JVMs; values are arithmetic means in milliseconds.

| Wall | Main | Solver | Startup | Harness |
|---:|---:|---:|---:|---:|
| 1514.094688 | 1470.163313 | 1438.374618 | 28.534617 | 31.788694 |

| Days | 01 | 02 | 03 | 04 | 05 |
|---|---:|---:|---:|---:|---:|
| 01–05 | 9.513184 | 6.321688 | 4.099034 | 2.770904 | 2.333683 |
| 06–10 | 3.787675 | 5.084925 | 11.711096 | 8.301842 | 0.407617 |
| 11–15 | 20.864904 | 29.595642 | 16.617425 | 14.598604 | 1.926396 |
| 16–20 | 195.234875 | 245.516458 | 7.631542 | 121.416004 | 261.910046 |
| 21–25 | 237.783204 | 8.174721 | 210.278967 | 9.913908 | 2.580275 |

<!-- CHAR COUNTS BEGIN -->

## Character counts

The table below counts non-whitespace characters in each normal solution file and its golfed sibling. The golfed files are intentionally separate from the readable/performance-oriented `DayXX.java` files so the PR tracks can merge independently.

| Day | Solution | Golfed solution | Source chars | Golfed chars |
| --- | --- | --- |-------------:|-------------:|
| 1 | [Source](src/aoc2022/Day01.java) | [Golfed](src/aoc2022/Day01Golfed.java) | 569 | 201 |
| 2 | [Source](src/aoc2022/Day02.java) | [Golfed](src/aoc2022/Day02Golfed.java) | 709 | 171 |
| 3 | [Source](src/aoc2022/Day03.java) | [Golfed](src/aoc2022/Day03Golfed.java) | 1,406 | 350 |
| 4 | [Source](src/aoc2022/Day04.java) | [Golfed](src/aoc2022/Day04Golfed.java) | 672 | 198 |
| 5 | [Source](src/aoc2022/Day05.java) | [Golfed](src/aoc2022/Day05Golfed.java) | 1,305 | 508 |
| 6 | [Source](src/aoc2022/Day06.java) | [Golfed](src/aoc2022/Day06Golfed.java) | 488 | 170 |
| 7 | [Source](src/aoc2022/Day07.java) | [Golfed](src/aoc2022/Day07Golfed.java) | 1,672 | 399 |
| 8 | [Source](src/aoc2022/Day08.java) | [Golfed](src/aoc2022/Day08Golfed.java) | 2,248 | 369 |
| 9 | [Source](src/aoc2022/Day09.java) | [Golfed](src/aoc2022/Day09Golfed.java) | 1,212 | 389 |
| 10 | [Source](src/aoc2022/Day10.java) | [Golfed](src/aoc2022/Day10Golfed.java) | 1,012 | 557 |
| 11 | [Source](src/aoc2022/Day11.java) | [Golfed](src/aoc2022/Day11Golfed.java) | 1,851 | 741 |
| 12 | [Source](src/aoc2022/Day12.java) | [Golfed](src/aoc2022/Day12Golfed.java) | 1,835 | 526 |
| 13 | [Source](src/aoc2022/Day13.java) | [Golfed](src/aoc2022/Day13Golfed.java) | 1,780 | 842 |
| 14 | [Source](src/aoc2022/Day14.java) | [Golfed](src/aoc2022/Day14Golfed.java) | 1,165 | 716 |
| 15 | [Source](src/aoc2022/Day15.java) | [Golfed](src/aoc2022/Day15Golfed.java) | 2,759 | 871 |
| 16 | [Source](src/aoc2022/Day16.java) | [Golfed](src/aoc2022/Day16Golfed.java) | 2,469 | 968 |
| 17 | [Source](src/aoc2022/Day17.java) | [Golfed](src/aoc2022/Day17Golfed.java) | 3,549 | 1,164 |
| 18 | [Source](src/aoc2022/Day18.java) | [Golfed](src/aoc2022/Day18Golfed.java) | 1,377 | 679 |
| 19 | [Source](src/aoc2022/Day19.java) | [Golfed](src/aoc2022/Day19Golfed.java) | 2,934 | 1,091 |
| 20 | [Source](src/aoc2022/Day20.java) | [Golfed](src/aoc2022/Day20Golfed.java) | 1,029 | 419 |
| 21 | [Source](src/aoc2022/Day21.java) | [Golfed](src/aoc2022/Day21Golfed.java) | 3,048 | 785 |
| 22 | [Source](src/aoc2022/Day22.java) | [Golfed](src/aoc2022/Day22Golfed.java) | 2,720 | 966 |
| 23 | [Source](src/aoc2022/Day23.java) | [Golfed](src/aoc2022/Day23Golfed.java) | 3,302 | 901 |
| 24 | [Source](src/aoc2022/Day24.java) | [Golfed](src/aoc2022/Day24Golfed.java) | 3,963 | 633 |
| 25 | [Source](src/aoc2022/Day25.java) | [Golfed](src/aoc2022/Day25Golfed.java) | 669 | 239 |
| Total |  |  | 45,743 | 14,853 |

<!-- CHAR COUNTS END -->
