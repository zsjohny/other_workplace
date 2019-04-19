package com.jiuy.rb.util;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * 个推的参数封装
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/29 11:47
 * @Copyright 玖远网络
 */
@Data
public class GeTuiVo {


    public GeTuiVo(String title, String abstracts, String linkUrl, String image, Integer type, String pushTime) {
        this.title = title;
        this.abstracts = abstracts;
        this.linkUrl = linkUrl;
        this.image = image;
        this.type = type.toString();
        this.pushTime = pushTime;
    }

    /**
     * 消息提示
     */
    private String title;

    /**
     * 消息概述
     */
    private String abstracts;

    /**
     * 消息链接
     */
    private String linkUrl;

    /**
     * 消息图片
     */
    private String image;

    /**
     * 消息类型
     */
    private String type;

    /**
     * 推送时间
     */
    private String pushTime;

    /**
     * 转换成jsonObject
     * @author Aison
     * @date 2018/6/29 11:51
     * @return com.alibaba.fastjson.JSONObject
     */
    public JSONObject toJsonObject() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", title);
        jsonObject.put("abstracts", abstracts);
        jsonObject.put("linkUrl", linkUrl);
        jsonObject.put("image", image);
        jsonObject.put("type", type);
        jsonObject.put("pushTime", pushTime);
        return jsonObject;
    }
}
