package com.jiuy.rb.model.user; 

import lombok.Data;

import java.util.List;

/**
 * ShopMemberRb的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月23日 上午 10:39:37
 * @Copyright 玖远网络 
*/
@Data
public class ShopMemberRbQuery extends ShopMemberRb {  

    /**
     * 用户ids
     */
    private List<Long> ids;
} 
