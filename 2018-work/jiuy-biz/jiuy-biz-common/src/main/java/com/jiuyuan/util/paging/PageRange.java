package com.jiuyuan.util.paging;

public class PageRange {

    private int page;

    private int start;

    /** exclusive */
    private int end;

    private int size;

    public PageRange(int page, int start, int end) {
        this.page = page;
        this.start = start;
        this.end = end;
        this.size = this.end - this.start;
    }

    public int getPage() {
        return page;
    }

    public int getStart() {
        return start;
    }

    /** exclusive */
    public int getEnd() {
        return end;
    }

    public int getSize() {
        return size;
    }
}
