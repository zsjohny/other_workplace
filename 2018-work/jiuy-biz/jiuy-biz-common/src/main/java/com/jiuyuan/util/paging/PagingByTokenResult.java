package com.jiuyuan.util.paging;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jiuyuan.constant.ResultCode;

public class PagingByTokenResult<T> extends AbstractPagingResult<T> {

    private static final long serialVersionUID = -131305622100418929L;

    private String nextPageToken;

    /**
     * Equivalent to <code>PagingByTokenResult(data, ResultCode.SUCCESS, false)</code>
     */
    public PagingByTokenResult(List<T> data) {
        this(data, ResultCode.COMMON_SUCCESS, null);
    }

    public PagingByTokenResult(List<T> data, ResultCode resultCode, String nextPageToken) {
        super(data, resultCode);
        this.nextPageToken = nextPageToken;
    }

    @Override
    public boolean hasMore() {
        return StringUtils.isNotBlank(nextPageToken);
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public PagingByTokenResult<T> setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
        return this;
    }

    @Override
    public AbstractPagingResult<T> copyStatesFrom(AbstractPagingResult<?> another) {
        throw new UnsupportedOperationException();
    }

    public PagingByTokenResult<T> copyStatesFrom(PagingByTokenResult<?> another) {
        super.copyStatesFrom(another);
        this.nextPageToken = another.getNextPageToken();
        return this;
    }
}
