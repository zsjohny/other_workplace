package com.finace.miscroservice.commons.enums;

import com.finace.miscroservice.commons.utils.JwtToken;
import org.apache.commons.lang.StringUtils;

/**
 * 加解密公共的枚举
 */
public enum EncryptPubEnum {

    DEVICE_SECRET_KEY(JwtToken.UID, "yiTongJin2017"), USER_SHARE_KEY("shareid", "2806c91f111c85f4e87727ede4505dee");

    private String field;
    private String key;

    EncryptPubEnum(String field, String key) {
        this.field = field;
        this.key = key;
    }

    public String toKey() {
        return key;
    }

    public static String getKeyByFiled(String field) {
        String result = "";

        if (StringUtils.isEmpty(field)) {
            return result;
        }

        EncryptPubEnum[] values = EncryptPubEnum.values();


        for (EncryptPubEnum encryptPubEnum : values) {
            if (encryptPubEnum.field.equals(field)) {
                result = encryptPubEnum.key;
                break;
            }
        }

        return result;


    }
}
