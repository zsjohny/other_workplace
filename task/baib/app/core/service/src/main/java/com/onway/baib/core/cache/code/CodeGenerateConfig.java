/**
 * onway.com Inc.
 * Copyright (c) 2013-2015 All Rights Reserved.
 */
package com.onway.baib.core.cache.code;

/**
 * 编码生成规则配置
 * 
 * @author li.hong
 * @version $Id: CodeGenerateConfig.java, v 0.1 2016年9月9日 上午11:55:23 li.hong Exp $
 */
public interface CodeGenerateConfig {

    /** 循环长度  */
    public static int NO_CIRCLE        = 100000000;

    /** seq长度  */
    public static int NO_CIRCLE_LENGTH = 7;

    /** seq初始值 */
    public static int INIT_VALUE       = 1;

    /** seq增长步进 */
    public static int INCREMENT_STEP   = 1;

    /** 订单号前缀*/
    String            ORDER_PRE        = "8001";

    /** 预约号前缀*/
    String            SUBSCRIBE_PRE        = "8002";
    
    /**企业号前缀*/
    String ENTERPRISE_PRE = "8003";
    
    /**商品号前缀*/
    String GOOD_PRE = "8004";
    
    /**保险号前缀*/
    String INSURANCE_PRE ="8005";

}
