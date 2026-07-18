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

The table below counts non-whitespace characters in each normal solution file and its golfed sibling. The golfed files are intentionally separate from the readable/performance-oriented `DayXX.java` files so the PR tracks can merge independently. Every golfed solver receives the same neutral `String[]` of logical input lines from `MasterSolver`; input parsing and all puzzle logic remain in its `DayXXGolfed.java` file.

| Day | Solution | Golfed solution | Source chars | Golfed chars |
| --- | --- | --- |-------------:|-------------:|
| 1 | [Source](src/aoc2022/Day01.java) | [Golfed](src/aoc2022/Day01Golfed.java) | 569 | 179 |
| 2 | [Source](src/aoc2022/Day02.java) | [Golfed](src/aoc2022/Day02Golfed.java) | 709 | 153 |
| 3 | [Source](src/aoc2022/Day03.java) | [Golfed](src/aoc2022/Day03Golfed.java) | 1,406 | 297 |
| 4 | [Source](src/aoc2022/Day04.java) | [Golfed](src/aoc2022/Day04Golfed.java) | 672 | 190 |
| 5 | [Source](src/aoc2022/Day05.java) | [Golfed](src/aoc2022/Day05Golfed.java) | 1,305 | 478 |
| 6 | [Source](src/aoc2022/Day06.java) | [Golfed](src/aoc2022/Day06Golfed.java) | 488 | 174 |
| 7 | [Source](src/aoc2022/Day07.java) | [Golfed](src/aoc2022/Day07Golfed.java) | 1,672 | 360 |
| 8 | [Source](src/aoc2022/Day08.java) | [Golfed](src/aoc2022/Day08Golfed.java) | 2,248 | 346 |
| 9 | [Source](src/aoc2022/Day09.java) | [Golfed](src/aoc2022/Day09Golfed.java) | 1,212 | 372 |
| 10 | [Source](src/aoc2022/Day10.java) | [Golfed](src/aoc2022/Day10Golfed.java) | 1,012 | 510 |
| 11 | [Source](src/aoc2022/Day11.java) | [Golfed](src/aoc2022/Day11Golfed.java) | 1,851 | 652 |
| 12 | [Source](src/aoc2022/Day12.java) | [Golfed](src/aoc2022/Day12Golfed.java) | 1,835 | 424 |
| 13 | [Source](src/aoc2022/Day13.java) | [Golfed](src/aoc2022/Day13Golfed.java) | 1,780 | 725 |
| 14 | [Source](src/aoc2022/Day14.java) | [Golfed](src/aoc2022/Day14Golfed.java) | 1,165 | 569 |
| 15 | [Source](src/aoc2022/Day15.java) | [Golfed](src/aoc2022/Day15Golfed.java) | 2,759 | 686 |
| 16 | [Source](src/aoc2022/Day16.java) | [Golfed](src/aoc2022/Day16Golfed.java) | 2,469 | 803 |
| 17 | [Source](src/aoc2022/Day17.java) | [Golfed](src/aoc2022/Day17Golfed.java) | 3,549 | 859 |
| 18 | [Source](src/aoc2022/Day18.java) | [Golfed](src/aoc2022/Day18Golfed.java) | 1,377 | 516 |
| 19 | [Source](src/aoc2022/Day19.java) | [Golfed](src/aoc2022/Day19Golfed.java) | 2,934 | 717 |
| 20 | [Source](src/aoc2022/Day20.java) | [Golfed](src/aoc2022/Day20Golfed.java) | 1,029 | 353 |
| 21 | [Source](src/aoc2022/Day21.java) | [Golfed](src/aoc2022/Day21Golfed.java) | 3,048 | 659 |
| 22 | [Source](src/aoc2022/Day22.java) | [Golfed](src/aoc2022/Day22Golfed.java) | 2,720 | 817 |
| 23 | [Source](src/aoc2022/Day23.java) | [Golfed](src/aoc2022/Day23Golfed.java) | 3,302 | 675 |
| 24 | [Source](src/aoc2022/Day24.java) | [Golfed](src/aoc2022/Day24Golfed.java) | 3,963 | 576 |
| 25 | [Source](src/aoc2022/Day25.java) | [Golfed](src/aoc2022/Day25Golfed.java) | 669 | 202 |
| Total |  |  | 45,743 | 12,292 |

<!-- CHAR COUNTS END -->
