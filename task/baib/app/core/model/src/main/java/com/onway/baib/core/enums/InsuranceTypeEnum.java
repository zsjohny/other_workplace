package com.onway.baib.core.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 保险险种枚举类
 * @author weina
 * @version $Id: InsuranceTypeEnum.java, v 0.1 2016年12月5日 上午11:15:43 ROG Exp $
 */
public enum InsuranceTypeEnum {
    
    CDINSURANCE("0","车损险 座位险"),
    
    FIFTYINSURANCE("1", "第三方责任险50万"),
    
    HUNDREDINSURANCE("2", "第三方责任险100万"),
    
    HUNDREDANDFIFTY("3", "第三方责任险150万"),
    
    COMPULSORYINSURANCE("4","不计免赔险 交强险"),
    
    ;
    
    private String code;
    
    private String message;
    
    
    /**
     * 通过枚举<code>code</code>获得枚举。
     * 
     * @param code         枚举编号
     * @return
     */
    public static InsuranceTypeEnum getCommonStatusByCode(String code) {
        for (InsuranceTypeEnum param : values()) {
            if (StringUtils.equals(param.getCode(), code)) {
                return param;
            }
        }
        return null;
    }
    

    private InsuranceTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
    
}
