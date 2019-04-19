package com.jiuyuan.util.oauth.common;

/**
 * 授权页面的终端类型（注意：OAuth 2规范没有对终端类型的支持，但大部分厂商都有相关的实现）
 */
public enum Display {

    /**
     * 不指定此参数
     */
    IMPLICIT,

    /**
     * 平板终端的页面，适用于支持html5的平板
     */
    TABLET,

    /**
     * 移动终端的页面，适用于支持html5的手机
     */
    MOBILE,

    /**
     * 移动终端的页面，适用于非智能手机
     */
    MOBILE_BASIC,

    /**
     * 使用于PC浏览器
     */
    PC_WEB,

    /**
     * PC客户端版本页面，适用于PC桌面应用
     */
    PC_CLIENT,

    /**
     * 站内应用页面
     */
    PLUGIN;
}