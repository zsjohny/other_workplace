package com.e_commerce.miscroservice.store.entity.response;

import com.e_commerce.miscroservice.commons.helper.util.application.conver.DateUtil;
import com.e_commerce.miscroservice.store.entity.vo.StoreRefundOrder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 售后列表
 * @author hyf
 * @version V1.0
 * @date 2018/9/27 9:58
 * @Copyright 玖远网络
 */
@Data
public class RefundOrderListResponse extends StoreRefundOrder {
    /**
     * exchange 售后订单状态名称
     */
    private  String refundStatusName;

    /**
     * exchange 申请售后时间
     */
    private String applyTimeExchange;
    /**
     * 总购买件数数
     */
    private Integer totalBuyCount;

    /**
     * 订单实付金额（含运费）
     */
    private Double practicalTotalPay;

    /**
     * 运费金额
     */
    private Double totalExpressMoney;

    /**
     * 商品名称
     * @param refundStatusName
     */
    private String name;

    /**
     * 商品的颜色
     * @param refundStatusName
     */
    private String color;

    /**
     * 商品的尺码
     * @param refundStatusName
     */
    private String size;

    /**
     * sku快照
     * @param refundStatusName
     */
    private String skuSnapshot;
    /**
     * 商品名称
     */
    private String shopName;

    /**
     * 商品图片
     */
    private String img;

    private String orderStatus;//订单状态




    public void setRefundStatusName(String refundStatusName) {
        Integer status = super.getRefundStatus();
//• 超时失效
//• 退款成功
        //  售后订单状态：1(待卖家确认、默认)、2（待买家发货）、3（待卖家确认收货）、4(退款成功)、5(卖家拒绝售后关闭)、6（买家超时未发货自动关闭）、7(卖家同意前买家主动关闭)、8（平台客服主动关闭）、9(卖家同意后买家主动关闭)
        if (status == 1) {// 1(待卖家确认、默认)、
            refundStatusName = "待卖家确认";
        } else if (status == 2) {// 2（待买家发货）
            refundStatusName = "卖家同意";
        } else if (status == 3) {// 、3（待卖家确认收货）、
            refundStatusName = "待卖家收货";
        } else if (status == 4) {// 4(退款成功)、
            refundStatusName = "已退款";
        } else if (status == 5) {// 5(卖家拒绝售后关闭)、
            refundStatusName = "卖家拒绝";
        } else if (status == 6) {// 6（买家超时未发货自动关闭）、
            refundStatusName = "已失效";
        } else if (status == 7) {// 7(卖家同意前买家主动关闭)、
            refundStatusName = "已撤销";
        } else if (status == 8) {// 8（平台客服主动关闭）
            refundStatusName = "平台关闭";
        } else if (status == 9) {// 9(卖家同意后买家主动关闭)、
            refundStatusName = "已撤销";
        } else {
            throw new RuntimeException("未知售后订单状态");
        }
        this.refundStatusName = refundStatusName;
    }

    public void setApplyTimeExchange(String applyTimeExchange) {
        Long applyTime = super.getApplyTime();
        applyTimeExchange = DateUtil.parseLongTime2Str(applyTime);
        this.applyTimeExchange = applyTimeExchange;
    }
}
