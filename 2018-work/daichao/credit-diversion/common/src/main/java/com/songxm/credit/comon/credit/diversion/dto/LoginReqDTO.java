package com.songxm.credit.comon.credit.diversion.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by sxm on 17/4/3.
 */
@Data
@ApiModel("注册请求参数")
public class LoginReqDTO{

    /**
     * 登录号
     */
    @ApiModelProperty(value = "手机号",required = true)
    @NotBlank(message = "手机号不能为空!")
    @Length(min = 11,max = 11,message = "手机号不正确")
    private String loginName;

    /**
     * 登录密码
     */
    @ApiModelProperty(value = "密码",required = true)
    @NotBlank(message = "密码不能为空!")
    private String loginPass;

    @ApiModelProperty(value = "短信验证码",required = true)
    @NotBlank(message = "短信验证码不能为空!")
    private String smsCode;

    /**
     * 登录号签约渠道
     */
    @ApiModelProperty(value = "注册渠道,1:ANDROID;2:IOS;3:H5",required = true)
    @NotBlank(message = "注册渠道不能为空!")
    private String logidSignchannel;

}
