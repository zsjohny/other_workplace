package com.jiuy.user.model; 

import lombok.Data; 

/**
 * User的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月08日 下午 05:02:05
 * @Copyright 玖远网络 
*/
@Data
public class UserQuery extends User {  
 
} 
