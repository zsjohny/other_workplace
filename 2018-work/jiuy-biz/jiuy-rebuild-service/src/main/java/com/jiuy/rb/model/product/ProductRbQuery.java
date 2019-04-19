package com.jiuy.rb.model.product; 

import lombok.Data;

import java.util.List;

/**
 * ProductRb的拓展实体。
 * 添加此类是为了避免污染映射的pojo,并解决查询使用map维护难的问题
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月13日 上午 09:22:00
 * @Copyright 玖远网络 
*/
@Data
public class ProductRbQuery extends ProductRb {

    private String keyword;

    private List<String> ids;

    private Integer saleTotalCountMin;

    private Integer saleTotalCountMax;

    private Long lastPutonTimeBegin;

    private Long lastPutonTimeEnd;

    private Integer salesVolume;

    private String campaignImage;


} 
