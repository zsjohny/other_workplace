package com.onway.baib.core.enums;

import com.onway.common.lang.StringUtils;
import com.onway.platform.common.enums.EnumBase;

/**
 * Created by 林俊杰
 * email : 490456661@qq.com
 * date : 2016/7/18
 * time : 17:47
 */
public enum PayTypeEnum implements EnumBase{

    ZHIFUBAO("ZHIFUBAO","支付宝","1"),

    WEIXIN("WEIXIN","微信","2"),

    BANKyudaoD("BANKyudaoD","银行卡","3")

    ;

    private String code;

    private String message;

    private String value;

    private PayTypeEnum(String code, String message, String value){
        this.code = code;
        this.message = message;
        this.value = value;
    }
    /**
     * 获取枚举消息
     *
     * @return
     */
    @Override
    public String message() {
        return null;
    }

    /**
     * 获取枚举值
     *
     * @return
     */
    @Override
    public Number value() {
        return null;
    }
    
    
    public static PayTypeEnum getPayTypeEnumByValue(String value){
        for(PayTypeEnum item : PayTypeEnum.values()){
            if(StringUtils.equals(item.getValue(),value)){
                return item;
            }
        }
        return null;
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
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
