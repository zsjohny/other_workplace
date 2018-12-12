package com.spring.elastic_job.task;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * job的解析工具
 */
public class JobParseUtil {


    /**
     * 数组转所需的字符串
     *
     * @param params 需要转的数组
     * @return
     */
    public static String arr2HexString(String... params) {

        int len = params.length;

        Map<Integer, String> paramsMap = new HashMap<>();

        for (int i = 0; i < len; i++) {
            paramsMap.put(i, params[i]);
        }
        String result = JSONObject.toJSONString(paramsMap).
                replaceAll(":", "=").
                replaceAll("\"", "");

        return result.substring(1, result.length() - 1);


    }

    private JobParseUtil() {

    }
}
