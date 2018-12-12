package com.onway.baib.core.enums;


import com.onway.common.lang.StringUtils;
import com.onway.platform.common.enums.EnumBase;


/**
 * 缓存名称枚举
 * 
 * @author guangdong.li
 * @version $Id: LocalCacheNameEnum.java, v 0.1 2015年11月2日 下午3:38:11 liudehong Exp $
 */
public enum LocalCacheNameEnum implements EnumBase {

    /**系统参数*/
    SYS_CONFIG("SYS_CONFIG", "系统参数"),

    ;

    /** 枚举码*/
    private String code;

    /** 描述信息*/
    private String message;

    private LocalCacheNameEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /** 
     * @see com.onway.platform.common.enums.EnumBase#message()
     */
    @Override
    public String message() {
        return message;
    }

    /** 
     * @see com.onway.platform.common.enums.EnumBase#value()
     */
    @Override
    public Number value() {
        return null;
    }

    /**
     * 通过枚举<code>code</code>获得枚举。
     * 
     * @param code         枚举编号
     * @return
     */
    public static LocalCacheNameEnum getLocalCacheNameEnumByCode(String code) {
        for (LocalCacheNameEnum param : values()) {
            if (StringUtils.equals(param.getCode(), code)) {
                return param;
            }
        }
        return null;
    }
}
