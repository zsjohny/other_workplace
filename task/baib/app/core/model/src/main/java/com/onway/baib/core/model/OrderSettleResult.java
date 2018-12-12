package com.onway.baib.core.model;

/**
 * 待结算
 * 
 * @author jiaming.zhu
 * @version $Id: OrderSettleResult.java, v 0.1 2017年2月7日 下午8:28:58 ZJM Exp $
 */
public class OrderSettleResult {
    //订单号
    private String orderNo;
    //车辆型号
    private String type;
    //车辆图片
    private String Icon;
    //用车时间
    private String useTime;
    //可结算费用
    private String settleAmount;

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getSettleAmount() {
        return settleAmount;
    }

    public void setSettleAmount(String settleAmount) {
        this.settleAmount = settleAmount;
    }

}
