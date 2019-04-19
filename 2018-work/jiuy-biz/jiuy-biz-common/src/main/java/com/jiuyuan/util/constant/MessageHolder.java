package com.jiuyuan.util.constant;

import java.text.MessageFormat;

public class MessageHolder {

    private String value;

    private int argCount;

    public MessageHolder() {
        //
    }

    public MessageHolder(String value, int argCount) {
        this.value = value;
        this.argCount = argCount;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String format(Object... arguments) {
        if (argCount > 0) {
            int realCount = arguments != null ? arguments.length : 0;
            if (realCount < argCount) {
                throw new IllegalArgumentException("Argument count mismatch, expected: " + argCount + ", found: " +
                    realCount + ", value: " + value);
            }
        }
        return MessageFormat.format(this.value, arguments);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getArgCount() {
        return argCount;
    }

    public void setArgCount(int argCount) {
        this.argCount = argCount;
    }
}
