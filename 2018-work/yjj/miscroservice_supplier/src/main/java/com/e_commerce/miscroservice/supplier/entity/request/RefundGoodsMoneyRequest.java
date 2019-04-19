package com.e_commerce.miscroservice.supplier.entity.request;

import com.e_commerce.miscroservice.commons.annotations.application.IsEmptyAnnotation;
import lombok.Data;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/12/3 16:46
 * @Copyright 玖远网络
 */
@Data
public class RefundGoodsMoneyRequest {
    @IsEmptyAnnotation
    private Long id;//售后单id
    @IsEmptyAnnotation
    private Integer status;//0 同意  1拒绝
    private Integer refundStatus;//退款状态
    private Double money;//退款的钱数
    private String msg;//处理说明
    private Long operTime;//受理时间
    private Integer type;//1 退款  2退货退款
    private String receiverName;//供应商名字
    private String receiverPhone;//供应商手机
    private String receiverAddress;//供应商地址
}
