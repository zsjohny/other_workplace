package com.finace.miscroservice.commons.utils;

import com.finace.miscroservice.commons.enums.DeviceEnum;
import com.finace.miscroservice.commons.enums.EncryptPubEnum;

/**
 * 客户端Uid的工具类
 */
public class UidUtils {

    private UidUtils() {

    }

    private static final String EMPTY = "";

    /**
     * 获取Uid
     *
     * @param data 解密数据
     * @param key  解密密钥
     * @return
     */
    public static String decryptUid(String data, String key) {
        if (data == null || key == null) {
            return EMPTY;
        }
        return Rc4Utils.toString(data, key);
    }


    /**
     * 解密Uid
     *
     * @param data 加密数据
     * @return
     */
    public static String decryptUid(String data) {
        if (data == null) {
            return EMPTY;
        }
        return Rc4Utils.toString(data, EncryptPubEnum.DEVICE_SECRET_KEY.toKey());
    }


    /**
     * 加密uid
     *
     * @param data 加密数据
     * @return
     */
    public static String encryptUid(String data) {

        if (data == null) {
            return EMPTY;
        }

        return Rc4Utils.toHexString(data, EncryptPubEnum.DEVICE_SECRET_KEY.toKey());

    }


    /**
     * 根据设备判断是否uid符合规范
     *
     * @param data       uid数据
     * @param deviceEnum 设备标识数组
     * @return
     */
    public static Boolean isRegularUidByDevice(String data, DeviceEnum... deviceEnum) {

        Boolean matchFlag = Boolean.FALSE;

        if (data == null || deviceEnum == null) {
            return matchFlag;

        }

        StringBuilder builder = new StringBuilder();
        for (DeviceEnum device : deviceEnum) {
            builder.append(device.toVal());
        }
        return Rc4Utils.toString(data, EncryptPubEnum.DEVICE_SECRET_KEY.toKey()).matches(
                ("[" + builder.toString() + "]\\w{20,30}").intern());

    }


}
