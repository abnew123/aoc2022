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

Current timings:

* Day 01 part 1 execution time: 41.2ms (acceptable)
* Day 01 part 2 execution time: 16.9ms (fast)
* Day 02 part 1 execution time: 11.0ms (fast)
* Day 02 part 2 execution time: 8.7ms (fast)
* Day 03 part 1 execution time: 8.5ms (fast)
* Day 03 part 2 execution time: 4.8ms (fast)
* Day 04 part 1 execution time: 11.6ms (fast)
* Day 04 part 2 execution time: 5.0ms (fast)
* Day 05 part 1 execution time: 5.5ms (fast)
* Day 05 part 2 execution time: 4.1ms (fast)
* Day 06 part 1 execution time: 3.0ms (fast)
* Day 06 part 2 execution time: 7.9ms (fast)
* Day 07 part 1 execution time: 16.8ms (fast)
* Day 07 part 2 execution time: 3.9ms (fast)
* Day 08 part 1 execution time: 22.0ms (acceptable)
* Day 08 part 2 execution time: 22.1ms (acceptable)
* Day 09 part 1 execution time: 12.7ms (fast)
* Day 09 part 2 execution time: 28.1ms (acceptable)
* Day 10 part 1 execution time: 0.7ms (fast)
* Day 10 part 2 execution time: 0.6ms (fast)
* Day 11 part 1 execution time: 4.8ms (fast)
* Day 11 part 2 execution time: 52.0ms (slow)
* Day 12 part 1 execution time: 50.7ms (slow)
* Day 12 part 2 execution time: 28.2ms (acceptable)
* Day 13 part 1 execution time: 11.0ms (fast)
* Day 13 part 2 execution time: 7.0ms (fast)
* Day 14 part 1 execution time: 9.1ms (fast)
* Day 14 part 2 execution time: 21.4ms (acceptable)
* Day 15 part 1 execution time: 624.0ms (slow)
* Day 15 part 2 execution time: 0.7ms (fast)
* Day 16 part 1 execution time: 107.8ms (slow)
* Day 16 part 2 execution time: 473.7ms (slow)
* Day 17 part 1 execution time: 121.7ms (slow)
* Day 17 part 2 execution time: 431.8ms (slow)
* Day 18 part 1 execution time: 12.0ms (fast)
* Day 18 part 2 execution time: 7.5ms (fast)
* Day 19 part 1 execution time: 53.1ms (slow)
* Day 19 part 2 execution time: 159.6ms (slow)
* Day 20 part 1 execution time: 61.0ms (slow)
* Day 20 part 2 execution time: 524.9ms (slow)
* Day 21 part 1 execution time: 315.1ms (slow)
* Day 21 part 2 execution time: 332.8ms (slow)
* Day 22 part 1 execution time: 10.9ms (fast)
* Day 22 part 2 execution time: 9.7ms (fast)
* Day 23 part 1 execution time: 130.6ms (slow)
* Day 23 part 2 execution time: 361.4ms (slow)
* Day 24 part 1 execution time: 500.3ms (slow)
* Day 24 part 2 execution time: 975.4ms (slow)
* Day 25 part 1 execution time: 0.3ms (fast)
* Day 25 part 2 execution time: 0.3ms (fast)
* Total execution time : 5633.8ms
* Slow parts: 17. Acceptable parts: 6. Fast parts: 27.

<!-- CHAR COUNTS BEGIN -->

## Character counts

The table below counts **all characters, including whitespace** in each normal solution file and its golfed sibling. (Metric changed 2026-07-25 — it previously excluded whitespace.) The golfed files are intentionally separate from the readable/performance-oriented `DayXX.java` files so the PR tracks can merge independently. Every golfed solver receives the same neutral `String[]` of logical input lines from `MasterSolver`; input parsing and all puzzle logic remain in its `DayXXGolfed.java` file.

| Day | Solution | Golfed solution | Source chars | Golfed chars |
| --- | --- | --- |-------------:|-------------:|
| 1 | [Source](src/solutions/Day01.java) | [Golfed](src/solutions/Day01Golfed.java) | 699 | 175 |
| 2 | [Source](src/solutions/Day02.java) | [Golfed](src/solutions/Day02Golfed.java) | 926 | 126 |
| 3 | [Source](src/solutions/Day03.java) | [Golfed](src/solutions/Day03Golfed.java) | 1,769 | 202 |
| 4 | [Source](src/solutions/Day04.java) | [Golfed](src/solutions/Day04Golfed.java) | 827 | 166 |
| 5 | [Source](src/solutions/Day05.java) | [Golfed](src/solutions/Day05Golfed.java) | 1,741 | 359 |
| 6 | [Source](src/solutions/Day06.java) | [Golfed](src/solutions/Day06Golfed.java) | 625 | 124 |
| 7 | [Source](src/solutions/Day07.java) | [Golfed](src/solutions/Day07Golfed.java) | 2,228 | 306 |
| 8 | [Source](src/solutions/Day08.java) | [Golfed](src/solutions/Day08Golfed.java) | 2,905 | 250 |
| 9 | [Source](src/solutions/Day09.java) | [Golfed](src/solutions/Day09Golfed.java) | 1,698 | 316 |
| 10 | [Source](src/solutions/Day10.java) | [Golfed](src/solutions/Day10Golfed.java) | 1,314 | 278 |
| 11 | [Source](src/solutions/Day11.java) | [Golfed](src/solutions/Day11Golfed.java) | 2,370 | 459 |
| 12 | [Source](src/solutions/Day12.java) | [Golfed](src/solutions/Day12Golfed.java) | 2,550 | 319 |
| 13 | [Source](src/solutions/Day13.java) | [Golfed](src/solutions/Day13Golfed.java) | 2,298 | 385 |
| 14 | [Source](src/solutions/Day14.java) | [Golfed](src/solutions/Day14Golfed.java) | 1,554 | 453 |
| 15 | [Source](src/solutions/Day15.java) | [Golfed](src/solutions/Day15Golfed.java) | 3,686 | 516 |
| 16 | [Source](src/solutions/Day16.java) | [Golfed](src/solutions/Day16Golfed.java) | 3,239 | 546 |
| 17 | [Source](src/solutions/Day17.java) | [Golfed](src/solutions/Day17Golfed.java) | 4,950 | 567 |
| 18 | [Source](src/solutions/Day18.java) | [Golfed](src/solutions/Day18Golfed.java) | 1,877 | 324 |
| 19 | [Source](src/solutions/Day19.java) | [Golfed](src/solutions/Day19Golfed.java) | 3,776 | 466 |
| 20 | [Source](src/solutions/Day20.java) | [Golfed](src/solutions/Day20Golfed.java) | 1,336 | 354 |
| 21 | [Source](src/solutions/Day21.java) | [Golfed](src/solutions/Day21Golfed.java) | 4,332 | 460 |
| 22 | [Source](src/solutions/Day22.java) | [Golfed](src/solutions/Day22Golfed.java) | 4,033 | 445 |
| 23 | [Source](src/solutions/Day23.java) | [Golfed](src/solutions/Day23Golfed.java) | 4,479 | 568 |
| 24 | [Source](src/solutions/Day24.java) | [Golfed](src/solutions/Day24Golfed.java) | 5,064 | 455 |
| 25 | [Source](src/solutions/Day25.java) | [Golfed](src/solutions/Day25Golfed.java) | 932 | 180 |
| Total |  |  | 61,208 | 8,799 |

<!-- CHAR COUNTS END -->
