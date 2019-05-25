package com.songxm.credit.comon.credit.diversion.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by sxm on 17/4/4.
 */
@Data
public class LoginpsReqDto {
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
    @Length(min = 6,message = "最少6个字符")
    private String loginPass;

    @ApiModelProperty(value = "短信验证码",required = true)
    @NotBlank(message = "短信验证码不能为空!")
    private String smsCode;

    @ApiModelProperty(value = "确认密码",required = true)
    @NotBlank(message = "确认密码不能为空!")
    @Length(min = 6,message = "最少6个字符")
    private String confim_pass;
}
