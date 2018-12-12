package com.finace.miscroservice.commons.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统一处理response
 */
public class Response implements Serializable {


    //成功的标识
    private static final int SUCCESS_CODE = 200;
    //注册临时表成功 但是未激活
    private static final int REGISTER_TMP_CODE = 201;
    //失败的标识
    private static final int ERROR_CODE = 400;

    //禁止的标识
    private static final int FAIL_CODE = 500;


    //服务器出错的标识
    private static final int SERVICE_CRASH = 503;

    //服务重试状态码
    private static final int SERVICE_RETRY = 504;

    //服务器获取验证码
    private static final int SERVICE_VALIDATE=505;



    /**
     * 返回成功标识
     *
     * @param data 需要返回的数据
     * @param <E>  返回给前端的数据
     * @return
     */
    public static <E> ResponseData success(E data) {
        ResponseData responseData = new ResponseData();
        responseData.data = data;
        responseData.code = SUCCESS_CODE;
        return responseData;

    }
    /**
     * 返回注册临时表成功标识
     *
     * @param <E>  返回给前端的数据
     * @return
     */
    public static <E> ResponseData successRegisterTmp() {
        ResponseData responseData = new ResponseData();
        responseData.data = new JSONObject();
        responseData.code = REGISTER_TMP_CODE;
        return responseData;

    }

    /**
     * 返回成功
     *
     * @param <E>
     * @return
     */
    public static <E> ResponseData success() {
        ResponseData responseData = new ResponseData();
        responseData.data = new JSONObject();
        responseData.code = SUCCESS_CODE;
        return responseData;

    }

    /**
     * 返回成功信息
     *
     * @param msg
     * @param <E>
     * @return
     */
    public static <E> ResponseData successMsg(String msg) {
        ResponseData responseData = new ResponseData();
        responseData.data = new JSONObject();
        responseData.code = SUCCESS_CODE;
        responseData.msg = msg;
        return responseData;
    }

    /**
     * 返回成功消息和数据
     * @param data
     * @param msg
     * @param <E>
     * @return
     */
    public static <E> ResponseData successDataMsg(E data, String msg) {
        ResponseData responseData = new ResponseData();
        responseData.data = data;
        responseData.code = SUCCESS_CODE;
        responseData.msg = msg;
        return responseData;
    }

    public static <E> ResponseData arraySuccess(E data) {
        ResponseData responseData = new ResponseData();
        List<E> list = new ArrayList<E>();
        list.add(data);
        responseData.data = list;
        responseData.code = SUCCESS_CODE;
        return responseData;

    }


    /**
     * 返回成功标识
     *
     * @param data 需要返回的数据
     * @param code 需要返回的状态
     * @param <E>  返回给前端的数据
     * @return
     */
    public static <E> ResponseData success(int code, E data) {
        ResponseData responseData = new ResponseData();
        responseData.data = data;
        responseData.code = code;
        return responseData;

    }


    /**
     * 返回成功标识
     *
     * @return
     */
    public static <E> ResponseData successByArray() {
        ResponseData responseData = new ResponseData();
        responseData.data = new ArrayList<>();
        responseData.code = SUCCESS_CODE;
        return responseData;

    }


    /**
     * 返回成功标识
     *
     * @return
     */
    public static <E> ResponseData successByMap(Map<String, Object> map) {
        ResponseData responseData = new ResponseData();
        responseData.data = map;
        responseData.code = SUCCESS_CODE;
        return responseData;

    }

    /**
     * 返回失败的标识
     *
     * @param data 需要返回的数据
     * @return
     */
    public static <E> ResponseData error(E data) {
        ResponseData responseData = new ResponseData();
        responseData.data = data;
        responseData.code = ERROR_CODE;
        return responseData;

    }


    /**
     * 返回失败的标识
     *
     * @param code 需要返回的code
     * @param data 需要返回的数据
     * @return
     */
    public static <E> ResponseData error(int code, E data) {
        ResponseData responseData = new ResponseData();
        responseData.data = data;
        responseData.code = code;
        return responseData;

    }


    /**
     * 返回失败的标识
     *
     * @return
     */
    public static <E> ResponseData errorByArray() {

        ResponseData responseData = new ResponseData();
        responseData.data = new JSONObject();
        responseData.code = ERROR_CODE;
        return responseData;

    }

    /**
     * 返回自定义的标识
     *
     * @return
     */
    public static ResponseData response(int code) {
        ResponseData responseData = new ResponseData();
        responseData.data = new JSONObject();
        responseData.code = code;
        return responseData;

    }

    /**
     * 返回自定义的标识
     *
     * @return
     */
    public static <E> ResponseData response(int code, E data) {
        ResponseData responseData = new ResponseData();
        responseData.data = data;
        responseData.code = code;
        return responseData;

    }

    /**
     * 返回失败的标识
     *
     * @return
     */
    public static <E> ResponseData errorByMap() {
        ResponseData responseData = new ResponseData();
        responseData.data = new JSONObject();
        responseData.code = ERROR_CODE;
        return responseData;

    }

    /**
     * 返回错误
     *
     * @param <E>
     * @return
     */
    public static <E> ResponseData error() {
        ResponseData responseData = new ResponseData();
        responseData.data = new JSONObject();
        responseData.code = ERROR_CODE;
        return responseData;

    }

    /**
     * 错误提示
     *
     * @param msg 错误信息
     * @param <E>
     * @return
     */
    public static <E> ResponseData errorMsg(String msg) {
        ResponseData responseData = new ResponseData();
        responseData.data = new JSONObject();
        responseData.code = ERROR_CODE;
        responseData.msg = msg;
        return responseData;
    }


    /**
     * 返回禁止的标识
     *
     * @return
     */
    public static <E> ResponseData fail() {
        ResponseData responseData = new ResponseData();
        responseData.data = new JSONObject();
        responseData.code = FAIL_CODE;
        return responseData;

    }

    /**
     * 返回服务器出错的标识
     *
     * @return
     */
    public static <E> ResponseData crash() {
        ResponseData responseData = new ResponseData();
        responseData.data = new JSONObject();
        responseData.code = SERVICE_CRASH;
        return responseData;

    }


    /**
     * 返回服务器需要重试的标识
     *
     * @return
     */
    public static <E> ResponseData retry() {
        ResponseData responseData = new ResponseData();
        responseData.data = new JSONObject();
        responseData.code = SERVICE_RETRY;
        return responseData;

    }


    /**
     * 返回服务器验证
     *
     * @return
     */
    public static <E> ResponseData validate() {
        ResponseData responseData = new ResponseData();
        responseData.data = new JSONObject();
        responseData.code = SERVICE_VALIDATE;
        return responseData;

    }


    private static class ResponseData<T> extends Response {

        /**
         * 错误提示信息
         */
        private String msg = "".intern();

        /**
         * 错误的参数代码
         */
        private int code;


        /**
         * 存储的数据
         */
        private T data;


        public String getMsg() {
            return msg;
        }

        public int getCode() {
            return code;
        }

        public T getData() {
            return data;
        }

        private ResponseData() {

        }
    }

    private Response() {

    }




    public Object readResolve() {
        return this;
    }

}
