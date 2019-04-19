package com.jiuyuan.constant;

import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.util.enumeration.IntEnum;

public enum MessageType implements IntEnum{
    A(1, "【俞姐姐门店宝】生成售后单，待卖家确认 (发送给卖家) "),
    
    B(2, "【俞姐姐门店宝】卖家拒绝买家申请的售后，卖家已拒绝(发送给买家)"),

    C(3, "【俞姐姐门店宝】卖家同意买家申请的售后,通知发货(发送给买家)"),

    D(4, "【俞姐姐门店宝】买家发货(发送给卖家)"),

    E(5, "【俞姐姐门店宝】买家3天内没发货，售后状态失效(发送给买家)"),

    F(6, "【俞姐姐门店宝】您提交的退货申请，款项已到帐。希望您的退货体验还不错，一定要继续进货哦(发给买家)"),

    G(7, "【俞姐姐门店宝】给买家增加1次售后机会的短信提醒内容(发送给买家)"),

    H(8, "【俞姐姐门店宝】经平台介入沟通，您提交的退货申请已被平台关闭,【俞姐姐门店宝】经平台介入沟通，买家提交的退货申请已被平台关闭(发送给双方)");

    private MessageType(int intValue, String displayName) {
        this.intValue = intValue;
        this.displayName = displayName;
    }

    private int intValue;

    private String displayName;

    @Override
    public int getIntValue() {
        return intValue;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean is(int intValue) {
        return getIntValue() == intValue;
    }
    
    public static MessageType getNameByValue(int intValue ) {
        if (intValue<0) {
            return null;
        }

        for (MessageType messageType : MessageType.values()) {
            if (messageType.getIntValue()==intValue) {
                return messageType;
            }
        }

        return null;
    }
    
    public static MessageType getByIntValue(int intValue){
    	MessageType messageType = null;
        for(MessageType os : MessageType.values()){
            if(os.getIntValue() == intValue){
            	messageType = os;
                break;
            }
        }
        return messageType;
    }
    

    public static MessageType parse(int intValue, MessageType defaultValue) {
        for (MessageType messageType : values()) {
            if (messageType.is(intValue)) {
                return messageType;
            }
        }
        return defaultValue;
    }

}
