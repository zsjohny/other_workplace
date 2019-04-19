package com.jiuyuan.constant.order;


import com.jiuyuan.util.enumeration.IntEnum;

public enum PaymentTypeDetail implements IntEnum{
    UNKNOWN(0, "未知"),

    ALIPAY(1,"支付宝"),

    ALIPAY_SDK(2, "支付宝"),

    WEIXINPAY_SDK(3,"微信支付"),

    WEIXINPAY_NATIVE(4, "微信支付"),
    
    BANKCARD_PAY(5 , "一网通"),
    
	WEIXIN_WEB_PAY(6, "微信WEB");
	
	private PaymentTypeDetail(int intValue, String showName) {
        this.intValue = intValue;
        this.showName = showName;
    }
	
	public static PaymentTypeDetail getByIntValue(int intValue) {
		PaymentTypeDetail paymentType = null;
        for (PaymentTypeDetail os : PaymentTypeDetail.values()) {
            if (os.getIntValue() == intValue) {
                paymentType = os;
                break;
            }
        }
        return paymentType;
    }
	private int intValue;
	
	private String showName;
	
    @Override
    public int getIntValue() {
        return intValue;
    }
	

    private PaymentTypeDetail(int intValue) {
        this.intValue = intValue;
    }
    
    
    public String getShowName() {
		return showName;
	}


	public void setShowName(String showName) {
		this.showName = showName;
	}


	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}


	/**
     * 通过代号获取活动
     * @param shortName
     * @return
     */
    public static PaymentTypeDetail getNameByValue(int intValue) {
        if (intValue<=0) {
            return null;
        }

        for (PaymentTypeDetail paymentTypeEnum : PaymentTypeDetail.values()) {
            if (paymentTypeEnum.getIntValue()==intValue) {
                return paymentTypeEnum;
            }
        }

        return null;
    }



}
