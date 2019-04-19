package com.jiuy.timer.model; 

import lombok.Data;

import java.util.Map;

/**
 * QrtzJobDetails的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年05月31日 上午 09:48:02
 * @Copyright 玖远网络 
*/
@Data
public class QrtzJobDetailsQuery extends QrtzJobDetails {  

    /**
     * dataJson
     */
    private String dataStr;

    /**
     * 当前任务的状态
     * */
    private String jobStatus;

    /**
     * jobData
     **/
    private Map<String,Object> jobDataMap;
} 
