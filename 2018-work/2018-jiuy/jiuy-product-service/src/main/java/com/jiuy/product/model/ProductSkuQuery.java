package com.jiuy.product.model; 

import lombok.Data; 

/**
 * ProductSku的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月05日 下午 04:04:18
 * @Copyright 玖远网络 
*/
@Data
public class ProductSkuQuery extends ProductSku {  
 
} 
