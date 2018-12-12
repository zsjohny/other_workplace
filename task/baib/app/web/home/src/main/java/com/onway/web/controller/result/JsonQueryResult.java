package com.onway.web.controller.result;

/**
 * 
 * @author li.hong
 * @version $Id: JsonQueryResult.java, v 0.1 2016年9月2日 下午5:34:40 li.hong Exp $
 */
public class JsonQueryResult<T> extends JsonResult {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7641724788545434094L;

    public JsonQueryResult(boolean bizSucc) {
        super(bizSucc);
    }

    public JsonQueryResult(boolean bizSucc, String errCode, String message) {
        super(bizSucc, errCode, message);
    }

    private T obj;

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

}
