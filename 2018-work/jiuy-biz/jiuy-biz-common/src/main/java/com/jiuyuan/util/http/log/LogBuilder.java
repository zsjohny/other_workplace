package com.jiuyuan.util.http.log;

import java.util.LinkedHashMap;
import java.util.Map;


public class LogBuilder {

    private String operation;

    private boolean info;

    private Map<String, Object> params = new LinkedHashMap<String, Object>();

    public LogBuilder(String operation, boolean info) {
        this.operation = operation;
        this.info = info;
    }

    public LogBuilder append(String name, Object value) {
        this.params.put(name, value);
        return this;
    }

    public LogBuilder init(LogInitializer initializer) {
        initializer.initLog(this);
        return this;
    }

    public String getLogText() {
        StringBuilder builder = new StringBuilder(info ? "Operation" : "Error in operation").append(": ");
        builder.append(this.operation).append(". ");
        if (!this.params.isEmpty()) {
            builder.append("Params: {");
            for (Map.Entry<String, Object> entry : this.params.entrySet()) {
            	builder.append(entry.getKey() + ":" + entry.getValue());
            	builder.append(", ");
            }
            builder.append("}");
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return getLogText();
    }
}
