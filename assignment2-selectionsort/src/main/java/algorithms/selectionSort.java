package algorithms;

import metrics.PerformanceTracker;
import java.util.Arrays;

/**
 * Selection Sort with early-termination optimizations.
 *
 * Optimizations included:
 * 1) Early-stop if the remaining suffix is already non-decreasing (no inversions seen in the inner pass)
 *    AND the current minimum stays at position i (no swap needed).
 * 2) Skips swap if minIndex == i (micro-optimization).
 *
 * Metrics collected via PerformanceTracker.
 */
public class selectionSort {

    public static void sort(int[] arr, PerformanceTracker tracker) {
        if (arr == null) {
            throw new IllegalArgumentException("Input array must not be null");
        }
        if (tracker == null) {
            tracker = new PerformanceTracker();
        }

        int n = arr.length;
        // Outer loop places the i-th smallest item at position i
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            boolean nonDecreasingSuffix = true; // track whether no inversion seen in this pass

            // Inner loop: find the minimum on [i..n-1]
            for (int j = i + 1; j < n; j++) {
                tracker.incArrayReads(2); // reading arr[j] and arr[minIndex] for comparison
                tracker.incComparisons();
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }

                // Also track if suffix is non-decreasing with respect to previous element
                tracker.incArrayReads(2);
                tracker.incComparisons();
                if (arr[j] < arr[j - 1]) {
                    nonDecreasingSuffix = false;
                }
            }

            // If no swap is needed and the examined suffix was already in order,
            // we can terminate early — the whole array is sorted.
            if (minIndex == i && nonDecreasingSuffix) {
                return;
            }

            // Place found minimum at position i
            if (minIndex != i) {
                swap(arr, i, minIndex, tracker);
            }
        }
    }

    private static void swap(int[] arr, int i, int j, PerformanceTracker tracker) {
        tracker.incArrayReads(2);
        tracker.incArrayWrites(2);
        tracker.incSwaps();
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    /** Convenience API that sorts and returns a new array copy (original remains unchanged). */
    public static int[] sortedCopy(int[] input, PerformanceTracker tracker) {
        int[] copy = input == null ? null : Arrays.copyOf(input, input.length);
        sort(copy, tracker);
        return copy;
    }
}
