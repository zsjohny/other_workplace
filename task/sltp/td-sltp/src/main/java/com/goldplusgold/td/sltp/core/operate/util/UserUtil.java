package com.goldplusgold.td.sltp.core.operate.util;

import java.util.UUID;

/**
 * 用户的生成随机记录的工具类
 * Created by Ness on 2017/5/12.
 */
public class UserUtil {

    public static String createUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
