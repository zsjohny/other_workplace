package com.jiuy.model.logs; 

import lombok.Data; 

/**
 * OperatorLogs的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月06日 上午 09:57:52
 * @Copyright 玖远网络 
*/
@Data
public class OperatorLogsQuery extends OperatorLogs {  

} 
