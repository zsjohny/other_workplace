package com.jiuy.rb.model.account; 

import lombok.Data; 

/**
 * CoinsLog的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月05日 下午 04:23:52
 * @Copyright 玖远网络 
*/
@Data
public class CoinsLogQuery extends CoinsLog {


    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 创建时间
     */
    private String createTimeStr;
} 
