package com.jiuyuan.util.performance;

public class PerformanceEntry {

    private String marker;

    private long startMillis;

    private long durationMillis;

    private int depth;

    public PerformanceEntry(String marker, int depth) {
        this.marker = marker;
        this.startMillis = System.currentTimeMillis();
        this.depth = depth;
    }

    public long end() {
        this.durationMillis = System.currentTimeMillis() - startMillis;
        return this.durationMillis;
    }

    public String getMarker() {
        return this.marker;
    }

    public long getDurationMillis() {
        return this.durationMillis;
    }

    public int getDepth() {
        return depth;
    }
}
