package com.jiuyuan.util.sort;

public class SortField {

    private String field;

    private boolean descending;

    public SortField(String field, boolean descending) {
        this.field = field;
        this.descending = descending;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public boolean isDescending() {
        return descending;
    }

    public void setDescending(boolean descending) {
        this.descending = descending;
    }
}
