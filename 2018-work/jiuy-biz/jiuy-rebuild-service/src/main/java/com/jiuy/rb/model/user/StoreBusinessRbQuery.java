package com.jiuy.rb.model.user; 

import lombok.Data;

import java.util.List;

/**
 * StoreBusinessRb的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月28日 下午 04:08:40
 * @Copyright 玖远网络 
*/
@Data
public class StoreBusinessRbQuery extends StoreBusinessRb {

    /** 门店id的集合 */
    private Long[] storeIds;
    /** 门店手机号的集合 */
    private String[] phoneNumbers;

} 
