# Assignment 2 — Selection Sort (Student B)

This repository contains the **Selection Sort with early-termination optimizations** plus a small
benchmark CLI and unit tests, matching the assignment structure.

## Contents
- `algorithms/SelectionSort.java` — selection sort with two optimizations
- `metrics/PerformanceTracker.java` — counts comparisons, swaps, reads, writes
- `cli/BenchmarkRunner.java` — CLI that benchmarks on multiple `n` and input distributions
- `src/test/java/.../SelectionSortTest.java` — JUnit tests for edge cases
- `docs/performance-plots/` — CSV output destination and (optionally) plots

## How to build & test
```bash
mvn -q -e -DskipTests=false test
mvn -q package
```

## Run the benchmark
```bash
# default sizes: 100, 1000, 10000, 100000
java -jar target/assignment2-selectionsort-1.0.0.jar

# or custom sizes
java -jar target/assignment2-selectionsort-1.0.0.jar 200 5000 20000
```

The CLI writes a CSV to: `docs/performance-plots/selection_sort_results.csv`

## Algorithm details
- **Time complexity**: Worst/average `Θ(n^2)`, best case with early-stop can terminate faster on sorted inputs,
  though asymptotically remains `Θ(n^2)` for selection sort because it still scans the suffix to find `min`.
- **Space complexity**: In-place, `Θ(1)` auxiliary space.
- **Optimizations**:
  1. Early-stop if the suffix is already non-decreasing *and* no swap is needed at position `i`.
  2. Skip swap when `minIndex == i`.

## Notes for Peer Review
The tracker fields (comparisons, swaps, reads, writes) are updated in a transparent way and can be compared
to theoretical counts for various distributions.
