package com.e_commerce.miscroservice.operate.entity.response;

import lombok.Data;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/10/19 15:46
 * @Copyright 玖远网络
 */
@Data
public class UserDetailVo {
//    	b.user_nickname,
//	c.BusinessName,
//	a.id,a.grade,
//a.team_user_count
    private String nickName;
    private String storeName;
    private Long id;
    /**
     * 团队人数
     */
    private Long teamUserCount;
    /**
     * 级别
     */
    private Integer grade;
    /**
     * 统计 合伙人人数
     */
    private Long partner;
    /**
     * 统计 分销商人数
     */
    private Long distribution;
    /**
     * 统计 店长人数
     */
    private Long store;
    /**
     * 统计 普通人数
     */
    private Long common;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 粉丝人数
     */
    private Long fansUserCount;
    /**
     * 1级粉丝人数
     */
    private Long fans1UserCount;
    /**
     * 2级粉丝人数
     */
    private Long fans2UserCount;

    /**
     * 分销商用户id
     */
    private Long  distributorId;

    /**
     * 合伙人用户id
     */
    private Long  partnerId;

}
