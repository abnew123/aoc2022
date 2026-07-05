# Performance Notes

The README table uses warm 10-run averages for each part because that makes solver-to-solver changes easier to compare. A single `MasterSolver` run is the better number for "I cloned the repo and ran all 50 parts once"; on this branch that is roughly 237ms.

The first invocation of a Java solver is often slower because the JVM is still loading classes, verifying bytecode, linking methods, compiling hot paths with the JIT, filling CPU caches, and sometimes paying one-time allocation or GC costs. These costs are real for a one-shot command, so the warm table should not be read as the literal end-to-end startup experience.

## Visual Examples

Bars are scaled to combined part 1 + part 2 time. Lower is better.

### Day 2: Rock Paper Scissors

The solver still scores the same rounds, but it no longer builds a list, splits each line, or looks up throws in a `Map`.

```text
Before  19.7 ms | ####################
After    3.2 ms | ###
```

### Day 8: Treetop Tree House

The solver still scans the tree grid, but the optimized version keeps the grid in primitive arrays and avoids repeated object-heavy lookups.

```text
Before   8.2 ms | ########
After    3.1 ms | ###
```

### Day 13: Distress Signal

The solver still compares packet ordering by the puzzle rules, but it parses and compares without building as much temporary nested structure.

```text
Before   9.6 ms | ##########
After    3.5 ms | ####
```
