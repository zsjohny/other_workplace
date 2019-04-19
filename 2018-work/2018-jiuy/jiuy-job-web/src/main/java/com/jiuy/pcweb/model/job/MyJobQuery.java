package com.jiuy.pcweb.model.job; 

import lombok.Data; 

/**
 * MyJob的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年08月01日 上午 11:52:10
 * @Copyright 玖远网络 
*/
@Data
public class MyJobQuery extends MyJob {  
 
} 
