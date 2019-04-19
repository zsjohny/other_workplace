package com.jiuy.base.util;
import com.alibaba.fastjson.JSONObject;
import com.jiuy.base.exception.BizException;
import okhttp3.*;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 基于okHttp的request请求 工具
 * @author Aison
 * @version V1.0
 * @Copyright 玖远网络
 * @date 2018/5/29 15:22
 */
public class HttpRequest {





    /**
     * post请求
     * @param url 请求地址
     * @param param 请求参数
     * @author Aison
     * @date 2018/5/29 15:23
     */
    public static String sendPostJson(String url, Map<String,Object> param) {

        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).build();
        FormBody.Builder builder  = new FormBody.Builder();
        for(Map.Entry<String,Object> entry:param.entrySet()) {
            builder.add(entry.getKey(),entry.getValue().toString());
        }
        RequestBody formBody = builder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return Biz.getFullException(e);
        }
    }

    public static void main(String[] args) {

    }


}
