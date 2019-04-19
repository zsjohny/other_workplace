package com.yujj.entity.member;

import lombok.Data;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/9/3 20:38
 * @Copyright 玖远网络
 */
@Data
public class MembersFindRequest {
    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 会员注册时间 起
     */
    private String startTime;

    /**
     * 会员注册时间 止
     */
    private String endTime;

    /**
     * 会员状态  0 正常 1删除 2 冻结 。。。
     */
    private Integer delState;

}
