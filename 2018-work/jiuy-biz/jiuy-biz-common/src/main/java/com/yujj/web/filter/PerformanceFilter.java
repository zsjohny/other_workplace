package com.yujj.web.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiuyuan.util.performance.PerformanceEntry;
import com.jiuyuan.util.performance.PerformanceRecord;
import com.yujj.util.performance.PerformanceWatcher;


public class PerformanceFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(PerformanceFilter.class);

    private static final int requestMillsLimit = 2000;

    private static final int entryMillsLimit = 100;

    public void init(FilterConfig filterConfig) {
        // do nothing
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
        ServletException {

        if (PerformanceWatcher.PERFORMANCE_RECORD_HOLDER.get() != null) {
            // In a single request, the performance logic should not be executed twice or more.
            chain.doFilter(request, response);
        } else {
            HttpServletRequest req = (HttpServletRequest) request;
            String uri = req.getRequestURI();
            // String operation = req.getParameter("operation");
           
            try {
                PerformanceRecord record = new PerformanceRecord();
                PerformanceWatcher.PERFORMANCE_RECORD_HOLDER.set(record);

                // String marker = uri + ", operation=" + operation;
                String marker = uri;
                log.info("==========调用接口url："+uri);
                record.start(marker);
                chain.doFilter(request, response);
                record.end(marker);

                if (record.getEntries().get(0).getDurationMillis() > requestMillsLimit) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("-------------------------------------------------------").append("\n");
                    List<PerformanceEntry> entries = record.getEntries();
                    for (PerformanceEntry entry : entries) {
                        if (entry.getDurationMillis() > entryMillsLimit) {
                            builder.append(getIndents(entry.getDepth()) + entry.getDurationMillis() + "ms: " +
                                entry.getMarker());
                            builder.append("\n");
                        }
                    }
                    builder.append("-------------------------------------------------------").append("\n");
                    log.info(builder.toString());
                }
                
                
            } finally {
                PerformanceWatcher.PERFORMANCE_RECORD_HOLDER.set(null);
            }
        }
    }

    private String getIndents(int indentSize) {
        String indent = "  ";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < indentSize; i++) {
            builder.append(indent);
        }
        return builder.toString();
    }

    public void destroy() {
        // do nothing
    }
}
