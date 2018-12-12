package com.wuai.company.enums;

/**
 * Created by 97947 on 2017/7/18.
 */
public enum ServerHandlerTypeEnum {
    INVITATION("邀请",1),//邀请接单---邀请
    ACCEPT("应约",2),//接受邀请---应约
    REFUSE_INV("拒绝邀请",5),//拒绝邀请
    REFUSE_JOIN("拒绝加入",6),//拒绝加入
//    JOIN("参加",3),//接受邀请---参加
    STORE_SURE("确认",3),//商家确认订单
    STORE_CANCEL("取消",4)//商家取消订单
    ;
    private Integer type;
    private String code;

    public String getCode() {
        return code;
    }

    public Integer getType() {
        return type;
    }

    ServerHandlerTypeEnum(Integer type) {
        this.type = type;
    }

    ServerHandlerTypeEnum( String code,Integer type) {
        this.type = type;
        this.code = code;
    }
}
