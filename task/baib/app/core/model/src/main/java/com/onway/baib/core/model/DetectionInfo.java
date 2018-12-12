package com.onway.baib.core.model;

/**
 * 测车info
 * 
 * @author jiaming.zhu
 * @version $Id: DetectionInfo.java, v 0.1 2017年2月15日 下午6:03:16 ZJM Exp $
 */
public class DetectionInfo {
    //启程测试日期
    private String  detectionDate;
    //启程测试时间
    private String  detectionTime;
    //说明
    private String  des;
    //是否返程
    private boolean isBack = false;

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public boolean isBack() {
        return isBack;
    }

    public void setBack(boolean isBack) {
        this.isBack = isBack;
    }

    public String getDetectionDate() {
        return detectionDate;
    }

    public void setDetectionDate(String detectionDate) {
        this.detectionDate = detectionDate;
    }

    public String getDetectionTime() {
        return detectionTime;
    }

    public void setDetectionTime(String detectionTime) {
        this.detectionTime = detectionTime;
    }

}
