package com.wuai.company.enums;

/**
 * Created by 97947 on 2017/8/28.
 */
public enum MsgTypeEnum {
    INVITATION_SEND_MSG("发出信息",1),  // 邀请 发送信息
    INVITATION_RECEIVE_MSG("接收信息",2),//邀请 接收信息
    JOIN_SEND_MSG("发出信息",3),//参加 接收信息
    JOIN_RECEIVE_MSG("接收信息",4),//参加 接收信息
    EXPEL_USER_MSG("踢出用户",5),//踢出用户 信息expelUser
    ARRIVED_PLACE_MSG("用户到达",6),//踢出用户 信息expelUser
    REFUSE_JOIN_MSG("拒绝",8),//拒绝
    SECEDE_MSG("退出订单",9),//退出订单secede
    SYS_MSG("系统消息",10000),//系统消息
    ;

    private String value;
    private Integer code;

    public String getValue() {
        return value;
    }
    public Integer getCode() {
        return code;
    }

    MsgTypeEnum(){}

    MsgTypeEnum(String value, Integer code) {
        this.code = code;
        this.value = value;
    }
}
