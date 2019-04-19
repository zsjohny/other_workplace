package com.jiuy.rb.util;


import lombok.Data;

/**
 * TOTO
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/11 10:29
 * @Copyright 玖远网络
 */
@Data
public class OssConfigVo {


    /**
     * oss url
     */
    private String ossUrl;

    /**
     * ossBucket
     */
    private String ossBucket;

    /**
     * ossAccessKeyId
     */
    private String ossAccessKeyId;

    /**
     * OssAccessKeySecret
     */
    private String ossAccessKeySecret;


}
