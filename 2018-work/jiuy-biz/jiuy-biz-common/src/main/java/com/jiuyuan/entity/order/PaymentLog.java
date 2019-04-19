/**
 * 
 */
package com.jiuyuan.entity.order;

import java.io.Serializable;

/**
 * @author LWS 
 *
 */
public class PaymentLog implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2026049921460788621L;

    private long id;
    private long userId;
//    private long orderId;
    private long paymentCount;
    private String buyerEmail;
    private String buyerId;
    private String sellerEmail;
    private String sellerId;
    private String tradeNo;
    private String tradeStatus;
    private String opertionPoint;
    private long createTime;
    private long updateTime;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
//    public long getOrderId() {
//        return orderId;
//    }
//    public void setOrderId(long orderId) {
//        this.orderId = orderId;
//    }
    public long getPaymentCount() {
        return paymentCount;
    }
    public void setPaymentCount(long paymentCount) {
        this.paymentCount = paymentCount;
    }
    public String getBuyerEmail() {
        return buyerEmail;
    }
    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }
    public String getBuyerId() {
        return buyerId;
    }
    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }
    public String getSellerEmail() {
        return sellerEmail;
    }
    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }
    public String getSellerId() {
        return sellerId;
    }
    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
    public String getTradeNo() {
        return tradeNo;
    }
    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }
    public String getTradeStatus() {
        return tradeStatus;
    }
    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }
    public long getCreateTime() {
        return createTime;
    }
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    public long getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
    public String getOpertionPoint() {
        return opertionPoint;
    }
    public void setOpertionPoint(String opertionPoint) {
        this.opertionPoint = opertionPoint;
    }
    
    
}
