/**
 * 
 */
package com.songxm.credit.controller;

import com.google.common.base.Preconditions;
import com.moxie.cloud.commons.controller.BaseController;
import com.songxm.credit.comon.credit.diversion.constant.AppConsts;
import com.songxm.credit.comon.credit.diversion.utils.MessageUtil;
import com.songxm.credit.compnent.RedisComponent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sxm
 *
 */
@RestController
@RequestMapping(value = AppConsts.BASE_PATH + AppConsts.VERSION)
@Api(value = "短信相关ApI.",description = "获取验证码5分钟有效",tags = "SMS API")
@Slf4j
public class VarCodeController extends BaseController {
    @Autowired
    RedisComponent redisService;

    @ApiOperation("获取短信验证码")
    @RequestMapping(value = "/sms/{mobile}",method = RequestMethod.POST)
    public boolean getOTP(@PathVariable("mobile") String loginName){
        String phone = loginName;
        log.info("短信参数信息:phone:{}",phone);
        System.out.println(StringUtils.isNotBlank(phone));
        Preconditions.checkArgument(StringUtils.isNotBlank(phone),"phone不能为空");
        Preconditions.checkArgument(phone.matches("^0?1\\d{10}$"),"手机号不正确");
        String code = MessageUtil.randomCode();
        String msg = MessageUtil.loadProperties("msg_regist");
        String var = msg.replace("XXXXXX",code);
        log.info("发送的短信:{}",var);

        redisService.set(phone,  code,5*60*1l);
        return MessageUtil.send(phone,var)==null?false:true;
    }

}
