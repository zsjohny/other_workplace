package com.e_commerce.miscroservice.operate.entity.response;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.helper.handler.DbHandler;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author hyf
 * @Date 2019/1/15 16:03
 */
@Data
public class FindAllLiveResponse {

    private static String SEX_UNKNOWN="未知";
    private static String SEX_MAN="男";
    private static String SEX_WOMAN="女";

    private Long  id;

    /**
     * 姓名
     */
    private String name;
    /**
     * 昵称
     */
    private String nickName;
    private String icon;
    /**
     * 年龄
     */
    private Integer age;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 性别
     */
    private Integer sex;

    private Long storeId;

    private Long roomNum;

    private Integer liveType;

    private Integer status;


    /**
     * 是否删除 0 正常  1 删除
     */
    private Integer delStatus;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    private String time;

    /**
     * 上级姓名
     */
    private String upName;
    /**
     * 上级手机号
     */
    private String upPhone;

    public void setTime(String time) {
        this.time = TimeUtils.stamp2Str(createTime);
    }

    private String sexStr;

    public void setSexStr(String sexStr) {
        if (sex==1){
            sexStr = SEX_MAN;
        }else
        if (sex==2){
            sexStr=SEX_WOMAN;
        }else {
            sexStr=SEX_UNKNOWN;
        }

        this.sexStr = sexStr;
    }
}
