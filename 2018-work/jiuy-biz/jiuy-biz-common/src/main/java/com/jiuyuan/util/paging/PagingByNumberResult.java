package com.jiuyuan.util.paging;

import java.util.List;

import com.jiuyuan.constant.ResultCode;

public class PagingByNumberResult<T> extends BasicPagingResult<T> {

    private static final long serialVersionUID = -131305622100418929L;

    private int totalCount;

    /**
     * Equivalent to <code>PagingByNumberResult(data, ResultCode.SUCCESS, false, 0)</code>
     */
    public PagingByNumberResult(List<T> data) {
        this(data, ResultCode.COMMON_SUCCESS, false, 0);
    }

    public PagingByNumberResult(List<T> data, ResultCode resultCode, boolean hasMore, int totalCount) {
        super(data, resultCode, hasMore);
        this.totalCount = totalCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public BasicPagingResult<T> copyStatesFrom(AbstractPagingResult<?> another) {
        throw new UnsupportedOperationException();
    }

    public PagingByNumberResult<T> copyStatesFrom(PagingByNumberResult<?> another) {
        super.copyStatesFrom(another);
        this.totalCount = another.getTotalCount();
        return this;
    }
}
