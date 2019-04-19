package com.jiuyuan.entity.query;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PageQuery implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8001520821436821945L;

	public static final int MIN_PAGE_SIZE = 1;

    public static final int MAX_PAGE_SIZE = 500;

    public static final int MIN_PAGE = 1;

    public static final int MAX_PAGE = 200;

    public static final int MAX_RECORDS_COUNT = 20000;

    private int page = 1;

    private int pageSize = 20;

    public PageQuery() {
        // blank
    }

    public PageQuery(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        if (page < MIN_PAGE) {
            page = MIN_PAGE;
        } else if (page > MAX_PAGE) {
            page = MAX_PAGE;
        }
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if (pageSize < MIN_PAGE_SIZE) {
            pageSize = MIN_PAGE_SIZE;
        } else if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        }
        this.pageSize = pageSize;
    }

    @JsonIgnore
    public int getLimit() {
        return this.pageSize;
    }

    @JsonIgnore
    public int getOffset() {
        return this.pageSize * (this.page - 1);
    }

    public void checkPageLimit() {
        int maxPage = Math.min(MAX_PAGE, (MAX_RECORDS_COUNT + getPageSize() - 1) / getPageSize());
        if (getPage() > maxPage) {
            setPage(maxPage);
        }
    }
}
