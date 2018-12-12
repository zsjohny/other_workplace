package com.finace.miscroservice.activity.po;

import com.finace.miscroservice.commons.entity.BasePage;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


@ToString
@Getter
@Setter
public class UserJiangPinPO extends BasePage implements Serializable{

    private Integer id;

    private String userId;

    private String jiangPinName;

    private String addTime;

    private String remark;
    private String underUser;
}
