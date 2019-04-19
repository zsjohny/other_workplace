package com.jiuy.rb.model.product; 

import lombok.Data;

import java.util.List;

/**
 * PropertyValueRb的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Think
 * @version V1.0  
 * @date 2018年07月06日 下午 06:05:11
 * @Copyright 玖远网络 
*/
@Data
public class PropertyValueRbQuery extends PropertyValueRb {

    /**
     * 主键集合
     */
    private List<Long> ids;
 
} 
