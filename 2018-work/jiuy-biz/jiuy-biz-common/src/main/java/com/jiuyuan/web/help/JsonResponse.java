package com.jiuyuan.web.help;

import com.jiuyuan.constant.ResultCode;

import java.io.Serializable;

public class JsonResponse  implements Serializable {
    public JsonResponse() {

    }

    public static JsonResponse getInstance() {
        return new JsonResponse();
    }

    private boolean successful;

    private String error;

    private int code;

    private Object data;

    /**
     * 某些接口期望直接返回html片段，可以通过该参数设置
     *
     * @see FreeMarkerTemplateRenderer#processTemplate(String, com.yujj.util.adapter.ValueSupplier)
     */
    private String html;

    public boolean isSuccessful() {
        return successful;
    }

    public JsonResponse setSuccessful() {
        return setResultCode(ResultCode.COMMON_SUCCESS);
    }

    public String getError() {
        return error;
    }

    public JsonResponse setError(String error) {
        this.error = error;
        return this;
    }

    public int getCode() {
        return code;
    }

    public JsonResponse setCode(int code) {
        this.code = code;
        this.successful = ResultCode.COMMON_SUCCESS.is(code);
        return this;
    }

    public Object getData() {
        return data;
    }

    public JsonResponse setData(Object data) {
        this.data = data;
        return this;
    }

    public String getHtml() {
        return html;
    }

    public JsonResponse setHtml(String html) {
        this.html = html;
        return this;
    }

    public JsonResponse setResultCode(int code, String msg) {
        this.code = code;
        this.error = msg;
        this.successful = false;
        return this;
    }

    public JsonResponse setCode(Integer code) {
        this.code = code;
        return this;
    }

    public JsonResponse setResultCode(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.successful = ResultCode.COMMON_SUCCESS == resultCode;
        if (!successful) {
            this.error = resultCode.getDesc();
        }
        return this;
    }


    public JsonResponse switchBetween(int code, Object data, String error) {
        this.data = data;
        this.code = code;
        this.successful = ResultCode.COMMON_SUCCESS.is(code);
        if (!successful) {
            this.error = error;
        }
        return this;
    }

    public static JsonResponse successful(Object data) {
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.data = data;
        jsonResponse.code = ResultCode.COMMON_SUCCESS.getCode();
        jsonResponse.successful = Boolean.TRUE;
        return jsonResponse;
    }

    public static JsonResponse successful() {
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.code = ResultCode.COMMON_SUCCESS.getCode();
        jsonResponse.successful = Boolean.TRUE;
        return jsonResponse;
    }

    public static JsonResponse fail(String error) {
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.code = ResultCode.COMMON_FAIL.getCode();
        jsonResponse.successful = Boolean.FALSE;
        jsonResponse.setError(error);

        return jsonResponse;
    }
}
