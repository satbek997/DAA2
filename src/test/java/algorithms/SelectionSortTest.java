package algorithms;

import metrics.PerformanceTracker;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SelectionSortTest {

    @Test
    void handlesEmptyArray() {
        int[] a = new int[0];
        selectionSort.sort(a, new PerformanceTracker());
        assertArrayEquals(new int[0], a);
    }

    @Test
    void handlesSingleElement() {
        int[] a = {42};
        selectionSort.sort(a, new PerformanceTracker());
        assertArrayEquals(new int[]{42}, a);
    }

    @Test
    void handlesDuplicates() {
        int[] a = {5, 2, 2, 5, 3, 3};
        selectionSort.sort(a, new PerformanceTracker());
        assertArrayEquals(new int[]{2, 2, 3, 3, 5, 5}, a);
    }

    @Test
    void handlesSorted() {
        int[] a = {1,2,3,4,5};
        selectionSort.sort(a, new PerformanceTracker());
        assertArrayEquals(new int[]{1,2,3,4,5}, a);
    }

    @Test
    void handlesReversed() {
        int[] a = {5,4,3,2,1};
        selectionSort.sort(a, new PerformanceTracker());
        assertArrayEquals(new int[]{1,2,3,4,5}, a);
    }

    @Test
    void sortedCopyDoesNotMutateOriginal() {
        int[] a = {3,1,2};
        PerformanceTracker t = new PerformanceTracker();
        int[] b = selectionSort.sortedCopy(a, t);
        assertArrayEquals(new int[]{1,2,3}, b);
        assertArrayEquals(new int[]{3,1,2}, a);
    }
}
