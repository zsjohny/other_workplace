package com.songxm.credit.comon.credit.diversion.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * Created by sxm on 17/4/4.
 */
@Data
@ApiModel("用户详情实体信息")
public class UserProfileReqDTO {
    /**
     * 登录号
     */
    @ApiModelProperty(value = "手机号",required = true)
    @Length(min = 11,max = 11,message = "手机号不正确")
    private String loginName;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String userHeadImage;
    /**
     * 用户姓名
     */
    @ApiModelProperty("用户姓名")
    private String userName;

    /**
     * 身份证号码
     */
    @ApiModelProperty(value = "身份证号码")
    private String idCard;

    /**
     * 用户绑定手机号
     */
    @ApiModelProperty(name = "用户绑定手机号")
    private String userPhone;

    /**
     * 性别 0 女,1 男
     */
    @ApiModelProperty("性别 0 女,1 男")
    private String sex;

    /**
     * 年龄
     */
    @ApiModelProperty(value = "年龄")
    private Integer age;

    /**
     * 学历(00--小学10--初中20--高中30--大专40--本科50--硕士60--博士)
     */
    @ApiModelProperty(value = "学历(00--小学10--初中20--高中30--大专40--本科50--硕士60--博士)")
    private String education;

    /**
     * 是否全日制 1 是 0 不是（暂时无法获取）
     */
    @ApiModelProperty(value = "是否全日制 1 是 0 不是")
    private String isDaily;

    /**
     * 专业（暂时无法获取）
     */
    @ApiModelProperty(value = "专业")
    private String profession;

    /**
     * 学校（暂时无法获取）
     */
    @ApiModelProperty("学校")
    private String school;

}
