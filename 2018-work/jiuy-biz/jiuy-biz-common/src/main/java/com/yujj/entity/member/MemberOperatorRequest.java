package com.yujj.entity.member;

import lombok.Data;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/9/3 19:36
 * @Copyright 玖远网络
 */
@Data
public class MemberOperatorRequest {
    /**
     * 姓名
     */
    private String  name;

    /**
     * 手机号
     */
    private String  phone;

    /**
     * 市
     */
    private String  city;

    /**
     * 省
     */
    private String  province;

    /**
     * 区
     */
    private String  district;

    /**
     * 会员等级
     */
    private Integer memberLevel;
  /**
     * 会员类型
     */
    private Integer type;
    /**
     * 状态
     */
    private Integer delState;
    /**
     * 会员Id
     */
    private Long memberId;

    /**
     * 用户id
     */
    private Long id;

}
