package com.onway.web.controller.result;

/**
 * post文件上传结果集
 * 
 * @author jiaming.zhu
 * @version $Id: FileUploadResult.java, v 0.1 2016年9月9日 下午4:29:03 ROG Exp $
 */
public class FileUploadResult extends JsonResult {

    private static final long serialVersionUID = 1475348231900998033L;

    private String            ReturnValue;

    public String getReturnValue() {
        return ReturnValue;
    }

    public void setReturnValue(String returnValue) {
        ReturnValue = returnValue;
    }

    public FileUploadResult(boolean bizSucc) {
        super(bizSucc);
    }

}
