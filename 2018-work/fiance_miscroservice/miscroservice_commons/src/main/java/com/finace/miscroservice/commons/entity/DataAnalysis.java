package com.finace.miscroservice.commons.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 数据分析
 */
@Data
public class DataAnalysis implements Serializable {


    private static final long serialVersionUID = 9116784261641514050L;
    /**
     * 设备标识
     */
    private String uid;


    /**
     * 用户Id
     */
    private String userId;


    /**
     * 请求路径
     */
    private String url;

    /**
     * 浏览器的唯一标识
     */
    private String did;

    /**
     * 渠道
     */
    private String channel;


}
