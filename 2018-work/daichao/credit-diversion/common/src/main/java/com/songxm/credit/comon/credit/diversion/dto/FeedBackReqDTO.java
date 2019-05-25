package com.songxm.credit.comon.credit.diversion.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by sxm on 17/4/8.
 */
@Data
public class FeedBackReqDTO {

    /**
     * 登录号
     */
    @ApiModelProperty(value = "登录名-目前一律手机号")
    @NotBlank
    @Length(min = 11,max = 11,message = "手机号不正确")
    private String loginName;

    /**
     * 反馈内容
     */
    @ApiModelProperty(value = "反馈内容")
    @NotBlank(message = "反馈内容不能为空!")
    private String feedbackContent;

    /**
     * 反馈类型
     */
    @ApiModelProperty(value = "反馈类型,1:产品问题;2:系统bug问题;3:服务态度")
    @NotBlank(message = "请填写响应的反馈类型")
    private String feedbackType;

    @ApiModelProperty(value = "APP当前版本号系统+版本 示例 android1.0")
    private String appVersion;
}
