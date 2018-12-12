package com.onway.baib.core.model;

import java.util.List;

/**
 * 司机端用车中
 * 
 * @author jiaming.zhu
 * @version $Id: DriverUsingResult.java, v 0.1 2017年2月8日 上午11:59:37 ZJM Exp $
 */
public class DriverUsingResult {
    //订单号
    private String                  orderNo;
    //车辆图片
    private String                  carIcon;
    //型号
    private String                  type;
    //颜色
    private String                  colour;
    //车牌
    private String                  carNo;
    //加盟商
    private String                  businessName;
    //加盟商联系电话
    private String                  businessCell;
    //用户联系人
    private String                  userName;
    //用户电话
    private String                  userCell;
    //详情
    private List<DriverUsingDetail> driverUsingDetailList;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCarIcon() {
        return carIcon;
    }

    public void setCarIcon(String carIcon) {
        this.carIcon = carIcon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessCell() {
        return businessCell;
    }

    public void setBusinessCell(String businessCell) {
        this.businessCell = businessCell;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCell() {
        return userCell;
    }

    public void setUserCell(String userCell) {
        this.userCell = userCell;
    }

    public List<DriverUsingDetail> getDriverUsingDetailList() {
        return driverUsingDetailList;
    }

    public void setDriverUsingDetailList(List<DriverUsingDetail> driverUsingDetailList) {
        this.driverUsingDetailList = driverUsingDetailList;
    }

}
