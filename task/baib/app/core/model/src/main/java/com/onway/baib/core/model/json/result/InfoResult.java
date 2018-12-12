package com.onway.baib.core.model.json.result;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户返回结果集
 * 
 * @author Administrator
 * @version $Id: UserResult.java, v 0.1 2016年4月14日 上午11:15:27 Administrator Exp $
 */
public class InfoResult extends JsonResult {

    /**  */
    private static final long serialVersionUID = 1L;

    public InfoResult(boolean bizSucc) {
        super(bizSucc);
    }

    /**
     * @param bizSucc
     * @param errCode
     * @param message
     */
    public InfoResult(boolean bizSucc, String errCode, String message) {
        super(bizSucc, errCode, message);
    }

    private List<Object> listData = new ArrayList<Object>();

    private String       returnValue;

    private String       returnMsg;

    private String       defaultErrorMsg;

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getDefaultErrorMsg() {
        return defaultErrorMsg;
    }

    public void setDefaultErrorMsg(String defaultErrorMsg) {
        this.defaultErrorMsg = defaultErrorMsg;
    }

    public List<Object> getListData() {
        return listData;
    }

    public void setListData(List<Object> listData) {
        this.listData = listData;
    }

}
