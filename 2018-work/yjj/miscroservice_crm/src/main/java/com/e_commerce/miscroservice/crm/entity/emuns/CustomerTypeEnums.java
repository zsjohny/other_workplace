package com.e_commerce.miscroservice.crm.entity.emuns;

/**
 * 客户领取状态
 * Create by hyf on 2018/9/18
 */
public enum CustomerTypeEnums {
    NOT_RECEIVED ("未领取",0),
    RECEIVED("已领取",1),
    DISCARD("已废弃",2),
    SIGN_A_CONTRACT ("已签约",3)
    ;
    private String key;
    private Integer val;

    CustomerTypeEnums(String key, Integer val) {
        this.key = key;
        this.val = val;
    }

    public String getKey() {
        return key;
    }

    public Integer getVal() {
        return val;
    }
}
