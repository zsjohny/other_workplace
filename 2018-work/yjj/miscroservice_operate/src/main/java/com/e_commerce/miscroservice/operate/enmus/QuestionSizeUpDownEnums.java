package com.e_commerce.miscroservice.operate.enmus;

/**
 * Create by hyf on 2018/12/1
 */
public enum QuestionSizeUpDownEnums {
    //默认操作的数量
    DEFAULT_OPERATE_SIZE(1),
    //0 为增加
    UP_SIZE(0),
    //1 为减少
    DOWN_SIZE(1);


    private Integer code;

    public Integer getCode() {
        return code;
    }

    QuestionSizeUpDownEnums(Integer code) {
        this.code = code;
    }
}
