package com.jiuy.rb.model.product; 

import lombok.Data;

import java.util.List;

/**
 * ProductSkuRbNew的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Administrator
 * @version V1.0  
 * @date 2018年09月03日 下午 03:22:34
 * @Copyright 玖远网络 
*/
@Data
public class ProductSkuRbNewQuery extends ProductSkuRbNew {

    /**
     * 状态:-3废弃，-2停用，-1下架，0正常，1定时上架
     */
    private List<Integer> statusList;
} 
