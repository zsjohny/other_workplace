package com.finace.miscroservice.activity.po;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class CommoditiesLogPO implements Serializable {
    private Integer shopId; //商品id
    private Integer peas; //消费金豆
    private Integer userId; //用户id


    private String orderId;//订单号
    private String addTime;//添加时间
    private String content;//内容

}
