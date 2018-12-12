package com.onway.baib.core.model;

/**
 * 服务端订单详情
 * 
 * @author jiaming.zhu
 * @version $Id: SelfOrderDetailResult.java, v 0.1 2017年2月7日 下午4:32:21 ZJM Exp $
 */
public class SelfOrderDetailResult {
    //司机
    private String driver;
    //司机电话
    private String driverCell;
    //加盟商
    private String business;
    //加盟商电话
    private String businessCell;
    //车辆预计出库日期
    private String estOutDate;
    //车辆预计出库时间
    private String estOutTime;
    //车辆第一次到达加盟商日期（未出车时为预计）
    private String estArriveDate;
    //车辆第一次到达加盟商时间（未出车时为预计）
    private String estArriveTime;
    //订单预计开始日期
    private String rentalDate;
    //订单预计开始时间
    private String rentalTime;
    //订单预计结束日期
    private String returnDate;
    //订单预计结束时间
    private String returnTime;
    //车辆第二次到达加盟商日期（未出车时为预计）
    private String secondArriveDate;
    //车辆第二次到达加盟商时间（未出车时为预计）
    private String secondArriveTime;
    //开始城市
    private String startAddr;
    //途径城市
    private String approachAddr;
    //结束城市
    private String endAddr;
    //注意事项
    private String userMemo;
    //联系人
    private String name;
    //联系人手机号
    private String cell;
    //订单号
    private String orderNo;
    //车辆型号
    private String carType;
    //车辆颜色
    private String carColour;
    //车辆车牌
    private String carNo;
    //车辆小图标
    private String carInco;
    //订单状态
    private String orderStatus;

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getEstOutDate() {
        return estOutDate;
    }

    public void setEstOutDate(String estOutDate) {
        this.estOutDate = estOutDate;
    }

    public String getEstArriveDate() {
        return estArriveDate;
    }

    public void setEstArriveDate(String estArriveDate) {
        this.estArriveDate = estArriveDate;
    }

    public String getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(String rentalDate) {
        this.rentalDate = rentalDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getSecondArriveDate() {
        return secondArriveDate;
    }

    public void setSecondArriveDate(String secondArriveDate) {
        this.secondArriveDate = secondArriveDate;
    }

    public String getCarInco() {
        return carInco;
    }

    public void setCarInco(String carInco) {
        this.carInco = carInco;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDriverCell() {
        return driverCell;
    }

    public void setDriverCell(String driverCell) {
        this.driverCell = driverCell;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getBusinessCell() {
        return businessCell;
    }

    public void setBusinessCell(String businessCell) {
        this.businessCell = businessCell;
    }

    public String getEstOutTime() {
        return estOutTime;
    }

    public void setEstOutTime(String estOutTime) {
        this.estOutTime = estOutTime;
    }

    public String getEstArriveTime() {
        return estArriveTime;
    }

    public void setEstArriveTime(String estArriveTime) {
        this.estArriveTime = estArriveTime;
    }

    public String getRentalTime() {
        return rentalTime;
    }

    public void setRentalTime(String rentalTime) {
        this.rentalTime = rentalTime;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public String getSecondArriveTime() {
        return secondArriveTime;
    }

    public void setSecondArriveTime(String secondArriveTime) {
        this.secondArriveTime = secondArriveTime;
    }

    public String getStartAddr() {
        return startAddr;
    }

    public void setStartAddr(String startAddr) {
        this.startAddr = startAddr;
    }

    public String getApproachAddr() {
        return approachAddr;
    }

    public void setApproachAddr(String approachAddr) {
        this.approachAddr = approachAddr;
    }

    public String getEndAddr() {
        return endAddr;
    }

    public void setEndAddr(String endAddr) {
        this.endAddr = endAddr;
    }

    public String getUserMemo() {
        return userMemo;
    }

    public void setUserMemo(String userMemo) {
        this.userMemo = userMemo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

}
