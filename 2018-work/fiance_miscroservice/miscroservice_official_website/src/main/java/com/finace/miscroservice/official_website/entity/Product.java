package com.finace.miscroservice.official_website.entity;

import com.finace.miscroservice.commons.utils.tools.NumberUtil;
import lombok.Data;
import lombok.ToString;

/**
 * Created by hyf on 2018/3/5.
 * 产品  -- 首页显示
 */
@Data
@ToString
public class Product extends BaseEntity{
    private String name;
    private String account;
    private String accountYes;
    private String surplusAccount;
    private String apr;
    private String lowestAccount;
    private String mostAccount;
    private String timeLimitDay;
    private String scales;
    private String use;
    private String addApr;
    private String id;
    private String litpic;
    private String status;
    private String remmoney;


    public String getScales() {

        return scales;
    }

    //格式转换
    public void setScales(String scales) {
        Double sca = Double.parseDouble(scales);
        String sacStr = String.valueOf(NumberUtil.multiply(2,sca,100));
        String scaStr2 = NumberUtil.strFormat2(sacStr);
        this.scales = scaStr2;
    }

  }
