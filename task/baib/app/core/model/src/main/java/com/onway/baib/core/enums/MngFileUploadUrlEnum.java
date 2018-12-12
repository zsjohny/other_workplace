package com.onway.baib.core.enums;

import com.onway.common.lang.StringUtils;
import com.onway.platform.common.enums.EnumBase;

/**
 * 后台文件上传枚举  
 *   要添加地址，在这里加枚举类型的同时，在system-config.properties 文件里面加入
 * 
 * @author wwf
 * @version $Id: MngFileUploadUrlEnum.java, v 0.1 2016年9月19日 上午11:32:11 wwf Exp $
 */
public enum MngFileUploadUrlEnum implements EnumBase {

    // 商品
    GOODS("goods", "goodImage/attachment"),

    // 活动页面
    ACTIVEINFO("active", "activeInfo/attachment"),

    // 企业
    FIRM("firm", "firm/attachment"),
    
    // 用户图片
    USERIMAGE("userImage", "/attachment"),

    ;

    /** 枚举码*/
    private String code;

    /** 描述信息*/
    private String message;

    private MngFileUploadUrlEnum(String code, String message) {
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
    public static MngFileUploadUrlEnum getMngFileUploadUrlEnumByCode(String code) {
        for (MngFileUploadUrlEnum param : values()) {
            if (StringUtils.equals(param.getCode(), code)) {
                return param;
            }
        }
        return null;
    }
}
