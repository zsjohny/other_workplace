package com.jiuyuan.constant;

import com.jiuyuan.util.constant.ConstantBinder;

public class Client {

    static {
        ConstantBinder.bind(Client.class, ConstantBinder.DEFAULT_CHARSET, "/client.properties");
    }

    public static String ANDROID_LATEST_VERSION;

    public static String ANDROID_LATEST_URL;
    
    public static Long ANDROID_LATEST_SIZE;
    
    public static Boolean ANDROID_FORCE_UPDATE;

    public static String IPHONE_LATEST_VERSION;

    public static String IPHONE_LATEST_URL;

    public static Boolean IPHONE_FORCE_UPDATE;
    
    public static String OSS_DEFAULT_BASEPATH_NAME;
    public static String OSS_ACCESS_KEY_ID;
    public static String OSS_ACCESS_KEY_SECRET;
    public static String OSS_END_POINT;
    public static String OSS_IMG_SERVICE;    

    public static String GETUI_APP_ID;
    public static String GETUI_APP_KEY;
    public static String GETUI_APP_SECRET;
    public static String GETUI_MASTER_SECRET;

    public static String STORE_GETUI_APP_ID;
    public static String STORE_GETUI_APP_KEY;
    public static String STORE_GETUI_APP_SECRET;
    public static String STORE_GETUI_MASTER_SECRET;

    public static String QIYUKF_APP_KEY;    
    public static String QIYUKF_PRODUCT_GROUPID;    
    public static String QIYUKF_HELP_GROUPID;    

    public static String ANDROID_UMENG_APP_KEY;
    public static String IPHONE_UMENG_APP_KEY;
}