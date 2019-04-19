package com.jiuy.rb.model.account; 

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jiuy.base.annotation.IgnoreCopy;
import lombok.Data;

/**
 * Coins的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月05日 上午 10:06:39
 * @Copyright 玖远网络 
*/
@Data
public class CoinsQuery extends Coins {  


    /**
     * 玖币账户和资金类型的分组
     */
    @JsonIgnore
    @IgnoreCopy
    @JsonIgnoreProperties
    private String coinsGroup;

    /**
     * 分组金额
     */
    @JsonIgnore
    @IgnoreCopy
    @JsonIgnoreProperties
    private Long count;

    /**
     * 查询昵称
     */
    @JsonIgnore
    @IgnoreCopy
    @JsonIgnoreProperties
    private String nickNameLike;

    /**
     * 注册结束时间
     */
    @JsonIgnore
    @IgnoreCopy
    @JsonIgnoreProperties
    private String createTimeBegin;

    /**
     * 注册开始时间
     */
    @JsonIgnore
    @IgnoreCopy
    @JsonIgnoreProperties
    private String createTimeEnd;

    /**
     * 待入账
     */
    private Long waitInCount;

    /**
     * 已失效
     */
    private Long lostCount;

    /**
     * 已到期
     */
    private Long expireCount;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 邀请数量
     */
    private Long inviteCount;

    /**
     * 所有的入账
     */
    private Long allIn;

    /**
     * 所有的出账
     */
    private Long allOut;


    /**
     * 用户来源 0 是直接注册用户  1是邀请用户
     */
    private String source;

    /**
     * 是否查询商家id(storeId),商家名称(storeName),开启后要慢一些
     */
    @JsonIgnore
    @IgnoreCopy
    @JsonIgnoreProperties
    private Boolean queryStore;
    /**
     * 用户所属商家id
     */
    private Long storeId;
    /**
     * 用户所属商家名
     */
    private String storeName;


    public CoinsQuery() {

    }

    public CoinsQuery(Long userId,Integer userType) {
        this.setUserType(userType);
        this.setUserId(userId);
    }
} 
