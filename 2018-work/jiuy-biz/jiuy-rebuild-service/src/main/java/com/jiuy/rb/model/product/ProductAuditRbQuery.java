package com.jiuy.rb.model.product; 

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * ProductAuditRb的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月13日 上午 09:21:43
 * @Copyright 玖远网络 
*/
@Data
public class


ProductAuditRbQuery extends ProductAuditRb {

    /**
     * 橱窗视频
     */
    private String vedioMain;

    /**
     * 搭配列表
     */
    private List<Map<String,String>> matchProductList;


    /**
     * 最小价格
     */
    @JsonIgnoreProperties
    private String priceBegin;
    /**
     * 最大价格
     */
    @JsonIgnoreProperties
    private String priceEnd;
    /**
     * 提交时间开始
     */
    @JsonIgnoreProperties
    private String submitAuditTimeBegin;
    /**
     * 提交时间结束
     */
    @JsonIgnoreProperties
    private String submitAuditTimeEnd;

    /**
     * 款号模糊查询
     */
    @JsonIgnoreProperties
    private String clothesNumberLike;

    /**
     * 商品名称模糊查询
     */
    @JsonIgnoreProperties
    private String productNameLike;

    /**
     * 品牌模糊查询
     */
    @JsonIgnoreProperties
    private String brandNameLike;

} 
