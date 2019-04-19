package com.yujj.entity.member;

import com.jiuyuan.util.DateUtil;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/9/4 9:16
 * @Copyright 玖远网络
 */

public class MemberOperatorResponse implements Serializable {

    /**
     * 会员id
     */
    private Long id;

    /**
     * 会员名称
     */
    private String name;

    /**
     * 会员手机号
     */
    private String phone;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String county;
    /**
     * 会员类型
     */
    private String type;
    /**
     * 渠道
     */
    private String canal;
    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 状态
     */
    private String delState;
    /**
     * 总金额
     */
    private Double countMoney;


    public Double getCountMoney() {
        return countMoney;
    }

    public void setCountMoney(Double countMoney) {
        this.countMoney = countMoney;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
//        1:基础版,2:会员版,3:至尊版',
        switch (type){
            case "1":
                type="基础版";
                break;
            case "2":
                type="会员版";
                break;
            case "3":
                type="至尊版";
                break;
            case "4":
                type = "试用版";
                break;
            case "5":
                type = "体验版";
                break;
            case "6":
                type = "会员套餐";
                break;
            case "7":
                type = "超值套餐";
                break;
            case "8":
                type = "至尊套餐";
                break;
        }
        this.type = type;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {

        this.canal = canal;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getDelState() {
        return delState;
    }

    public void setDelState(String delState) {
        switch (delState){
            case "0":
                delState="正常";
                break;
            case "1":
                delState="删除";
                break;
            case "2":
                delState="冻结";
                break;
        }
        this.delState = delState;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        if (Long.valueOf (endTime) <= System.currentTimeMillis ()) {
            this.endTime = "";
        }
        else {
            this.endTime = DateUtil.getDateByLongTime(Long.valueOf(endTime));
        }
    }
}
