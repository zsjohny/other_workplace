package com.onway.baib.core.model;

/**
 * 车辆状态变更详情
 * 
 * @author jiaming.zhu
 * @version $Id: DriverUsingDetail.java, v 0.1 2017年2月8日 下午1:50:45 ZJM Exp $
 */
public class DriverUsingDetail {

    //标题
    private String title;
    //日期时间
    private String Datetime;
    //时间
    private String time;
    //说明
    private String des;
    //状态
    private String status;
    //图片地址（开始服务与结束服务时有图片）
    private String img;
    //开始公里数(超出订单时)
    private String startMileage = "0";
    //结束公里数(超出订单时)
    private String endMileage   = "0";
    //相差公里数(超出订单时)
    private String diffMileage  = "0";
    //超出公里数(超出订单时)
    private String overMileage  = "0";

    public String getStartMileage() {
        return startMileage;
    }

    public void setStartMileage(String startMileage) {
        this.startMileage = startMileage;
    }

    public String getEndMileage() {
        return endMileage;
    }

    public void setEndMileage(String endMileage) {
        this.endMileage = endMileage;
    }

    public String getDiffMileage() {
        return diffMileage;
    }

    public void setDiffMileage(String diffMileage) {
        this.diffMileage = diffMileage;
    }

    public String getOverMileage() {
        return overMileage;
    }

    public void setOverMileage(String overMileage) {
        this.overMileage = overMileage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDatetime() {
        return Datetime;
    }

    public void setDatetime(String datetime) {
        Datetime = datetime;
    }

}
