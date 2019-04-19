package com.jiuyuan.util.performance;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerformanceRecord {

    private static final Logger log = LoggerFactory.getLogger(PerformanceRecord.class);

    private Stack<PerformanceEntry> stack = new Stack<PerformanceEntry>();

    private List<PerformanceEntry> list = new ArrayList<PerformanceEntry>();

    public void start(String marker) {
        PerformanceEntry entry = new PerformanceEntry(marker, stack.size());
        stack.push(entry);
        list.add(entry);
    }

    public void end(String marker) {
        PerformanceEntry entry = stack.peek();
        if (!entry.getMarker().equals(marker)) {
            log.error("Error calling end for marker \"{}\", expecting \"{}\".", marker, entry.getMarker());
            return;
        }
        entry.end();
        stack.pop();
    }

    public List<PerformanceEntry> getEntries() {
        return list;
    }
}
