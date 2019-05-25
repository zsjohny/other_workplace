package com.jiuy.rb.model.account; 

import lombok.Data; 

/**
 * WxaPayConfig的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月23日 下午 05:09:49
 * @Copyright 玖远网络 
*/
@Data
public class WxaPayConfigQuery extends WxaPayConfig {  
 
} 