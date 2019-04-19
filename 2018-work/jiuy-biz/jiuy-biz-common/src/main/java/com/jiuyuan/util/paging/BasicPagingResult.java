package com.jiuyuan.util.paging;

import java.util.List;

import com.jiuyuan.constant.ResultCode;

public class BasicPagingResult<T> extends AbstractPagingResult<T> {

    private static final long serialVersionUID = -6293319965094093662L;

    private boolean hasMore;

    public BasicPagingResult(List<T> data, ResultCode resultCode, boolean hasMore) {
        super(data, resultCode);
        this.hasMore = hasMore;
    }

    @Override
    public boolean hasMore() {
        return hasMore;
    }

    public BasicPagingResult<T> setMore(boolean more) {
        this.hasMore = more;
        return this;
    }

    @Override
    public BasicPagingResult<T> copyStatesFrom(AbstractPagingResult<?> another) {
        super.copyStatesFrom(another);
        this.hasMore = another.hasMore();
        return this;
    }
}
