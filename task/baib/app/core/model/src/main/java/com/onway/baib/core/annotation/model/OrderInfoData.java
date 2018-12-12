/**
 * onway.com Inc.
 * Copyright (c) 2016-2016 All Rights Reserved.
 */
package com.onway.baib.core.annotation.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 产品类目信息
 * 
 * @author guangdong.li
 * @version $Id: ItemInfoData.java, v 0.1 2016年5月6日 下午8:15:18 guangdong.li Exp $
 */
public class OrderInfoData implements Serializable {

    /**  */
    private static final long     serialVersionUID = 5981159531497827317L;

    /** 订单号*/
    private String                orderNo;

    /** 用户id*/
    private String                userId;

    /** 用户姓名*/
    private String                userName;

    /** 用户手机号*/
    private String                uesrCell;

    /** 店铺id*/
    private String                shopId;

    /** 订单价格*/
    private BigDecimal            prodPrice;

    /** 订单支付价格*/
    private BigDecimal            orderPrice;

    /** 优惠价格*/
    private BigDecimal            discountPrice;

    /** 订单支付时间*/
    private Date                  orderTime;

    /** 物流方式*/
    private int                   shippmentId;

    /** 支付方式*/
    private String                payType;

    /** 支付期数*/
    private int                   term;

    /** 每期金额*/
    private BigDecimal            amount;

    /** 管理费*/
    private BigDecimal            charge;

    /** 利息*/
    private BigDecimal            interest;

    /** 订单状态*/
    private String                orderState;

    /** 订单支付状态*/
    private String                payState;

    /** 订单评价*/
    private String                commnets;

    /** 是否可以添加评价*/
    private boolean               addComment;

    /** 产品名称*/
    private String                prodName;

    /** 产品图片地址*/
    private String                prodUrl;

    /** 产品颜色*/
    private String                prodColor;

    /** 班级类型*/
    private String                classType;

    /**
     * Getter method for property <tt>addComment</tt>.
     * 
     * @return property value of addComment
     */
    public boolean isAddComment() {
        return addComment;
    }

    /**
     * Setter method for property <tt>addComment</tt>.
     * 
     * @param addComment value to be assigned to property addComment
     */
    public void setAddComment(boolean addComment) {
        this.addComment = addComment;
    }

    /**
     * Getter method for property <tt>orderNo</tt>.
     * 
     * @return property value of orderNo
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * Setter method for property <tt>orderNo</tt>.
     * 
     * @param orderNo value to be assigned to property orderNo
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * Getter method for property <tt>userId</tt>.
     * 
     * @return property value of userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Setter method for property <tt>userId</tt>.
     * 
     * @param userId value to be assigned to property userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Getter method for property <tt>shopId</tt>.
     * 
     * @return property value of shopId
     */
    public String getShopId() {
        return shopId;
    }

    /**
     * Setter method for property <tt>shopId</tt>.
     * 
     * @param shopId value to be assigned to property shopId
     */
    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    /**
     * Getter method for property <tt>prodPrice</tt>.
     * 
     * @return property value of prodPrice
     */
    public BigDecimal getProdPrice() {
        return prodPrice;
    }

    /**
     * Setter method for property <tt>prodPrice</tt>.
     * 
     * @param prodPrice value to be assigned to property prodPrice
     */
    public void setProdPrice(BigDecimal prodPrice) {
        this.prodPrice = prodPrice;
    }

    /**
     * Getter method for property <tt>orderPrice</tt>.
     * 
     * @return property value of orderPrice
     */
    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    /**
     * Setter method for property <tt>orderPrice</tt>.
     * 
     * @param orderPrice value to be assigned to property orderPrice
     */
    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    /**
     * Getter method for property <tt>discountPrice</tt>.
     * 
     * @return property value of discountPrice
     */
    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    /**
     * Setter method for property <tt>discountPrice</tt>.
     * 
     * @param discountPrice value to be assigned to property discountPrice
     */
    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    /**
     * Getter method for property <tt>orderTime</tt>.
     * 
     * @return property value of orderTime
     */
    public Date getOrderTime() {
        return orderTime;
    }

    /**
     * Setter method for property <tt>orderTime</tt>.
     * 
     * @param orderTime value to be assigned to property orderTime
     */
    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    /**
     * Getter method for property <tt>shippmentId</tt>.
     * 
     * @return property value of shippmentId
     */
    public int getShippmentId() {
        return shippmentId;
    }

    /**
     * Setter method for property <tt>shippmentId</tt>.
     * 
     * @param shippmentId value to be assigned to property shippmentId
     */
    public void setShippmentId(int shippmentId) {
        this.shippmentId = shippmentId;
    }

    /**
     * Getter method for property <tt>payType</tt>.
     * 
     * @return property value of payType
     */
    public String getPayType() {
        return payType;
    }

    /**
     * Setter method for property <tt>payType</tt>.
     * 
     * @param payType value to be assigned to property payType
     */
    public void setPayType(String payType) {
        this.payType = payType;
    }

    /**
     * Getter method for property <tt>term</tt>.
     * 
     * @return property value of term
     */
    public int getTerm() {
        return term;
    }

    /**
     * Setter method for property <tt>term</tt>.
     * 
     * @param term value to be assigned to property term
     */
    public void setTerm(int term) {
        this.term = term;
    }

    /**
     * Getter method for property <tt>amount</tt>.
     * 
     * @return property value of amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Setter method for property <tt>amount</tt>.
     * 
     * @param amount value to be assigned to property amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Getter method for property <tt>charge</tt>.
     * 
     * @return property value of charge
     */
    public BigDecimal getCharge() {
        return charge;
    }

    /**
     * Setter method for property <tt>charge</tt>.
     * 
     * @param charge value to be assigned to property charge
     */
    public void setCharge(BigDecimal charge) {
        this.charge = charge;
    }

    /**
     * Getter method for property <tt>interest</tt>.
     * 
     * @return property value of interest
     */
    public BigDecimal getInterest() {
        return interest;
    }

    /**
     * Setter method for property <tt>interest</tt>.
     * 
     * @param interest value to be assigned to property interest
     */
    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    /**
     * Getter method for property <tt>orderState</tt>.
     * 
     * @return property value of orderState
     */
    public String getOrderState() {
        return orderState;
    }

    /**
     * Setter method for property <tt>orderState</tt>.
     * 
     * @param orderState value to be assigned to property orderState
     */
    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    /**
     * Getter method for property <tt>payState</tt>.
     * 
     * @return property value of payState
     */
    public String getPayState() {
        return payState;
    }

    /**
     * Setter method for property <tt>payState</tt>.
     * 
     * @param payState value to be assigned to property payState
     */
    public void setPayState(String payState) {
        this.payState = payState;
    }

    /**
     * Getter method for property <tt>commnets</tt>.
     * 
     * @return property value of commnets
     */
    public String getCommnets() {
        return commnets;
    }

    /**
     * Setter method for property <tt>commnets</tt>.
     * 
     * @param commnets value to be assigned to property commnets
     */
    public void setCommnets(String commnets) {
        this.commnets = commnets;
    }

    /**
     * Getter method for property <tt>userName</tt>.
     * 
     * @return property value of userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Setter method for property <tt>userName</tt>.
     * 
     * @param userName value to be assigned to property userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Getter method for property <tt>uesrCell</tt>.
     * 
     * @return property value of uesrCell
     */
    public String getUesrCell() {
        return uesrCell;
    }

    /**
     * Setter method for property <tt>uesrCell</tt>.
     * 
     * @param uesrCell value to be assigned to property uesrCell
     */
    public void setUesrCell(String uesrCell) {
        this.uesrCell = uesrCell;
    }


    /**
     * Getter method for property <tt>prodName</tt>.
     * 
     * @return property value of prodName
     */
    public String getProdName() {
        return prodName;
    }

    /**
     * Setter method for property <tt>prodName</tt>.
     * 
     * @param prodName value to be assigned to property prodName
     */
    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    /**
     * Getter method for property <tt>prodUrl</tt>.
     * 
     * @return property value of prodUrl
     */
    public String getProdUrl() {
        return prodUrl;
    }

    /**
     * Setter method for property <tt>prodUrl</tt>.
     * 
     * @param prodUrl value to be assigned to property prodUrl
     */
    public void setProdUrl(String prodUrl) {
        this.prodUrl = prodUrl;
    }

    /**
     * Getter method for property <tt>prodColor</tt>.
     * 
     * @return property value of prodColor
     */
    public String getProdColor() {
        return prodColor;
    }

    /**
     * Setter method for property <tt>prodColor</tt>.
     * 
     * @param prodColor value to be assigned to property prodColor
     */
    public void setProdColor(String prodColor) {
        this.prodColor = prodColor;
    }

    /**
     * Getter method for property <tt>classType</tt>.
     * 
     * @return property value of classType
     */
    public String getClassType() {
        return classType;
    }

    /**
     * Setter method for property <tt>classType</tt>.
     * 
     * @param classType value to be assigned to property classType
     */
    public void setClassType(String classType) {
        this.classType = classType;
    }

}
