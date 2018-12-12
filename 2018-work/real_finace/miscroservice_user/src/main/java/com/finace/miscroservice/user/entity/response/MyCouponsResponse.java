package com.finace.miscroservice.user.entity.response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.user.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hyf on 2018/3/8.
 */
@Data
@ToString
public class MyCouponsResponse extends BaseEntity {
    private Double hbmoney;//红包金额 或 加息券 利息
    private String hbname; //红包名称
    private String hbdetail; //红包内容
    private String hbendtime;//红包有效期
    private Integer hbtype; //红包类型
    private Integer hbstatus;//状态

}
