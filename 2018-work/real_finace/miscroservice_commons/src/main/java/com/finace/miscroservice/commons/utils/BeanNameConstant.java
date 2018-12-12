package com.finace.miscroservice.commons.utils;

/**
 * bean的常量名称
 */
public class BeanNameConstant {
    private BeanNameConstant() {

    }

    /**
     * flyway的名称
     */
    public static final String FLYWAY_BEAN_NAME = "flyway";
    /**
     * mqManagerDistribute的名称
     */
    public static final String MQ_MANAGER_DISTRIBUTE_BEAN_NAME = "mqManagerDistribute";
    /**
     * sql初始化执行的名称
     */
    public static final String SQL_INIT_BEAN_NAME = "sqlInitScript";


}
