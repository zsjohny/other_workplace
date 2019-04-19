package com.jiuy.rb.model.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jiuy.base.annotation.IgnoreCopy;
import lombok.Data;

/**
 * CoinsCashOut的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月18日 下午 03:43:49
 * @Copyright 玖远网络 
*/
@Data
public class CoinsCashOutQuery extends CoinsCashOut {  

    /**
     * 返回nickName
     */
    private String nickName;

    /**
     * 提现开始时间
     */
    private String beginTime;

    /**
     * 提现结束时间
     */
    private String endTime;

    /**
     * 状态名称
     */
    private String statusName;


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
} 
