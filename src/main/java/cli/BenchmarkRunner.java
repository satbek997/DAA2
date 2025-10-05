package cli;

import algorithms.SelectionSort;
import algorithms.InsertionSort;
import metrics.PerformanceTracker;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class BenchmarkRunner {

    private static final int[] SIZES = {100, 1000, 10000, 100000};
    private static final String[] DISTRIBUTIONS = {"random", "sorted", "reversed", "nearly_sorted"};

    public static void main(String[] args) {
        String outputPath = "docs/performance-plots/selection_sort_results.csv";

        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.write("n,distribution,time_ms,comparisons,swaps,arrayReads,arrayWrites\n");

            for (int n : SIZES) {
                for (String dist : DISTRIBUTIONS) {

                    // Generate test array
                    int[] arr = generateArray(n, dist);
                    int[] arrCopy = Arrays.copyOf(arr, arr.length);

                    // --- Selection Sort ---
                    PerformanceTracker selectionTracker = new PerformanceTracker();
                    long start = System.nanoTime();
                    SelectionSort.sort(arr, selectionTracker);
                    long end = System.nanoTime();
                    double selectionTime = (end - start) / 1_000_000.0;

                    // --- Insertion Sort ---
                    InsertionSort insertion = new InsertionSort();
                    start = System.nanoTime();
                    insertion.sort(arrCopy);
                    end = System.nanoTime();
                    double insertionTime = (end - start) / 1_000_000.0;

                    PerformanceTracker insertionTracker = insertion.getTracker();

                    // --- Print to console ---
                    System.out.printf(
                            "n=%d, %s → Selection=%.3f ms, Insertion=%.3f ms%n",
                            n, dist, selectionTime, insertionTime
                    );

                    // --- Write to CSV ---
                    writer.write(String.format("%d,%s,%.3f,%d,%d,%d,%d\n",
                            n, dist, selectionTime,
                            selectionTracker.getComparisons(),
                            selectionTracker.getSwaps(),
                            selectionTracker.getArrayReads(),
                            selectionTracker.getArrayWrites()));
                }
            }

            System.out.println("\n✅ Benchmark completed. Results saved to: " + outputPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- Helper for generating arrays ---
    private static int[] generateArray(int n, String type) {
        int[] arr = new int[n];
        Random rand = new Random();

        switch (type) {
            case "sorted":
                for (int i = 0; i < n; i++) arr[i] = i;
                break;
            case "reversed":
                for (int i = 0; i < n; i++) arr[i] = n - i;
                break;
            case "nearly_sorted":
                for (int i = 0; i < n; i++) arr[i] = i;
                // make small random swaps
                for (int i = 0; i < n / 10; i++) {
                    int a = rand.nextInt(n);
                    int b = rand.nextInt(n);
                    int temp = arr[a];
                    arr[a] = arr[b];
                    arr[b] = temp;
                }
                break;
            default: // random
                for (int i = 0; i < n; i++) arr[i] = rand.nextInt(n * 10);
        }

        return arr;
    }
}
