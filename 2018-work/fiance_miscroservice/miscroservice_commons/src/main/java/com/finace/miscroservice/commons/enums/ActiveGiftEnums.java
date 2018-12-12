package com.finace.miscroservice.commons.enums;

public enum ActiveGiftEnums {
    SING_UP_GIRT("荐面奖",1),
    INVITATION_GIRT("佣金奖",2),
    TEAM_GIRT("人脉奖",3);
    private String value;
    private Integer code;

    ActiveGiftEnums(String value, Integer code) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public Integer getCode() {
        return code;
    }
}
