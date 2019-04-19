package com.e_commerce.miscroservice.publicaccount.service.proxy;


import com.e_commerce.miscroservice.commons.enums.colligate.RedisKeyEnum;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/25 9:25
 * @Copyright 玖远网络
 */
public interface SmsService{

    boolean sendAuthCode(RedisKeyEnum key, String phone);
}
