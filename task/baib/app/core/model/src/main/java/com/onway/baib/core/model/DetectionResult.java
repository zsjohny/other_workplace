package com.onway.baib.core.model;

/**
 * 测车返回结果集
 * 
 * @author jiaming.zhu
 * @version $Id: DetectionResult.java, v 0.1 2017年2月7日 上午11:41:39 ZJM Exp $
 */
public class DetectionResult {
    //订单号
    private String orderNo;
    //车辆图片
    private String carImg;
    //型号
    private String carType;
    //颜色
    private String carColour;
    //司机
    private String driver;
    //车牌
    private String carNo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCarImg() {
        return carImg;
    }

    public void setCarImg(String carImg) {
        this.carImg = carImg;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarColour() {
        return carColour;
    }

    public void setCarColour(String carColour) {
        this.carColour = carColour;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

}
