package src.main.java.cli;

import algorithms.selectionSort;
import metrics.PerformanceTracker;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Random;

/**
 * CLI to benchmark Selection Sort.
 *
 * Usage examples:
 *   java -jar target/assignment2-selectionsort-1.0.0.jar
 *   java -jar target/assignment2-selectionsort-1.0.0.jar 100 1000 10000 100000
 *
 * Outputs CSV to docs/performance-plots/selection_sort_results.csv
 */
public class BenchmarkRunner {

    private static final int[] DEFAULT_SIZES = {100, 1000, 10000, 100000};

    public static void main(String[] args) throws Exception {
        int[] sizes = args.length == 0 ? DEFAULT_SIZES : parseSizes(args);
        Path outDir = Path.of("docs", "performance-plots");
        Files.createDirectories(outDir);
        Path csvPath = outDir.resolve("selection_sort_results.csv");

        try (FileWriter fw = new FileWriter(csvPath.toFile(), false)) {
            fw.write("n,distribution,time_ms,comparisons,swaps,arrayReads,arrayWrites\n");
            for (int n : sizes) {
                runForDistribution(fw, n, "random", BenchmarkRunner::randomArray);
                runForDistribution(fw, n, "sorted", BenchmarkRunner::sortedArray);
                runForDistribution(fw, n, "reversed", BenchmarkRunner::reversedArray);
                runForDistribution(fw, n, "nearly_sorted", BenchmarkRunner::nearlySortedArray);
            }
        }

        System.out.println("Benchmark complete. CSV written to: " + csvPath.toAbsolutePath());
    }

    // ------- Distributions --------

    private static int[] randomArray(int n) {
        int[] a = new int[n];
        Random rnd = new Random(42);
        for (int i = 0; i < n; i++) a[i] = rnd.nextInt();
        return a;
    }

    private static int[] sortedArray(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = i;
        return a;
    }

    private static int[] reversedArray(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = n - i;
        return a;
    }

    private static int[] nearlySortedArray(int n) {
        int[] a = sortedArray(n);
        Random rnd = new Random(7);
        int swaps = Math.max(1, n / 100); // swap ~1% items
        for (int k = 0; k < swaps; k++) {
            int i = rnd.nextInt(n);
            int j = rnd.nextInt(n);
            int t = a[i];
            a[i] = a[j];
            a[j] = t;
        }
        return a;
    }

    // ------- Benchmark harness --------

    @FunctionalInterface
    interface Gen { int[] make(int n); }

    private static void runForDistribution(FileWriter fw, int n, String name, Gen gen) throws IOException {
        int[] a = gen.make(n);
        PerformanceTracker tracker = new PerformanceTracker();
        long start = System.nanoTime();
        selectionSort.sort(a, tracker);
        long end = System.nanoTime();

        // sanity check
        if (!isSorted(a)) {
            throw new AssertionError("Array is not sorted for distribution: " + name + ", n=" + n);
        }

        double ms = (end - start) / 1_000_000.0;
        fw.write(String.format("%d,%s,%.3f,%d,%d,%d,%d%n",
                n, name, ms,
                tracker.getComparisons(), tracker.getSwaps(),
                tracker.getArrayReads(), tracker.getArrayWrites()));
        fw.flush();
        System.out.printf("n=%d, dist=%s, time=%.3f ms, comps=%d, swaps=%d%n",
                n, name, ms, tracker.getComparisons(), tracker.getSwaps());
    }

    private static boolean isSorted(int[] a) {
        for (int i = 1; i < a.length; i++) {
            if (a[i] < a[i - 1]) return false;
        }
        return true;
    }

    private static int[] parseSizes(String[] args) {
        return Arrays.stream(args).mapToInt(Integer::parseInt).toArray();
    }
}
