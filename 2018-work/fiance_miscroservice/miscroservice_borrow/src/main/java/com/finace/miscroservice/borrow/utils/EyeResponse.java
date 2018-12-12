package com.finace.miscroservice.borrow.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * 天眼查询返回数据结构
 */
public class EyeResponse {

    private static final int CODE_200 = 200;  //成功

    public static final int CODE_4001 = 4001;  //查询时间戳超时
    public static final int CODE_4002 = 4002;  //签名校验失败
    public static final int CODE_4003 = 4003;  //白名单校验失败
    public static final int CODE_4004 = 4004;  //接口服务不存在
    public static final int CODE_4005 = 4005;  //查询的用户不存在
    public static final int CODE_4006 = 4006;  //查询的订单不存在
    public static final int CODE_4007 = 4007;  //请求数据解析错误
    public static final int CODE_4008 = 4008;  //查询结束时间小于开始时间
    public static final int CODE_4009 = 4009;  //查询时间跨度超出限制
    public static final int CODE_5000 = 5000;  //系统服务暂停


    /**
     * 返回成功标识
     *
     * @param data 需要返回的数据
     * @param <E>  返回给前端的数据
     * @return
     */
    public static <E> EyeResponseData success(E data) {
        EyeResponseData responseData = new EyeResponseData();
        responseData.data = data;
        responseData.code = CODE_200;
        return responseData;

    }

    /**
     * @param code
     * @param <E>
     * @return
     */
    public static <E> EyeResponseData error(int code) {
        EyeResponseData responseData = new EyeResponseData();
        responseData.data = new JSONObject();
        responseData.code = code;
        return responseData;
    }


    private static class EyeResponseData<T> extends EyeResponse {

        /**
         * 错误提示信息
         */
        private String message = "".intern();

        /**
         * 错误的参数代码
         */
        private int code;


        /**
         * 存储的数据
         */
        private T data;

        private EyeResponseData() {

        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }
}
