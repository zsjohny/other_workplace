package com.jiuy.product.model; 

import lombok.Data; 

/**
 * StoreCoupon的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年08月10日 下午 04:44:54
 * @Copyright 玖远网络 
*/
@Data
public class StoreCouponQuery extends StoreCoupon {  
 
} 
