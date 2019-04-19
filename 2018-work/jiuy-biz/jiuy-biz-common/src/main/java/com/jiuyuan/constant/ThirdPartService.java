package com.jiuyuan.constant;

import com.jiuyuan.util.constant.ConstantBinder;

public class ThirdPartService {
    static {
        ConstantBinder.bind(ThirdPartService.class, ConstantBinder.DEFAULT_CHARSET, "/thirdparty_service.properties");
    }

    public static String OSS_DEFAULT_BASEPATH_NAME;
    public static String OSS_ACCESS_KEY_ID;
    public static String OSS_ACCESS_KEY_SECRET;
    public static String OSS_END_POINT;
    public static String OSS_IMG_SERVICE;    
    
    public static String GETUI_HOST;
    public static String GETUI_APP_ID;
    public static String GETUI_APP_KEY;
    public static String GETUI_APP_SECRET;
    public static String GETUI_MASTER_SECRET;
}
