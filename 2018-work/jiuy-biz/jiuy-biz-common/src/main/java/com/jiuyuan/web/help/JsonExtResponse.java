package com.jiuyuan.web.help;

import java.util.Map;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/8/21 18:17
 * @Copyright 玖远网络
 */
public class JsonExtResponse extends JsonResponse{

    public static JsonExtResponse me(){
        return new JsonExtResponse ();
    }

    /**
     * 扩展的返回信息
     */
    private Map<String, Object> extData;

    public Map<String, Object> getExtData() {
        return extData;
    }

    public void setExtData(Map<String, Object> extData) {
        this.extData = extData;
    }
}
