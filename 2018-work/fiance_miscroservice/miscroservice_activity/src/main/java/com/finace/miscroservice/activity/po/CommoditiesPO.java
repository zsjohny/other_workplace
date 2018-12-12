package com.finace.miscroservice.activity.po;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CommoditiesPO {
    private String name; //商品名称
    private String serviceConditions; //使用条件
    private String pic; //图片地址
    private String imgs; //图片描述内容地址分隔符","
    private String addTime; //添加时间

    private Integer id; //id
    private Integer endTime; //截止日期 几天后
    private Integer classify; //商品类型 默认 1  出借红包
    private Integer goldPeas; //所需金豆
    private Integer count; //数量
    private Integer status; //状态 默认 0  失效 1 有效 2售罄
    private Integer number; //序号
    private Integer nid; //红包类型id

    private Double money; //红包金额大小
}
