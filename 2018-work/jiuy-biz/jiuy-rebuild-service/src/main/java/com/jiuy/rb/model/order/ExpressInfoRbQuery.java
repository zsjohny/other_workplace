package com.jiuy.rb.model.order; 

import lombok.Data;

import java.util.Set;

/**
 * ExpressInfoRb的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月28日 下午 05:24:48
 * @Copyright 玖远网络 
*/
@Data
public class ExpressInfoRbQuery extends ExpressInfoRb {


    /**
     * 物流公司中文名称
     */
    private String expressCnName;

    /**
     * 订单号码
     */
    private Set<Long> orderNos;

} 
