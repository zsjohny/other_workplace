package com.e_commerce.miscroservice.operate.entity.request;

import lombok.Data;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/10/18 14:57
 * @Copyright 玖远网络
 */
@Data
public class UserInformationRequest {
    /**
     * storeName 所属商家
     */
    private String storeName;
    /**
     * 会员id
     */
    private Long memberId;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 角色
     */
    private Integer role;
    /**
     *  团队数量 起
     */
    private Integer teamSizeMin;
    /**
     * 团队数量 止
     */
    private Integer teamSizeMax;
    /**
     * 粉丝数量 起
     */
    private Integer followerMin;
    /**
     * 粉丝数量 止
     */
    private Integer followerMax;
    /**
     * 创建时间 起
     */
    private String createTimeStart;

    /**
     * 创建时间 止
     */
    private String createTimeEnd;
    /**
     * 推荐人
     */
    private Long higher;
    /**
     * 管理人直属
     */
    private Long leader;
    /**
     * 总金额 起
     */
    private Double countMoneyMin;
    /**
     * 总金额 止
     */
    private Double countMoneyMax;

    private Integer pageNum;
    private Integer pageSize;

}
