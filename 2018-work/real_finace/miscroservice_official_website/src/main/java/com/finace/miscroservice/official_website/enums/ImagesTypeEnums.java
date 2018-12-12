package com.finace.miscroservice.official_website.enums;

/**
 * Created by hyf on 2018/3/6.
 */
public enum ImagesTypeEnums {
    APP_IMAGES(1,"APP首页"),
    ACTIVE_IMAGES(2,"活动中心"),
    PC_IMAGES(3,"PC首页"),
    PMS_IMAGES(4,"运营报告");
    private Integer code;
    private String value;

    public Integer getCode(){
        return code;
    }
    public String getValue(){
        return value;
    }
    ImagesTypeEnums(Integer code,String value){
        this.code = code;
        this.value = value;
    }

}
