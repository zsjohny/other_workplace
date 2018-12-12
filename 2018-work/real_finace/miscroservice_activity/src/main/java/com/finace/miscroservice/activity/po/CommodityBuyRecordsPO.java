package com.finace.miscroservice.activity.po;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class CommodityBuyRecordsPO implements Serializable {
    private String name; //商品名称
    private String addTime; //发货时间
    private String pic;//图片
    private String orderId;//订单号
    private Integer peas;//金豆数量
    private Integer status;//发货状态



}
