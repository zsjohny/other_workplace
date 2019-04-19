package com.e_commerce.miscroservice.product.vo;

import com.e_commerce.miscroservice.product.entity.LiveProduct;
import lombok.Data;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/14 9:29
 * @Copyright 玖远网络
 */
@Data
public class LiveProductVO extends LiveProduct {

    private Long memberId;

    /**
     * 房间编号
     */
    private List<Long> roomNumList;

    /**
     * 1平台直播,2用户直播
     */
    private Integer anchorType;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 款号
     */
    private String styleNo;

    /**
     * 商品状态 1上架,2下架,10失效(其他非正常状态)
     */
    private Integer productStatus;

    /**
     * 原价
     */
    private Double originalPrice;

    /**
     * 售卖价
     */
    private Double currentPrice;

    /**
     * 橱窗图
     */
    private String summaryImgJsonArr;
    private String summaryImg;

    /**
     * 详情图和文字文本
     */
    private String showDetailImgJsonArr;

    /**
     * 产品参数
     */
    private String propertyDesc;


    /**
     * 操作类型
     */
    private String operType;
    /**
     * 排序json
     */
    private String sortNoJson;


    private Integer pageSize;

    private Integer pageNumber;
    private List<Long> shopProductIds;
    /**
     * 库存
     */
    private Integer inventory;
}
