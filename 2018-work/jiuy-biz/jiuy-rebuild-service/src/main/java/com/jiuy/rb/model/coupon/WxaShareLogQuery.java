package com.jiuy.rb.model.coupon; 

import lombok.Data; 

/**
 * WxaShareLog的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月05日 下午 05:27:16
 * @Copyright 玖远网络 
*/
@Data
public class WxaShareLogQuery extends WxaShareLog {

    /**
     * 当天
     */
    private String today;
} 
