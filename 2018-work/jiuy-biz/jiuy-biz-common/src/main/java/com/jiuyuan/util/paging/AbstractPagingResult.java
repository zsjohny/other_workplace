package com.jiuyuan.util.paging;

import java.io.Serializable;
import java.util.List;

import com.jiuyuan.constant.ResultCode;

public abstract class AbstractPagingResult<T> implements Serializable {

    private static final long serialVersionUID = -3433734853069121121L;

    private List<T> data;

    private ResultCode resultCode;

    public AbstractPagingResult(List<T> data, ResultCode resultCode) {
        this.data = data;
        this.resultCode = resultCode;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public abstract boolean hasMore();

    /**
     * 拷贝状态，包括resultCode
     */
    public AbstractPagingResult<T> copyStatesFrom(AbstractPagingResult<?> another) {
        this.resultCode = another.getResultCode();
        return this;
    }
}
