package metrics;

/**
 * Tracks core operations for algorithmic analysis.
 */
public class PerformanceTracker {
    private long comparisons;
    private long swaps;
    private long arrayReads;
    private long arrayWrites;

    public void incComparisons() { comparisons++; }
    public void incComparisons(long k) { comparisons += k; }

    public void incSwaps() { swaps++; }
    public void incSwaps(long k) { swaps += k; }

    public void incArrayReads() { arrayReads++; }
    public void incArrayReads(long k) { arrayReads += k; }

    public void incArrayWrites() { arrayWrites++; }
    public void incArrayWrites(long k) { arrayWrites += k; }

    public long getComparisons() { return comparisons; }
    public long getSwaps() { return swaps; }
    public long getArrayReads() { return arrayReads; }
    public long getArrayWrites() { return arrayWrites; }

    public void reset() {
        comparisons = 0;
        swaps = 0;
        arrayReads = 0;
        arrayWrites = 0;
    }

    @Override
    public String toString() {
        return "PerformanceTracker{" +
                "comparisons=" + comparisons +
                ", swaps=" + swaps +
                ", arrayReads=" + arrayReads +
                ", arrayWrites=" + arrayWrites +
                '}';
    }
}
