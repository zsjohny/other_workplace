package com.e_commerce.miscroservice.distribution.utils;

import com.e_commerce.miscroservice.commons.helper.util.colligate.other.ApplicationContextUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 体现的白名单工具类--临时方案使用
 * TODO:如果可以补充配置,也可以不补充,因为前期只有小范围推广
 */
public class WithdrawWhiteBackListUtil {

    private WithdrawWhiteBackListUtil() {

    }

    private static Map<Long, Boolean> storeCache;

    static {

        storeCache = new HashMap<>();

        if (ApplicationContextUtil.isDevEnviron()) {
            //小粉蝶
            storeCache.put(11878L, Boolean.TRUE);
            storeCache.put(121L, Boolean.TRUE);
        } else {
            //测试线
            storeCache.put(3L, Boolean.TRUE);
        }
        storeCache = Collections.unmodifiableMap(storeCache);
    }


    public static Boolean exist(Long storeId) {
        return storeCache.containsKey(storeId);
    }

    public static Map<String, String> success(String orderNo) {
        Map<String, String> successMap = new HashMap<>();
        successMap.put("result_code", "SUCCESS");
        successMap.put("payment_no", orderNo);

        return successMap;
    }


}
