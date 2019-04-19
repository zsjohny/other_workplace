package com.util;

import com.jiuyuan.util.BizUtil;

import java.util.List;

/**
 * @author Charlie(唐静)
 * @version V1.0
 * @title 调用job服务接收接口返回参数
 * @package jiuy-biz
 * @description
 * @date 2018/5/31 18:38
 * @copyright 玖远网络
 */
public class JobResponse{

    private Integer status;
    private Integer code;
    private String msg;
    private List data;
    private Boolean successful;
    private String error;
    private String html;

    /**
     * job服务调用返回值接收
     * @param json
     * @return com.util.JobResponse
     * @auther Charlie(唐静)
     * @date 2018/5/31 18:47
     */
    public static JobResponse acceptResponse(String json) {
        return BizUtil.json2bean(json, JobResponse.class);
    }


    private JobResponse() {
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public Boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
