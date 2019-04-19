package com.jiuy.timer.model; 

import lombok.Data; 

/**
 * QrtzOptLog的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年05月30日 下午 03:17:31
 * @Copyright 玖远网络 
*/
@Data
public class QrtzOptLogQuery extends QrtzOptLog {

    /**
     * 操作类型名称
     */
    private String optTypeName;

} 
