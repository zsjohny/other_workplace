package com.onway.baib.core.model;

/**
 * 获取订单所有车辆
 * 
 * @author jiaming.zhu
 * @version $Id: OrderCarResult.java, v 0.1 2017年2月8日 上午10:30:02 ZJM Exp $
 */
public class OrderCarResult {
    //是否头车标识
    private boolean isLeader = false;
    //类型
    private String  type;
    //颜色
    private String  carColour;
    //车牌
    private String  carNo;
    //司机
    private String  driver;
    //司机电话
    private String  cell;

    public boolean isLeader() {
        return isLeader;
    }

    public void setLeader(boolean isLeader) {
        this.isLeader = isLeader;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

}
