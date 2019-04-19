package com.jiuy.rb.model.coupon; 

import lombok.Data;

/**
 * WxaShare的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月05日 下午 05:13:44
 * @Copyright 玖远网络 
*/
@Data
public class WxaShareQuery extends WxaShare {  

    /**
     * 分享类型中文
     */
    private String shareTypeName;


    /**
     * 成功邀请时间
     */
    private String successTime;

    /**
     * 创建时间的可读
     */
    private String createTimeReadable;

} 
