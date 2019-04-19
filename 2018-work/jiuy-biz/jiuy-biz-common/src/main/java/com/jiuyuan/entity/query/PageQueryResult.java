package com.jiuyuan.entity.query;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;;

public class PageQueryResult extends PageQuery {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2423182617243904267L;

	private int recordCount;

    private int maxRecordCount = MAX_RECORDS_COUNT;

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getMaxRecordCount() {
        return maxRecordCount;
    }

    public void setMaxRecordCount(int maxRecordCount) {
        this.maxRecordCount = maxRecordCount;
    }

    public int getPageCount() {
        int visibleRecordCount = Math.min(this.recordCount, Math.min(MAX_RECORDS_COUNT, maxRecordCount));
        int pageCount = (visibleRecordCount + getPageSize() - 1) / getPageSize();

        if (pageCount > MAX_PAGE) {
            pageCount = MAX_PAGE;
        }

        if (pageCount < MIN_PAGE) {
            pageCount = MIN_PAGE;
        }

        return pageCount;
    }

    public boolean isMore() {
        return getPage() < getPageCount();
    }

    @Override
    public void checkPageLimit() {
        if (getPage() > getPageCount()) {
            setPage(getPageCount());
        }
    }

    public static PageQueryResult copyFrom(PageQuery pageQuery, int recordCount) {
        PageQueryResult vo = new PageQueryResult();
        try {
            BeanUtils.copyProperties(vo, pageQuery);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        vo.setRecordCount(recordCount);
        return vo;
    }
}
