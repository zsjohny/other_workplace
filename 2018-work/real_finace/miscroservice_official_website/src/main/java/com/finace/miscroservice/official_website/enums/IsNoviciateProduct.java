package com.finace.miscroservice.official_website.enums;

/**
 * Created by hyf on 2018/3/5.
 */
public enum  IsNoviciateProduct {
    IS_NOVICIATE("xsb","新手标"),
    NON_NOVICIATE("sycp","推荐标");
    private String key;
    private String value;

    public String getKey(){
        return key;
    }
    public String getValue(){
        return value;
    }

    IsNoviciateProduct(String key,String value){
        this.key=key;
        this.value=value;
    }
}
